package com.example.allegroRESTAPItests.models

import com.google.gson.annotations.SerializedName

data class Parameter(
        @SerializedName("id")
        var parameterId: String,

        @SerializedName("name")
        var parameterName: String,

        @SerializedName("type")
        var parameterType: String,

        @SerializedName("required")
        var parameterRequired: Boolean,

        @SerializedName("requiredForProduct")
        var parameterRequiredForProduct: Boolean,

        @SerializedName("unit")
        var parameterUnit: String,

        @SerializedName("options")
        var parameterOptions: String,

        @SerializedName("dictionary")
        var parameterDictionary: List<Dictionary>,

        @SerializedName("restrictions")
        var parameterRestrictions: String
)