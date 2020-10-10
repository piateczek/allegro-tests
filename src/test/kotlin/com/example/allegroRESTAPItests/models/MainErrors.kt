package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class MainErrors(
        @SerializedName("errors")
        val errors: List<Error>
)
