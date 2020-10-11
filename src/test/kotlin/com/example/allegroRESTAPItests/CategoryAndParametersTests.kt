package com.example.allegroRESTAPItests

import com.example.allegroRESTAPItests.helpers.Api
import com.example.allegroRESTAPItests.helpers.Constants
import com.example.allegroRESTAPItests.models.Error
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.io.File
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CategoryAndParametersTests {
    private val projectDirectory = System.getProperty("user.dir")
    private val pathToTokenFile = "/src/test/kotlin/com/example/allegroRESTAPItests/helpers/token.txt"

    @Test
    @Order(1)
    fun `get access token` () {
        val encoding: String = Base64.getEncoder().encodeToString("${Constants.CLIENT_ID}:${Constants.CLIENT_SECRET}".toByteArray())

        val api = Api.create(Constants.BASE_URL)
        val response = api.getAccessToken(
                "client_credentials",
                "Basic $encoding"
        ).execute()

        val accessToken = response.body()?.accessToken.toString()
        writeTextToFile(accessToken)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    @Order(2)
    fun `get IDs of Allegro categories` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getIDsOfAllegroCategories(
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(13, response.body()?.categories?.size)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    @Order(3)
    fun `get a category by ID` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "5",
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(response.body()?.categoryId, "5")
        assertEquals(response.body()?.categoryName, "Dom i Ogród")
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    @Order(4)
    fun `get a category by ID error` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByIDError(
                "999",
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        val jsonError =  response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals(error.errorCode, "ERROR")
        assertEquals(error.errorMessage, "Category '999' not found")
        assertThat(response.code()).isEqualTo(404)
    }

    @Test
    @Order(5)
    fun `get parameters supported by a category` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategory(
                "1",
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(response.body()?.parameters?.size, 3)
        assertEquals(response.body()?.parameters?.first()?.categoryId, "11323")
        assertEquals(response.body()?.parameters?.first()?.categoryName, "Stan")
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    @Order(6)
    fun `get parameters supported by a category error` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategoryError(
                "999",
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        val jsonError =  response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals(error.errorCode, "ERROR")
        assertEquals(error.errorMessage, "Category '999' not found")
        assertThat(response.code()).isEqualTo(404)
    }

    @Test
    @Order(7)
    fun `get categories suggestions` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoriesSuggestions(
                "Muzyka",
                "Bearer $accessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(response.body()?.matchingCategories?.first()?.matchingCategoryId, "111810")
        assertEquals(response.body()?.matchingCategories?.first()?.matchingCategoryName, "Muzyka")
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    @Order(8)
    fun `not acceptable representation request` () {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "1",
                "Bearer $accessToken",
                "pl-PL",
                "TEST",
                "application/x-www-form-urlencoded"
        ).execute()

        val jsonError =  response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals(error.errorCode, "NotAcceptableException")
        assertEquals(error.errorMessage, "Not acceptable representation requested. Please check 'Accept' request header")
        assertThat(response.code()).isEqualTo(406)
    }

    private fun writeTextToFile(fileContent: String) =
        File("$projectDirectory/$pathToTokenFile").writeText(fileContent)

    private fun readFileAsText(fileName: String)
            = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)
}