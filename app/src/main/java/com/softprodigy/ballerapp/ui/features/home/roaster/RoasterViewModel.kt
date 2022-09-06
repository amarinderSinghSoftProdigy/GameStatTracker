package com.softprodigy.ballerapp.ui.features.home.roaster

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.Roaster
import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.features.home.teams.TeamChannel
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.TeamSetupUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
            getCoach()
        }
    }

    private fun getCoach() {
        val coach = ArrayList<PlayerDetail>()
        coach.add(
            PlayerDetail(firstName = "Harsh", role = "Coach")
        )
        coach.add(
            PlayerDetail(firstName = "Kaushal", role = "Coach")
        )
        _roasterUIState.value = _roasterUIState.value.copy(isLoading = false, coachList = coach)
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
                            playerList = response.data.playerDetails as ArrayList<PlayerDetail>
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