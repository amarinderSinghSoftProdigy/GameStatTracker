package com.softprodigy.ballerapp.ui.features.home.teams.manage_team

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.SwapUser
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.roaster.User
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayStatus
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.dragDrop.ReorderableItem
import com.softprodigy.ballerapp.ui.utils.dragDrop.detectReorderAfterLongPress
import com.softprodigy.ballerapp.ui.utils.dragDrop.rememberReorderableLazyListState
import com.softprodigy.ballerapp.ui.utils.dragDrop.reorderable

@Composable
fun ManageTeamRoster(vm: TeamViewModel, onAddPlayerCLick: () -> Unit) {
    val state = vm.teamUiState.value
    val scroll = remember {
        mutableStateOf(false)
    }
    val recordState =
        rememberReorderableLazyListState(
            onMove = vm::moveItemRoaster,
            canDragOver = vm::isRoasterDragEnabled
        )

    val recordStatePending =
        rememberReorderableLazyListState(
            onMove = vm::moveItemUser,
            canDragOver = vm::isUserDragEnabled
        )
    Box {
        if (state.coaches.isNotEmpty() || state.players.isNotEmpty() || state.acceptPending.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow {
                    Column {
                        if (state.coaches.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                            Text(
                                text = stringResource(id = R.string.coaches),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontWeight = FontWeight.W600,
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.size_56dp) * state.coaches.size)
                            ) {
                                items(state.coaches) {
                                    MangeTeamDataHeaderItem(coaches = it)
                                }
                            }
                        }
                        if (state.players.isNotEmpty()) {
                            Column {
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                                Text(
                                    text = stringResource(id = R.string.all_players),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    fontWeight = FontWeight.W600,
                                )
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            }
                            LazyColumn(
                                state = recordState.listState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.size_56dp) * state.players.size)
                                    .then(
                                        Modifier
                                            .reorderable(recordState)
                                            .detectReorderAfterLongPress(recordState)
                                    ),
                            ) {
                                items(state.players, key = { item -> item.uniqueId }) { item ->
                                    ReorderableItem(
                                        reorderableState = recordState,
                                        key = item.uniqueId,
                                    ) { dragging ->
                                        val elevation =
                                            animateDpAsState(if (dragging) dimensionResource(id = R.dimen.size_10dp) else 0.dp)
                                        MangeTeamDataHeaderItem(
                                            modifier = Modifier
                                                .shadow(elevation.value),
                                            players = item
                                        )
                                    }

                                }
                            }
                        }
                        if (state.acceptPending.isNotEmpty()) {
                            Column {
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                                Text(
                                    text = stringResource(id = R.string.invited),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    fontWeight = FontWeight.W600,
                                )
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            }
                            LazyColumn(
                                state = recordStatePending.listState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.size_56dp) * state.acceptPending.size)
                                    .then(
                                        Modifier
                                            .reorderable(recordStatePending)
                                            .detectReorderAfterLongPress(recordStatePending)
                                    ),
                            ) {
                                items(state.acceptPending, key = { item -> item._Id }) { item ->
                                    ReorderableItem(
                                        reorderableState = recordStatePending,
                                        key = item._Id,
                                    ) { dragging ->
                                        val elevation =
                                            animateDpAsState(if (dragging) dimensionResource(id = R.dimen.size_10dp) else 0.dp)
                                        MangeTeamDataHeaderItem(
                                            modifier = Modifier
                                                .shadow(elevation.value),
                                            user = item
                                        )
                                    }

                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.appColors.material.surface.copy(alpha = .97f))
                    .height(dimensionResource(id = R.dimen.size_72dp))
            ) {
                ButtonWithLeadingIcon(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.invite_team_member),
                    onClick = {
                        onAddPlayerCLick.invoke()
                    },
                    painter = painterResource(id = R.drawable.ic_add_button),
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppText(
                        text = stringResource(id = R.string.no_players_in_team),
                        color = ColorBWBlack,
                        style = MaterialTheme.typography.h3
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    AppText(
                        text = stringResource(id = R.string.please_add_players),
                        color = ColorBWBlack,
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    ButtonWithLeadingIcon(
                        text = stringResource(id = R.string.invite_team_member),
                        onClick = {
                            onAddPlayerCLick.invoke()
                        },
                        painter = painterResource(id = R.drawable.ic_add_button),
                    )
                }
            }
        }
        if (state.isLoading) {
            CommonProgressBar()
        }
    }
}

@Composable
fun MangeTeamDataHeaderItem(
    modifier: Modifier = Modifier,
    coaches: Coach? = null,
    players: Player? = null,
    user: SwapUser? = null,
) {
    Column(modifier = modifier) {
        if (coaches != null) {
            TeamUserListItem(coachUser = coaches, type = User.Coach)
        } else if (players != null) {
            TeamUserListItem(modifier, teamUser = players, type = User.Player)
        } else if (user != null) {
            TeamUserListItem(modifier, user = user, type = User.User)
        }
    }
}

@Composable
fun TeamUserListItem(
    modifier: Modifier = Modifier,
    user: SwapUser? = null,
    teamUser: Player? = null,
    coachUser: Coach? = null,
    type: User,
) {
    val data: SwapUser = when (type) {
        User.Coach -> {
            SwapUser(
                coachUser?._id ?: "",
                coachUser?.firstName ?: "",
                coachUser?.lastName ?: "",
                coachUser?.role ?: "",
                coachUser?.profileImage ?: "",
                ""
            )
        }
        User.Player -> {
            SwapUser(
                teamUser?._id ?: "",
                teamUser?.firstName ?: "",
                teamUser?.lastName ?: "",
                teamUser?.role ?: "",
                teamUser?.profileImage ?: "",
                teamUser?.status ?: ""
            )
        }
        User.User, User.Staff -> {
            user ?: SwapUser()
        }
    }

    Column {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
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
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                val url = "" + data.profileImage
                CoilImage(
                    src = BuildConfig.IMAGE_SERVER + data.profileImage,
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
                    text = "${data.firstName} ${data.lastName}",
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
                    text = if (User.Coach == type) coachUser?.coachPosition
                        ?: "" else if (User.Player == type) teamUser?.jerseyNumber
                        ?: "" else "",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = MaterialTheme.appColors.material.primaryVariant
                )
                if (User.Player == type) {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = teamUser?.position ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = MaterialTheme.appColors.textField.label.copy(alpha = 1f)
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))
                Icon(
                    painter = painterResource(id = R.drawable.ic_drag),
                    contentDescription = "",
                    modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.size_12dp)),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

            }

        }
    }
}