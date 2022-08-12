package com.softprodigy.deliveryapp.ui.features.sign_up

import com.softprodigy.deliveryapp.data.GoogleUserModel
import com.softprodigy.deliveryapp.ui.features.welcome.WelcomeUIEvent

sealed class SignUpUIEvent {
    data class NameChange(val name: String) : SignUpUIEvent()
    data class EmailChange(val email: String) : SignUpUIEvent()
    data class PasswordChange(val password: String) : SignUpUIEvent()
    data class PasswordToggleChange(val showPassword: Boolean) : SignUpUIEvent()
    data class ConfirmTermsChange(val acceptTerms: Boolean) : SignUpUIEvent()
    data class OnGoogleClick(val googleUser: GoogleUserModel) : SignUpUIEvent()
    object Submit : SignUpUIEvent()

    data class Submit(val name: String, val email: String, val password: String) : SignUpUIEvent()

}