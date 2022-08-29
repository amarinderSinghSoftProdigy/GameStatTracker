package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData

sealed class SignUpUIEvent {

    data class Submit(val signUpData: SignUpData) : SignUpUIEvent()

}