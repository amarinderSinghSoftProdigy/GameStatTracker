package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar

@Composable
fun TeamsChatScreen(vm: ChatViewModel = hiltViewModel(), onTeamItemClick: () -> Unit) {
    val state = vm.chatUiState.value
    val selected = remember {
        mutableStateOf(0)
    }
    Column {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        LazyRow {
            itemsIndexed(state.teams) { index, item ->
                Row {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                    AppTab(
                        title = item.name,
                        selected = index == selected.value,
                        onClick = {
                            selected.value = index
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        AndroidViewBinding(FragmentConversationBinding::inflate) {
            converstionContainer.getFragment<CometChatConversationList>()
        }
    }

    if (state.isLoading) {
        CommonProgressBar()
    }
}