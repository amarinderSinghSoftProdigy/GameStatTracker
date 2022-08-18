package com.softprodigy.ballerapp.ui.features.sign_up

import com.softprodigy.ballerapp.data.UserInfo


data class SignUpUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var user: UserInfo? = null
)
