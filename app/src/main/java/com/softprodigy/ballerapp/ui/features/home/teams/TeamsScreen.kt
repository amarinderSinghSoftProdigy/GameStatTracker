package com.softprodigy.ballerapp.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.AppScrollableTabRow
import com.softprodigy.ballerapp.ui.features.components.AppTabLikeViewPager
import com.softprodigy.ballerapp.ui.features.components.SelectTeamDialog
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
    name: String?,
    showDialog: Boolean,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    dismissDialog: (Boolean) -> Unit,
    OnTeamDetailsSuccess: (String) -> Unit,
    onCreateTeamClick: () -> Unit
) {
    val vm: TeamViewModel = hiltViewModel()
    val context = LocalContext.current
    val state = vm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        vm.onEvent(TeamUIEvent.OnTeamSelected(team))
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                team.colorCode.replace(
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
                    OnTeamDetailsSuccess.invoke(uiEvent.teamId)
                }
            }
        }
    }
    val tabData = listOf(
        TeamsTabItems.Standings,
        TeamsTabItems.Chat,
        TeamsTabItems.Roaster,
        TeamsTabItems.Leaderboard,
    )

    val pagerState = rememberPagerState(
        pageCount = tabData.size,
        initialOffScreenLimit = 1,
        infiniteLoop = true,
        initialPage = 0,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()


    Column {
        TeamsTopTabs(pagerState = pagerState, tabData = tabData)
        TeamsContent(pagerState = pagerState, vm)
    }


    Box(Modifier.fillMaxSize()) {

        if (showDialog) {
            SelectTeamDialog(
                teams = vm.teamUiState.value.teams,
                onDismiss = { dismissDialog.invoke(false) },
                onConfirmClick = { vm.onEvent(TeamUIEvent.OnConfirmTeamClick(it)) },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam,
                showLoading = state.isLoading,
                onCreateTeamClick = onCreateTeamClick
            )
        }

/*        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_teams_large),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.no_players_in_team),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.add_players_to_use_app),
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))


            LeadingIconAppButton(
                icon = painterResource(id = R.drawable.ic_add_player),
                text = stringResource(id = R.string.add_players),
                onClick = {
                    if (state.selectedTeam == null) {
                        vm.onEvent(TeamUIEvent.ShowToast(message))
                    } else {
                        addPlayerClick(state.selectedTeam)
                    }
                },
            )
        }*/
    }
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
            1 -> EmptyScreen()
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

enum class TeamsTabItems(val icon: Int, val stringId: String) {
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Chat(R.drawable.ic_chat, stringId = "chat"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
    Leaderboard(R.drawable.ic_leaderboard, stringId = "leaderboard")
}