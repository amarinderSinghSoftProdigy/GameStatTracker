package com.allballapp.android.ui.features.home.teams.roaster

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.data.response.SwapUser
import com.allballapp.android.data.response.team.Coach
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.teams.TeamUIState
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.ceil

@Composable
fun RoasterScreen(vm: TeamViewModel, onAddPlayerClick: () -> Unit, showAddButton: Boolean = false) {

    val state = vm.teamUiState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.isLoading) {
            CommonProgressBar()
        } else {
            if (state.coaches.isEmpty() && state.players.isEmpty() && state.supportStaff.isEmpty() && state.acceptPending.isEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
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
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FlowRow {
                        Column {
                            ParentItem(
                                id = R.string.coaches,
                                count = state.coaches.size,
                                state = state,
                                User.Coach
                            )
                            ParentItem(
                                id = R.string.players,
                                count = state.players.size,
                                state = state,
                                User.Player
                            )
                            ParentItem(
                                id = R.string.supporting_staff,
                                count = state.supportStaff.size,
                                state = state,
                                User.Staff
                            )
                            ParentItem(
                                id = R.string.invited,
                                count = state.acceptPending.size,
                                state = state,
                                User.User
                            )
                            Spacer(
                                modifier = Modifier.height(
                                    if (showAddButton) dimensionResource(
                                        id = R.dimen.size_72dp
                                    ) else 0.dp
                                )
                            )
                        }
                    }
                }
            }
        }
        if (showAddButton) {
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
                        onAddPlayerClick()
                    },
                    painter = painterResource(id = R.drawable.ic_add_button),
                )
            }
        }
    }
}

enum class User {
    Coach, Player, Staff, User
}


@Composable
fun ParentItem(id: Int, count: Int, state: TeamUIState, type: User) {
    Column {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        ShowHeading(id, count.toString())
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        val gridCount = 3
        val listing = when (type) {
            User.Coach -> {
                state.coaches
            }
            User.Player -> {
                state.players
            }
            User.User -> {
                state.acceptPending
            }
            User.Staff -> {
                state.supportStaff
            }

        }
        val heightFactor = ceil(listing.size.toDouble() / gridCount)

        LazyVerticalGrid(
            userScrollEnabled = false,
            columns = GridCells.Fixed(gridCount),
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .height(
                    dimensionResource(id = R.dimen.size_150dp) * (if (heightFactor.toInt() == 0) {
                        if (count == 0) 0 else 1
                    } else heightFactor.toInt())
                ),
            content = {
                items(listing) {
                    CoachListItem(
                        type = type,
                        coach = if (type == User.Coach) it as Coach else null,
                        player = if (type == User.Player) it as Player else null,
                        user = if (type == User.Staff || type == User.User) it as SwapUser else null,
                        modifier = Modifier.padding(
                            bottom = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        )
                    )
                }
            })
        DividerCommon()
    }
}

@Composable
fun ShowHeading(id: Int, count: String) {
    Row(
        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = id),
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
        Text(
            text = "(${count})",
            color = MaterialTheme.appColors.textField.labelDark,
            fontWeight = FontWeight.W400,
            fontSize = dimensionResource(
                id = R.dimen.txt_size_12
            ).value.sp
        )

    }
}

@Composable
fun CoachListItem(
    modifier: Modifier = Modifier,
    coach: Coach? = null,
    player: Player? = null,
    user: SwapUser? = null,
    type: User,
) {
    val data: SwapUser = when (type) {
        User.Coach -> {
            SwapUser(
                coach?._id ?: "",
                coach?.firstName ?: "",
                coach?.lastName ?: "",
                coach?.role ?: "",
                coach?.profileImage ?: "",
                ""
            )
        }
        User.Player -> {
            SwapUser(
                player?._id ?: "",
                player?.firstName ?: "",
                player?.lastName ?: "",
                player?.role ?: "",
                player?.profileImage ?: "",
                player?.status ?: ""
            )
        }
        User.User, User.Staff -> {
            user ?: SwapUser()
        }
    }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + data.profileImage,
            modifier =
            Modifier
                .background(
                    color = MaterialTheme.appColors.material.onSurface,
                    shape = CircleShape
                )
                .size(dimensionResource(id = R.dimen.size_80dp))
                .clip(CircleShape),
            isCrossFadeEnabled = false,
            onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
            onError = { Placeholder(R.drawable.ic_user_profile_icon) }
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        AppText(
            text = ("${data.firstName} ${data.lastName}").capitalize(),
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_120dp))
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AppText(
                text = if (type == User.Coach || type == User.Player) data.role else data.status,
                color = if (type == User.Coach || type == User.Player) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label,
                style = MaterialTheme.typography.h6
            )

            if (type == User.Player) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                AppText(
                    text = player?.jerseyNumber ?: "",
                    color = MaterialTheme.appColors.textField.label,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}