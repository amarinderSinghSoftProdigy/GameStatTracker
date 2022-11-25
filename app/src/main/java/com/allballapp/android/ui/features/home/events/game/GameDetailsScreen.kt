package com.allballapp.android.ui.features.home.events.game

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
fun GameDetailsScreen(gameId: String, vm: EventViewModel, moveToGameRules: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = 3)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(gameId, pagerState = pagerState, vm, moveToGameRules)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        GameTabItems.Details,
        GameTabItems.Stats,
        GameTabItems.Summary,
    )
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
fun TabsContent(
    gameId: String,
    pagerState: PagerState,
    vm: EventViewModel,
    moveToGameRules: () -> Unit
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> GameDetailsTab(gameId, vm, moveToGameRules)
            1 -> GameStatsTab(vm)
            2 -> GameSumTab()
        }
    }
}

enum class GameTabItems(val icon: Int, val stringId: String) {
    Details(R.drawable.ic_details, stringId = "details"),
    Stats(R.drawable.ic_summary, stringId = "stats"),
    Summary(R.drawable.ic_stats, stringId = "summary"),
}
