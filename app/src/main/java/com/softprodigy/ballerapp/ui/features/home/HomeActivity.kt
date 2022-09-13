package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.softprodigy.ballerapp.ui.features.home.manage_team.MainManageTeamScreen
import com.softprodigy.ballerapp.ui.features.home.teams.TeamsScreen
import com.softprodigy.ballerapp.ui.features.profile.ProfileEditScreen
import com.softprodigy.ballerapp.ui.features.profile.ProfileScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.AddPlayersScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
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
            val state = homeViewModel.state.value
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
            AppConstants.SELECTED_COLOR = fromHex(color.value.ifEmpty { "0177C1" })
            homeViewModel.setColor(AppConstants.SELECTED_COLOR)
            BallerAppMainTheme(customColor = state.color ?: Color.White) {
                val navController = rememberNavController()
                val userType = remember { mutableStateOf(BottomNavKey.HOME) }
                if (state.screen) {
                    Surface {
                        NavControllerComposable(
                            homeViewModel,
                            navController = navController,
                            fromSplash = fromSplash
                        )
                    }
                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (userType.value != BottomNavKey.HOME) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        state.topBar,
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
                                                TopBar.MANAGE_TEAM->{
                                                    //TODO add the login to save the team deatils
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
                            Box(modifier = Modifier.padding(it)) {
                                NavControllerComposable(
                                    homeViewModel,
                                    navController = navController,
                                    showDialog = state.showDialog,
                                    fromSplash = fromSplash
                                )
                            }
                        },
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController,
                                selectionColor = state.color ?: Color.Black
                            ) {
                                userType.value = it
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
    showDialog: Boolean = false,
    navController: NavHostController = rememberNavController(),
    fromSplash: Boolean = false
) {
    val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = hiltViewModel()
    NavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN) {
            if (fromSplash)
                HomeScreen(name = "") { homeViewModel.setLogoutDialog(true) }
            else {
                HomeFirstTimeLoginScreen()
            }
        }
        composable(route = Route.PROFILE_SCREEN) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                moveToEditProfile={   navController.navigate(Route.PROFILE_EDIT_SCREEN)}
            )
        }
        composable(route = Route.PROFILE_EDIT_SCREEN) {
            ProfileEditScreen(
                onBackClick = { navController.popBackStack() },
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
                name = "",
                showDialog = showDialog,
                setupTeamViewModelUpdated = setupTeamViewModelUpdated,
                dismissDialog = { homeViewModel.setDialog(it) },
                OnTeamDetailsSuccess = { teamId ->
                    UserStorage.teamId = teamId
//                    navController.navigate(Route.ADD_PLAYER_SCREEN + "/${teamId}")
                },
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
            MainManageTeamScreen(onAddPlayerCLick = {
                navController.navigate(Route.ADD_PLAYER_SCREEN + "/${UserStorage.teamId}")
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
                homeViewModel.setScreen(false)
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
