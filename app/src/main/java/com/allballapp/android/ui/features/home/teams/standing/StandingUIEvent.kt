package com.allballapp.android.ui.features.home.teams.standing

import com.allballapp.android.data.response.Standing

sealed class StandingUIEvent {
    data class OnStandingSelected(val standing: Standing) : StandingUIEvent()
}