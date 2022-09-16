package com.softprodigy.ballerapp.ui.features.home.teams.standing

import com.softprodigy.ballerapp.data.response.Standing

sealed class StandingUIEvent {
    data class OnStandingSelected(val standing: Standing) : StandingUIEvent()
}