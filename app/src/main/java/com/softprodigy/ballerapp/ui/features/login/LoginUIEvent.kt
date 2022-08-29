package com.softprodigy.ballerapp.ui.features.login

import com.softprodigy.ballerapp.data.GoogleUserModel

sealed class LoginUIEvent {
    data class Submit(val email: String, val password: String) : LoginUIEvent()
    data class OnGoogleClick(val googleUser: GoogleUserModel) : LoginUIEvent()

}