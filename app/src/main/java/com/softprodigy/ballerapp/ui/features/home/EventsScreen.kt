package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.features.home.events.NewEventScreen
import com.softprodigy.ballerapp.ui.features.home.events.PracticeScreen
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventsScreen(name: String?, tabUpdate: (Int) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        TabLayout(tabUpdate)
    }
}

// on below line we are creating a
// composable function for our tab layout
@ExperimentalPagerApi
@Composable
fun TabLayout(tabUpdate: (Int) -> Unit) {
    // on below line we are creating variable for pager state.
    val pagerState = rememberPagerState(pageCount = 3) // Add the count for number of pages
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Tabs(pagerState = pagerState, tabUpdate)
        TabsContent(pagerState = pagerState, tabUpdate)
    }
}

// on below line we are
// creating a function for tabs
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, tabUpdate: (Int) -> Unit) {
    val list = listOf(
        TabItems.Events,
        TabItems.Leagues,
        TabItems.Opportunity,
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
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
                            color = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                    }
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        tabUpdate(index)
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

// on below line we are creating a tab content method
// in which we will be displaying the individual page of our tab .
@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, tabUpdate: (Int) -> Unit) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> {
                NewEventScreen()

            }
            1 -> {
                TabContentScreen(data = "Welcome to Shopping Screen")
            }
            2 -> {
                TabContentScreen(data = "Welcome to Settings Screen")
            }
        }
    }
}

// on below line we are creating a Tab Content
// Screen for displaying a simple text message.
@Composable
fun BoxScope.TabContentScreen(data: String) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_events_large),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
        Text(
            color = MaterialTheme.appColors.textField.label,
            text = stringResource(id = R.string.no_upcoming_events),
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.W700
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
        Text(
            color = MaterialTheme.appColors.textField.label,
            text = stringResource(id = R.string.add_events_to_use_app),
            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        /* LeadingIconAppButton(
             icon = painterResource(id = R.drawable.ic_add_player),
             text = stringResource(id = R.string.add_events),
             onClick = {},
         )*/
    }
}

enum class TabItems(val icon: Int, val stringId: String) {
    Events(R.drawable.ic_events, stringId = "my_events"),
    Leagues(R.drawable.ic_leagues, stringId = "my_leagues"),
    Opportunity(R.drawable.ic_star, stringId = "opportunities")
}