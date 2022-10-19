package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsChatScreen(
    vm: ChatViewModel = hiltViewModel(),
    onTeamItemClick: () -> Unit,
    onCreateNewConversationClick: () -> Unit
) {

    val state = vm.chatUiState.value
    val selected = rememberSaveable {
        mutableStateOf(0)
    }
    var key = remember { mutableStateOf("selected")}
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

//                                userIds = mergedIds
                                    selected.value = index

                                    CometChatConversationList.memberIds = mergedIds
                                    key.value = index.toString()
                                })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                key(key.value) {
                AndroidViewBinding(FragmentConversationBinding::inflate) {
                    converstionContainer.getFragment<CometChatConversationList>()

                }
                }
            }
            IconButton(
                modifier = Modifier
                    .offset((-20).dp, (-20).dp)
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .background(
                        MaterialTheme.appColors.material.primaryVariant,
                        RoundedCornerShape(50)
                    )
                    .align(Alignment.BottomEnd),
                enabled = true,
                onClick = { onCreateNewConversationClick.invoke() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross_1),
                    "",
                    tint = MaterialTheme.appColors.buttonColor.textEnabled,
                    modifier = Modifier
                        .width(
                            dimensionResource(id = R.dimen.size_16dp)
                        )
                        .rotate(45f)
                )
            }
            if (state.isLoading) {
                CommonProgressBar()
            }

        }

    }

