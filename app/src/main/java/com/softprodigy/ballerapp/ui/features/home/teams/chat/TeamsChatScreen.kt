package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageList
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding

@Composable
fun TeamsChatScreen(onTeamItemClick: () -> Unit) {
    val check = mutableStateOf(true)
    val scope = rememberCoroutineScope()
    Box {
        if (check.value) {
            AndroidViewBinding(FragmentConversationBinding::inflate) {
                converstionContainer.getFragment<CometChatConversationList>()
            }
        } else {
            AndroidViewBinding(FragmentConversationBinding::inflate) {
                converstionContainer.getFragment<CometChatMessageList>()
            }
        }
    }
}