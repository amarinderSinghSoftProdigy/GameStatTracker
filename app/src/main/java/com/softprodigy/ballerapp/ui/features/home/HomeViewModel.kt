package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.data.repository.TeamRepository
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
) : ViewModel() {

    private val _homeChannel = Channel<HomeChannel>()
    val homeChannel = _homeChannel.receiveAsFlow()

    private val _homeUiState = mutableStateOf(HomeUIState())
    val homeUiState: State<HomeUIState> = _homeUiState

    fun onEvent(event: HomeUIEvent) {
        when (event) {
            HomeUIEvent.OnConfirmClick -> {}
            HomeUIEvent.OnDismissClick -> {}
            is HomeUIEvent.OnHomeSelected -> {}
        }
    }
}

sealed class HomeChannel {
    data class ShowToast(val message: UiText) : HomeChannel()
}