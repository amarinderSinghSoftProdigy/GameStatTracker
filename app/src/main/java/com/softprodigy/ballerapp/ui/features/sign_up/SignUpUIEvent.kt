package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.SocialUserModel

sealed class SignUpUIEvent {
    data class NameChange(val name: String) : SignUpUIEvent()
    data class EmailChange(val email: String) : SignUpUIEvent()
    data class PasswordChange(val password: String) : SignUpUIEvent()
    data class PasswordToggleChange(val showPassword: Boolean) : SignUpUIEvent()
    data class ConfirmTermsChange(val acceptTerms: Boolean) : SignUpUIEvent()
    data class OnGoogleClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class Submit(val name: String, val email: String, val password: String) : SignUpUIEvent()

}