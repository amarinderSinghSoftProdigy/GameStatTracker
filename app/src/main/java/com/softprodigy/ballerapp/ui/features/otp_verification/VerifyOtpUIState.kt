package com.softprodigy.ballerapp.ui.features.otp_verification

import com.softprodigy.ballerapp.data.UserInfo


data class VerifyOtpUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var user: UserInfo? = null,
    var message: String? = null,
    var isOtpError: Boolean? = null,
)