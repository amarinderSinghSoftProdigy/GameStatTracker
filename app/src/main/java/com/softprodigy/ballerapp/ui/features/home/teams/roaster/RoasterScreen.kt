package com.softprodigy.ballerapp.ui.features.home.teams.roaster

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoasterScreen(vm: TeamViewModel) {

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
                            isCoach = true,
                            onItemClick = {}
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
                                    ),
                                    onItemClick = {}
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
    isRoasterSelection:Boolean = false,
    coach: Coach? = null,
    player: Player? = null,
    onItemClick : (Player) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(isRoasterSelection)
            Box(modifier = Modifier
                .align(alignment = Alignment.End)
                .padding(
                    top = dimensionResource(id = R.dimen.size_20dp),
                    end = dimensionResource(id = R.dimen.size_10dp),
                )
            ){
                Column( modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_16dp))
                    .height(dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.End,

                ){
                   Icon(painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = "",
                        tint = Color.Unspecified,
                   )
                }
            }

        val url = "" + if (isCoach) coach?.profileImage else player?.profileImage
        Card(
            border = BorderStroke(dimensionResource(id = R.dimen.size_4dp),
                when (player?.substitutionType) {
                    AppConstants.PLAYER_IN -> colorResource(id = R.color.player_in_color)
                    AppConstants.PLAYER_OUT -> colorResource(id = R.color.player_out_color)
                    else -> colorResource(id = R.color.game_setting_bg_color)
                }
            ),
            shape = CircleShape,
            modifier = Modifier.clickable {
                onItemClick.invoke(player!!)
            }
        ) {
            CoilImage(
                src = if (url.contains("http")) url else BuildConfig.IMAGE_SERVER + url,
                modifier =
                Modifier
                    .background(
                        color = MaterialTheme.appColors.material.onSurface,
                        shape = CircleShape
                    )
                    .size(dimensionResource(id = R.dimen.size_80dp))
                    //.clip(CircleShape)
                ,
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                onError = { Placeholder(R.drawable.ic_user_profile_icon) }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        AppText(
            text = if (isCoach) ((("${coach?.firstName} ${coach?.lastName}")
                ?: "")).capitalize() else ("${player?.firstName} ${player?.lastName}"
                ?: "").capitalize(),
            color = if(isRoasterSelection) colorResource(id = R.color.game_roaster_name_text_color) else MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            textAlign = if(isRoasterSelection) TextAlign.Center else TextAlign.Start,
            maxLines = 1,
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_100dp))
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AppText(
                text = if (isCoach) (coach?.coachPosition ?: "").capitalize() else (player?.position
                    ?: "").capitalize(),
                color = if(isRoasterSelection) colorResource(id = R.color.game_point_list_item_text_color) else MaterialTheme.appColors.material.primaryVariant,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Normal,
            )

            if (!isCoach && !isRoasterSelection) {
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