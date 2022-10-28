package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.SwapUser

data class SignUpUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var successMessage: String? = null,
    var signUpData: SignUpData = SignUpData(),
    var profileList: ArrayList<SwapUser> = ArrayList(),
    var phoneCode: String? = null,
    var registered: Boolean = false,
)
