package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.ManageLeaderBoardResponse
import com.softprodigy.ballerapp.ui.utils.dragDrop.ItemPosition
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class ManageLeaderBoardViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    fun isDragEnabled(pos: ItemPosition) = true

    private val _manageLeaderBoardUiState = mutableStateOf(ManageLeaderBoardUIState())
    val manageLeaderBoardUiState: State<ManageLeaderBoardUIState> = _manageLeaderBoardUiState

    fun moveItem(from: ItemPosition, to: ItemPosition) {
        _manageLeaderBoardUiState.value =
            _manageLeaderBoardUiState.value.copy(
                leaderBoardList = _manageLeaderBoardUiState.value.leaderBoardList.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    }
            )
    }

    private fun updateSelection(name: String) {
        val list = _manageLeaderBoardUiState.value.selected
        if (name == "All") {
            _manageLeaderBoardUiState.value.leaderBoardList.forEach {
                if (!list.contains(it.name))
                    list.add(it.name)
            }
        } else if (name.isEmpty()) {
            list.clear()
        } else {
            if (list.contains(name)) {
                list.remove(name)
            } else {
                list.add(name)
            }
        }
        _manageLeaderBoardUiState.value =
            _manageLeaderBoardUiState.value.copy(
                selected = list,
                leaderBoardList = _manageLeaderBoardUiState.value.leaderBoardList.toMutableList()
            )
    }

    fun onEvent(event: ManageLeaderBoardUIEvent) {

        when (event) {
            is ManageLeaderBoardUIEvent.OnItemSelected -> {
                updateSelection(event.name)
            }
        }

    }

    init {
        getLeaderBoard()
    }

    private fun getLeaderBoard() {

        val leaderBoardData = ArrayList<ManageLeaderBoardResponse>()
        leaderBoardData.add(ManageLeaderBoardResponse("Award points"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total game points"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total game rebounds"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total 3's"))
        leaderBoardData.add(ManageLeaderBoardResponse("Total FT's"))
        leaderBoardData.add(ManageLeaderBoardResponse("FG%"))
        leaderBoardData.add(ManageLeaderBoardResponse("3pt%"))
        leaderBoardData.add(ManageLeaderBoardResponse("Rebounding"))
        leaderBoardData.add(ManageLeaderBoardResponse("Steals"))
        leaderBoardData.add(ManageLeaderBoardResponse("Assists"))
        leaderBoardData.add(ManageLeaderBoardResponse("Practice 3s"))

        _manageLeaderBoardUiState.value =
            _manageLeaderBoardUiState.value.copy(leaderBoardList = leaderBoardData)
    }
}