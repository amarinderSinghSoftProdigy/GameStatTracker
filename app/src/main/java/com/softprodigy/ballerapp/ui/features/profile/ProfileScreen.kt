package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppStaticTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.profile.tabs.DocumentTab
import com.softprodigy.ballerapp.ui.features.profile.tabs.ProfileTabScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProfileScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    moveToEditProfile: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = 2)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState,vm)
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
fun TabsContent(pagerState: PagerState,vm:ProfileViewModel) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> ProfileTabScreen(vm)
            1 -> DocumentTab(vm)
        }
    }
}

enum class TabItems(val icon: Int, val stringId: String) {
    Profile(R.drawable.ic_profile, stringId = "profile_label"),
    Documents(R.drawable.ic_documents, stringId = "documents"),
}




