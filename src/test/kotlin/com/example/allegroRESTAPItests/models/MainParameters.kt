package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class MainParameters(
        @SerializedName("parameters")
        val parameters: List<Category>
)