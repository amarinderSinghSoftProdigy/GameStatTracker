package com.allballapp.android.ui.features.home.teams.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.data.response.team.Coach
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.theme.ColorBWGrayStatus
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import timber.log.Timber

@Composable
fun NewConversationScreen(
    teamVm: TeamViewModel,
    chatVM: ChatViewModel = hiltViewModel(),
    cometChat: CometChatUI,
    onGroupCreateSuccess: () -> Unit
) {
    val teamState = teamVm.teamUiState.value
    val chatState = chatVM.chatUiState.value
    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        chatVM.chatChannel.collect { chatChannel ->
            when (chatChannel) {
                is ChatChannel.OnNewConversationResponse -> {
                    Toast.makeText(
                        context,
                        chatChannel.message.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (chatChannel.isSuccess) {
                        onGroupCreateSuccess.invoke()
                    }
                }
                is ChatChannel.ShowToast -> {

                }
                is ChatChannel.TriggerUserGetIntent -> {
                    if (chatChannel.isSuccess) {
                        chatChannel.user?.let { cometChat.startUserIntent(it) }
                        onGroupCreateSuccess.invoke()
                    } else if (chatChannel.message != null) {
                        Toast.makeText(
                            context,
                            chatChannel.message.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(id = R.dimen.size_16dp))
    ) {
        if (teamState.coaches.isNotEmpty() || teamState.players.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.appColors.material.surface,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
            ) {
                if (teamState.coaches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                    val coachSize = buildAnnotatedString {
                        append(stringResource(id = R.string.coaches))
                        val startIndex = length
                        append(" ( ")
                        append("" + teamState.coaches.size)
                        append(" )")
                        addStyle(
                            SpanStyle(
                                color = MaterialTheme.appColors.textField.label,
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.W400
                            ),
                            startIndex,
                            length,
                        )
                    }
                    Text(
                        text = coachSize,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    LazyColumn {
                        items(teamState.coaches) { coach ->
                            TeamUserListCheckbox(
                                isCoach = true,
                                coachUser = coach,
                                selectedTeamMembers = chatState.selectedCoachesForNewGroup
                            ) {
                                chatVM.onEvent(ChatUIEvent.OnCoachChange(selectedCoaches = it))
                                Log.i("selectedCoach", "NewConversationScreen: $it")

                            }
                        }
                    }
                }
                if (teamState.players.isNotEmpty()) {
                    AppDivider()
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                    val playersSize = buildAnnotatedString {
                        append(stringResource(id = R.string.players))
                        val startIndex = length
                        append(" ( ")
                        append("" + teamState.players.size)
                        append(" )")
                        addStyle(
                            SpanStyle(
                                color = MaterialTheme.appColors.textField.label,
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.W400
                            ),
                            startIndex,
                            length,
                        )
                    }
                    Text(
                        text = playersSize,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                    LazyColumn {
                        items(teamState.players) { player ->
                            TeamUserListCheckbox(
                                isCoach = false,
                                teamUser = player,
                                selectedTeamMembers = chatState.selectedPlayersForNewGroup
                            ) {
                                chatVM.onEvent(ChatUIEvent.OnPlayerChange(selectedPlayers = it))
                                Timber.i(
                                    "selectedPlayers NewConversationScreen: ${
                                        it.map { playerId ->
                                            playerId
                                        }
                                    }"
                                )
                            }
                        }
                    }
                }
            }
        } else {
            EmptyScreen(
                singleText = true,
                stringResource(id = R.string.no_members_in_the_team_currently)
            )
        }
        IconButton(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_44dp))
                .background(
                    color = if (chatState.selectedPlayersForNewGroup.isNotEmpty() || chatState.selectedCoachesForNewGroup.isNotEmpty())
                        MaterialTheme.appColors.material.primaryVariant
                    else MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                    RoundedCornerShape(50)
                )
                .align(Alignment.BottomEnd),
            enabled = chatState.selectedPlayersForNewGroup.isNotEmpty() || chatState.selectedCoachesForNewGroup.isNotEmpty(),
            onClick = {
                if (chatState.selectedPlayersForNewGroup.size.plus(chatState.selectedCoachesForNewGroup.size) > 1)
                    chatVM.onEvent(ChatUIEvent.ShowDialog(true))
                else {
                    chatVM.onEvent(ChatUIEvent.OnInitiateNewConversation)
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_next),
                "",
                modifier = Modifier.width(
                    dimensionResource(id = R.dimen.size_14dp)
                )
            )
        }
        if (teamState.isLoading) {
            CommonProgressBar()
        }

    }
    if (chatState.showCreateGroupNameDialog) {
        DeclineEventDialog(
            title = stringResource(id = R.string.enter_group_name),
            onDismiss = {
                chatVM.onEvent(ChatUIEvent.ShowDialog(false))
            },
            onConfirmClick = {
                chatVM.onEvent(ChatUIEvent.ShowDialog(false))
                chatVM.onEvent(ChatUIEvent.OnInitiateNewConversation)
            },
            onReasonChange = {
                chatVM.onEvent(ChatUIEvent.OnGroupNameChange(it))
            },
            reason = chatState.groupName,
            placeholderText = stringResource(id = R.string.enter_group_name)
        )
    }


}

@Composable
fun TeamUserListCheckbox(
    modifier: Modifier = Modifier,
    teamUser: Player? = null,
    coachUser: Coach? = null,
    isCoach: Boolean = false,
    selectedTeamMembers: SnapshotStateList<String>,
    onSelectedMemberChange: (SnapshotStateList<String>) -> Unit
) {

    val statusTeam = remember {
        if (teamUser != null)
            mutableStateOf(selectedTeamMembers.contains(teamUser._id))
        else {
            mutableStateOf(selectedTeamMembers.contains(coachUser?._id))
        }
    }

    Column {
        AppDivider()
        Box(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .align(Alignment.CenterStart)
                    .padding(vertical = dimensionResource(id = R.dimen.size_8dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                val url = "" + if (isCoach) coachUser?.profileImage else teamUser?.profileImage
                CoilImage(
                    src = if (url.contains("http")) url else com.allballapp.android.BuildConfig.IMAGE_SERVER + url,
                    modifier =
                    Modifier
                        .background(
                            color = ColorBWGrayStatus,
                            shape = CircleShape
                        )
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape),
                    isCrossFadeEnabled = false,
                    onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                    onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = if (isCoach) "${coachUser?.firstName} ${coachUser?.lastName}"
                        ?: "" else "${teamUser?.firstName} ${teamUser?.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isCoach) coachUser?.coachPosition ?: "" else teamUser?.jerseyNumber
                        ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = MaterialTheme.appColors.material.primaryVariant
                )
                if (!isCoach) {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = teamUser?.position ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = MaterialTheme.appColors.textField.label.copy(alpha = 1f)
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))
                if (coachUser != null) {
                    CustomCheckBox(statusTeam.value) {
                        statusTeam.value = !statusTeam.value
                        if (selectedTeamMembers.contains(coachUser._id)) {
                            selectedTeamMembers.remove(coachUser._id)
                        } else {
                            coachUser._id?.let { selectedTeamMembers.add(it) }
                        }
                        onSelectedMemberChange.invoke(selectedTeamMembers)
                    }
                } else if (teamUser != null) {
                    CustomCheckBox(statusTeam.value) {
                        statusTeam.value = !statusTeam.value
                        if (selectedTeamMembers.contains(teamUser._id)) {
                            selectedTeamMembers.remove(teamUser._id)
                        } else {
                            teamUser._id.let { selectedTeamMembers.add(it) }
                        }
                        onSelectedMemberChange.invoke(selectedTeamMembers)
                    }
                }



                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

            }

        }
    }
}


