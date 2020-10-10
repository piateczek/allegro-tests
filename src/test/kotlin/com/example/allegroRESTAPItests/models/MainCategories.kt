package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class MainCategories(
        @SerializedName("categories")
        val categories: List<Category>
)
