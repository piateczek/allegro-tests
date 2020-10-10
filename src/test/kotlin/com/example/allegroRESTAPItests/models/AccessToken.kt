package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class AccessToken(
        @SerializedName("access_token")
        var accessToken: String,

        @SerializedName("token_type")
        var tokenType: String,

        @SerializedName("expires_in")
        var expiresIn: Int,

        @SerializedName("scope")
        var scope: String,

        @SerializedName("allegro_api")
        var allegroApi: Boolean,

        @SerializedName("jti")
        var jti: String
)