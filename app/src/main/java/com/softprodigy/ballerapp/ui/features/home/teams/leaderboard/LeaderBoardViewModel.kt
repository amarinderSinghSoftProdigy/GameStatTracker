package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.response.LeaderBoard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor() : ViewModel() {
    private val _leaderChannel = Channel<LeaderBoardChannel>()
    val leaderChannel = _leaderChannel.receiveAsFlow()

    private val _leaderUiState = mutableStateOf(LeaderBoardUiState())
    var leaderUiState: State<LeaderBoardUiState> = _leaderUiState
        private set

    init {
        // TODO: temp data for design
        _leaderUiState.value = LeaderBoardUiState(
            leaders = mutableListOf(
                LeaderBoard(
                    "1111111111",
                    "Joe",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "1490"
                ),
                LeaderBoard(
                    "222222222",
                    "Charlie",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "3249"
                ),
                LeaderBoard(
                    "33333333",
                    "George",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "7936"
                ),
                LeaderBoard(
                    "444444444",
                    "Jacob",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "2343"
                ),
                LeaderBoard(
                    "111111111",
                    "Joe",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "1490"
                ),
                LeaderBoard(
                    "22222222",
                    "Charlie",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "3249"
                ),
                LeaderBoard(
                    "3333333",
                    "George",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "7936"
                ),
                LeaderBoard(
                    "44444444",
                    "Jacob",
                    "http://ballerapp.s3.us-east-2.amazonaws.com/teamLogo/1662473370996-IMG_20220805_120020_710.jpg",
                    "2343"
                )
            )
        )
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