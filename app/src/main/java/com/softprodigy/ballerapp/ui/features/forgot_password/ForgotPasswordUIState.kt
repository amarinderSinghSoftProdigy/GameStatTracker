package com.softprodigy.ballerapp.ui.features.forgot_password

data class ForgotPasswordUIState(

    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var successMessage: String? = null,
)
