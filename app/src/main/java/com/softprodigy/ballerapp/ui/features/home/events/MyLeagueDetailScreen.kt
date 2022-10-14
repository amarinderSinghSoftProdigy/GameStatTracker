package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppScrollableTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.events.division.DivisionScreen
import com.softprodigy.ballerapp.ui.features.home.events.schedule.EventScheduleScreen
import com.softprodigy.ballerapp.ui.features.home.events.team.EventTeamsScreen
import com.softprodigy.ballerapp.ui.features.home.events.venues.VenuesScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyLeagueDetailScreen(
    moveToOpenDetails: (String) -> Unit,
    moveToOpenVenues: (String, String) -> Unit,
    moveToOpenDivisions: (String, String) -> Unit,
    moveToOpenTeams: (String, String) -> Unit,
    eventViewModel: EventViewModel
) {
    val tabData = listOf(
        MyLeagueTabItems.Schedule,
        MyLeagueTabItems.Divisions,
        MyLeagueTabItems.Teams,
        MyLeagueTabItems.Venues,
    )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffScreenLimit = 1,
    )

    Column {
        MyLeagueTopTabs(pagerState = pagerState, tabData = tabData)
        MyLeagueContent(
            pagerState = pagerState,
            moveToOpenDetails,
            moveToOpenVenues,
            moveToOpenDivisions,
            moveToOpenTeams,
            eventViewModel
        )
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyLeagueTopTabs(pagerState: PagerState, tabData: List<MyLeagueTabItems>) {
    val coroutineScope = rememberCoroutineScope()
    AppScrollableTabRow(
        pagerState = pagerState, tabs = {
            tabData.forEachIndexed { index, item ->
                AppTabLikeViewPager(
                    title = item.stringId,
                    painter = painterResource(id = item.icon),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyLeagueContent(
    pagerState: PagerState,
    moveToOpenDetails: (String) -> Unit,
    moveToOpenVenues: (String, String) -> Unit,
    moveToOpenDivisions: (String, String) -> Unit,
    moveToOpenTeams: (String, String) -> Unit,
    eventViewModel: EventViewModel
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> EventScheduleScreen(eventViewModel, moveToOpenDetails = moveToOpenDetails)
            1 -> DivisionScreen(moveToOpenDivisions, eventViewModel)
            2 -> EventTeamsScreen(moveToOpenTeams, eventViewModel)
            3 -> VenuesScreen(moveToOpenVenues, eventViewModel)
        }
    }
}

enum class MyLeagueTabItems(val icon: Int, val stringId: String) {
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),
    Divisions(R.drawable.ic_divisions, stringId = "divisions"),
    Teams(R.drawable.ic_users, stringId = "teams"),
    Venues(R.drawable.ic_location, stringId = "venues")
}