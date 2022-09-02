package com.softprodigy.ballerapp.ui.features.home.teams

import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.Team


sealed class TeamUIEvent {
    data class OnTeamSelected(val team: Team) : TeamUIEvent()
    object OnConfirmTeamClick : TeamUIEvent()
    object OnDismissClick : TeamUIEvent()
    data class ShowToast(val message:String) : TeamUIEvent()

    // TODO: temp
    data class OnStandingSelected(val standing: Standing) : TeamUIEvent()

}