package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail

data class RoasterUIState(
    //val roasterCoachesList: ArrayList<Roaster> = ArrayList(),
    val playerList: ArrayList<PlayerDetail> = ArrayList(),
    val isLoading: Boolean = false,
    val coachList: ArrayList<PlayerDetail> = ArrayList()
)