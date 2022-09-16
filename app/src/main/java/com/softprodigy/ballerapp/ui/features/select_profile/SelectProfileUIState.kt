package com.softprodigy.ballerapp.ui.features.select_profile

import com.softprodigy.ballerapp.data.response.SelectProfileResponse

data class SelectProfileUIState(
    val isLoading: Boolean = false,
    val selectProfileList: List<SelectProfileResponse> = arrayListOf(),
    val isSelectedRole: String = ""
)