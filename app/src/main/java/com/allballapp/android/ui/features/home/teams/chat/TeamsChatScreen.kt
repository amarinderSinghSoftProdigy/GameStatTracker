package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.databinding.FragmentConversationBinding
import com.allballapp.android.ui.features.components.AppTab
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.theme.appColors
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity

@Composable
fun TeamsChatScreen(
    color: String = "",
    homeVm:HomeViewModel,
    vm: ChatViewModel = hiltViewModel(),
    onTeamItemClick: () -> Unit,
    onCreateNewConversationClick: (teamId: String) -> Unit
) {

    val state = vm.chatUiState.value
    val selected = rememberSaveable {
        mutableStateOf(-1)
    }
    val key = remember { mutableStateOf("selected") }

    remember {
        homeVm.getUnreadMessageCount()
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                                addChatIds(item)
                                selected.value = index
                                key.value = index.toString()
                            })
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            if (selected.value != -1) {
                key(key.value) {
                    AndroidViewBinding(FragmentConversationBinding::inflate) {
                        CometChatMessageListActivity.toolbarColor =
                            if (color.startsWith("#")) color else "#" + color
                        converstionContainer.getFragment<CometChatConversationList>()
                    }
                }
            } else if (state.teams.isNotEmpty()) {
                addChatIds(state.teams[0])
                key.value = 0.toString()
                selected.value = 0
            } else {
                EmptyScreen(
                    singleText = false,
                    icon = com.cometchat.pro.uikit.R.drawable.ic_chat_placeholder,
                    heading = stringResource(id = com.cometchat.pro.uikit.R.string.no_conversations)
                )
            }
        }
        IconButton(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .size(dimensionResource(id = R.dimen.size_44dp))
                .background(
                    MaterialTheme.appColors.material.primaryVariant,
                    RoundedCornerShape(50)
                )
                .align(Alignment.BottomEnd),
            enabled = true,
            onClick = {
                if (selected.value != -1)
                    onCreateNewConversationClick.invoke(state.teams[selected.value]._id)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_button),
                "",
                tint = MaterialTheme.appColors.buttonColor.textEnabled,
                modifier = Modifier
                    .width(
                        dimensionResource(id = R.dimen.size_16dp)
                    )
            )
        }
        if (state.isLoading) {
            CommonProgressBar()
        }

    }

}

fun addChatIds(item: Team) {
    val mergedIds = mutableListOf<String>()
    val playerIds = item.players.map {
        it._id
    }
    val coachIds = item.coaches.map {
        it._id
    }
    val groupId = item.teamChatGroups.map {
        it.groupId
    }
    mergedIds.addAll(playerIds)
    mergedIds.addAll(coachIds)
    mergedIds.addAll(groupId)
    CometChatConversationList.memberIds = mergedIds
}
