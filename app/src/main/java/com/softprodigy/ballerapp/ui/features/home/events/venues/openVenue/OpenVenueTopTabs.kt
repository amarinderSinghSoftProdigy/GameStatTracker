package com.softprodigy.ballerapp.ui.features.home.events.venues.openVenue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppStaticTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.EmptyScreen
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(
                start = dimensionResource(id = R.dimen.size_20dp),
                end = dimensionResource(id = R.dimen.size_20dp)
            )
    ) {
        AppStaticTabRow(
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
            1 -> EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
            2 -> EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
        }
    }
}

enum class OpenVenueTabItems(val icon: Int, val stringId: String) {

    Details(R.drawable.ic_details, stringId = "details"),
    Courts(R.drawable.ic_court, stringId = "courts"),
    Schedule(R.drawable.ic_schedule, stringId = "schedule"),

}