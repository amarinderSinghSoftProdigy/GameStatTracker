package com.softprodigy.ballerapp.ui.features.home.events.division.divisionTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingListItem
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingTopTabs
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StandingByLeagueAndDivisionScreen(divisionId: String, eventViewModel: EventViewModel) {

    val state = eventViewModel.eventState.value.standingUIState
    val onStandingSelectionChange = { standing: Standing ->
        eventViewModel.onEvent(EvEvents.OnLeagueDivisionStandingSelected(standing))
    }

    remember {
        eventViewModel.onEvent(EvEvents.RefreshStandingByLeagueDivision(divisionId))
    }

    Box(modifier = Modifier.background(color = MaterialTheme.appColors.material.primary)) {

        if (state.isLoading) {
            CommonProgressBar()
        } else {
            val pagerState = rememberPagerState(
                pageCount = state.categories.size,
                initialOffScreenLimit = 1,
            )
            Column(Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                StandingTopTabs(pagerState, state.categories)
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    itemsIndexed(state.allTeam.registeredTeams) { index, standing ->
                        StandingListItem(
                            index + 1,
                            standing = standing,
                            selected = state.selectedStanding == standing,
                            key = state.categories[pagerState.currentPage].key
                        ) {
                            onStandingSelectionChange.invoke(standing)
                        }

                    }
                }
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp)))
            }
        }
    }
}
