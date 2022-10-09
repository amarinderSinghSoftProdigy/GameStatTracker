package com.softprodigy.ballerapp.ui.features.home.home_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.HomeChannel
import com.softprodigy.ballerapp.ui.features.home.HomeViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.TeamChannel
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    vm: HomeViewModel,
    teamVm: TeamViewModel,
    logoClick: () -> Unit,
    onInvitationCLick: () -> Unit,
    gotToProfile: () -> Unit,
    OnTeamDetailsSuccess: (String, String) -> Unit,
    showDialog: Boolean,
    dismissDialog: (Boolean) -> Unit,
    onCreateTeamClick: (Team?) -> Unit,
    onTeamNameClick: () -> Unit,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
    val role = dataStoreManager.getRole.collectAsState(initial = "")
    val teamName = dataStoreManager.getTeamName.collectAsState(initial = "")
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
    val homeState = vm.state.value
    val homeScreenState = homeScreenViewModel.homeScreenState.value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val teamState = teamVm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        teamVm.onEvent(TeamUIEvent.OnTeamSelected(team))
    }
    val showSwapDialog = remember {
        mutableStateOf(false)
    }

    remember {
        coroutineScope.launch {
            if (!UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
                homeScreenViewModel.getHomePageDetails()
                teamVm.getTeams()
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

    LaunchedEffect(key1 = Unit) {
        vm.homeChannel.collect { uiEvent ->
            when (uiEvent) {
                is HomeChannel.OnSwapListSuccess -> {
                    showSwapDialog.value = true
                }
                is HomeChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        teamVm.teamChannel.collect { uiEvent ->
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

    CoachFlowBackground(
        colorCode = color.value.ifEmpty { "0177C1" },
        teamLogo = BuildConfig.IMAGE_SERVER + homeState.user.profileImage, click = {
            when (it) {
                Options.PROFILE -> {
                    gotToProfile()
                }
                Options.LOGOUT -> {
                    logoClick()
                }
                Options.SWAP_PROFILES -> {
                    vm.onEvent(HomeScreenEvent.OnSwapClick)
                }
            }
        }) {
        Box {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                        start = dimensionResource(id = R.dimen.size_16dp)
                    )
                /*.verticalScroll(rememberScrollState())*/,
                verticalArrangement = Arrangement.Center
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

                if(teamState.teams.isNotEmpty()){
                UserFlowBackground(
                    padding = 0.dp,
                    color = Color.White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /*logoClick()*/
                                onTeamNameClick.invoke()
                            }
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            CoilImage(
                                src =BuildConfig.IMAGE_SERVER+teamState.logo,
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
                                text = if (UserStorage.role.equals(
                                        UserType.REFEREE.key,
                                        ignoreCase = true
                                    )
                                ) "Springfield Bucks Staff" else teamState.teamName.ifEmpty { teamName.value },
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.W700,
                                modifier = Modifier.weight(1f)
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
                if (role.value != UserType.REFEREE.key) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                /*.clickable {
                                   onInvitationCLick.invoke()
                               }*/
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize(),
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
                                )
                            }
                            Text(
                                text = homeScreenState.homePageCoachModel.opportunityToWork.toString(),
                                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onInvitationCLick.invoke()
                                }
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize(),
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
                                )
                            }
                            Text(
                                text = homeScreenState.homePageCoachModel.pendingInvitations.toString(),
                                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                /*UserFlowBackground(
                    padding = 0.dp,
                ) {
                    MessageComponent()
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))*/

                Row {
                    EventItem(
                        if (role.value != UserType.REFEREE.key)
                            "my_events" else "my_shifts",
                        "events_label",
                        homeScreenState.homePageCoachModel.myEvents.toString()
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                    EventInviteItem("invite_members")
                }

                if (role.value == UserType.REFEREE.key) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                /*.clickable {
                                   onInvitationCLick.invoke()
                               }*/
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize(),
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
                                )
                            }
                            Text(
                                text = homeScreenState.homePageCoachModel.opportunityToWork.toString(),
                                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }

                if (role.value != UserType.REFEREE.key) {

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    UserFlowBackground(
                        padding = 0.dp,
                        color = Color.White
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                            contentAlignment = Alignment.Center
                        ) {
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
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Text(
                                text = homeScreenState.homePageCoachModel.opportunityToPlay.toString(),
                                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Row {
                    EventItem(
                        "my_leagues",
                        "leagues",
                        homeScreenState.homePageCoachModel.myLeagues.toString(),
                        R.drawable.ic_leagues
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                    if (role.value == UserType.REFEREE.key) {
                        EventItem(
                            "", "", "", null, MaterialTheme.appColors.material.primary
                        )
                    }

                    if (role.value != UserType.REFEREE.key) {
                        EventItem(
                            "all_leagues",
                            "leagues",
                            homeScreenState.homePageCoachModel.allLeagues.toString(),
                            R.drawable.ic_leagues
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            }

            if (homeScreenState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.appColors.material.primaryVariant
                )
            }
        }
    }
    if (showDialog) {
        SelectTeamDialog(
            teams = teamVm.teamUiState.value.teams,
            onDismiss = { dismissDialog.invoke(false) },
            onConfirmClick = { teamId, teamName ->
                if (UserStorage.teamId != teamId) {
                    onTeamSelectionConfirmed(teamState.selectedTeam)
                    teamVm.onEvent(TeamUIEvent.OnConfirmTeamClick(teamId, teamName))
                }
            },
            onSelectionChange = onTeamSelectionChange,
            selected = teamState.selectedTeam,
            showLoading = teamState.isLoading,
            onCreateTeamClick = { onCreateTeamClick(teamState.selectedTeam) },
            showCreateTeamButton = role.value.equals(UserType.COACH.key, ignoreCase = true)
        )
    }

    if (showSwapDialog.value) {
        SwapProfile(
            users = homeState.swapUsers,
            onDismiss = { showSwapDialog.value = false },
            onConfirmClick = {
                if (it != UserStorage.userId) {
                    vm.onEvent(HomeScreenEvent.OnSwapUpdate(it))
                }
                showSwapDialog.value = false
            },
            showLoading = homeState.isDataLoading,
            onCreatePlayerClick = {
                vm.setAddProfile(true)
                showSwapDialog.value = false
            },
            showCreatePlayerButton = false
        )
    }
    if (homeState.isDataLoading) {
        CommonProgressBar()
    }
}


@Composable
fun RowScope.EventItem(
    headingId: String,
    stringId: String,
    value: String,
    painter: Int? = R.drawable.ic_events,
    color: Color = Color.White
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = color

    ) {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            horizontalAlignment = Alignment.CenterHorizontally
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
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_5dp)),
                text = stringResourceByName(stringId),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.textField.label
            )
        }
    }
}

@Composable
fun RowScope.EventInviteItem(
    headingId: String,
    painter: Int = R.drawable.ic_invite
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = Color.White
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                ButtonWithLeadingIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.invite),
                    onClick = { },
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
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