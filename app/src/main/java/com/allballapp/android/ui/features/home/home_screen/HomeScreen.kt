package com.allballapp.android.ui.features.home.home_screen

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.HomeChannel
import com.allballapp.android.ui.features.home.HomeFirstTimeLoginScreen
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.features.home.teams.TeamChannel
import com.allballapp.android.ui.features.home.teams.TeamUIEvent
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.allballapp.android.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
//    role: String,
//    isOrganization: Boolean,
    vm: HomeViewModel,
    teamVm: TeamViewModel,
    addProfileClick: () -> Unit,
    logoClick: () -> Unit,
    onInvitationCLick: () -> Unit,
    onChatCLick: () -> Unit,
    gotToProfile: () -> Unit,
    OnTeamDetailsSuccess: (String, String) -> Unit,
    showDialog: Boolean,
    dismissDialog: (Boolean) -> Unit,
    onCreateTeamClick: (Team?) -> Unit,
    onTeamNameClick: (Boolean) -> Unit,
    onInviteClick: () -> Unit,
    onSettingClick: () -> Unit,
    onOpportunityClick: (String) -> Unit,
    onLeagueClick: (String) -> Unit,
    onEventsClick: () -> Unit,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    refreshTeamListing: String = "",
) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = AppConstants.DEFAULT_COLOR)
    val teamName = dataStoreManager.getTeamName.collectAsState(initial = "")
    val homeState = vm.state.value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val teamState = teamVm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        teamVm.onEvent(TeamUIEvent.OnTeamSelected(team))
    }
    val refresh = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = refreshTeamListing, block = {
        /* New team is added so refresh the teams listing*/
        if (refreshTeamListing == true.toString()) {
            teamVm.getTeamsUserId()
        }
    })
    remember {
        coroutineScope.launch {
            if (UserStorage.token.isNotEmpty()) {
                vm.getUnreadMessageCount()
                vm.getUserInfo()
                if (UserStorage.userId.isNotEmpty()) {
                    teamVm.getTeamsUserId()
                }
            }
        }
    }

    val onTeamSelectionConfirmed = { team: Team? ->
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                (team?.colorCode ?: "").replace(
                    "#", ""
                )
            )
        )
    }

    LaunchedEffect(key1 = Unit) {
        vm.homeChannel.collect { uiEvent ->
            when (uiEvent) {
                is HomeChannel.OnUserIdUpdate -> {
                    teamVm.getTeamsUserId()
                }
                is HomeChannel.OnSwapListSuccess -> {
                    vm.onEvent(HomeScreenEvent.HideSwap(true))
                    //showSwapDialog.value = true
                }
                is HomeChannel.ShowToast -> {
                    Toast.makeText(
                        context, uiEvent.message.asString(context), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        teamVm.teamChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamChannel.ShowToast -> {
                    /* Toast.makeText(
                         context,
                         uiEvent.message.asString(context),
                         Toast.LENGTH_LONG
                     ).show()*/
                }
                is TeamChannel.OnTeamDetailsSuccess -> {
                    OnTeamDetailsSuccess.invoke(uiEvent.teamId, uiEvent.teamName)
                    vm.getHomePageDetails(uiEvent.teamId)
                    vm.showBottomAppBar(uiEvent.show)
                }
                else -> {}
            }
        }
    }
    //val swipeRefreshState = rememberSwipeRefreshState(refresh.value)

    /*SwipeRefresh(state = swipeRefreshState,
        onRefresh = {

        }) {*/

    CoachFlowBackground(show = homeState.showBottomAppBar,
        colorCode = color.value.ifEmpty { AppConstants.DEFAULT_COLOR },
        teamLogo = com.allballapp.android.BuildConfig.IMAGE_SERVER + homeState.user.profileImage,
        click = {
            when (it) {
                Options.PROFILE -> {
                    gotToProfile()
                }
                Options.LOGOUT -> {
                    logoClick()
                }
                Options.SWAP_PROFILES -> {
                    vm.onEvent(HomeScreenEvent.OnSwapClick(true))
                }
                Options.INVITE -> {
                    onInviteClick()
                }
                Options.SETTINGS -> {
                    onSettingClick()
                }
                else -> {

                }
            }
        }) {
        if (!homeState.showBottomAppBar) {
            HomeFirstTimeLoginScreen(vm, teamVm, {
                onLeagueClick(it)
            }, {
                onOpportunityClick(it)
            }, {
                onTeamNameClick(it)
            }, {
                onCreateTeamClick(null)
            }, {
                onInvitationCLick()
            })
        } else /*if (role.isNotEmpty())*/ {
            Box {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.size_16dp),
                            end = dimensionResource(id = R.dimen.size_20dp),
                            start = dimensionResource(id = R.dimen.size_20dp)
                        ), verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))
                    AppText(
                        text = stringResource(id = R.string.hey_label).replace(
                            "name", homeState.user.firstName
                        ),
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500,
                        color = ColorBWBlack
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
                    AppText(
                        text = stringResource(id = R.string.welcome_back),
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.appColors.material.primaryVariant
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                    CompositionLocalProvider(
                        LocalOverScrollConfiguration provides null
                    ) {

                        Column(Modifier.verticalScroll(rememberScrollState())) {

                            if (teamState.teams.isNotEmpty()) {
                                UserFlowBackground(
                                    padding = 0.dp, color = Color.White.copy(0.95F)
                                ) {
                                    Box(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { /*logoClick()*/
                                            onTeamNameClick.invoke(true)
                                        }
                                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                        contentAlignment = Alignment.CenterStart) {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.CenterStart),
                                            verticalAlignment = Alignment.CenterVertically,

                                            ) {
                                            CoilImage(
                                                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + teamState.logo,
                                                modifier = Modifier
                                                    .size(dimensionResource(id = R.dimen.size_48dp))
                                                    .clip(CircleShape),
                                                isCrossFadeEnabled = false,
                                                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                                onError = { Placeholder(R.drawable.ic_team_placeholder) },
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                            Text(
                                                text =/* if (UserStorage.role.equals(
                                                UserType.REFEREE.key,
                                                ignoreCase = true
                                            )
                                        ) stringResource(id = R.string.team_total_hoop) else*/ teamState.teamName.ifEmpty { teamName.value },
                                                style = MaterialTheme.typography.h3,
                                                fontWeight = FontWeight.W700,
                                                modifier = Modifier.weight(1f),
                                                color = ColorBWBlack
                                            )
                                        }
                                        Icon(
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = ColorGreyLighter
                                        )
                                    }
                                }
                            }
                            /* if (role != UserType.REFEREE.key) {*/
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            if (homeState.unReadMessageCount > 0) {
                                UserFlowBackground(
                                    padding = 0.dp, color = Color.White.copy(0.95F)
                                ) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onChatCLick.invoke()
                                            }
                                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                        contentAlignment = Alignment.Center) {
                                        Row(
                                            Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_chat),
                                                contentDescription = "",
                                                tint = MaterialTheme.appColors.material.primaryVariant,
                                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                                            )
                                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                            Text(
                                                text = stringResource(id = R.string.team_chat),
                                                style = MaterialTheme.typography.h6,
                                                modifier = Modifier.weight(1f),
                                                color = ColorBWBlack
                                            )
                                        }
                                        Text(
                                            text = homeState.unReadMessageCount.toString(),
                                            fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                            modifier = Modifier.align(Alignment.CenterEnd),
                                            color = ColorBWBlack
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            }
                            UserFlowBackground(
                                padding = 0.dp, color = Color.White.copy(0.95F)
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onInvitationCLick.invoke()
                                        }
                                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                    contentAlignment = Alignment.Center) {
                                    Row(
                                        Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_invite),
                                            contentDescription = "",
                                            tint = MaterialTheme.appColors.material.primaryVariant,
                                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                        Text(
                                            text = stringResource(id = R.string.pending_invitations),
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.weight(1f),
                                            color = ColorBWBlack
                                        )
                                    }
                                    Text(
                                        text = homeState.homePageCoachModel.pendingInvitations.toString(),
                                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                        modifier = Modifier.align(Alignment.CenterEnd),
                                        color = ColorBWBlack
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            UserFlowBackground(
                                padding = 0.dp, color = Color.White.copy(0.95F)
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onOpportunityClick.invoke(AppConstants.OPP_WORK)
                                        }
                                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                    contentAlignment = Alignment.Center) {

                                    Row(
                                        Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_briefcase),
                                            contentDescription = "",
                                            tint = MaterialTheme.appColors.material.primaryVariant,
                                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                                        Text(
                                            text = stringResource(id = R.string.opportunities_to_work),
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.weight(1f),
                                            color = ColorBWBlack
                                        )
                                    }

                                    Text(
                                        text = homeState.homePageCoachModel.opportunityToWork.toString(),
                                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                        modifier = Modifier.align(Alignment.CenterEnd),
                                        color = ColorBWBlack
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                            Row {
                                EventItem(
                                    "my_events",
                                    "events_label",
                                    homeState.homePageCoachModel.myEvents.toString()
                                ) { onEventsClick() }
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                                EventInviteItem("invite_members", onInviteClick = onInviteClick)
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            UserFlowBackground(
                                padding = 0.dp, color = Color.White.copy(0.95F)
                            ) {
                                Box(Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpportunityClick(AppConstants.OPP_PLAY) }
                                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                    contentAlignment = Alignment.Center) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_home),
                                            contentDescription = "",
                                            tint = MaterialTheme.appColors.material.primaryVariant,
                                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                                        Text(
                                            text = stringResource(id = R.string.opportunities_to_play),
                                            color = ColorBWBlack,
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Text(
                                        text = homeState.homePageCoachModel.opportunityToPlay.toString(),
                                        color = ColorBWBlack,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                            Row {
                                EventItem(
                                    "my_leagues",
                                    "leagues",
                                    homeState.homePageCoachModel.myLeagues.toString(),
                                    R.drawable.ic_leagues
                                ) {
                                    onLeagueClick(AppConstants.MY_LEAGUE)
                                }
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                                EventItem(
                                    "all_leagues",
                                    "leagues",
                                    homeState.homePageCoachModel.allLeagues.toString(),
                                    R.drawable.ic_leagues
                                ) {
                                    //onOpportunityClick(AppConstants.ALL_LEAGUE)
                                    onLeagueClick(AppConstants.ALL_LEAGUE)
                                }

                            }
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                        }
                    }
                }
            }
        }
        //}
    }
    if (showDialog) {
        SelectTeamDialog(teams = teamVm.teamUiState.value.teams,
            onDismiss = { dismissDialog.invoke(false) },
            onConfirmClick = { teamId, teamName,isOrganization ->
                if (UserStorage.teamId != teamId) {
                    if (teamId == teamState.allBallId) {
                        vm.showBottomAppBar(false)
                    }
                    onTeamSelectionConfirmed(teamState.selectedTeam)
                    teamVm.onEvent(TeamUIEvent.OnConfirmTeamClick(teamId, teamName,isOrganization))
                }
            },
            onSelectionChange = onTeamSelectionChange,
            selected = teamState.selectedTeam,
            showLoading = teamState.isLoading,
            onCreateTeamClick = { onCreateTeamClick(teamState.selectedTeam) },
            teamVm = teamVm
        )
    }

    if (homeState.showSwapProfile) {
        SwapProfile(users = homeState.swapUsers, onDismiss = {
            vm.onEvent(HomeScreenEvent.HideSwap(false))
            //showSwapDialog.value = false
        }, onConfirmClick = {
            if (it._Id != UserStorage.userId) {
                vm.onEvent(HomeScreenEvent.OnSwapUpdate(it._Id))
            }
            vm.onEvent(HomeScreenEvent.HideSwap(false))
            //showSwapDialog.value = false
        }, showLoading = homeState.isDataLoading, onCreatePlayerClick = {
            addProfileClick()
            vm.onEvent(HomeScreenEvent.HideSwap(false))
        }, showCreatePlayerButton = true
        )
    }
    if (homeState.isDataLoading || teamState.isLoading) {
        CommonProgressBar()
    }
}


@Composable
fun RowScope.EventItem(
    headingId: String,
    stringId: String,
    value: String,
    painter: Int? = R.drawable.ic_events,
    color: Color = Color.White,
    click: () -> Unit
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = color.copy(0.95F)

    ) {
        Column(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .clickable { click() }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                painter?.let { painterResource(it) }?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = MaterialTheme.appColors.material.primaryVariant,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))

                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    text = stringResourceByName(headingId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }
            AppText(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                text = value,
                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                color = ColorBWBlack
            )
            AppText(
                /*   modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_5dp)),*/
                text = stringResourceByName(stringId),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.textField.label
            )
        }
    }
}

@Composable
fun RowScope.EventInviteItem(
    headingId: String, painter: Int = R.drawable.ic_invite, onInviteClick: () -> Unit
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = Color.White.copy(0.95F)
    ) {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(painter),
                    contentDescription = null,
                    tint = MaterialTheme.appColors.material.primaryVariant,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    text = stringResourceByName(headingId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
            ) {
                ButtonWithLeadingIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.invite),
                    onClick = {
                        onInviteClick()
                    },
                    painter = painterResource(id = R.drawable.ic_add_button),
                    isTransParent = false,
                    iconSize = dimensionResource(id = R.dimen.size_10dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MessageComponent() {
    val items = ArrayList<String>()
    items.add("")
    items.add("")
    items.add("")
    items.add("")

    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffScreenLimit = 2,
    )

    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxWidth()
        ) { page ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ball),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_48dp))
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = "Team name",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.weight(1f)
                )

            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        PagerIndicator(size = items.size, currentPage = pagerState.currentPage)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    }
}