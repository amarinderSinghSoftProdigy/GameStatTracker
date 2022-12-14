package com.softprodigy.ballerapp.core.model

import com.google.gson.annotations.SerializedName


data class NetworkError(
    @SerializedName("Code") val code: Int,
    @SerializedName("Error") val errorMessage: String,
)
