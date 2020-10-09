package com.example.allegroRESTAPItests

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {
    @POST(Constants.AUTH_TOKEN_ENDPOINT)
    fun getAccessToken(
            @Query("grant_type") grandType: String,
            @Header("Authorization") bearerAuth: String
    ): Call<AccessToken>

    companion object {
        fun create(baseUrl: String): Api {
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(Api::class.java)
        }
    }
}