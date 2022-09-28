package com.softprodigy.ballerapp.ui.features.home.manage_team

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.ui.features.components.AppScrollableTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard.ManageTeamLeaderBoard
import com.softprodigy.ballerapp.ui.features.home.manage_team.teams.ManageTeamScreen
import com.softprodigy.ballerapp.ui.features.home.teams.TeamChannel
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.manage_team.ManageTeamRoster
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainManageTeamScreen(vm: TeamViewModel, onSuccess: () -> Unit, onAddPlayerCLick: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        vm.teamChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamChannel.OnTeamsUpdate -> {
                    UserStorage.teamId = uiEvent.teamId
                    UserStorage.teamName = uiEvent.teamName
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context = context),
                        Toast.LENGTH_LONG
                    ).show()
                    onSuccess()
                }
            }
        }
    }

    val managedTabData = listOf(
        ManageTeamsTabItems.Team,
        ManageTeamsTabItems.Roaster,
        ManageTeamsTabItems.Leaderboards,
        /*ManageTeamsTabItems.Users,*/
    )

    val pagerState = rememberPagerState(
        pageCount = managedTabData.size,
        initialOffScreenLimit = 1,
    )

    Column {
        ManageTeamsTopTabs(pagerState = pagerState, tabData = managedTabData)
        ManageTeamsContent(pagerState = pagerState, vm, onAddPlayerCLick)
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ManageTeamsContent(
    pagerState: PagerState, vm: TeamViewModel, onAddPlayerCLick: () -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> ManageTeamScreen(vm)
            1 -> ManageTeamRoster(vm) {
                onAddPlayerCLick.invoke()
            }
            2 -> ManageTeamLeaderBoard(vm)
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