package com.softprodigy.ballerapp.ui.features.confirm_phone

data class VerifyPhoneUIState(
    val isDataLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)