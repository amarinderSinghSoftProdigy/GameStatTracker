package com.softprodigy.ballerapp.ui.features.home.roaster

import com.softprodigy.ballerapp.data.response.Roaster
import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail
import com.softprodigy.ballerapp.data.response.roaster.RoasterResponse

data class RoasterUIState(
    //val roasterCoachesList: ArrayList<Roaster> = ArrayList(),
    val playerList: ArrayList<PlayerDetail> = ArrayList(),
    val isLoading: Boolean = false,
    val coachList: ArrayList<PlayerDetail> = ArrayList()
)