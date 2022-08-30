package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
) : ViewModel() {

    private val _homeChannel = Channel<HomeChannel>()
    val homeChannel = _homeChannel.receiveAsFlow()

    private val _homeUiState = mutableStateOf(HomeUIState())
    val homeUiState: State<HomeUIState> = _homeUiState

    init {
        viewModelScope.launch {
            getTeams()
        }
    }

    fun onEvent(event: HomeUIEvent) {
        when (event) {
            HomeUIEvent.OnConfirmClick -> {}
            HomeUIEvent.OnDismissClick -> {}
            is HomeUIEvent.OnTeamSelected -> {}
        }
    }

    private suspend fun getTeams() {
        when (val teamResponse = teamRepo.getTeams()) {
            is ResultWrapper.GenericError -> {
                _homeChannel.send(
                    HomeChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.code} ${teamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _homeChannel.send(
                    HomeChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status) {
                        _homeUiState.value =
                            _homeUiState.value.copy(teams = response.data, isLoading = false)
                    } else {
                        _homeUiState.value =
                            _homeUiState.value.copy(isLoading = false)
                        _homeChannel.send(
                            HomeChannel.ShowToast(
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

sealed class HomeChannel {
    data class ShowToast(val message: UiText) : HomeChannel()
}