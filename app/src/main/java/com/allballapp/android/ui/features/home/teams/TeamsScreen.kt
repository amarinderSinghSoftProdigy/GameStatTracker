package com.allballapp.android.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.allballapp.android.R
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.features.home.teams.chat.ChatViewModel
import com.allballapp.android.ui.features.home.teams.chat.TeamsChatScreen
import com.allballapp.android.ui.features.home.teams.leaderboard.LeaderBoardScreen
import com.allballapp.android.ui.features.home.teams.roaster.RoasterScreen
import com.allballapp.android.ui.features.home.teams.standing.StandingScreen
import com.allballapp.android.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.allballapp.android.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsScreen(
    vm: TeamViewModel,
    homeVm: HomeViewModel,
    chatViewModel: ChatViewModel,
    showDialog: Boolean,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    dismissDialog: (Boolean) -> Unit,
    OnTeamDetailsSuccess: (String, String) -> Unit,
    onCreateTeamClick: (Team?) -> Unit,
    onCreateNewConversationClick: (teamId: String) -> Unit,
    onTeamItemClick: () -> Unit,
    onAddPlayerClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    //val dataStoreManager = DataStoreManager(LocalContext.current)
    //val role = dataStoreManager.getRole.collectAsState(initial = "")
    val context = LocalContext.current
    val state = vm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        vm.onEvent(TeamUIEvent.OnTeamSelected(team))
    }

    val scope = rememberCoroutineScope()

    remember {
        scope.launch {
            if (UserStorage.role != UserType.REFEREE.key) {
                vm.getTeams()
            }
        }
    }

    val onTeamSelectionConfirmed = { team: Team? ->
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                (team?.colorCode ?: "").replace(
                    "#",
                    ""
                )
            )
        )
    }

    val message = stringResource(id = R.string.no_team_selected)

    LaunchedEffect(key1 = Unit) {


        vm.teamChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is TeamChannel.OnTeamDetailsSuccess -> {
                    OnTeamDetailsSuccess.invoke(uiEvent.teamId, uiEvent.teamName)
                }
            }
        }
    }

    val tabData = if (UserStorage.role == UserType.REFEREE.key) {
        listOf(TeamsTabItems.Chat)
    } else {
        listOf(
            TeamsTabItems.Standings,
            TeamsTabItems.Chat,
            TeamsTabItems.Roaster,
            TeamsTabItems.Leaderboard,
        )
    }


    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffScreenLimit = 1,
    )

    Column {
        if (UserStorage.role != UserType.REFEREE.key) {
            TeamsTopTabs(pagerState = pagerState, tabData = tabData)
            TeamsContent(
                pagerState = pagerState,
                onTeamItemClick = onTeamItemClick,
                onCreateNewConversationClick = onCreateNewConversationClick,
                viewModel = vm,
                onAddPlayerClick = onAddPlayerClick,
                homeVm = homeVm,
                chatViewModel = chatViewModel
            )
        } else {
            RefereeTeamsTopTabs(pagerState = pagerState, tabData = tabData)
            TeamsChatScreen(
                vm.teamUiState.value.teamColorPrimary,
                onTeamItemClick = onTeamItemClick,
                onCreateNewConversationClick = onCreateNewConversationClick,
                homeVm = homeVm,
                vm = chatViewModel
            )
            //EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
        }
    }


    Box(Modifier.fillMaxSize()) {
        if (showDialog) {
            SelectTeamDialog(
                teams = vm.teamUiState.value.teams,
                onDismiss = { dismissDialog.invoke(false) },
                onConfirmClick = { teamId, teamName ->
                    if (UserStorage.teamId != teamId) {
                        onTeamSelectionConfirmed(state.selectedTeam)
                        vm.onEvent(TeamUIEvent.OnConfirmTeamClick(teamId, teamName))
                        if(teamName == context.getString(R.string.team_total_hoop)) {
                            onHomeClick()
                        }
                    }
                },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam,
                showLoading = state.isLoading,
                onCreateTeamClick = { onCreateTeamClick(state.selectedTeam) },
                teamVm = vm
            )
        }
    }
    /* if (state.isLoading) {
         CommonProgressBar()
     }*/
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsContent(
    pagerState: PagerState,
    onTeamItemClick: () -> Unit,
    onCreateNewConversationClick: (teamId: String) -> Unit,
    onAddPlayerClick: () -> Unit,
    viewModel: TeamViewModel,
    homeVm: HomeViewModel,
    chatViewModel: ChatViewModel
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> StandingScreen()
            1 -> TeamsChatScreen(
                viewModel.teamUiState.value.teamColorPrimary,
                homeVm = homeVm,
                onTeamItemClick = onTeamItemClick,
                onCreateNewConversationClick = onCreateNewConversationClick,
                vm = chatViewModel
            )
            2 -> RoasterScreen(viewModel, onAddPlayerClick, true)
            3 -> LeaderBoardScreen()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsTopTabs(pagerState: PagerState, tabData: List<TeamsTabItems>) {

    val coroutineScope = rememberCoroutineScope()

    AppScrollableTabRow(
        pagerState = pagerState,
        tabs = {
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
        },
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RefereeTeamsTopTabs(pagerState: PagerState, tabData: List<TeamsTabItems>) {

    val coroutineScope = rememberCoroutineScope()

    tabData.forEachIndexed { index, item ->
        AppSingleTabLikeViewPager(
            modifier = Modifier,
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

}

enum class TeamsTabItems(val icon: Int, val stringId: String) {
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Chat(R.drawable.ic_chat, stringId = "chat"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
    Leaderboard(R.drawable.ic_leaderboard, stringId = "leaderboards")
}