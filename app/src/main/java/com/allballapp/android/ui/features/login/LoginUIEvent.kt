package com.allballapp.android.ui.features.login


import com.allballapp.android.data.SocialUserModel

sealed class LoginUIEvent {
    data class Submit(val email: String, val password: String) : LoginUIEvent()
    data class OnFacebookClick(val socialUser: SocialUserModel) : LoginUIEvent()
    data class OnTwitterClick(val socialUser: SocialUserModel) : LoginUIEvent()
    data class OnGoogleClick(val socialUser: SocialUserModel) : LoginUIEvent()

}