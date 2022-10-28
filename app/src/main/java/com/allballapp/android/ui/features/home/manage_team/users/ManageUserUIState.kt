package com.allballapp.android.ui.features.home.manage_team.users

import com.allballapp.android.data.response.ManagedUserResponse

data class ManageUserUIState(
    val isLoading: Boolean = false,
    val coachList: List<ManagedUserResponse> = arrayListOf()
)