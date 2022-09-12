package com.softprodigy.ballerapp.ui.features.home.manage_team.users

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.ManagedUserResponse
import com.softprodigy.ballerapp.ui.features.home.manage_team.teams.ManageTeamChannel
import com.softprodigy.ballerapp.ui.features.home.manage_team.teams.ManageTeamUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ManageUserViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _manageUserChannel = Channel<ManageTeamChannel>()
    val teamSetupChannel = _manageUserChannel.receiveAsFlow()

    private val _manageUserUiState = mutableStateOf(ManageUserUIState())
    val manageUserUiState: State<ManageUserUIState> = _manageUserUiState

    init {
        getCoachData()
    }

    private fun getCoachData() {

        val coachList = ArrayList<ManagedUserResponse>()
        coachList.add(ManagedUserResponse("Sam", "Coach"))
        coachList.add(ManagedUserResponse("Michael", "Coach"))

        _manageUserUiState.value = _manageUserUiState.value.copy(coachList = coachList)
    }

}

