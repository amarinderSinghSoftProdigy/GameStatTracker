package com.allballapp.android.ui.features.home.event_kd.event_team

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.FoldableItem
import com.allballapp.android.ui.features.components.Placeholder
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.R

@Composable
fun EventTeamsScreen(vm: EventTeamViewModel = hiltViewModel()) {
    val state = vm.eventTeamState.value
    val expand = remember {
        true
    }
    LazyColumn(Modifier.fillMaxWidth()) {
        items(state.eventTeams) { item ->
            FoldableItem(
                expanded = expand,
                headerBackground = MaterialTheme.appColors.material.primary,
                headerBorder = BorderStroke(0.dp, Color.Transparent),
                header = { isExpanded ->
                    EventTeamHeader(divisionName = item.divisionName, isExpanded)
                },
                childItems = item.teams,
                hasItemLeadingSpacing = false,
                hasItemTrailingSpacing = false,
                itemSpacing = 0.dp,
                itemHorizontalPadding = 0.dp,
                itemsBackground = MaterialTheme.appColors.material.primary,
                item = { team, index ->
//                    EventScheduleSubItem(match, index)
                    EventTeamSubItem(team)
                }
            )
        }
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
                    height = dimensionResource(id = R.dimen.size_6dp),
                    width = dimensionResource(id = R.dimen.size_8dp)
                )
                .then(
                    if (!isExpanded) Modifier.rotate(180f) else Modifier
                ),
            tint = MaterialTheme.appColors.buttonColor.textDisabled
        )
    }
}

@Composable
fun EventTeamSubItem(team: Team) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_12dp),
                vertical = dimensionResource(id = R.dimen.size_1dp)
            )
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .background(color = MaterialTheme.appColors.material.surface)
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .clickable { },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        PaddingValues(
                            dimensionResource(id = R.dimen.size_12dp),
                            dimensionResource(id = R.dimen.size_12dp)
                        )
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
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
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                    contentDescription = "",
                    modifier = Modifier
                        .size(
                            height = dimensionResource(id = R.dimen.size_6dp),
                            width = dimensionResource(id = R.dimen.size_8dp)
                        )
                        .rotate(270f),
                    tint = MaterialTheme.appColors.buttonColor.textDisabled
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_24dp)))
            }
        }
    }
}