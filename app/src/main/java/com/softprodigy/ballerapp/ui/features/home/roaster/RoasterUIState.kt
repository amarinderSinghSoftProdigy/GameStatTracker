package com.softprodigy.ballerapp.ui.features.home.roaster

import com.softprodigy.ballerapp.data.response.Roaster

data class RoasterUIState(
    val roasterCoachesList: ArrayList<Roaster> = ArrayList(),
    val roasterTeamList: ArrayList<Roaster> = ArrayList(),
    )