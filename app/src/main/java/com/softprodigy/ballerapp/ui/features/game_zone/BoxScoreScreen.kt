package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.theme.rubikFamily


@Composable
fun BoxScoreScreen()  {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.game_box_bg_color))
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_44dp))
                    .background(Color.Transparent.copy(alpha = .75f))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_12dp),
                        vertical = dimensionResource(id = R.dimen.size_10dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = stringResource(id = R.string.box_score),
                    color = colorResource(id = R.color.game_grid_item_text_color),
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    fontFamily = rubikFamily,
                    fontWeight = FontWeight.W500,
                )

                ImageButton(
                    icon = painterResource(id = R.drawable.ic_wrong_white),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.size_24dp))
                        .height(dimensionResource(id = R.dimen.size_24dp))
                        ,
                    onClick = { /*TODO*/ },
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_1dp))
                .background(Color.White.copy(alpha = 0.1f)))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_44dp))
                    .background(Color.Transparent.copy(alpha = 0.9f))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_12dp),
                        vertical = dimensionResource(id = R.dimen.size_10dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    teamLogo();
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    teamTitle("My Team name", endPadding = dimensionResource(id = R.dimen.size_8dp));
                }

                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var periods = stringArrayResource(id = R.array.game_box_periods);
                    LazyRow {
                        itemsIndexed(periods) { index, period ->
                            boxPeriodListItem(index, period, 1) // ToDo Need to pass selected item index
                        }
                    }
                }

                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    teamTitle("Other Team name", endPadding = dimensionResource(id = R.dimen.size_8dp))
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    teamLogo();
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    teamScore(true)
                }
                Column(modifier = Modifier.weight(1f)) {
                    teamScore(false)
                }
            }
        }
    }
}

@Composable
fun boxPeriodListItem(index: Int, period:String, selectedIndex: Int, ){
    Box(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.size_6dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_24dp))
            .height(dimensionResource(id = R.dimen.size_24dp))
            .clip(CircleShape)
            .background(
                color = if (index == selectedIndex) colorResource(id = R.color.game_period_selection_background_color)
                else colorResource(id = R.color.game_box_period_bg_color)
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppText(
                text = period,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.game_point_list_item_text_color),
                fontSize = dimensionResource(id = if (index == selectedIndex) R.dimen.txt_size_12 else R.dimen.txt_size_11).value.sp,
                fontFamily = rubikFamily,
                fontWeight = if (index == selectedIndex) FontWeight.W700 else FontWeight.W400,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun teamScore(hasMyTeamScore:Boolean) {
    var points = stringArrayResource(id = R.array.game_periods)
    LazyColumn() {
        stickyHeader {
            pointListHeader(hasMyTeamScore)
        }

        itemsIndexed(points) { index, point ->
            pointListItem(index, hasMyTeamScore, point = "")
        }

        stickyHeader { pointListHeader(hasMyTeamScore) }
    }
}

@Composable
private fun pointListHeader(hasMyTeamScore:Boolean) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(dimensionResource(id = R.dimen.size_32dp))
        .background(colorResource(id = R.color.game_box_point_header_bg_color))
        .padding(
            start = if (hasMyTeamScore) dimensionResource(id = R.dimen.size_16dp) else dimensionResource(
                id = R.dimen.size_8dp
            ),
            top = 0.dp,
            end = if (hasMyTeamScore) dimensionResource(id = R.dimen.size_8dp) else dimensionResource(
                id = R.dimen.size_16dp
            ),
            bottom = 0.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_64dp)),   //Player
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.player_label))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Pts
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.pts_label))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Fouls
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.fouls))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Fg
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.fg))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //3pt
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.three_pt))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Ft
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.ft))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Rbnd
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.rbnd))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //asst
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.asst))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //to
        ) {
            pointHeaderTitle(title = stringResource(id = R.string.to))
        }
    }
}

@Composable
private fun pointHeaderTitle(title: String) {
    AppText(
        text = title,
        fontWeight = FontWeight.W500,
        fontSize = dimensionResource(id = R.dimen.txt_size_10).value.sp,
        color = colorResource(id = R.color.game_box_score_header_text_color),
        fontFamily = rubikFamily,
    )
}

@Composable
private fun pointListItem(index: Int, hasMyTeamScore: Boolean, point: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_32dp))
            .background(if (index % 2 == 0) Color.Transparent.copy(alpha = 0.4f) else colorResource(id = R.color.game_box_score_list_item_bg_color))
            .padding(
                start = if (hasMyTeamScore) dimensionResource(id = R.dimen.size_16dp) else dimensionResource(
                    id = R.dimen.size_8dp
                ),
                top = 0.dp,
                end = if (hasMyTeamScore) dimensionResource(id = R.dimen.size_8dp) else dimensionResource(
                    id = R.dimen.size_16dp
                ),
                bottom = 0.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_64dp)),   //Player
        ) {
            pointListItemTitle(title = "68 Cooper")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Pts
        ) {
            pointListItemTitle(title = "100", )
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Fouls
        ) {
            pointListItemTitle(title = "10")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Fg
        ) {
            pointListItemTitle(title = "0")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //3pt
        ) {
            pointListItemTitle(title = "1")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Ft
        ) {
            pointListItemTitle(title = "2")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //Rbnd
        ) {
            pointListItemTitle(title = "3")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //asst
        ) {
            pointListItemTitle(title = "4")
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))

        Column(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp)),   //to
        ) {
            pointListItemTitle(title = "5")
        }
    }
}

@Composable
private fun pointListItemTitle(title: String) {

    AppText(
        text = title,
        fontWeight = FontWeight.W400,
        fontSize = dimensionResource(id = R.dimen.txt_size_11).value.sp,
        color = if(title == "0") colorResource(id = R.color.game_box_score_header_text_color).copy(alpha = 0.4f) else colorResource(
            id = R.color.game_box_score_header_text_color).copy(alpha = 1f),
        fontFamily = rubikFamily,
    )
}