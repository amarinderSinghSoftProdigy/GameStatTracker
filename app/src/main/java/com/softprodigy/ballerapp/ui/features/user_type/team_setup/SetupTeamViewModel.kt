package com.softprodigy.ballerapp.ui.features.user_type.team_setup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SetupTeamViewModel @Inject constructor(private val teamRepo: ITeamRepository) : ViewModel() {

    private val _teamSetupChannel = Channel<TeamSetupChannel>()
    val teamSetupChannel = _teamSetupChannel.receiveAsFlow()


    private val _teamSetupUiState = mutableStateOf(TeamSetupUIState())
    val teamSetupUiState: State<TeamSetupUIState> = _teamSetupUiState


    fun onEvent(event: TeamSetupUIEvent) {
        when (event) {
            is TeamSetupUIEvent.OnColorSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamColor = event.selectedColor)
            }
            is TeamSetupUIEvent.OnImageSelected -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(teamImageUri = event.teamImageUri)

            }
            is TeamSetupUIEvent.OnTeamNameChange -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(teamName = event.teamName)

            }
            TeamSetupUIEvent.OnTeamSetupNextClick -> {
                viewModelScope.launch {
                    _teamSetupChannel.send(TeamSetupChannel.OnTeamSetupNextClick)
                }
            }
            is TeamSetupUIEvent.OnSearchPlayer -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(search = event.searchPlayerQuery)
                if (event.searchPlayerQuery.length > 2) {
                    viewModelScope.launch {
                        getPlayers(event.searchPlayerQuery)
                    }
                }
            }
            is TeamSetupUIEvent.OnAddPlayerClick -> {
                if (!_teamSetupUiState.value.selectedPlayers.contains(event.player)) {
                    _teamSetupUiState.value =
                        _teamSetupUiState.value.copy(selectedPlayers = (((_teamSetupUiState.value.selectedPlayers) + event.player) as ArrayList<Player>))
                } else {
                    viewModelScope.launch {
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
                                UiText.StringResource(
                                    R.string.player_already_added
                                )
                            )
                        )
                    }
                }
            }
            is TeamSetupUIEvent.OnDismissDialogCLick -> {
                _teamSetupUiState.value =
                    _teamSetupUiState.value.copy(showDialog = event.showDialog)

            }
            is TeamSetupUIEvent.OnRemovePlayerClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = true,
                    removePlayer = event.player,
                )

            }
            is TeamSetupUIEvent.OnRemovePlayerConfirmClick -> {
                _teamSetupUiState.value = _teamSetupUiState.value.copy(
                    showDialog = false,
                    selectedPlayers = (((_teamSetupUiState.value.selectedPlayers) - event.player) as ArrayList<Player>)
                )

            }
        }
    }

    private suspend fun getPlayers(searchQuery: String) {

        when (val playersResponse = teamRepo.getAllPlayers(searchPlayer = searchQuery)) {
            is ResultWrapper.GenericError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            "${playersResponse.code} ${playersResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _teamSetupChannel.send(
                    TeamSetupChannel.ShowToast(
                        UiText.DynamicString(
                            playersResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                playersResponse.value.let { response ->
                    if (response.status) {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(
                                players = response.data,
                                isLoading = false
                            )
                    } else {
                        _teamSetupUiState.value =
                            _teamSetupUiState.value.copy(isLoading = false)
                        _teamSetupChannel.send(
                            TeamSetupChannel.ShowToast(
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


sealed class TeamSetupChannel {
    data class ShowToast(val message: UiText) : TeamSetupChannel()
    object OnTeamSetupNextClick : TeamSetupChannel()
}