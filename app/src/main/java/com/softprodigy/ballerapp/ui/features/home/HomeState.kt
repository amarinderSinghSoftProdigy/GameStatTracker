package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.ui.graphics.Color


data class HomeState(
    val color: Color? = null,
    val isDataLoading: Boolean = false,
    val errorMessage: String? = null,
    val userNameValid: Boolean = true,
    val userNameValidError:String? = null
)
