package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import com.softprodigy.ballerapp.ui.utils.dragDrop.ItemPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor(
    private val teamRepo: ITeamRepository,
    private val dataStoreManager: DataStoreManager,
    ) : ViewModel() {
    private val _leaderChannel = Channel<LeaderBoardChannel>()
    val leaderChannel = _leaderChannel.receiveAsFlow()


    private val _leaderUiState = mutableStateOf(LeaderBoardUiState())
    var leaderUiState: State<LeaderBoardUiState> = _leaderUiState
        private set
    fun isDragEnabled(pos: ItemPosition) =  true

    fun moveItem(from: ItemPosition, to: ItemPosition) {
        _leaderUiState.value =
            _leaderUiState.value.copy(leaders = _leaderUiState.value.leaders.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
            )
    }

    init {
        viewModelScope.launch {
            getLeaderBoard()
        }
        // TODO: temp data for design
//        _leaderUiState.value = LeaderBoardUiState(
//            leaders = mutableListOf(
//                LeaderBoard(
//                    "1111111111",
//                    "Joe",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "1490"
//                ),
//                LeaderBoard(
//                    "222222222",
//                    "Charlie",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "3249"
//                ),
//                LeaderBoard(
//                    "33333333",
//                    "George",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "7936"
//                ),
//                LeaderBoard(
//                    "444444444",
//                    "Jacob",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "2343"
//                ),
//                LeaderBoard(
//                    "111111111",
//                    "Joe",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "1490"
//                ),
//                LeaderBoard(
//                    "22222222",
//                    "Charlie",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "3249"
//                ),
//                LeaderBoard(
//                    "3333333",
//                    "George",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "7936"
//                ),
//                LeaderBoard(
//                    "44444444",
//                    "Jacob",
//                    "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
//                    "2343"
//                )
//            )
//        )
    }

    private suspend fun getLeaderBoard() {
        _leaderUiState.value =
            _leaderUiState.value.copy(
                isLoading = true
            )
        dataStoreManager.getId.collect {
            val leaderReponse = teamRepo.getLeaderBoard(it)

            when (leaderReponse) {
                is ResultWrapper.GenericError -> {
                    _leaderChannel.send(
                        LeaderBoardChannel.ShowToast(
                            UiText.DynamicString(
                                "${leaderReponse.message}"
                            )
                        )
                    )
                    _leaderUiState.value =
                        _leaderUiState.value.copy(
                            isLoading = false
                        )
                }
                is ResultWrapper.NetworkError -> {
                    _leaderChannel.send(
                        LeaderBoardChannel.ShowToast(
                            UiText.DynamicString(
                                leaderReponse.message
                            )
                        )
                    )
                    _leaderUiState.value =
                        _leaderUiState.value.copy(
                            isLoading = false
                        )
                }
                is ResultWrapper.Success -> {
                    leaderReponse.value.let { response ->
                        if (response.status) {
                            _leaderUiState.value =
                                _leaderUiState.value.copy(
                                    players = response.data.players,
                                    leaderBoard = response.data.teamLeaderBoard,
                                    isLoading = false
                                )
                        } else {
                            _leaderUiState.value =
                                _leaderUiState.value.copy(isLoading = false)
                            _leaderChannel.send(
                                LeaderBoardChannel.ShowToast(
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

    fun onEvent(event: LeaderBoardUIEvent) {
        when (event) {
            is LeaderBoardUIEvent.OnLeaderSelected -> {
                _leaderUiState.value =
                    _leaderUiState.value.copy(selectedLeader = event.leader)
            }
        }
    }
}

sealed class LeaderBoardChannel {
    data class ShowToast(val message: UiText) : LeaderBoardChannel()
}