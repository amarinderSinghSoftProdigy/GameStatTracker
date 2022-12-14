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
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.components.CommonTabView
import com.softprodigy.ballerapp.ui.features.components.LogoutDialog
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.components.TopBar
import com.softprodigy.ballerapp.ui.features.components.TopBarData
import com.softprodigy.ballerapp.ui.features.components.fromHex
import com.softprodigy.ballerapp.ui.features.home.home_screen.HomeScreen
import com.softprodigy.ballerapp.ui.features.home.invitation.InvitationScreen
import com.softprodigy.ballerapp.ui.features.home.manage_team.MainManageTeamScreen
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.home.teams.TeamsScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.AddPlayersScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
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
            val state = homeViewModel.state.value
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
            val teamId = dataStoreManager.getId.collectAsState(initial = "")
            val role = dataStoreManager.getRole.collectAsState(initial = "")
            UserStorage.teamId = teamId.value
            AppConstants.SELECTED_COLOR = fromHex(color.value.ifEmpty { "0177C1" })
            homeViewModel.setColor(AppConstants.SELECTED_COLOR)

            BallerAppMainTheme(customColor = state.color ?: Color.White) {
                val navController = rememberNavController()
                if (state.screen) {
                    Surface(
                        color = MaterialTheme.appColors.material.primary
                    ) {
                        NavControllerComposable(
                            homeViewModel,
                            teamViewModel,
                            navController = navController,
                            fromSplash = fromSplash
                        )
                    }
                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (state.appBar) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        topBarData = state.topBar,
                                        userRole = role.value,
                                        backClick = {
                                            navController.popBackStack()
                                        },
                                        labelClick = {
                                            homeViewModel.setDialog(true)
                                        },
                                        iconClick = {
                                            when (state.topBar.topBar) {
                                                TopBar.MY_EVENT -> {
                                                    navController.navigate(Route.MANAGED_TEAM_SCREEN)
                                                }
                                                TopBar.TEAMS -> {
                                                    navController.navigate(Route.MANAGED_TEAM_SCREEN)
                                                }
                                                TopBar.MANAGE_TEAM -> {
                                                    teamViewModel.onEvent(TeamUIEvent.OnTeamUpdate)
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
                                    navController = navController,
                                    showDialog = state.showDialog,
                                    fromSplash = fromSplash
                                )
                            }
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                state.bottomBar,
                                navController = navController,
                                selectionColor = state.color ?: Color.Black
                            ) {
                                homeViewModel.setBottomNav(it)
                                if (it == BottomNavKey.HOME) {
                                    homeViewModel.setAppBar(false)
                                } else {
                                    homeViewModel.setAppBar(true)
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
            }
        }
    }
}

@Composable
fun NavControllerComposable(
    homeViewModel: HomeViewModel,
    teamViewModel: TeamViewModel,
    showDialog: Boolean = false,
    navController: NavHostController = rememberNavController(),
    fromSplash: Boolean = false
) {
    val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = hiltViewModel()
    NavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN) {
            homeViewModel.setAppBar(false)
            //if (fromSplash)
            HomeScreen(name = "", onInvitationCLick = {
                navController.navigate(Route.INVITATION_SCREEN)
            }, logoClick = {
                homeViewModel.setLogoutDialog(true)
            }, vm = homeViewModel)
            /* else {
                 HomeFirstTimeLoginScreen(onCreateTeamClick = {
                     navController.navigate(Route.TEAM_SETUP_SCREEN)
                 }, viewModel = homeViewModel)
             }*/
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
                OnTeamDetailsSuccess = { teamId ->
                    UserStorage.teamId = teamId
                },
                onCreateTeamClick = {
                    navController.navigate(Route.TEAM_SETUP_SCREEN) {
                        navController.popBackStack()
                        setupTeamViewModelUpdated.onEvent(
                            TeamSetupUIEventUpdated.OnColorSelected(
                                (it?.colorCode ?: "").replace(
                                    "#",
                                    ""
                                )
                            )
                        )
                    }
                },
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Route.EVENTS_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.MY_EVENT,
                )
            )
            EventsScreen(name = "") { }
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
            homeViewModel.setScreen(true)
            BackHandler {
                homeViewModel.setScreen(false)
                navController.popBackStack()

            }
            AddPlayersScreenUpdated(
                vm = setupTeamViewModelUpdated,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(Route.TEAMS_SCREEN) {
                        popUpTo(Route.HOME_SCREEN)
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
            homeViewModel.setScreen(true)
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
                homeViewModel.setAppBar(false)
                navController.popBackStack()
            }
            InvitationScreen()

        }
        composable(route = Route.TEAM_SETUP_SCREEN) {
            homeViewModel.setScreen(true)
            BackHandler {
                setColorToOriginalOnBack(
                    navController,
                    setupTeamViewModelUpdated,
                    homeViewModel,
                    teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                )
            }
            TeamSetupScreenUpdated(
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
                    "#",
                    ""
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
                    "#",
                    ""
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
