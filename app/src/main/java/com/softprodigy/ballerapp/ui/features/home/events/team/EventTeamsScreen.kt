package com.softprodigy.ballerapp.ui.features.home.events.team

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.DividerCommon
import com.softprodigy.ballerapp.ui.features.components.FoldableItem
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun EventTeamsScreen(moveToOpenTeams: (String) -> Unit, vm: EventTeamViewModel = hiltViewModel()) {
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
                    Column {
                        EventTeamHeader(divisionName = item.divisionName, isExpanded)
                        if (!isExpanded) {
                            DividerCommon()
                        }
                    }
                },
                childItems = item.teams,
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
                                        bottomEnd = if (index == item.teams.size - 1) dimensionResource(
                                            id = R.dimen.size_8dp
                                        ) else 0.dp,
                                        bottomStart = if (index == item.teams.size - 1) dimensionResource(
                                            id = R.dimen.size_8dp
                                        ) else 0.dp
                                    )
                                )
                        ) {
                            EventTeamSubItem(team) {
                                moveToOpenTeams(team.name)
                            }
                        }
                        if (index == item.teams.size - 1) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                            DividerCommon()
                        }
                    }
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
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
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
                src = BuildConfig.IMAGE_SERVER + team.logo,
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
                MaterialTheme.appColors.buttonColor.bckgroundEnabled

            )
        }
        Row {
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
        }
    }
}