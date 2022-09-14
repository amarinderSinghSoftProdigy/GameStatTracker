package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoasterScreen(vm: TeamViewModel) {

    val state = vm.teamUiState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

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
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
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
        AsyncImage(
            model = if (url.contains("http")) url else BuildConfig.IMAGE_SERVER + url,
            contentDescription = "",
            modifier =
            Modifier
                .size(dimensionResource(id = R.dimen.size_80dp))
                .clip(CircleShape),
            contentScale = ContentScale.Crop,

            )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        AppText(
            text = if (isCoach) (coach?.name ?: "").capitalize() else (player?.name
                ?: "").capitalize(),
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AppText(
                text = if (isCoach) (coach?.coachPosition ?: "").capitalize() else (player?.position
                    ?: "").capitalize(),
                color = MaterialTheme.appColors.material.primaryVariant,
                style = MaterialTheme.typography.h6
            )

            if (!isCoach) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                AppText(
                    text = player?.position ?: "",
                    color = MaterialTheme.appColors.textField.label,
                    style = MaterialTheme.typography.h6
                )

            }
        }
    }
}