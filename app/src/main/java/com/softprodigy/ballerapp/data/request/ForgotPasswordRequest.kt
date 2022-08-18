package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(@SerializedName("email") var email: String? = null)
