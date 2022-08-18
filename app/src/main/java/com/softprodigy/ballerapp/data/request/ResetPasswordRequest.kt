package com.softprodigy.ballerapp.data.request

import com.google.gson.annotations.SerializedName

class ResetPasswordRequest(@SerializedName("password") var password: String? = null)