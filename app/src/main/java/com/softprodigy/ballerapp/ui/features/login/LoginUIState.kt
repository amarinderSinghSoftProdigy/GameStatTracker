package com.softprodigy.ballerapp.ui.features.login

import com.softprodigy.ballerapp.data.response.UserInfo


data class LoginUIState(
    val isDataLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: UserInfo? = null
)
