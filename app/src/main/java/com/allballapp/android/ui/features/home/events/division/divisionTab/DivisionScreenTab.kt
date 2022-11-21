package com.allballapp.android.ui.features.home.events.division.divisionTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.stringResourceByName
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionScreenTab(divisionId: String, eventViewModel: EventViewModel) {

    val list = listOf(
        DivisionTabs.Teams,
        DivisionTabs.Standings,
        DivisionTabs.Schedule,
    )

    val pagerState = rememberPagerState(pageCount = list.size)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        //DivisionTabs(pagerState = pagerState, list = list)
        DivisionTabsContent(pagerState = pagerState, divisionId, eventViewModel)
    }
}

@ExperimentalPagerApi
@Composable
fun DivisionTabs(pagerState: PagerState, list: List<DivisionTabs>) {

    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val width = screenWidth.minus(dimensionResource(id = R.dimen.size_20dp).times(3))

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
                            tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        Text(
                            stringResourceByName(name = item.stringId),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = if (pagerState.currentPage == index) MaterialTheme.appColors.buttonColor.backgroundEnabled else MaterialTheme.appColors.textField.label
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
fun DivisionTabsContent(
    pagerState: PagerState,
    divisionId: String,
    eventViewModel: EventViewModel
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> DivisionTeamScreen(divisionId, eventViewModel)
            /*1 -> StandingByLeagueAndDivisionScreen(divisionId, eventViewModel)
            2 -> EventScheduleScreen(eventViewModel, moveToOpenDetails = {})*/
        }
    }
}

enum class DivisionTabs(val icon: Int, val stringId: String) {
    Teams(R.drawable.ic_users, stringId = "teams"),
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),
}
