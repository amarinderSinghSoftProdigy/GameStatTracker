package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.runtime.snapshots.SnapshotStateList


sealed class ChatUIEvent {

  data class OnCoachChange(val selectedCoaches: SnapshotStateList<String>) : ChatUIEvent()
  data class OnPlayerChange(val selectedPlayers: SnapshotStateList<String>) : ChatUIEvent()
  data class ShowDialog(val showDialog: Boolean) : ChatUIEvent()
  data class OnGroupNameChange(val groupName: String) : ChatUIEvent()
  object OnInitiateNewConversation : ChatUIEvent()
  object GetChatListing : ChatUIEvent()
  object ClearData : ChatUIEvent()
  data class GetAllMembers(val teamId: String) : ChatUIEvent()
  data class TeamSelectionChange(val teamIndex: Int) : ChatUIEvent()

}