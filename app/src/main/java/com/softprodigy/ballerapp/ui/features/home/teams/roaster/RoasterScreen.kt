package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoasterScreen(vm: TeamViewModel = hiltViewModel()) {

    val state = vm.teamUiState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.coaches.isEmpty() && state.players.isEmpty()) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    state.coaches.forEach {
                        CoachListItem(
                            coach = it,
                            modifier = Modifier
                                .padding(
                                    bottom = dimensionResource(
                                        id = R.dimen.size_16dp
                                    )
                                ),
                            isCoach = true
                        )
                    }
                }
                CompositionLocalProvider(
                    LocalOverScrollConfiguration provides null
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            items(state.players) {
                                CoachListItem(
                                    isCoach = false,
                                    player = it, modifier = Modifier.padding(
                                        bottom = dimensionResource(
                                            id = R.dimen.size_16dp
                                        )
                                    )
                                )
                            }
                        })
                }
            }
        }
        if (state.isLoading) {
            CommonProgressBar()
        }
    }
}

@Composable
fun CoachListItem(
    modifier: Modifier = Modifier,
    isCoach: Boolean = false,
    coach: Coach? = null,
    player: Player? = null,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val url = "" + if (isCoach) coach?.profileImage else player?.profileImage
        CoilImage(
            src = if (url.contains("http")) url else BuildConfig.IMAGE_SERVER + url,
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
            text = if (isCoach) ((("${coach?.firstName} ${coach?.lastName}"))).capitalize()
            else ("${player?.firstName} ${player?.lastName}").capitalize(),
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
                text = if (!isCoach) player?.userInviteData?.role
                    ?: "" else coach?.userInviteData?.role
                    ?: "",
                color = MaterialTheme.appColors.material.primaryVariant,
                style = MaterialTheme.typography.h6
            )

            if (!isCoach) {
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