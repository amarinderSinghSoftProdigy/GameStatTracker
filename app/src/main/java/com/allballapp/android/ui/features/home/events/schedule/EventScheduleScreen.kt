package com.allballapp.android.ui.features.home.events.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventDetail
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.features.home.events.Matches
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.utils.CommonUtils
import com.google.accompanist.flowlayout.FlowRow
import timber.log.Timber

@Composable
fun EventScheduleScreen(
    vm: EventViewModel,
    moveToOpenDetails: (String) -> Unit,
) {
    val state = vm.eventState.value
    val expand = remember { true }

    remember {
        vm.onEvent(EvEvents.GetSchedule(state.eventId))
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.appColors.material.primary)
    ) {
        if (state.scheduleResponse.isEmpty()) {
            EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))
        } else {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(state.scheduleResponse) { item ->
                    if (item.matches.isNotEmpty()) {
                        FoldableItem(
                            expanded = expand,
                            headerBackground = MaterialTheme.appColors.material.surface,
                            headerBorder = BorderStroke(0.dp, Color.Transparent),
                            header = { isExpanded ->
                                EventScheduleHeaderItem(
                                    isExpanded = isExpanded,
                                    date = CommonUtils.formatDateSingle(item._id),
                                    gamesCount = item.totalgames
                                )
                            },
                            childItems = item.matches,
                            hasItemLeadingSpacing = false,
                            hasItemTrailingSpacing = false,
                            itemSpacing = 0.dp,
                            itemHorizontalPadding = 0.dp,
                            itemsBackground = MaterialTheme.appColors.material.primary,
                            item = { match, index ->
                                EventScheduleSubItem(match, item.event, index) {
                                    //moveToOpenDetails(match.divisions)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (state.showLoading) {
        CommonProgressBar()
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
                    color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = "(${gamesCount} ${stringResource(id = R.string.games)})",
                    color = MaterialTheme.appColors.textField.labelDark,
                    style = MaterialTheme.typography.h5
                )

            }
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        height = dimensionResource(id = R.dimen.size_12dp),
                        width = dimensionResource(id = R.dimen.size_12dp)
                    )
                    .then(
                        if (isExpanded) Modifier else Modifier.rotate(180f)
                    ),
                tint = ColorGreyLighter
            )
        }
        AppDivider(color = MaterialTheme.appColors.buttonColor.backgroundDisabled)
    }

}

@Composable
fun EventScheduleSubItem(
    match: Matches,
    event: EventDetail,
    index: Int,
    moveToOpenDetails: () -> Unit
) {
    if (index == 0) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8dp)))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
    ) {
        Text(
            text = match.timeSlot,
            color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(
                id = R.dimen.txt_size_12
            ).value.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.size_8dp)))

        FlowRow {
            Timber.e("match " + match.timeSlot + " - " + match.pairs.size)
            match.pairs.forEach {
                Column {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { moveToOpenDetails() }
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
                                    text = it.teams[0].name,
                                    color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dimensionResource(
                                        id = R.dimen.txt_size_12
                                    ).value.sp
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                                CoilImage(src = com.allballapp.android.BuildConfig.IMAGE_SERVER + it.teams[0].logo,
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

                                CoilImage(src = com.allballapp.android.BuildConfig.IMAGE_SERVER + it.teams[1].logo,
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.size_32dp))
                                        .clip(CircleShape),
                                    isCrossFadeEnabled = false,
                                    onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                    onError = { Placeholder(R.drawable.ic_team_placeholder) })
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.size_12dp)))
                                Text(
                                    text = it.teams[1].name,
                                    color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dimensionResource(
                                        id = R.dimen.txt_size_12
                                    ).value.sp
                                )

                            }
                        }
                        AppDivider(color = MaterialTheme.appColors.buttonColor.backgroundDisabled)
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
                                text = event.address,
                                color = MaterialTheme.appColors.textField.labelDark,
                                fontWeight = FontWeight.W400,
                                fontSize = dimensionResource(
                                    id = R.dimen.txt_size_12
                                ).value.sp
                            )
                            Text(
                                text = it.divisionName,
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
        }
    }
}