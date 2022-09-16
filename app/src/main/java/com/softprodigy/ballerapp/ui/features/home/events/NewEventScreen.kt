package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.home.TabContentScreen
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewEventScreen() {

    Box(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = 3) // Add the count for number of pages

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    top = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {

            EventTabs(pagerState = pagerState)
            EventTabsContent(pagerState = pagerState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventTabs(pagerState: PagerState) {

    val list = listOf(EventTabItems.Practice, EventTabItems.Game, EventTabItems.Activity)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.size_8dp),
                    topEnd = dimensionResource(id = R.dimen.size_16dp)
                ),
                color = Color.White
            )
    ) {

        AppText(
            text = stringResource(id = R.string.event_type),
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                top = dimensionResource(id = R.dimen.size_16dp)
            )
        )

        TabRow(selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    top = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_12dp)
                )
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))),
            indicator = { tabPositions: List<TabPosition> ->

            }
        ) {
            list.forEachIndexed { index, text ->
                val selected = pagerState.currentPage == index
                Tab(
                    modifier = if (selected) Modifier
                        .background(
                            MaterialTheme.appColors.material.primaryVariant
                        )
                    else Modifier
                        .background(
                            MaterialTheme.appColors.material.primary
                        ),
                    selected = selected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = text.name,
                            color = if (selected) Color.White else MaterialTheme.appColors.textField.label
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun EventTabsContent(pagerState: PagerState) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> {
                PracticeScreen()
            }
            1 -> {
                TabContentScreen(data = "Coming Soon")
            }
            2 -> {
                TabContentScreen(data = "Coming Soon")
            }
        }
    }
}

enum class EventTabItems(val stringId: String) {
    Practice(stringId = "practice"),
    Game(stringId = "game"),
    Activity(stringId = "activity")
}