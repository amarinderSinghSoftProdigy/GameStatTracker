package com.allballapp.android.ui.features.home.event_kd.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.FoldableItem
import com.allballapp.android.ui.features.components.Placeholder
import com.allballapp.android.ui.theme.appColors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventScheduleScreen(vm: EventScheduleViewModel = hiltViewModel()) {

    val state = vm.eventScheduleState.value
    val expand = remember {
        false
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(state.leagueSchedules) { item: LeagueScheduleModel ->
                FoldableItem(
                    expanded = expand,
                    headerBackground = MaterialTheme.appColors.material.surface,
                    headerBorder = BorderStroke(0.dp, Color.Transparent),
                    header = { isExpanded ->
                        EventScheduleHeaderItem(
                            isExpanded = isExpanded,
                            date = item.date,
                            gamesCount = item.gameCount
                        )
                    },
                    childItems = item.matches,
                    hasItemLeadingSpacing = false,
                    hasItemTrailingSpacing = false,
                    itemSpacing = 0.dp,
                    itemHorizontalPadding = 0.dp,
                    itemsBackground = MaterialTheme.appColors.material.primary,
                    item = { match, index ->
                        EventScheduleSubItem(match, index)
                    }
                )
            }
        }
    }

}


@Composable
fun Space(dp: Dp) {
    Spacer(modifier = Modifier.size(dp))
}

@Composable
fun EventScheduleHeaderItem(date: String, gamesCount: String, isExpanded: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_18dp),
                    vertical = dimensionResource(id = R.dimen.size_16dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = "(${gamesCount} ${stringResource(id = R.string.games)})",
                    color = MaterialTheme.appColors.textField.labelDark,
                    fontWeight = FontWeight.W400,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp
                )

            }
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        height = dimensionResource(id = R.dimen.size_4dp),
                        width = dimensionResource(id = R.dimen.size_8dp)
                    )
                    .then(
                        if (!isExpanded) Modifier.rotate(180f) else Modifier
                    ),
                tint = MaterialTheme.appColors.buttonColor.textDisabled
            )
        }
        Divider(color = MaterialTheme.appColors.buttonColor.bckgroundDisabled)
    }

}

@Composable
fun EventScheduleSubItem(match: Match, index: Int) {
    if (index == 0) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8dp)))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
    ) {
        if (index == 0) {
            Text(
                text = match.time,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(
                    id = R.dimen.txt_size_12
                ).value.sp
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8dp)))
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface.copy(0.9f),
                    shape = RoundedCornerShape(
                        dimensionResource(id = R.dimen.size_8dp)
                    )
                )

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_8dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = match.teamA.name,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(
                            id = R.dimen.txt_size_12
                        ).value.sp
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                    CoilImage(src = com.allballapp.android.BuildConfig.IMAGE_SERVER + match.teamA.logo,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_32dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        onError = { Placeholder(R.drawable.ic_team_placeholder) })
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                Text(
                    text = stringResource(id = R.string.vs),
                    color = MaterialTheme.appColors.textField.labelDark,
                    fontWeight = FontWeight.W400,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))

                    CoilImage(src = com.allballapp.android.BuildConfig.IMAGE_SERVER + match.teamB.logo,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_32dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        onError = { Placeholder(R.drawable.ic_team_placeholder) })
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                    Text(
                        text = match.teamB.name,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(
                            id = R.dimen.txt_size_12
                        ).value.sp
                    )

                }


            }
            Divider(color = MaterialTheme.appColors.buttonColor.bckgroundDisabled)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_8dp),
                        vertical = dimensionResource(
                            id = R.dimen.size_4dp
                        )
                    ), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = match.location,
                    color = MaterialTheme.appColors.textField.labelDark,
                    fontWeight = FontWeight.W400,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp
                )
                Text(
                    text = match.divisions,
                    color = MaterialTheme.appColors.textField.labelDark,
                    fontWeight = FontWeight.W400,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8dp)))
    }
}