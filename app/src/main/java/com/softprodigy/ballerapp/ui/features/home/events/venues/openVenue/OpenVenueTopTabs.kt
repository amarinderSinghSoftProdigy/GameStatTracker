package com.softprodigy.ballerapp.ui.features.home.events.venues.openVenue

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
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OpenVenueTopTabs() {

    val tabData = listOf(
        OpenVenueTabItems.Details,
        OpenVenueTabItems.Courts,
        OpenVenueTabItems.Schedule,

        )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffScreenLimit = 1,
    )

    Column {
        OpenVenueTabs(pagerState = pagerState, tabData = tabData)
        OpenVenueContents(pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OpenVenueTabs(pagerState: PagerState, tabData: List<OpenVenueTabItems>) {
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
fun OpenVenueContents(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> OpenVenuesDetailsScreen()
            1 -> DivisionScreen()
            2 -> EventTeamsScreen()
        }
    }
}

enum class OpenVenueTabItems(val icon: Int, val stringId: String) {

    Details(R.drawable.ic_details, stringId = "details"),
    Courts(R.drawable.ic_court, stringId = "courts"),
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),

}