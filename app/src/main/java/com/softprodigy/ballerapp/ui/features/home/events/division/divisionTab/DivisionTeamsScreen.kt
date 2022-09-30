package com.softprodigy.ballerapp.ui.features.home.events.division.divisionTab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.home.events.team.EventTeamViewModel
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun DivisionTeamScreen(eventTeamViewModel: EventTeamViewModel = hiltViewModel()) {

    val state = eventTeamViewModel.eventTeamState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.appColors.material.primary)
    ) {

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_16dp))
                )
        ) {
            items(state.eventTeams[0].teams) { item ->

                DivisionTeamItem(item)

            }
        }
    }
}

@Composable
fun DivisionTeamItem(team: Team) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_12dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + team.logo,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.appColors.material.onSurface, CircleShape
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
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

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
                tint = MaterialTheme.appColors.buttonColor.textDisabled
            )
        }
    }
    Divider(color = MaterialTheme.appColors.material.primary)
}