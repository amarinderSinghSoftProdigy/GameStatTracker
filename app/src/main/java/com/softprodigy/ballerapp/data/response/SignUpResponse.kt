package com.softprodigy.ballerapp.data.response

import com.softprodigy.ballerapp.data.UserInfo
import com.google.gson.annotations.SerializedName


data class SignUpResponse(
    @SerializedName("data") var userInfo: UserInfo = UserInfo(),
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("verifyToken") var verifyToken: String? = null,

)


