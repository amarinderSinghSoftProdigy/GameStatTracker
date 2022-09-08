package com.softprodigy.ballerapp.ui.features.home.manage_team.teams

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ManageTeamViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _manageTeamChannel = Channel<ManageTeamChannel>()
    val teamSetupChannel = _manageTeamChannel.receiveAsFlow()

    private val _manageTeamUiState = mutableStateOf(ManageTeamUIState())
    val manageTeamUiState: State<ManageTeamUIState> = _manageTeamUiState

    fun onEvent(event: ManageTeamUIEvent) {
        when (event) {
            is ManageTeamUIEvent.OnColorSelected -> {
                _manageTeamUiState.value =
                    _manageTeamUiState.value.copy(teamColor = event.selectedColor)
            }

            is ManageTeamUIEvent.OnImageSelected -> {
                _manageTeamUiState.value =
                    _manageTeamUiState.value.copy(teamImageUri = event.teamImageUri)
            }

            is ManageTeamUIEvent.OnTeamNameChange -> {
                _manageTeamUiState.value = _manageTeamUiState.value.copy(teamName = event.teamName)

            }
        }
    }
}

sealed class ManageTeamChannel {
    data class ShowToast(val message: UiText) : ManageTeamChannel()

}