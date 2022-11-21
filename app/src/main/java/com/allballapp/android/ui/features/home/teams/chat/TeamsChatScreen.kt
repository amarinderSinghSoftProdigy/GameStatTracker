package com.allballapp.android.ui.features.home.teams.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.*
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily
import com.allballapp.android.ui.utils.CommonUtils
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import timber.log.Timber
import kotlin.random.Random

@Composable
fun TeamsChatScreen(
    color: String = "",
    homeVm: HomeViewModel,
    vm: ChatViewModel,
    onTeamItemClick: () -> Unit,
    onCreateNewConversationClick: (teamId: String) -> Unit
) {

    val context = LocalContext.current
    val fragmentManager = (context as FragmentActivity).supportFragmentManager
    val state = vm.chatUiState.value
    val selected = rememberSaveable {
        mutableStateOf(-1)
    }
    val key = remember { mutableStateOf("selected") }

    remember {
        homeVm.getUnreadMessageCount()
        vm.onEvent(ChatUIEvent.GetChatListing)
    }
    LaunchedEffect(key1 = selected.value, block = {
        vm.onEvent(ChatUIEvent.TeamSelectionChange(selected.value))

    })
    LaunchedEffect(key1 = Unit) {
        vm.onEvent(ChatUIEvent.ClearData)
    }
    LaunchedEffect(key1 = homeVm.state.value.unreadUsersGroupsIds) {
        vm.onEvent(ChatUIEvent.RefreshChatListingAPI(homeVm.state.value.unreadUsersGroupsIds))
    }

    LaunchedEffect(key1 = Unit) {
        vm.chatChannel.collect { chatChannel ->
            when (chatChannel) {
                is ChatChannel.OnNewChatListingSuccess -> {
                    Timber.i("OnNewChatListingSuccess")
                    if (chatChannel.teams.isNotEmpty() && selected.value > -1) {
                        addChatIds(chatChannel.teams[selected.value], onKeyChange = { newKey ->
                            key.value = newKey
                        })
                    }
                }

            }
        }
    }

    if (AppConstants.ENABLE_CHAT) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                LazyRow {
                    itemsIndexed(state.teams) { index, item ->
                        Box {
                            Row {
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                AppTab(
                                    title = item.name,
                                    selected = index == selected.value,
                                    onClick = {
                                        addChatIds(item, {})
                                        selected.value = index
                                        key.value = index.toString()
                                    })
                            }
                            if (item.unreadMessageCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(50),
                                            color = if (index != selected.value) AppConstants.SELECTED_COLOR else MaterialTheme.appColors.material.surface
                                        )
                                        .clip(CircleShape)
                                        .align(Alignment.TopEnd)

                                        .padding(
                                            horizontal = dimensionResource(id = R.dimen.size_6dp),
                                            vertical = dimensionResource(id = R.dimen.size_2dp)
                                        )
                                ) {
                                    AppText(
                                        text = item.unreadMessageCount.toString(),
                                        fontFamily = rubikFamily,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                        color = if (index != selected.value) MaterialTheme.appColors.material.surface else AppConstants.SELECTED_COLOR
                                    )
                                }
                            }
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_24dp)))
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                if (selected.value != -1) {
                    key(key.value) {
                        FragmentContainer(
                            modifier = Modifier.fillMaxSize(),
                            fragmentManager = fragmentManager,
                            commit = {
                                try {
                                    add(it, CometChatConversationList())
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                        )
                    }
                } else if (state.teams.isNotEmpty() && selected.value == -1) {
                    addChatIds(state.teams[0], onKeyChange = {
                        key.value = it
                    })
                    key.value = 0.toString()
                    selected.value = 0
                    EmptyScreen(
                        singleText = false,
                        icon = com.cometchat.pro.uikit.R.drawable.ic_chat_placeholder,
                        heading = stringResource(id = com.cometchat.pro.uikit.R.string.no_conversations)
                    )
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
    } else {
        EmptyScreen(
            singleText = false,
            icon = com.cometchat.pro.uikit.R.drawable.ic_chat_placeholder,
            heading = stringResource(id = com.cometchat.pro.uikit.R.string.no_conversations)
        )
    }
}

fun addChatIds(team: Team, onKeyChange: (String) -> Unit) {
    val mergedIds = mutableListOf<String>()
    val playerIds = team.players.map {
        it._id
    }
    val coachIds = team.coaches.map {
        it._id
    }
    val supportingStaffIds = team.supportingCastDetails.map {
        it._Id
    }
    val groupId = team.teamChatGroups.map {
        it.groupId
    }
    mergedIds.addAll(playerIds)
    mergedIds.addAll(coachIds)
    mergedIds.addAll(supportingStaffIds)
    mergedIds.addAll(groupId)
    CometChatConversationList.memberIds = mergedIds

    onKeyChange.invoke(Random.nextLong().toString())

    Timber.i(" addChatIds-teamChatGroups ${team.teamChatGroups.map { it.groupId }}")
}

@Composable
fun ConversationItem(conversation: Conversation) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_4dp)
            )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface,
                    shape = RoundedCornerShape(
                        dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = "",
                modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .clip(CircleShape)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                onError = { Placeholder(R.drawable.ic_team_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            Column(Modifier.weight(1f)) {
                Text(
                    text = when (val conversationWith = conversation.conversationWith) {
                        is User -> {
                            conversationWith.name
                        }
                        is Group -> {
                            conversationWith.name
                        }
                        else -> {
                            ""
                        }
                    },
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.W500,
                    fontFamily = rubikFamily
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (conversation.lastMessage != null) {
                            if (conversation.lastMessage.deletedAt > 0L) {
                                ""
                            } else CommonUtils.getLastMessage(context, conversation.lastMessage)
                        } else {
                            context.resources.getString(R.string.tap_to_start_conversation)
                        } ?: "",
                        color = MaterialTheme.appColors.textField.label,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W400,
                        fontFamily = rubikFamily
                    )

                }
            }
        }
    }
}

@Composable
fun FragmentContainer(
    modifier: Modifier = Modifier,
    fragmentManager: FragmentManager,
    commit: FragmentTransaction.(containerId: Int) -> Unit
) {
    val containerId by rememberSaveable {
        mutableStateOf(R.id.converstion_container)
    }
    var initialized by rememberSaveable { mutableStateOf(false) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            FragmentContainerView(context)
                .apply { id = containerId }
        },
        update = {
            if (!initialized) {
                fragmentManager.commit { commit(it.id) }
                initialized = true
            } else {
                fragmentManager.onMyContainerAvailable(it)
            }
        }
    )
}

/** Access to package-private method in FragmentManager through reflection */
fun FragmentManager.onMyContainerAvailable(view: FragmentContainerView) {
    val method = FragmentManager::class.java.getDeclaredMethod(
        "onContainerAvailable",
        FragmentContainerView::class.java
    )
    method.isAccessible = true
    method.invoke(this, view)
}
