package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.ManageLeaderBoardResponse
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class ManageLeaderBoardViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _manageLeaderBoardUiState = mutableStateOf(ManageLeaderBoardUIState())
    val manageLeaderBoardUiState: State<ManageLeaderBoardUIState> = _manageLeaderBoardUiState

    fun onEvent(event: ManageLeaderBoardUIEvent) {

        when (event) {
            is ManageLeaderBoardUIEvent.OnTeamSelected -> {
                _manageLeaderBoardUiState.value =
                    _manageLeaderBoardUiState.value.copy(checked = event.team)
            }
        }

    }

    init {
        getLeaderBoard()
    }

    private fun getLeaderBoard() {

        val leaderBoardData = ArrayList<ManageLeaderBoardResponse>()
        leaderBoardData.add(ManageLeaderBoardResponse("Award points"))
        leaderBoardData.add(ManageLeaderBoardResponse( "Total game points"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total game rebounds"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total 3's",))
        leaderBoardData.add(ManageLeaderBoardResponse(   "Total FT's",))
        leaderBoardData.add(ManageLeaderBoardResponse("FS",))
        leaderBoardData.add(ManageLeaderBoardResponse( "Rebounding",))
        leaderBoardData.add(ManageLeaderBoardResponse("Steals",))
        leaderBoardData.add(ManageLeaderBoardResponse("Award points"))
        leaderBoardData.add(ManageLeaderBoardResponse("Assists",))
        leaderBoardData.add(ManageLeaderBoardResponse("Practice 3s"))

        /*listOf(
        "Award points",
        "Total game points",
        "Total game rebounds",
        "Total 3's",
        "Total FT's",
        "FS",
        "Rebounding",
        "Steals",
        "Assists",
        "Practice 3s"
    )*/

        _manageLeaderBoardUiState.value =
            _manageLeaderBoardUiState.value.copy(leaderBoardList = leaderBoardData)
    }
}