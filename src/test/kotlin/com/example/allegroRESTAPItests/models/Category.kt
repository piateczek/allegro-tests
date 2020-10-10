package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    var categoryId: String,

    @SerializedName("name")
    var categoryName: String,

    @SerializedName("parent")
    var categoryParent: String,

    @SerializedName("leaf")
    var categoryLeaf: Boolean
)