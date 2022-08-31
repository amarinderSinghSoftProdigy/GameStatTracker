package com.softprodigy.ballerapp.ui.features.forgot_password


sealed class ForgotPasswordEvent {

    data class OnForgotPassword(val email: String) : ForgotPasswordEvent()

}

