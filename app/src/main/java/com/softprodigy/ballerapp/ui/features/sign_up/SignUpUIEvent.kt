package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.ui.features.login.LoginUIEvent

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
    data class OnBirthdayChanged(val birthday: String) : SignUpUIEvent()
    data class OnAddressChanged(val address: String) : SignUpUIEvent()

    data class OnFacebookClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnGoogleClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnTwitterClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnGenderChange(val gender: String) : SignUpUIEvent()


}