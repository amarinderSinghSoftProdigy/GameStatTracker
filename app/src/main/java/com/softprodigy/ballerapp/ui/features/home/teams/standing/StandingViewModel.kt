package com.softprodigy.ballerapp.ui.features.home.teams.standing

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.Standing
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
        viewModelScope.launch {
            getStandings()
        }
        // TODO: temp data for design
//        getStandings()
//
//        val standingData= arrayListOf<Standing>(
//            Standing("aaaaaaa", name = "Team 1", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "10-5"),
//            Standing("addaaaa", name = "Team 3", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "7-10"),
//            Standing("bb", name = "Team 4", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "8-9"),
//            Standing("fdsf", name = "Team 5", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "1-33"),
//            Standing("addaafsdfdaa", name = "Team 6", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "3-4"),
//            Standing("fds", name = "Team 7", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "8-1"),
//            Standing("gsfg", name = "Team 8", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "7-10"),
//            Standing("gds", name = "Team 9", logo = "profileImage/1662458857474-selected_image_4465069980681186921.jpg", standings = "9-10"),
//
//            )
//        _standingUiState.value =
//            _standingUiState.value.copy(
//                standing = standingData,
//                isLoading = false
//            )
    }

    private fun getStandings() {
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
                                standing = response.data.teamsStandings,
                                categories = response.data.categories,
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

