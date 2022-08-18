package com.baller_app.core.model

import com.google.gson.annotations.SerializedName


data class NetworkError(
    @SerializedName("Code") val code: Int,
    @SerializedName("Error") val errorMessage: String,
)
