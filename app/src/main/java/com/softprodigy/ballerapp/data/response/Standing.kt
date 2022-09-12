package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("standings") val standings: String = "",
    )