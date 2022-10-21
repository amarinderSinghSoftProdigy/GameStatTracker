package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.HomeItemResponse
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeFirstTimeLoginScreen(
    viewModel: HomeViewModel,
    teamVm: TeamViewModel,
    onTeamNameClick: (Boolean) -> Unit,
    onCreateTeamClick: () -> Unit,
    onInvitationCLick: () -> Unit
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val teamState = teamVm.teamUiState.value
    Box {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))
            AppText(
                text = stringResource(id = R.string.hey_label).replace(
                    "name",
                    state.user.firstName
                ),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = ColorBWBlack
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

            AppText(
                text = stringResource(id = R.string.welcome_to_total_hoop),
                fontWeight = FontWeight.W600,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.appColors.material.primaryVariant
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            Column(modifier = Modifier.fillMaxWidth()) {
                if (teamState.teams.isNotEmpty()) {
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTeamNameClick.invoke(true)
                                }
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterStart),
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                CoilImage(
                                    src = BuildConfig.IMAGE_SERVER + teamState.logo,
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.size_48dp))
                                        .clip(CircleShape),
                                    isCrossFadeEnabled = false,
                                    onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                    onError = { Placeholder(R.drawable.ic_team_placeholder) },
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                Text(
                                    text = teamState.teamName.ifEmpty { context.getString(R.string.team_total_hoop) },
                                    style = MaterialTheme.typography.h3,
                                    fontWeight = FontWeight.W700,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Icon(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = ColorGreyLighter
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                if (state.homePageCoachModel.pendingInvitations > 0) {
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onInvitationCLick.invoke()
                                }
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_invite),
                                    contentDescription = "",
                                    tint = MaterialTheme.appColors.material.primaryVariant,
                                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                Text(
                                    text = stringResource(id = R.string.pending_invitations),
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            Text(
                                text = state.homePageCoachModel.pendingInvitations.toString(),
                                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                }
                FlowRow {
                    state.homeItemList.forEachIndexed { index, item ->
                        HomeScreenItem(item)
                    }
                }
                if (state.homeItemList.isNotEmpty())
                    ButtonWithLeadingIcon(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.create_new_team),
                        onClick = onCreateTeamClick,
                        painter = painterResource(id = R.drawable.ic_add_circle),
                        isTransParent = false,
                        iconSize = dimensionResource(id = R.dimen.size_20dp),
                        noTheme = true
                    )
            }
        }
    }
}

@Composable
fun HomeScreenItem(data: HomeItemResponse) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_72dp))
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_24dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = data.image!!),
                        contentDescription = null,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)),
                        colorFilter = ColorFilter.tint(MaterialTheme.appColors.material.primaryVariant)
                    )

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                    AppText(
                        text = stringResource(id = if (data.item != 0) data.item else R.string.opportunities),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.W700,
                        color = ColorBWBlack
                    )
                }

                AppText(
                    text = data.total,
                    fontWeight = FontWeight.W300,
                    fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                    color = ColorBWBlack,
                    style = MaterialTheme.typography.h1,
                )
            }

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
    }
}