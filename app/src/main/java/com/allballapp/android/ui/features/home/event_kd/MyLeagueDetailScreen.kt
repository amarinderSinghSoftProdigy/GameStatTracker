package com.allballapp.android.ui.features.home.event_kd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.AppScrollableTabRow
import com.allballapp.android.ui.features.components.AppTabLikeViewPager
import com.allballapp.android.ui.features.components.rememberPagerState
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.event_kd.event_team.EventTeamsScreen
import com.allballapp.android.ui.features.home.event_kd.schedule.EventScheduleScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@Composable
fun MyLeagueDetailScreen() {
    val tabData = listOf(

        MyLeaguTabItems.Schedule,
        MyLeaguTabItems.Divisions,
        MyLeaguTabItems.Teams,
        MyLeaguTabItems.Venues,
    )
    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffScreenLimit = 1,
    )

    Column {
        MyLeagueTopTabs(pagerState = pagerState, tabData = tabData)
        MyLeagueContent(pagerState = pagerState)
    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyLeagueTopTabs(pagerState: PagerState, tabData: List<MyLeaguTabItems>) {
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
fun MyLeagueContent(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> EventScheduleScreen()
            1 -> EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
            2 -> EventTeamsScreen()
            3 -> EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
        }
    }
}

enum class MyLeaguTabItems(val icon: Int, val stringId: String) {
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),
    Divisions(R.drawable.ic_divisions, stringId = "divisions"),
    Teams(R.drawable.ic_users, stringId = "teams"),
    Venues(R.drawable.ic_location, stringId = "venues")
}