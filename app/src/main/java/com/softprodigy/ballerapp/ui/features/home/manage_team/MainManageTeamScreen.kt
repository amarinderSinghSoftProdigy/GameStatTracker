package com.softprodigy.ballerapp.ui.features.home.manage_team

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
import com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard.ManageTeamLeaderBoard
import com.softprodigy.ballerapp.ui.features.home.manage_team.teams.ManageTeamScreen
import com.softprodigy.ballerapp.ui.features.home.teams.manage_team.ManageTeamRoster
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainManageTeamScreen() {
    val managedTabData = listOf(
        ManageTeamsTabItems.Team,
        ManageTeamsTabItems.Roaster,
        ManageTeamsTabItems.Leaderboards,
        /*ManageTeamsTabItems.Users,*/
    )

    val pagerState = com.softprodigy.ballerapp.ui.features.components.rememberPagerState(
        pageCount = managedTabData.size,
        initialOffScreenLimit = 1,
        infiniteLoop = true,
        initialPage = 0,
    )

    Column {
        ManageTeamsTopTabs(pagerState = pagerState, tabData = managedTabData)
        ManageTeamsContent(pagerState = pagerState)
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ManageTeamsContent(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> ManageTeamScreen()
            1 -> ManageTeamRoster()
            2 -> ManageTeamLeaderBoard()
            /*3 -> ManageTeamScreen()*/
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ManageTeamsTopTabs(pagerState: PagerState, tabData: List<ManageTeamsTabItems>) {
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

enum class ManageTeamsTabItems(val icon: Int, val stringId: String) {
    Team(R.drawable.ic_team, stringId = "team"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
    Leaderboards(R.drawable.ic_leaderboard, stringId = "leaderboard"),
    Users(R.drawable.ic_users, stringId = "users")
}