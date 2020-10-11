package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class MainMatchingCategories(
        @SerializedName("matching_categories")
        val matchingCategories: List<MatchingCategory>
)