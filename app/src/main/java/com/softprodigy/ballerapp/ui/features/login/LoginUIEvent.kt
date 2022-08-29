package com.softprodigy.ballerapp.ui.features.login


import com.softprodigy.ballerapp.data.SocialUserModel

sealed class LoginUIEvent {
    data class Submit(val email: String, val password: String) : LoginUIEvent()
    data class OnFacebookClick(val socialUser: SocialUserModel) : LoginUIEvent()
    data class OnGoogleClick(val socialUser: SocialUserModel) : LoginUIEvent()

}