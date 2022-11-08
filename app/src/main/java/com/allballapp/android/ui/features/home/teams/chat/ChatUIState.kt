package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.UpdateTeamDetailRequest
import com.allballapp.android.data.response.PlayerDetails
import com.allballapp.android.data.response.team.*


data class ChatUIState(
    val teamIndex: Int = 0,
    val isLoading: Boolean = false,
    val teams: ArrayList<Team> = ArrayList(),

    /*New conversation states*/
    val selectedPlayersForNewGroup:SnapshotStateList<String> = mutableStateListOf(),
    val selectedCoachesForNewGroup:SnapshotStateList<String> = mutableStateListOf(),
    val showCreateGroupNameDialog : Boolean = false,
    val groupName : String = ""
    )
