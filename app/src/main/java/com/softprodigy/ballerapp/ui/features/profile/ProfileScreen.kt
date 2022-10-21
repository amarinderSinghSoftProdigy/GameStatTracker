package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.SetTopBar
import com.softprodigy.ballerapp.ui.features.profile.tabs.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel,
    updateTopBar: (TopBarData) -> Unit
) {
    val state = vm.state.value
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
            val list = listOf(
                TabItems.RefereeProfile,
                TabItems.Documents,
                TabItems.Schedule,
                TabItems.Pay
            )
            val pagerState = rememberPagerState(pageCount = list.size)
            Tabs(pagerState = pagerState, list)
            TabsRefereeContent(pagerState, vm)
        } else {
            val pagerState = rememberPagerState(pageCount = 2)
            Tabs(pagerState = pagerState)
            TabsContent(pagerState = pagerState, vm, updateTopBar)
        }
    }

    if (state.isLoading) {
        CommonProgressBar()
    }
}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        TabItems.Profile,
        TabItems.Documents,
    )
    val coroutineScope = rememberCoroutineScope()
    Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.size_40dp),
                    end = dimensionResource(id = R.dimen.size_40dp)
                )
        ) {
            AppStaticTabRow(
                pagerState = pagerState, tabs = {
                    list.forEachIndexed { index, item ->
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
}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState, list: List<TabItems>) {

    val coroutineScope = rememberCoroutineScope()
    Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppScrollableTabRow(
                pagerState = pagerState, tabs = {
                    list.forEachIndexed { index, item ->
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
}

@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    vm: ProfileViewModel,
    updateTopBar: (TopBarData) -> Unit,
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> ProfileTabScreen(vm)
            1 -> DocumentTab(vm)
        }
        SetProfileTopBar(pagerState, page, updateTopBar)
    }
}

@ExperimentalPagerApi
@Composable
fun TabsRefereeContent(
    pagerState: PagerState,
    vm: ProfileViewModel,

    ) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> RefereeProfileScreen(vm)
            1 -> DocumentTab(vm)
            2 -> RefereeScheduleTab(vm)
            3 -> RefereePayTab(vm)
        }

    }
}

enum class TabItems(val icon: Int, val stringId: String) {
    Profile(R.drawable.ic_profile, stringId = "profile_label"),
    Documents(R.drawable.ic_documents, stringId = "documents"),
    Schedule(R.drawable.ic_schedule, "schedule"),
    Pay(R.drawable.ic_pay, "pay"),
    RefereeProfile(R.drawable.ic_profile, stringId = "profile_label"),

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SetProfileTopBar(
    pagerState: PagerState,
    page: Int,
    updateTopBar: (TopBarData) -> Unit,

    ) {
    val label = stringResource(id = R.string.profile_label)
    if (!pagerState.isScrollInProgress) {
        if (pagerState.currentPage == page) {
            val top = when (page) {
                0 -> TopBar.PROFILE
                1 -> TopBar.DOCUMENT
                else -> TopBar.SINGLE_LABEL
            }
            updateTopBar(TopBarData(label, top))
        }
    }
}
