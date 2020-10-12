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
    fun `get access token`() {
        val encoding: String = Base64.getEncoder().encodeToString("${Constants.CLIENT_ID}:${Constants.CLIENT_SECRET}".toByteArray())

        val api = Api.create(Constants.BASE_URL)
        val response = api.getAccessToken(
                "client_credentials",
                "Basic $encoding"
        ).execute()
        assertThat(response.code()).isEqualTo(200)

        val tempAccessToken = response.body()?.accessToken.toString()
        writeTextToFile(tempAccessToken)
    }

    @Test
    fun `get IDs of Allegro categories`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getIDsOfAllegroCategories(
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        assertEquals(13, response.body()?.categories?.size)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get a category by ID`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "5",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        assertEquals("5", response.body()?.categoryId)
        assertEquals("Dom i Ogród", response.body()?.categoryName)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get a category by ID error`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "999",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        val jsonError = response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals("ERROR", error.errorCode)
        assertEquals("Category '999' not found", error.errorMessage)
        assertThat(response.code()).isEqualTo(404)
    }

    @Test
    fun `get parameters supported by a category`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategory(
                "1",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        assertEquals(3, response.body()?.parameters?.size)
        assertEquals("11323", response.body()?.parameters?.first()?.categoryId)
        assertEquals("Stan", response.body()?.parameters?.first()?.categoryName)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get parameters supported by a category error`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategory(
                "999",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        val jsonError = response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals("ERROR", error.errorCode)
        assertEquals("Category '999' not found", error.errorMessage)
        assertThat(response.code()).isEqualTo(404)
    }

    @Test
    fun `get categories suggestions`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoriesSuggestions(
                "Muzyka",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                Constants.ACCEPT,
                Constants.CONTENT_TYPE
        ).execute()

        assertEquals("111810", response.body()?.matchingCategories?.first()?.matchingCategoryId)
        assertEquals("Muzyka", response.body()?.matchingCategories?.first()?.matchingCategoryName)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `not acceptable representation request`() {
        val accessToken = readFileAsText("$projectDirectory/$pathToTokenFile")
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "1",
                "Bearer $accessToken",
                Constants.ACCEPT_LANGUAGE,
                "TEST",
                Constants.CONTENT_TYPE
        ).execute()

        val jsonError = response.errorBody()?.string()?.substringAfter("[")?.substringBefore("]")
        val gson = Gson()
        val error = gson.fromJson(jsonError, Error::class.java)

        assertEquals("NotAcceptableException", error.errorCode)
        assertEquals("Not acceptable representation requested. Please check 'Accept' request header", error.errorMessage)
        assertThat(response.code()).isEqualTo(406)
    }

    private fun writeTextToFile(fileContent: String) = File("$projectDirectory/$pathToTokenFile").writeText(fileContent)

    private fun readFileAsText(fileName: String) = File(fileName).inputStream().readBytes().toString(Charsets.UTF_8)
}