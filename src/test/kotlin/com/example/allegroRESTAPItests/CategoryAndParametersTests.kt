package com.example.allegroRESTAPItests

import com.example.allegroRESTAPItests.helpers.Api
import com.example.allegroRESTAPItests.helpers.Constants
import com.example.allegroRESTAPItests.models.Error
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class CategoryAndParametersTests {
    private var mainAccessToken: String = ""

    @Test
    fun `get access token` () {
        val encoding: String = Base64.getEncoder().encodeToString("${Constants.CLIENT_ID}:${Constants.CLIENT_SECRET}".toByteArray())

        val api = Api.create(Constants.BASE_URL)
        val response = api.getAccessToken(
                "client_credentials",
                "Basic $encoding"
        ).execute()

        mainAccessToken = response.body()?.accessToken.toString()
        println("\nAccess Token: $mainAccessToken\n")
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get IDs of Allegro categories` () {
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getIDsOfAllegroCategories(
                "Bearer $mainAccessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(response.body()?.categories?.size, 13)
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get a category by ID` () {
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByID(
                "5",
                "Bearer $mainAccessToken",
                "pl-PL",
                "application/vnd.allegro.public.v1+json",
                "application/x-www-form-urlencoded"
        ).execute()

        assertEquals(response.body()?.categoryId, "5")
        assertEquals(response.body()?.categoryName, "Dom i Ogród")
        assertThat(response.code()).isEqualTo(200)
    }

    @Test
    fun `get a category by ID error` () {
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getCategoryByIDError(
                "999",
                "Bearer $mainAccessToken",
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
    fun `get parameters supported by a category` () {
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategory(
                "1",
                "Bearer $mainAccessToken",
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
    fun `get parameters supported by a category error` () {
        val api = Api.create(Constants.API_ALLEGRO_BASE_URL)
        val response = api.getParametersSupportedByCategoryError(
                "999",
                "Bearer $mainAccessToken",
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
}