package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.SwapUser

sealed class SignUpUIEvent {

    data class OnImageSelected(val profileImageUri: String) : SignUpUIEvent()
    data class OnImageUploadSuccess(val fromNewProfile: Boolean = false) : SignUpUIEvent()
    object OnScreenNext : SignUpUIEvent()
    object OnAddProfile : SignUpUIEvent()
    data class OnSignUpDataSelected(val signUpData: SignUpData) : SignUpUIEvent()
    object SetRegister : SignUpUIEvent()
    object OnVerifyNumber : SignUpUIEvent()
    data class OnRoleChanged(val role: String) : SignUpUIEvent()
    data class OnConfirmNumber(val phoneNumber: String, val otp: String) : SignUpUIEvent()
    data class OnFirstNameChanged(val firstName: String) : SignUpUIEvent()
    data class OnLastNameChanged(val lastName: String) : SignUpUIEvent()
    data class OnEmailChanged(val email: String) : SignUpUIEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : SignUpUIEvent()
    data class OnBirthdayChanged(val birthday: String) : SignUpUIEvent()
    data class OnAddressChanged(val address: String) : SignUpUIEvent()
    //object OnAddProfileClicked : SignUpUIEvent()
    data class OnSwapUpdate(val user : SwapUser)  : SignUpUIEvent()

    //data class OnFacebookClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    //data class OnGoogleClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    //data class OnTwitterClick(val socialUser: SocialUserModel) : SignUpUIEvent()
    data class OnGenderChange(val gender: String) : SignUpUIEvent()
    data class OnCountryCode(val countryCode: String) : SignUpUIEvent()

}