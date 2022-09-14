package com.softprodigy.ballerapp.ui.features.home.teams

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.utils.dragDrop.ItemPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _teamChannel = Channel<TeamChannel>()
    val teamChannel = _teamChannel.receiveAsFlow()

    private val _teamUiState = mutableStateOf(TeamUIState())
    val teamUiState: State<TeamUIState> = _teamUiState
    fun isDragEnabled(pos: ItemPosition) = true
    fun moveItem(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                leaderBoard = _teamUiState.value.leaderBoard.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    }
            )
    }

    private fun updateSelection(name: String) {
        val list = _teamUiState.value.selected
        if (name == "All") {
            _teamUiState.value.leaderBoard.forEach {
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
        _teamUiState.value =
            _teamUiState.value.copy(
                selected = list,
                leaderBoard = _teamUiState.value.leaderBoard.toMutableList()
            )
    }

    var userId = ""

    init {
        viewModelScope.launch {
            getTeams()
            getTeamById()
        }
    }

    private suspend fun getTeamById() {
        dataStoreManager.getId.collect{
            getTeamByTeamId(it)
        }

    }

    fun onEvent(event: TeamUIEvent) {
        when (event) {
            is TeamUIEvent.OnConfirmTeamClick -> {
                viewModelScope.launch {
/*
                    dataStoreManager.getId.map {
                        Log.d("harsh", "onEvent: " + it)
                        userId = it
                    }
                        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(500L), 1)
*/

                    dataStoreManager.getId.collect{
                        userId = it
                    }
                    getTeamByTeamId(userId)
                }
            }
            is TeamUIEvent.OnDismissClick -> {}

            is TeamUIEvent.ShowToast -> {
                viewModelScope.launch {
                    showToast(event.message)
                }
            }
            is TeamUIEvent.OnTeamSelected -> {
                _teamUiState.value = _teamUiState.value.copy(selectedTeam = event.team)
                viewModelScope.launch {
                    dataStoreManager.setId(event.team._id)
                }
            }
            is TeamUIEvent.OnColorSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamColor = event.selectedColor)
            }

            is TeamUIEvent.OnImageSelected -> {
                _teamUiState.value =
                    _teamUiState.value.copy(teamImageUri = event.teamImageUri)
            }

            is TeamUIEvent.OnTeamNameChange -> {
                _teamUiState.value = _teamUiState.value.copy(teamName = event.teamName)
            }

            is TeamUIEvent.OnItemSelected -> {
                updateSelection(event.name)
            }
        }
    }

    private suspend fun showToast(message: String) {
        _teamChannel.send(
            TeamChannel.ShowToast(
                UiText.DynamicString(
                    message
                )
            )
        )
    }

    private suspend fun getTeams() {
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = true
            )
        val teamResponse = teamRepo.getTeams()

        when (teamResponse) {
            is ResultWrapper.GenericError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.NetworkError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
                _teamUiState.value =
                    _teamUiState.value.copy(
                        isLoading = false
                    )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status) {
                        _teamUiState.value =
                            _teamUiState.value.copy(
                                teams = response.data,
                                isLoading = false
                            )
                    } else {
                        _teamUiState.value =
                            _teamUiState.value.copy(isLoading = false)
                        _teamChannel.send(
                            TeamChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun getTeamByTeamId(userId: String) {

        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = true
            )

        val teamResponse =
            teamRepo.getTeamsByTeamID(
                _teamUiState.value.selectedTeam?._id ?: userId
            )
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = false,
            )

        when (teamResponse) {
            is ResultWrapper.GenericError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamChannel.send(
                    TeamChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status) {
                        _teamUiState.value = _teamUiState.value.copy(
                            isLoading = false,
                            players = response.data.players as ArrayList<Player>,
                            coaches = response.data.coaches as ArrayList<Coach>,
                            teamName = response.data.name,
                            teamColor = response.data.colorCode,
                            teamImageUri = response.data.logo,
                            leaderBoard = response.data.teamLeaderBoard
                        )

                        _teamChannel.send(
                            TeamChannel.OnTeamDetailsSuccess(response.data._id)
                        )

                    } else {
                        _teamUiState.value =
                            _teamUiState.value.copy(isLoading = false)
                        _teamChannel.send(
                            TeamChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed class TeamChannel {
    data class ShowToast(val message: UiText) : TeamChannel()
    data class OnTeamDetailsSuccess(val teamId: String) : TeamChannel()
}