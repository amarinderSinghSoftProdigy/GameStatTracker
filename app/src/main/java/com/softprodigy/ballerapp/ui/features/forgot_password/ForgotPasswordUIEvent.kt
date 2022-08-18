package com.softprodigy.ballerapp.ui.features.forgot_password


sealed class ForgotPasswordUIEvent {

    data class Submit(val email: String) : ForgotPasswordUIEvent()
}