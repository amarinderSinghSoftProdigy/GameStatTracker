package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.request.SignUpData

data class SignUpUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var successMessage: String? = null,
    var signUpData: SignUpData = SignUpData(),
//    val token: String? = null,
    var phoneCode: String ?= null,
    var registered: Boolean = false,
    var isSocialUser:Boolean=false
)
