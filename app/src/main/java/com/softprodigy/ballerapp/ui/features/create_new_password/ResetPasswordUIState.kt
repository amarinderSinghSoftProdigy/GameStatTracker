package com.softprodigy.ballerapp.ui.features.create_new_password

import com.softprodigy.ballerapp.data.UserInfo


data class ResetPasswordUIState(
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var message: String? = null,
    var user: UserInfo? = null,
    var isPasswordError: Boolean? = null,
)