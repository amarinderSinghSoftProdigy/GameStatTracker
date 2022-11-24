package com.allballapp.android.ui.features.home.teams.roaster

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.allballapp.android.data.response.AllUser
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.invitation.InvitationStatus
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.profile.ProfileEvent
import com.allballapp.android.ui.features.profile.ProfileViewModel
import com.allballapp.android.ui.features.profile.tabs.ProfileTabScreen
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.utils.CommonUtils
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.ceil

@Composable
fun RoasterScreen(
    vm: TeamViewModel,
    onAddPlayerClick: () -> Unit,
    showAddButton: Boolean = false,
    onProfileDetailScreen: (String, String) -> Unit
) {

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
                            var list =
                                CommonUtils.getUsersList(state.allUsers, UserType.COACH)
                            ParentItem(
                                id = R.string.coaches,
                                count = list.size,
                                list,
                                onProfileDetailScreen
                            )
                            list = CommonUtils.getUsersList(state.allUsers, UserType.PLAYER)
                            ParentItem(
                                id = R.string.players,
                                count = list.size,
                                list,
                                onProfileDetailScreen
                            )
                            list = CommonUtils.getUsersList(state.allUsers, UserType.PARENT)
                            ParentItem(
                                id = R.string.supporting_staff,
                                count = list.size,
                                list,
                                onProfileDetailScreen
                            )
                            /*ParentItem(
                                id = R.string.invited,
                                count = state.acceptPending.size,

                                )*/
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

@Composable
fun ParentItem(
    id: Int,
    count: Int,
    listing: List<AllUser>,
    onProfileDetailScreen: (String, String) -> Unit
) {
    if (count != 0)
        Column {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            ShowHeading(id, count.toString())
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            val gridCount = 3
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
                            modifier = Modifier.padding(
                                bottom = dimensionResource(
                                    id = R.dimen.size_16dp
                                )
                            ),
                            it,
                        ) {
                            onProfileDetailScreen(it.userId, it.firstName)
                        }
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
    data: AllUser,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable {
            onClick()
        },
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
            if (data.role != UserType.PLAYER.key) {
                AppText(
                    text = stringResourceByName(name = CommonUtils.getRole(data.role)),
                    color = MaterialTheme.appColors.material.primaryVariant,
                    style = MaterialTheme.typography.h6
                )
            }

            if (data.role == UserType.PLAYER.key) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                AppText(
                    text = data.jersey + " " + data.position,
                    color = MaterialTheme.appColors.textField.label,
                    style = MaterialTheme.typography.h6
                )
            }
        }
        if (data.status.equals(InvitationStatus.PENDING.status, true) || data.status.equals(
                InvitationStatus.DECLINED.status,
                true
            )
        )
            AppText(
                text = data.status,
                color = MaterialTheme.appColors.textField.label,
                style = MaterialTheme.typography.h6
            )
    }
}

@Composable
fun RoasterProfileDetails(vm: ProfileViewModel, userId:String) {


    ProfileTabScreen(vm = vm, userId)

}