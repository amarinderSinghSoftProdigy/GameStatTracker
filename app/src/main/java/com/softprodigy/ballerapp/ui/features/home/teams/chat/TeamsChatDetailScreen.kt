package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageList
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding

@Composable
fun TeamsChatDetailScreen(vm: ChatViewModel = hiltViewModel()) {
    Column {
        AndroidViewBinding(FragmentConversationBinding::inflate) {
            converstionContainer.getFragment<CometChatMessageList>()
        }
    }
}