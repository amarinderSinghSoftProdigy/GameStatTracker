package com.softprodigy.ballerapp.ui.features.confirm_phone

import com.softprodigy.ballerapp.ui.features.login.LoginUIEvent

sealed class VerifyPhoneUIEvent {

    data class Submit(val phoneNumber: String) : VerifyPhoneUIEvent()
    data class Confirm(val phoneNumber: String, val otp: String) : VerifyPhoneUIEvent()

}