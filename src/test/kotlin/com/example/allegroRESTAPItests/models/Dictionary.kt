package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class Dictionary(
        @SerializedName("id")
        var dictionaryId: String,

        @SerializedName("value")
        var dictionaryValue: String
)