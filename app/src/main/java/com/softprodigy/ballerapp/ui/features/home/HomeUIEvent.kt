package com.softprodigy.ballerapp.ui.features.home

import com.softprodigy.ballerapp.data.response.Team





sealed class HomeUIEvent {
    data class OnTeamSelected(val team: Team) : HomeUIEvent()
    object OnConfirmClick : HomeUIEvent()
    object OnDismissClick : HomeUIEvent()
}