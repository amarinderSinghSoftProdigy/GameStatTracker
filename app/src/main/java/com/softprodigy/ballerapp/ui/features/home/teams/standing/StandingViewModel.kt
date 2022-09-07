package com.softprodigy.ballerapp.ui.features.home.teams.standing

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
import javax.inject.Inject

@HiltViewModel
class StandingViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
) : ViewModel() {
    private val _standingChannel = Channel<StandingChannel>()
    val standingChannel = _standingChannel.receiveAsFlow()

    private val _standingUiState = mutableStateOf(StandingUIState())
    var standingUiState: State<StandingUIState> = _standingUiState
        private set

    init {
        viewModelScope.launch { getTeamStanding() }
    }

    fun onEvent(event: StandingUIEvent) {
        when (event) {
            is StandingUIEvent.OnStandingSelected -> {
                _standingUiState.value =
                    _standingUiState.value.copy(selectedStanding = event.standing)
            }
        }
    }

    suspend fun getTeamStanding() {
        _standingUiState.value = _standingUiState.value.copy(isLoading = true)
        val standingResponse = teamRepo.getTeamsStanding()

        _standingUiState.value = _standingUiState.value.copy(isLoading = false)
        when (standingResponse) {
            is ResultWrapper.GenericError -> {
                _standingChannel.send(
                    StandingChannel.ShowToast(
                        UiText.DynamicString(
                            "${standingResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _standingChannel.send(
                    StandingChannel.ShowToast(
                        UiText.DynamicString(
                            standingResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                standingResponse.value.let { response ->
                    if (response.status) {
                        _standingUiState.value =
                            _standingUiState.value.copy(
                                standing = response.data,
                                isLoading = false
                            )
                    } else {
                        _standingUiState.value =
                            _standingUiState.value.copy(isLoading = false)
                        _standingChannel.send(
                            StandingChannel.ShowToast(
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


sealed class StandingChannel {
    data class ShowToast(val message: UiText) : StandingChannel()
}

