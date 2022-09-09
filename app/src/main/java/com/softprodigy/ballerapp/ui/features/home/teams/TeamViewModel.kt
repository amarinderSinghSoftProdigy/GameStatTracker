package com.softprodigy.ballerapp.ui.features.home.teams

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
) : ViewModel() {

    private val _teamChannel = Channel<TeamChannel>()
    val teamChannel = _teamChannel.receiveAsFlow()

    private val _teamUiState = mutableStateOf(TeamUIState())
    val teamUiState: State<TeamUIState> = _teamUiState

    init {
        viewModelScope.launch {
            getTeams()
        }
    }

    fun onEvent(event: TeamUIEvent) {
        when (event) {
            is TeamUIEvent.OnConfirmTeamClick -> {
                viewModelScope.launch { getTeamByTeamId() }
            }
            is TeamUIEvent.OnDismissClick -> {}
            is TeamUIEvent.ShowToast -> {
                viewModelScope.launch {
                    showToast(event.message)
                }
            }
            is TeamUIEvent.OnTeamSelected -> {
                _teamUiState.value = _teamUiState.value.copy(selectedTeam = event.team)
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
                isLoading = true)
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
                                isLoading = false)
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

    private suspend fun getTeamByTeamId() {
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = true
            )
        val teamResponse =
            teamRepo.getTeamsByTeamID(_teamUiState.value.selectedTeam?.Id ?: "")
        _teamUiState.value =
            _teamUiState.value.copy(
                isLoading = false
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
                        Timber.i("TeamsGetSuccessfully ${response.data.name}")

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
}