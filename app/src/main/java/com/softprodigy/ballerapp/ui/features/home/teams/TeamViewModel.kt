package com.softprodigy.ballerapp.ui.features.home.teams

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.UpdateTeamDetailRequest
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.utils.CommonUtils
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
    fun isRoasterDragEnabled(pos: ItemPosition) = true
    fun moveItem(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                leaderBoard = _teamUiState.value.leaderBoard.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    }
            )
    }

    fun moveItemRoaster(from: ItemPosition, to: ItemPosition) {
        _teamUiState.value =
            _teamUiState.value.copy(
                roasterTabs = _teamUiState.value.roasterTabs.toMutableList()
                    .apply {
                        add(to.index, removeAt(from.index))
                    } as ArrayList<Player>
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

    init {
        viewModelScope.launch {
            getTeams()
        }
    }

    fun onEvent(event: TeamUIEvent) {
        when (event) {
            is TeamUIEvent.OnConfirmTeamClick -> {
                viewModelScope.launch {
                    dataStoreManager.setId(event.teamId)
                    getTeamByTeamId(event.teamId)
                }
            }
            is TeamUIEvent.OnDismissClick -> {}

            is TeamUIEvent.OnTeamUpdate -> {
                viewModelScope.launch {
                    updateTeam()
                }
            }

            is TeamUIEvent.ShowToast -> {
                viewModelScope.launch {
                    showToast(event.message)
                }
            }
            is TeamUIEvent.OnTeamSelected -> {
                _teamUiState.value = _teamUiState.value.copy(selectedTeam = event.team)
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

                        if (_teamUiState.value.selectedTeam == null && response.data.size > 0) {
                            var selectionTeam: Team? = null
                            response.data.toMutableList().forEach {
                                if (UserStorage.teamId == it._id) {
                                    selectionTeam = it
                                }
                            }
                            _teamUiState.value =
                                _teamUiState.value.copy(
                                    teams = response.data,
                                    selectedTeam = if (selectionTeam == null) response.data[0] else selectionTeam,
                                    isLoading = true
                                )
                            val idToSearch ="63156ea1a13d3cc299a41ab6"
                                //if (selectionTeam == null) response.data[0]._id else selectionTeam?._id
                            getTeamByTeamId(idToSearch ?: "")
                            viewModelScope.launch {
                                dataStoreManager.setId(idToSearch ?: "")
                            }
                        }
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

    private suspend fun updateTeam() {
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = true
            )

        //Need to update the request object.
        val request = UpdateTeamDetailRequest(
            id = UserStorage.teamId,
            leaderboardPoints = _teamUiState.value.leaderBoard,
            playerPositions = _teamUiState.value.roaster,
            name = _teamUiState.value.teamName,
            logo = _teamUiState.value.teamImageUri ?: "",
            colorCode = _teamUiState.value.teamColor,
        )
        val teamResponse = teamRepo.updateTeamDetails(request)
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
                        //getTeamByTeamId("")
                        //Handle the case to move back to teams screen and hit the get team deatil api
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

        val teamResponse = teamRepo.getTeamsByTeamID(userId)
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
                            players = response.data.players,
                            roasterTabs = CommonUtils.getPlayerTabs(response.data.players),
                            coaches = response.data.coaches,
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