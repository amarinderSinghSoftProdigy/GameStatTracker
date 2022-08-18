package com.softprodigy.ballerapp.data.response

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.UserInfo

class ResetPasswordResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val userInfo: UserInfo = UserInfo()
)