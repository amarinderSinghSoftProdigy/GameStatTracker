package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softprodigy.ballerapp.MainActivity
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.IntentData
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.*
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.components.CommonTabView
import com.softprodigy.ballerapp.ui.features.components.LogoutDialog
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.components.TopBar
import com.softprodigy.ballerapp.ui.features.components.TopBarData
import com.softprodigy.ballerapp.ui.features.components.fromHex
import com.softprodigy.ballerapp.ui.features.home.events.EventDetailsScreen
import com.softprodigy.ballerapp.ui.features.home.events.opportunities.EventRegistraionDetails
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.features.home.events.EventsScreen
import com.softprodigy.ballerapp.ui.features.home.events.FilterScreen
import com.softprodigy.ballerapp.ui.features.home.events.MyLeagueDetailScreen
//import com.softprodigy.ballerapp.ui.features.home.events.NewEventScreen
import com.softprodigy.ballerapp.ui.features.home.events.division.divisionTab.DivisionScreenTab
import com.softprodigy.ballerapp.ui.features.home.events.game.GameDetailsScreen
import com.softprodigy.ballerapp.ui.features.home.events.game.GameRuleScreen
import com.softprodigy.ballerapp.ui.features.home.events.new_event.NewEventScreen
import com.softprodigy.ballerapp.ui.features.home.events.opportunities.EventRegisterSuccessScreen
import com.softprodigy.ballerapp.ui.features.home.events.opportunities.EventRegistraionDetails
import com.softprodigy.ballerapp.ui.features.home.events.opportunities.OppEventDetails
import com.softprodigy.ballerapp.ui.features.home.events.team.team_tabs.EventTeamTabs
import com.softprodigy.ballerapp.ui.features.home.events.venues.openVenue.OpenVenueTopTabs
import com.softprodigy.ballerapp.ui.features.home.home_screen.HomeScreen
import com.softprodigy.ballerapp.ui.features.home.invitation.InvitationScreen
import com.softprodigy.ballerapp.ui.features.home.manage_team.MainManageTeamScreen
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.TeamsScreen
import com.softprodigy.ballerapp.ui.features.profile.ProfileEditScreen
import com.softprodigy.ballerapp.ui.features.profile.ProfileScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.AddPlayersScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.softprodigy.ballerapp.ui.features.venue.VenueListScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            val fromSplash = intent.getBooleanExtra(IntentData.FROM_SPLASH, false)
            val homeViewModel: HomeViewModel = hiltViewModel()
            val teamViewModel: TeamViewModel = hiltViewModel()
            val eventViewModel: EventViewModel = hiltViewModel()
            val state = homeViewModel.state.value
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
            val teamId = dataStoreManager.getId.collectAsState(initial = "")
            val teamName = dataStoreManager.getTeamName.collectAsState(initial = "")
            val role = dataStoreManager.getRole.collectAsState(initial = "")
            UserStorage.teamId = teamId.value
            UserStorage.teamName = teamName.value
            AppConstants.SELECTED_COLOR = fromHex(color.value.ifEmpty { "0177C1" })
            homeViewModel.setColor(AppConstants.SELECTED_COLOR)
            homeViewModel.showBottomAppBar(true)
            BallerAppMainTheme(customColor = state.color ?: Color.White) {
                val navController = rememberNavController()
                if (state.screen) {
                    Surface(
                        color = MaterialTheme.appColors.material.primary
                    ) {
                        NavControllerComposable(
                            homeViewModel,
                            teamViewModel,
                            eventViewModel,
                            navController = navController,
                            fromSplash = fromSplash
                        )
                    }
                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (state.showTopAppBar) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        topBarData = state.topBar,
                                        userRole = role.value,
                                        backClick = {
                                            if (state.topBar.topBar == TopBar.MY_EVENT) {
                                                homeViewModel.setDialog(true)
                                            } else {
                                                navController.popBackStack()
                                            }
                                        },
                                        labelClick = {
                                            homeViewModel.setDialog(true)
                                        },
                                        iconClick = {
                                            when (state.topBar.topBar) {
                                                TopBar.MY_EVENT -> {
                                                    navController.navigate(Route.NEW_EVENT)
                                                }
                                                TopBar.TEAMS -> {
                                                    navController.navigate(Route.MANAGED_TEAM_SCREEN)
                                                }
                                                TopBar.MANAGE_TEAM -> {
                                                    teamViewModel.onEvent(TeamUIEvent.OnTeamUpdate)
                                                }
                                                TopBar.PROFILE -> {
                                                    navController.navigate(Route.PROFILE_EDIT_SCREEN)
                                                }
                                                TopBar.EVENT_OPPORTUNITIES -> {
                                                    navController.navigate(Route.EVENTS_FILTER_SCREEN)
                                                }
                                                else -> {}
                                                //Add events cases for tool bar icon clicks
                                            }
                                        }
                                    )
                                }
                            }
                        },
                        content = {
                            Box(
                                modifier = Modifier
                                    .padding(it)
                                    .background(
                                        color = MaterialTheme.appColors.material.primary
                                    )
                            ) {
                                NavControllerComposable(
                                    homeViewModel,
                                    teamViewModel,
                                    eventViewModel,
                                    navController = navController,
                                    showDialog = state.showDialog,
                                    fromSplash = fromSplash
                                )
                            }
                        },
                        bottomBar = {
                            if (state.showBottomAppBar) {
                                BottomNavigationBar(
                                    state.bottomBar,
                                    navController = navController,
                                    selectionColor = state.color ?: Color.Black
                                ) {
                                    homeViewModel.setBottomNav(it)
                                    if (it == BottomNavKey.HOME) {
                                        homeViewModel.setTopAppBar(false)
                                    } else {
                                        homeViewModel.setTopAppBar(true)
                                    }
                                }
                            }
                        },
                    )
                }
                if (state.showLogout) {
                    LogoutDialog(
                        onDismiss = { homeViewModel.setLogoutDialog(false) },
                        onConfirmClick = {
                            homeViewModel.clearToken()
                            moveToLogin(this)
                        })
                }
                if (state.showSwapProfile) {
                    SwapPlayer(
                        players = state.players,
                        onDismiss = { homeViewModel.setSwapProfile(false) },
                        onConfirmClick = {
                            if (UserStorage.playerId != it) {

                            }
                        },
                        onSelectionChange = {},
                        selected = state.selectedPlayer!!,
                        showLoading = state.isLoading,
                        onCreatePlayerClick = {
                            homeViewModel.setSwapProfile(false)
                            homeViewModel.setAddProfile(true)
                        },
                        showCreatePlayerButton = role.value.equals(
                            UserType.COACH.key,
                            ignoreCase = true
                        )
                    )
                }
                if (state.showAddProfile) {
                    AddPlayer(
                        onDismiss = { homeViewModel.setAddProfile(false) },
                        onConfirmClick = {
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun NavControllerComposable(
    homeViewModel: HomeViewModel,
    teamViewModel: TeamViewModel,
    eventViewModel: EventViewModel,
    showDialog: Boolean = false,
    navController: NavHostController = rememberNavController(),
    fromSplash: Boolean = false
) {
    val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = hiltViewModel()
    var eventTitle by rememberSaveable { mutableStateOf("") }
    NavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN) {
            homeViewModel.setTopAppBar(false)
            //if (fromSplash)
            HomeScreen(name = "", onInvitationCLick = {
                navController.navigate(Route.INVITATION_SCREEN)
            },
                logoClick = {
                    homeViewModel.setLogoutDialog(true)
                },
                onTeamNameClick = { homeViewModel.setDialog(true) },
                swap_profile = {
                    homeViewModel.setSwapProfile(true)
                },
                vm = homeViewModel, gotToProfile = {
                    navController.navigate(Route.PROFILE_SCREEN)
                },
                teamVm = teamViewModel,
                OnTeamDetailsSuccess = { teamId, teamName ->
                    UserStorage.teamId = teamId
                    UserStorage.teamName = teamName
                }, showDialog = showDialog,
                dismissDialog = {
                    homeViewModel.setDialog(it)
                }, onCreateTeamClick = {
                    navController.navigate(Route.TEAM_SETUP_SCREEN) {
//                        navController.popBackStack()
                        setupTeamViewModelUpdated.onEvent(
                            TeamSetupUIEventUpdated.OnColorSelected(
                                (it?.colorCode ?: "").replace(
                                    "#", ""
                                )
                            )
                        )
                    }
                }, setupTeamViewModelUpdated = setupTeamViewModelUpdated
            )
            /* else {
                 HomeFirstTimeLoginScreen(onCreateTeamClick = {
                     navController.navigate(Route.TEAM_SETUP_SCREEN)
                 }, viewModel = homeViewModel)
             }*/
        }
        composable(route = Route.PROFILE_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.PROFILE,
                )
            )
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                moveToEditProfile = { navController.navigate(Route.PROFILE_EDIT_SCREEN) }
            )
        }
        composable(route = Route.PROFILE_EDIT_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EDIT_PROFILE,
                )
            )
            ProfileEditScreen(
                onBackClick = { navController.popBackStack() },
                onUpdateSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Route.TEAMS_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.teams_label),
                    topBar = TopBar.TEAMS,
                )
            )
            BackHandler {
                homeViewModel.setScreen(false)
            }
            TeamsScreen(
                teamViewModel,
                showDialog = showDialog,
                setupTeamViewModelUpdated = setupTeamViewModelUpdated,
                dismissDialog = { homeViewModel.setDialog(it) },
                OnTeamDetailsSuccess = { teamId, teamName ->
                    UserStorage.teamId = teamId
                    UserStorage.teamName = teamName
                },
                onCreateTeamClick = {
                    navController.navigate(Route.TEAM_SETUP_SCREEN) {
//                        navController.popBackStack()
                        setupTeamViewModelUpdated.onEvent(
                            TeamSetupUIEventUpdated.OnColorSelected(
                                (it?.colorCode ?: "").replace(
                                    "#", ""
                                )
                            )
                        )
                    }
                },
                onBackPress = {
                    navController.popBackStack()
                })
        }
        composable(route = Route.EVENTS_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.MY_EVENT,
                )
            )
            EventsScreen(eventViewModel,
                showDialog = showDialog,
                dismissDialog = { homeViewModel.setDialog(it) },
                moveToDetail = {
                    navController.navigate(Route.LEAGUE_DETAIL_SCREEN)
                },
                moveToPracticeDetail = {
                    eventTitle = it
                    navController.navigate(Route.EVENTS_DETAIL_SCREEN)
                },
                moveToGameDetail = {
                    eventTitle = it
                    navController.navigate(Route.GAME_DETAIL_SCREEN)
                },
                moveToOppDetails = {
                    eventTitle = it
                    navController.navigate(Route.OPP_DETAIL_SCREEN)
                },
                updateTopBar = {
                    homeViewModel.setTopBar(it)
                }
            )
        }
        composable(route = Route.GAME_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_DETAILS,
                    label = eventTitle
                )
            )
            GameDetailsScreen(eventViewModel, moveToGameRules = {
                navController.navigate(Route.GAME_RULES_SCREENS)
            })
        }
        composable(route = Route.OPP_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS,
                    label = eventTitle
                )
            )
            OppEventDetails(eventViewModel, moveToRegistration = {
                navController.navigate(Route.EVENT_REGISTRATION)
            })
        }
        composable(route = Route.EVENT_REGISTRATION) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.registration_form),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            EventRegistraionDetails(eventViewModel, teamViewModel) {
                navController.navigate(Route.EVENT_REGISTRATION_SUCCESS)
            }
        }

        composable(route = Route.EVENT_REGISTRATION_SUCCESS) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.registration_form),
                    topBar = TopBar.SINGLE_LABEL,
                )
            )
            EventRegisterSuccessScreen {
                navController.navigate(Route.EVENTS_SCREEN) {
                    popUpTo(Route.EVENT_REGISTRATION)
                }
            }
        }
        composable(route = Route.LEAGUE_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.back_to_school_tournament),
                    topBar = TopBar.MY_LEAGUE
                )
            )
            MyLeagueDetailScreen(
                moveToOpenDetails = {
                    eventTitle = it
                    navController.navigate(Route.GAME_DETAIL_SCREEN)

                }, moveToOpenVenues = {
                    eventTitle = it
                    navController.navigate(Route.OPEN_VENUE)

                }, moveToOpenDivisions = {
                    eventTitle = it
                    navController.navigate(Route.DIVISION_TAB)
                }, moveToOpenTeams = {
                    eventTitle = it
                    navController.navigate(Route.TEAM_TAB)
                })
        }
        composable(route = Route.EVENTS_FILTER_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.FILTER_EVENT,
                )
            )
            FilterScreen(eventViewModel) {
                navController.popBackStack()
            }
        }
        composable(route = Route.EVENTS_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS, label = eventTitle
                )
            )
            EventDetailsScreen(eventViewModel)
        }
        composable(route = Route.GAME_RULES_SCREENS) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_RULES, label = eventTitle
                )
            )
            GameRuleScreen(eventViewModel)
        }
        composable(route = Route.MANAGED_TEAM_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.manage_team),
                    topBar = TopBar.MANAGE_TEAM,
                )
            )
            MainManageTeamScreen(teamViewModel, onSuccess = {
                navController.popBackStack()
            }, onAddPlayerCLick = {
                setColorUpdate(
                    setupTeamViewModelUpdated,
                    teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                )
                navController.navigate(Route.ADD_PLAYER_SCREEN + "/${UserStorage.teamId}")
            })
        }


        composable(route = Route.ADD_PLAYER_SCREEN) {

            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.INVITE_TEAM_MEMBERS,
                )
            )
            homeViewModel.setTopAppBar(true)
            homeViewModel.showBottomAppBar(false)

            BackHandler {
                homeViewModel.setScreen(false)
                navController.popBackStack()

            }
            AddPlayersScreenUpdated(
                vm = setupTeamViewModelUpdated,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(Route.TEAMS_SCREEN) {
                        popUpTo(Route.TEAM_SETUP_SCREEN) {
                            inclusive = true
                        }
                    }
                    homeViewModel.setScreen(false)
                }, onInvitationSuccess = {
                })
        }

        composable(
            route = Route.ADD_PLAYER_SCREEN + "/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                }),
        ) {
            homeViewModel.setTopAppBar(true)
            homeViewModel.showBottomAppBar(false)

            BackHandler {
                moveBackFromAddPlayer(homeViewModel, navController)
            }
            val teamId = it.arguments?.getString("teamId")
            AddPlayersScreenUpdated(
                teamId,
                vm = setupTeamViewModelUpdated,
                onBackClick = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                },
                onNextClick = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                    //navController.navigate(TEAMS_SCREEN)
                }, onInvitationSuccess = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                })
        }

        composable(route = Route.INVITATION_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.invitation),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            BackHandler {
                homeViewModel.setTopAppBar(false)
                navController.popBackStack()
            }
            InvitationScreen()
        }
        composable(route = Route.TEAM_SETUP_SCREEN) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""

            homeViewModel.setTopAppBar(true)
            homeViewModel.showBottomAppBar(false)
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.CREATE_TEAM,
                )
            )
            BackHandler {
                setColorToOriginalOnBack(
                    navController,
                    setupTeamViewModelUpdated,
                    homeViewModel,
                    teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                )
            }
            TeamSetupScreenUpdated(
                venue = venue,
                vm = setupTeamViewModelUpdated,
                onBackClick = {
                    setColorToOriginalOnBack(
                        navController,
                        setupTeamViewModelUpdated,
                        homeViewModel,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )
                },
                onNextClick = {
                    navController.navigate(Route.ADD_PLAYER_SCREEN)
                }, onVenueClick = {
                    navController.navigate(Route.SELECT_VENUE)

                })


        }

        composable(route = Route.NEW_EVENT) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""

            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.new_event),
                    topBar = TopBar.NEW_EVENT,
                )
            )
            NewEventScreen(venue=venue,onVenueClick = {
                navController.navigate(Route.SELECT_VENUE)

            }, onEventCreationSuccess = {
                navController.popBackStack()
            })
        }


        composable(route = Route.OPEN_VENUE) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.OPEN_VENUE,
                )
            )
            OpenVenueTopTabs()
        }

        composable(route = Route.DIVISION_TAB) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.DIVISION_TAB,
                )
            )
            DivisionScreenTab()
        }

        composable(route = Route.TEAM_TAB) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.TEAM_TAB,
                )
            )
            EventTeamTabs(vm = teamViewModel)
        }
        /*composable(route = Route.MY_LEAGUE) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.back_to_school_tournament),
                    topBar = TopBar.MY_LEAGUE
                )
            )
            MyLeagueDetailScreen()
        }*/
        composable(route = Route.SELECT_VENUE) {
            VenueListScreen(onVenueClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("venue", it)
                navController.popBackStack()
            })
        }
    }
}

fun setColorToOriginalOnBack(
    navController: NavHostController,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    homeViewModel: HomeViewModel,
    colorCode: String
) {
    moveBackFromAddPlayer(homeViewModel, navController)
    if (colorCode.isNotEmpty()) {
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                colorCode.replace(
                    "#", ""
                )
            )
        )
    }
}

fun setColorUpdate(
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated, colorCode: String
) {
    if (colorCode.isNotEmpty()) {
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                colorCode.replace(
                    "#", ""
                )
            )
        )
    }
}

fun moveBackFromAddPlayer(homeViewModel: HomeViewModel, navController: NavHostController) {
    homeViewModel.setScreen(false)
    navController.popBackStack()
}

private fun moveToLogin(activity: HomeActivity) {
    val intent = Intent(activity, MainActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}
