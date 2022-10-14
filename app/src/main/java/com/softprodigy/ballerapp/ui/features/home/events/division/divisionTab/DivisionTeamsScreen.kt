package com.softprodigy.ballerapp.ui.features.home.events.division.divisionTab

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.TeamsByLeagueDivisionResponse
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.home.EmptyScreen
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventChannel
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun DivisionTeamScreen(
    divisionId: String,
    eventViewModel: EventViewModel /*eventTeamViewModel: EventTeamViewModel = hiltViewModel()*/
) {

    val state = eventViewModel.eventState.value.teamsByLeagueDivision
    val context = LocalContext.current

    remember {
        eventViewModel.onEvent(EvEvents.RefreshTeamsByLeagueAndDivision(divisionId))
    }

    LaunchedEffect(key1 = Unit) {
        eventViewModel.eventChannel.collect { uiEvent ->
            when (uiEvent) {
                is EventChannel.ShowDivisionTeamToast -> {
                   /* Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    )
                        .show()*/
                }
                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.appColors.material.primary)
    ) {

        if(state.isEmpty()){
            EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))
        }
        else {
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
                items(state) { item ->
                    DivisionTeamItem(item)

                }
            }
        }


    }
}

@Composable
fun DivisionTeamItem(teamLeague: TeamsByLeagueDivisionResponse) {
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
                src = BuildConfig.IMAGE_SERVER + teamLeague.team.logo,
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
                text = teamLeague.team.name,
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