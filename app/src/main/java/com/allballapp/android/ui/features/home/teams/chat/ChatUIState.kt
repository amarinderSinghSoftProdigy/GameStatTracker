package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.allballapp.android.data.response.team.Team
import com.cometchat.pro.models.Conversation


data class ChatUIState(
    val teamIndex: Int = 0,
    val isLoading: Boolean = false,
    val teams: List<Team> = ArrayList(),

    /*New conversation states*/
    val searchText:String="",
    val selectedPlayersForNewGroup: SnapshotStateList<String> = mutableStateListOf(),
    val selectedCoachesForNewGroup: SnapshotStateList<String> = mutableStateListOf(),
    val showCreateGroupNameDialog: Boolean = false,
    val groupName: String = "",
    val conversation: List<Conversation?>? = listOf()
)
