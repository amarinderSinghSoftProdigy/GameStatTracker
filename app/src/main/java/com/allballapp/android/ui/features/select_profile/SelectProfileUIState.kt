package com.allballapp.android.ui.features.select_profile

import com.allballapp.android.data.response.SelectProfileResponse

data class SelectProfileUIState(
    val isLoading: Boolean = false,
    val selectProfileList: List<SelectProfileResponse> = arrayListOf(),
    val isSelectedRole: String = ""
)