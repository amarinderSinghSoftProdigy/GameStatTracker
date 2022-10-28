package com.allballapp.android.ui.features.login

import com.allballapp.android.data.response.UserInfo


data class LoginUIState(
    val isDataLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: UserInfo? = null
)
