package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("points") val points: String = "",
    )