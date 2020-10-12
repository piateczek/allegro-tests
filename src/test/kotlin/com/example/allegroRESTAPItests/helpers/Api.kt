package com.example.allegroRESTAPItests.helpers

import com.example.allegroRESTAPItests.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {
    @POST("/auth/oauth/token")
    fun getAccessToken(
            @Query("grant_type") grandType: String,
            @Header("Authorization") bearerAuth: String
    ): Call<AccessToken>

    @GET("/sale/categories")
    fun getIDsOfAllegroCategories(
            @Header("Authorization") accessToken: String,
            @Header("Accept-Language") acceptLanguage: String,
            @Header("Accept") accept: String,
            @Header("Content-Type") contentType: String
    ): Call<MainCategories>

    @GET("/sale/categories/{categoryId}")
    fun getCategoryByID(
            @Path("categoryId") categoryId: String,
            @Header("Authorization") accessToken: String,
            @Header("Accept-Language") acceptLanguage: String,
            @Header("Accept") accept: String,
            @Header("Content-Type") contentType: String
    ): Call<Category>

    @GET("/sale/categories/{categoryId}/parameters")
    fun getParametersSupportedByCategory(
            @Path("categoryId") categoryId: String,
            @Header("Authorization") accessToken: String,
            @Header("Accept-Language") acceptLanguage: String,
            @Header("Accept") accept: String,
            @Header("Content-Type") contentType: String
    ): Call<MainParameters>

    @GET("/sale/matching-categories")
    fun getCategoriesSuggestions(
            @Query("name") name: String,
            @Header("Authorization") accessToken: String,
            @Header("Accept-Language") acceptLanguage: String,
            @Header("Accept") accept: String,
            @Header("Content-Type") contentType: String
    ): Call<MainMatchingCategories>

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