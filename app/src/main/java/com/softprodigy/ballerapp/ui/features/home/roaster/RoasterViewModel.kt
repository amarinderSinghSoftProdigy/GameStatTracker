package com.softprodigy.ballerapp.ui.features.home.roaster

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.Roaster
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.TeamSetupUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoasterViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _roasterUIState = mutableStateOf(RoasterUIState())
    val roasterUIState: State<RoasterUIState> = _roasterUIState

    init {
        addCoachesItems()
        addTeamItems()
    }

    private fun addCoachesItems() {
        val roasterCoachesList = ArrayList<Roaster>()
        roasterCoachesList.add(Roaster(name = "Sam Willow", role = "Coach"))
        roasterCoachesList.add(Roaster(name = "Sam Willow", role = "Coach"))
        _roasterUIState.value = _roasterUIState.value.copy(roasterCoachesList = roasterCoachesList)
    }

    private fun addTeamItems() {
        val teamPlayerList = ArrayList<Roaster>()
        teamPlayerList.add(Roaster(name = "Sam Willow", role = "Coach", tag = "SF"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        teamPlayerList.add(Roaster(name = "Jain", role = "77", tag = "SG"))
        _roasterUIState.value = _roasterUIState.value.copy(roasterTeamList = teamPlayerList)
    }
}