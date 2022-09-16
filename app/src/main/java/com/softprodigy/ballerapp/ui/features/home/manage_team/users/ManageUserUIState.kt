package com.softprodigy.ballerapp.ui.features.home.manage_team.users

import com.softprodigy.ballerapp.data.response.ManagedUserResponse

data class ManageUserUIState(
    val isLoading: Boolean = false,
    val coachList: List<ManagedUserResponse> = arrayListOf()
)