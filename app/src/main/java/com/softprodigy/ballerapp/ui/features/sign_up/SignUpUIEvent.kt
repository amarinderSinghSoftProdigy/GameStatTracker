package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.request.SignUpData

sealed class SignUpUIEvent {

    data class OnImageSelected(val profileImageUri: String) : SignUpUIEvent()
    object OnImageUploadSuccess : SignUpUIEvent()
    object OnScreenNext : SignUpUIEvent()
    data class OnSignUpDataSelected(val signUpData: SignUpData) : SignUpUIEvent()
    data class OnVerifyNumber(val phoneNumber: String) : SignUpUIEvent()
    data class OnConfirmNumber(val phoneNumber: String, val otp: String) : SignUpUIEvent()
}