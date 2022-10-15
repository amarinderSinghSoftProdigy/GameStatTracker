package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding

@Composable
fun TeamsChatScreen(onTeamItemClick: () -> Unit) {

    Column {
        AndroidViewBinding(FragmentConversationBinding::inflate) {
            converstionContainer.getFragment<CometChatConversationList>()
        }
    }
}