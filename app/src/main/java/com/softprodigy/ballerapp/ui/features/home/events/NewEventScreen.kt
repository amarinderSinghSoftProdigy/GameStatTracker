package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.features.home.events.practice.PracticeScreen
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewEventScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(pageCount = 3) // Add the count for number of pages
        Column(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    color = Color.White
                )

        ) {
            AppText(
                text = stringResource(id = R.string.event_type),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                EventTabs(pagerState = pagerState)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            PracticeScreen()
        }

        ButtonWithLeadingIcon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(
                    dimensionResource(
                        id = R.dimen.size_140dp
                    )
                ),
            text = stringResource(id = R.string.save),
            onClick = {

            },
            iconAllowed = false
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventTabs(pagerState: PagerState) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val width = screenWidth.minus(dimensionResource(id = R.dimen.size_16dp).times(4))

    val list = listOf(EventTabItems.Practice, /*EventTabItems.Game,*/ EventTabItems.Activity)
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .width(width)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))),
        indicator = {}
    ) {
        list.forEachIndexed { index, text ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_32dp))
                    .width(dimensionResource(id = R.dimen.size_100dp))
                    .background(
                        if (selected)
                            MaterialTheme.appColors.material.primaryVariant
                        else
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

enum class EventTabItems(val stringId: String) {
    Practice(stringId = "practice"),
    Game(stringId = "game"),
    Activity(stringId = "activity")
}