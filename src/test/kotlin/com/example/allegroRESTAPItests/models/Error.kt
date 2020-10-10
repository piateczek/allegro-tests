package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("code")
        var errorCode: String,

        @SerializedName("message")
        var errorMessage: String,

        @SerializedName("details")
        var errorDetails: String,

        @SerializedName("path")
        var errorPath: String,

        @SerializedName("userMessage")
        var errorUserMessage: String
)
