package com.softprodigy.ballerapp.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.AppScrollableTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.SelectTeamDialog
import com.softprodigy.ballerapp.ui.features.components.UserType
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.EmptyScreen
import com.softprodigy.ballerapp.ui.features.home.teams.leaderboard.LeaderBoardScreen
import com.softprodigy.ballerapp.ui.features.home.teams.roaster.RoasterScreen
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsScreen(
    vm: TeamViewModel = hiltViewModel(),
    showDialog: Boolean,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    dismissDialog: (Boolean) -> Unit,
    OnTeamDetailsSuccess: (String, String) -> Unit,
    onCreateTeamClick: (Team?) -> Unit,
    onBackPress: () -> Unit
) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val role = dataStoreManager.getRole.collectAsState(initial = "")
    val context = LocalContext.current
    val state = vm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        vm.onEvent(TeamUIEvent.OnTeamSelected(team))
    }

    val scope = rememberCoroutineScope()

    remember {
        scope.launch {
            if (role.value != UserType.REFEREE.key) {
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

    val tabData = if (role.value == UserType.REFEREE.key) {
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
        TeamsTopTabs(pagerState = pagerState, tabData = tabData)
        if (role.value != UserType.REFEREE.key) {
            TeamsContent(pagerState = pagerState, vm)
        } else {
            EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
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
                    }
                },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam,
                showLoading = state.isLoading,
                onCreateTeamClick = { onCreateTeamClick(state.selectedTeam) },
                showCreateTeamButton = role.value.equals(UserType.COACH.key, ignoreCase = true)
            )
        }
    }
    /* if (state.isLoading) {
         CommonProgressBar()
     }*/
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsContent(pagerState: PagerState, viewModel: TeamViewModel) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
        when (index) {
            0 -> StandingScreen()
            1 -> EmptyScreen(singleText = true, heading = stringResource(id = R.string.coming_soon))
            2 -> RoasterScreen(viewModel)
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

enum class TeamsTabItems(val icon: Int, val stringId: String) {
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Chat(R.drawable.ic_chat, stringId = "chat"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
    Leaderboard(R.drawable.ic_leaderboard, stringId = "leaderboards")
}