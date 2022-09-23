package com.softprodigy.ballerapp.ui.features.home.teams.manage_team

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
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
    val recordState =
        rememberReorderableLazyListState(
            onMove = vm::moveItemRoaster,
            canDragOver = vm::isRoasterDragEnabled
        )
    Box {
        if (state.coaches.isNotEmpty() || state.players.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                    )

            ) {
                if (state.coaches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                    Text(
                        text = stringResource(id = R.string.coaches),
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontWeight = FontWeight.W600,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    MangeTeamDataHeaderItem(coaches = state.coaches)
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
                            .padding(bottom = dimensionResource(id = R.dimen.size_80dp))
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
                                /* if (item.locked) {
                                     Column {
                                         Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                                         Text(
                                             text = item._id,
                                             fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                             color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                             fontWeight = FontWeight.W600,
                                         )
                                         Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                                     }
                                 } else {*/
                                MangeTeamDataHeaderItem(
                                    modifier = Modifier
                                        .shadow(elevation.value),
                                    dragging = dragging,
                                    players = item
                                )
                                // }
                            }
                        }
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
                    text = stringResource(id = R.string.add_player),
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
                        text = stringResource(id = R.string.add_player),
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
    dragging: Boolean = false,
    coaches: ArrayList<Coach>? = null,
    players: Player? = null,
) {
    Column(modifier = modifier) {
        if (!coaches.isNullOrEmpty()) {
            FlowRow {
                coaches.forEach {
                    TeamUserListItem(coachUser = it, isCoach = true)
                }
            }
        } else if (players != null) {
            TeamUserListItem(modifier, teamUser = players, isCoach = false, dragging = dragging)
        }
    }
}

@Composable
fun TeamUserListItem(
    modifier: Modifier = Modifier,
    dragging: Boolean = false,
    teamUser: Player? = null,
    coachUser: Coach? = null,
    isCoach: Boolean = false,
) {
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
                if (!isCoach) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_drag),
                        contentDescription = "",
                        modifier =
                        Modifier
                            .size(dimensionResource(id = R.dimen.size_12dp)),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

            }

        }
    }
}