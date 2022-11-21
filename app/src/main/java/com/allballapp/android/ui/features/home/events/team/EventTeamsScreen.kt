package com.allballapp.android.ui.features.home.events.team

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.R
@Composable
fun EventTeamsScreen(moveToOpenTeams: (String, String) -> Unit, eventViewModel: EventViewModel) {
    val state = eventViewModel.eventState.value.divisionWiseTeamResponse
    remember {
        eventViewModel.onEvent(EvEvents.RefreshTeamsByDivision)
    }
    val expand = remember {
        true
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxWidth()) {
            items(state) { item ->
                if (item.team.isNotEmpty())
                    FoldableItem(
                        expanded = expand,
                        headerBackground = MaterialTheme.appColors.material.primary,
                        headerBorder = BorderStroke(0.dp, Color.Transparent),
                        header = { isExpanded ->
                            Column {
                                EventTeamHeader(divisionName = item.divisionName, isExpanded)
                                if (!isExpanded) {
                                    DividerCommon()
                                }
                            }
                        },
                        childItems = item.team,
                        hasItemLeadingSpacing = false,
                        hasItemTrailingSpacing = false,
                        itemSpacing = 0.dp,
                        itemHorizontalPadding = 0.dp,
                        itemsBackground = MaterialTheme.appColors.material.primary,
                        item = { team, index ->
                            Column {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = dimensionResource(id = R.dimen.size_16dp),
                                            end = dimensionResource(id = R.dimen.size_16dp)
                                        )
                                        .background(
                                            color = MaterialTheme.appColors.material.surface,
                                            shape = RoundedCornerShape(
                                                topStart = if (index == 0) dimensionResource(id = R.dimen.size_8dp) else 0.dp,
                                                topEnd = if (index == 0) dimensionResource(id = R.dimen.size_8dp) else 0.dp,
                                                bottomEnd = if (index == item.team.size - 1) dimensionResource(
                                                    id = R.dimen.size_8dp
                                                ) else 0.dp,
                                                bottomStart = if (index == item.team.size - 1) dimensionResource(
                                                    id = R.dimen.size_8dp
                                                ) else 0.dp
                                            )
                                        )
                                ) {
                                    EventTeamSubItem(team) {
                                        moveToOpenTeams(team.name, team.logo)
                                    }
                                }
                                if (index == item.team.size - 1) {
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                                    DividerCommon()
                                }
                            }
                        }
                    )
            }
        }
    }
    if (eventViewModel.eventState.value.isLoading) {
        CommonProgressBar()
    }
}

@Composable
fun EventTeamHeader(divisionName: String, isExpanded: Boolean = false) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_14dp)
            ), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = divisionName,
            color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(
                id = R.dimen.txt_size_12
            ).value.sp
        )
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
}

@Composable
fun EventTeamSubItem(team: Team, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_12dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + team.logo,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.appColors.material.onSurface,
                        CircleShape
                    ),
                onError = {
                    Placeholder(R.drawable.ic_team_placeholder)
                },
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                isCrossFadeEnabled = false
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = team.name,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                color =
                MaterialTheme.appColors.buttonColor.backgroundEnabled

            )
        }
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        height = dimensionResource(id = R.dimen.size_12dp),
                        width = dimensionResource(id = R.dimen.size_12dp)
                    )
                    .rotate(270f),
                tint = ColorGreyLighter
            )
        }
    }
}