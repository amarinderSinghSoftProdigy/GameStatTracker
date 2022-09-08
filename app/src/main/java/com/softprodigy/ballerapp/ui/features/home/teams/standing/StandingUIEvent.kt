package com.softprodigy.ballerapp.ui.features.home.teams.standing

import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent

sealed class StandingUIEvent {
    data class OnStandingSelected(val standing: Standing) : StandingUIEvent()
}