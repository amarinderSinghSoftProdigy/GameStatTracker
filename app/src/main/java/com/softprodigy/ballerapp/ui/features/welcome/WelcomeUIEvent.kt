package com.softprodigy.ballerapp.ui.features.welcome

import com.softprodigy.ballerapp.data.GoogleUserModel

sealed class WelcomeUIEvent {
    data class OnGoogleClick(val googleUser:GoogleUserModel) : WelcomeUIEvent()
}
