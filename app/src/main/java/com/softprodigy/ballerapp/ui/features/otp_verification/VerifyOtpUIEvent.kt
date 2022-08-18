package com.softprodigy.ballerapp.ui.features.otp_verification


sealed class VerifyOtpUIEvent {

    data class Submit(val otp: String, val token: String) : VerifyOtpUIEvent()
    data class ResendOtp(val email: String) : VerifyOtpUIEvent()
}