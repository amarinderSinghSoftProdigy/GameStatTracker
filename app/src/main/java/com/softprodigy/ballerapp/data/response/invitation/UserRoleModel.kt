package com.softprodigy.ballerapp.data.response.invitation

import com.google.gson.annotations.SerializedName

data class UserRoleModel(
    @SerializedName("key") val key: String = "",
    @SerializedName("value") val value: String = ""
)
