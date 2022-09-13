package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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
        // TODO: temp remove API call


        val coachList = arrayListOf(
            PlayerDetail(
                _id = "aaa",
                name = "John Cena",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "Coach"
            ),
            PlayerDetail(
                _id = "bbb",
                name = "Cena Jogn",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "Coach"
            )
        )
        val playerList = arrayListOf(
            PlayerDetail(
                _id = "aaa",
                name = "John Cena",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "PF",
                position = "PF",
                jerseyNumber = "5"
            ),
            PlayerDetail(
                _id = "bbb",
                name = "Cena Jogn",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "SF",
                position = "SF",
                jerseyNumber = "8"
            ),
            PlayerDetail(
                _id = "bbb",
                name = "Cena Jogn",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "C",
                position = "C",
                jerseyNumber = "90"
            ),
            PlayerDetail(
                _id = "bbb",
                name = "Cena Jogn",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "CF",
                position = "CF",
                jerseyNumber = "50"
            ),
            PlayerDetail(
                _id = "bbb",
                name = "Cena Jogn",
                profileImage = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
                role = "SF",
                position = "SF",
                jerseyNumber = "12"
            ),
        )
        _roasterUIState.value = _roasterUIState.value.copy(
            isLoading = false,
            playerList = playerList,
            coachList = coachList
        )

        /* viewModelScope.launch {
             getCoachPlayerByID()
         }*/
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