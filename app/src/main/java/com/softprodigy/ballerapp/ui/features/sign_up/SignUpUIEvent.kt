package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.request.SignUpData

sealed class SignUpUIEvent {

    data class OnImageSelected(val profileImageUri: String) : SignUpUIEvent()
    object OnImageUploadSuccess : SignUpUIEvent()
    object OnScreenNext : SignUpUIEvent()
    data class OnSignUpDataSelected(val signUpData: SignUpData) : SignUpUIEvent()
    object OnVerifyNumber : SignUpUIEvent()
    data class OnConfirmNumber(val phoneNumber: String, val otp: String) : SignUpUIEvent()
    data class OnFirstNameChanged(val firstName: String) : SignUpUIEvent()
    data class OnLastNameChanged(val lastName: String) : SignUpUIEvent()
    data class OnEmailChanged(val email: String) : SignUpUIEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : SignUpUIEvent()
}