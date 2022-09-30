package com.softprodigy.ballerapp.ui.features.home.events.opportunities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventState
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.features.home.events.LeagueItem
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun OpportunitieScreen(state: EventState, vm: EventViewModel, moveToOppDetails: (String) -> Unit) {

    remember {
        vm.onEvent(EvEvents.GetOpportunities)
    }
    if (state.isLoading) {
        CommonProgressBar()
    } else if (state.opportunitiesList.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(id = R.dimen.size_16dp),
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp)
                    )
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.opportunitiesList) { leag ->
                        LeagueItem(leag, true) {
                            vm.onEvent(EvEvents.SetEventId(leag.id))
                            moveToOppDetails(leag.name)
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_events_large),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))
                Text(
                    color = MaterialTheme.appColors.textField.label,
                    text = stringResource(id = R.string.no_opportunities),
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }

}
