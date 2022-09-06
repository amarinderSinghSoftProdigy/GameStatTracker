package com.softprodigy.ballerapp.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.LeadingIconAppButton
import com.softprodigy.ballerapp.ui.features.components.SelectTeamDialog
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.features.home.TabContentScreen
import com.softprodigy.ballerapp.ui.features.home.teams.standing.StandingScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.SetupTeamViewModel
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.TeamSetupUIEvent
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamsScreen(
    name: String?,
    showDialog: Boolean,
    setupTeamViewModel: SetupTeamViewModel,
    dismissDialog: (Boolean) -> Unit,
    addPlayerClick: (Team) -> Unit
) {
    val vm: TeamViewModel = hiltViewModel()
    val context = LocalContext.current
    val state = vm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        vm.onEvent(TeamUIEvent.OnTeamSelected(team))
        setupTeamViewModel.onEvent(
            TeamSetupUIEvent.OnColorSelected(
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
        initialOffScreenLimit = 2,
        infiniteLoop = true,
        initialPage = 1,
    )
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()


    Column {
        StandingTopTabs(pagerState = pagerState, tabData = tabData)
        StandingContent(pagerState = pagerState, setupTeamViewModel = setupTeamViewModel)
    }


    Box(Modifier.fillMaxSize()) {

        if (showDialog) {
            SelectTeamDialog(
                teams = vm.teamUiState.value.teams,
                onDismiss = { dismissDialog.invoke(false) },
                onConfirmClick = { vm.onEvent(TeamUIEvent.OnConfirmTeamClick) },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam
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
fun  StandingContent(pagerState: PagerState,setupTeamViewModel:SetupTeamViewModel){
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { index ->
   /*     Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tabData[index].first, color = Color.Black
            )
        }*/
        when (index) {
            0 -> StandingScreen(setupTeamViewModel)
            1 -> StandingScreen(setupTeamViewModel)
            2 -> StandingScreen(setupTeamViewModel)
            3 -> StandingScreen(setupTeamViewModel)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StandingTopTabs(pagerState: PagerState, tabData: List<TeamsTabItems>){
    val coroutineScope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions), color = MaterialTheme.appColors.material.primaryVariant
            )
        },
        backgroundColor =  MaterialTheme.appColors.material.surface,
        edgePadding = dimensionResource(id = R.dimen.size_25dp)
    ) {
        tabData.forEachIndexed { index, item ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }, text = {
                Row() {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                    Text(
                        stringResourceByName(name = item.stringId),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                    )
                }

            }/*, icon = {
//                Icon(imageVector = item.second, contentDescription = null, tint = Color.Black)
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                )
            }*/)
        }
    }
}

enum class TeamsTabItems(val icon: Int, val stringId: String) {
    Standings(R.drawable.ic_standing, stringId = "standings"),
    Chat(R.drawable.ic_chat, stringId = "chat"),
    Roaster(R.drawable.ic_roaster, stringId = "roaster"),
    Leaderboard(R.drawable.ic_leaderboard, stringId = "leaderboard")
}