package com.softprodigy.ballerapp.ui.features.home.teams.chat

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.EmptyScreen
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayStatus
import com.softprodigy.ballerapp.ui.theme.appColors
import timber.log.Timber

@Composable
fun NewConversationScreen(teamVm: TeamViewModel, chatVM: ChatViewModel = hiltViewModel()) {
    val teamState = teamVm.teamUiState.value
    val chatState = chatVM.chatUiState.value

    Box(Modifier.fillMaxSize()) {
        if (teamState.coaches.isNotEmpty() || teamState.players.isNotEmpty()) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                    )


            ) {
                if (teamState.coaches.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.appColors.material.surface,
                                shape = RoundedCornerShape(
                                    topStart = dimensionResource(
                                        id = R.dimen.size_8dp
                                    ),
                                    topEnd = dimensionResource(
                                        id = R.dimen.size_8dp
                                    )
                                )
                            )
                    ) {


                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                        Text(
                            text = stringResource(id = R.string.coaches),
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontWeight = FontWeight.W600,
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                        LazyColumn() {
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

                }

                if (teamState.players.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.appColors.material.surface,
                            )
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                            Text(
                                text = stringResource(id = R.string.players),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))

                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                        }
                        LazyColumn() {
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
            }

        } else {
            EmptyScreen(
                singleText = true,
                stringResource(id = R.string.no_members_in_the_team_currently)
            )
        }
        IconButton(
            modifier = Modifier
                .offset((-20).dp, (-20).dp)
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
                chatVM.onEvent(ChatUIEvent.ShowDialog(true))

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
                chatVM.onEvent(ChatUIEvent.OnConfirmGroupName)
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
                .fillMaxWidth()
                .background(
                    color = Color.White,
                )
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = MaterialTheme.appColors.material.surface),
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
                    src = if (url.contains("http")) url else BuildConfig.IMAGE_SERVER + url,
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


