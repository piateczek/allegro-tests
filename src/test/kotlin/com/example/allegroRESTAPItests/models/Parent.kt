package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class Parent(
        @SerializedName("id")
        val matchingCategoryId: String,

        @SerializedName("name")
        val matchingCategoryName: String,

        @SerializedName("parent")
        val matchingCategoryParent: Parent
)