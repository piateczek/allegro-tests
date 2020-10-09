package com.example.allegroRESTAPItests

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class CategoriesAndParameters {
    private var globalAccessToken: String = ""

    @Test
    fun `get access token` () {
        val encoding: String = Base64.getEncoder().encodeToString("${Constants.CLIENT_ID}:${Constants.CLIENT_SECRET}".toByteArray())

        val api = Api.create(Constants.BASE_URL)
        val response = api.getAccessToken("client_credentials","Basic $encoding").execute()

        globalAccessToken = response.body()?.accessToken.toString()
        assertThat(response.code()).isEqualTo(200)
    }
}