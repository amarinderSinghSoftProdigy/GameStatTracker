package com.allballapp.android.ui.features.home.events.team.team_tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.stringResourceByName
import com.allballapp.android.ui.features.home.events.schedule.EventScheduleChildScreen
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.home.teams.roaster.RoasterScreen
import com.allballapp.android.ui.features.home.teams.standing.StandingScreen
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventTeamTabs(vm: TeamViewModel = hiltViewModel()) {

    val scope = rememberCoroutineScope()
    remember {
        scope.launch {
            vm.getTeams()
        }
    }
    val list = listOf(
        TeamTabs.Standings,
        TeamTabs.Schedule,
        TeamTabs.Roaster,
    )

    val pagerState = rememberPagerState(pageCount = list.size)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        TeamTabs(pagerState = pagerState, list = list)
        TeamTabsContent(pagerState = pagerState, vm)
    }
}

@ExperimentalPagerApi
@Composable
fun TeamTabs(pagerState: PagerState, list: List<TeamTabs>) {

    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val width = screenWidth.minus(dimensionResource(id = R.dimen.size_10dp).times(3))

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        modifier = Modifier.width(width),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = dimensionResource(id = R.dimen.size_2dp),
                color = MaterialTheme.appColors.material.primaryVariant
            )
        }
    ) {
        list.forEachIndexed { index, item ->
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)),
                            tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        Text(
                            stringResourceByName(name = item.stringId),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = if (pagerState.currentPage == index) MaterialTheme.appColors.buttonColor.bckgroundEnabled else MaterialTheme.appColors.textField.label
                        )
                    }
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TeamTabsContent(pagerState: PagerState, vm: TeamViewModel) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> StandingScreen()
            1 -> EventScheduleChildScreen(moveToOpenDetails = {})
            2 -> RoasterScreen(vm, {})
        }
    }
}

enum class TeamTabs(val icon: Int, val stringId: String) {
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
}
