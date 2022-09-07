package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoasterViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
    application: Application
) :
    AndroidViewModel(application) {

    private val _roasterChannel = Channel<RoasterChannel>()
    val roasterChannel = _roasterChannel.receiveAsFlow()

    private val _roasterUIState = mutableStateOf(RoasterUIState())
    val roasterUIState: State<RoasterUIState> = _roasterUIState

    init {
        viewModelScope.launch {
            getCoachPlayerByID()
        }
    }

    private suspend fun getCoachPlayerByID() {
        _roasterUIState.value = _roasterUIState.value.copy(
            isLoading = true
        )

        when (val teamResponse =
            teamRepo.getTeamCoachPlayerByID("6304b10244cae324b011e1b5")) {

            is ResultWrapper.GenericError -> {
                _roasterUIState.value = _roasterUIState.value.copy(
                    isLoading = true
                )
                _roasterChannel.send(
                    RoasterChannel.ShowToast(
                        UiText.DynamicString(
                            "${teamResponse.message}"
                        )
                    )
                )

            }
            is ResultWrapper.NetworkError -> {
                _roasterUIState.value = _roasterUIState.value.copy(
                    isLoading = true
                )
                _roasterChannel.send(
                    RoasterChannel.ShowToast(
                        UiText.DynamicString(
                            teamResponse.message
                        )
                    )
                )

            }
            is ResultWrapper.Success -> {
                teamResponse.value.let { response ->
                    if (response.status) {
                        _roasterUIState.value = _roasterUIState.value.copy(
                            isLoading = false,
                            playerList = response.data.playerDetails as ArrayList<PlayerDetail>,
                            coachList = response.data.coachDetails as ArrayList<PlayerDetail>
                        )

                    } else {
                        _roasterUIState.value =
                            _roasterUIState.value.copy(isLoading = false)
                        _roasterChannel.send(
                            RoasterChannel.ShowToast(
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

sealed class RoasterChannel {
    data class ShowToast(val message: UiText) : RoasterChannel()
}