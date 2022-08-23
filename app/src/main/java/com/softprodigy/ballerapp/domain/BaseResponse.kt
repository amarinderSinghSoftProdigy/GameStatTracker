package com.softprodigy.ballerapp.domain

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status") val status: Boolean,
    @SerializedName("statusCode") val statusCode: Int? = null,
    @SerializedName("statusMessage") val statusMessage: String? = null,
    @SerializedName("totalRecords") val totalRecords: Int? = null,
    @SerializedName("data") val data: T
)