package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData

sealed class SignUpUIEvent {

    data class OnImageSelected(val profileImageUri: String) : SignUpUIEvent()
    data class OnImageUploadSuccess(val fromNewProfile: Boolean = false) : SignUpUIEvent()
    object OnScreenNext : SignUpUIEvent()
    data class OnSignUpDataSelected(val signUpData: SignUpData) : SignUpUIEvent()
    object OnVerifyNumber : SignUpUIEvent()
    data class OnRoleChanged(val role: String) : SignUpUIEvent()
    data class OnConfirmNumber(val phoneNumber: String, val otp: String) : SignUpUIEvent()
    data class OnFirstNameChanged(val firstName: String) : SignUpUIEvent()
    data class OnLastNameChanged(val lastName: String) : SignUpUIEvent()
    data class OnEmailChanged(val email: String) : SignUpUIEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : SignUpUIEvent()
    data class OnBirthdayChanged(val birthday: String) : SignUpUIEvent()
    data class OnAddressChanged(val address: String) : SignUpUIEvent()
    object OnAddProfileClicked : SignUpUIEvent()

    data class OnFacebookClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnGoogleClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnTwitterClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnGenderChange(val gender: String) : SignUpUIEvent()
    data class OnCountryCode(val countryCode: String) : SignUpUIEvent()

}