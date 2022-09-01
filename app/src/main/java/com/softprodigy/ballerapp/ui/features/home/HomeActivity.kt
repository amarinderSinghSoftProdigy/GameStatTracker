package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.components.CommonTabView
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.components.fromHex
import com.softprodigy.ballerapp.ui.features.home.teams.TeamsScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.SetupTeamViewModel
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
            val homeViewModel: HomeViewModel = hiltViewModel()
            val state = homeViewModel.state.value
            val dataStoreManager = DataStoreManager(LocalContext.current)
            val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
            AppConstants.SELECTED_COLOR = fromHex(color.value.ifEmpty { "0177C1" })
            homeViewModel.setColor(AppConstants.SELECTED_COLOR)
            BallerAppMainTheme(customColor = state.color ?: Color.White) {
                val navController = rememberNavController()
                val showDialog = remember { mutableStateOf(false) }
                val userType = remember { mutableStateOf(BottomNavKey.HOME) }

                if (state.screen) {
                    Surface {
                        NavControllerComposable(
                            homeViewModel,
                            navController = navController,
                            showDialog = showDialog.value,
                            dismissDialog = {
                                showDialog.value = it
                            })
                    }
                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (userType.value != BottomNavKey.HOME) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        canMoveBack = false,
                                        user = userType.value,
                                        label = when (userType.value) {
                                            BottomNavKey.TEAMS -> {
                                                stringResource(id = R.string.teams_label)
                                            }
                                            BottomNavKey.EVENTS -> {
                                                stringResource(id = R.string.events_label)
                                            }
                                            else -> {
                                                ""
                                            }
                                        },
                                        icon = painterResource(id = R.drawable.ic_settings),
                                        onLabelClick = {
                                            showDialog.value = true
                                        }, iconClick = {
                                        })
                                }
                            }
                        },
                        content = {
                            NavControllerComposable(
                                homeViewModel,
                                navController = navController,
                                showDialog = showDialog.value,
                                dismissDialog = {
                                    showDialog.value = it
                                })
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
            }
        }
    }
}

@Composable
fun NavControllerComposable(
    homeViewModel: HomeViewModel,
    showDialog: Boolean = false,
    dismissDialog: (Boolean) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val setupTeamViewModel: SetupTeamViewModel = hiltViewModel()
    NavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN) {
            HomeScreen(name = "")
        }
        composable(route = Route.TEAMS_SCREEN) {
            BackHandler {
                homeViewModel.setScreen(false)
            }
            TeamsScreen(
                name = "",
                showDialog = showDialog,
                dismissDialog = dismissDialog,
                setupTeamViewModel = setupTeamViewModel
            ) {
                navController.navigate(Route.ADD_PLAYER_SCREEN + "/${it.Id}")
            }
        }
        composable(route = Route.EVENTS_SCREEN) {
            EventsScreen(name = "")
        }

        composable(
            route = Route.ADD_PLAYER_SCREEN + "/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                }),
        ) {
            homeViewModel.setScreen(true)
            val teamId = it.arguments?.getString("teamId")
            AddPlayersScreen(
                teamId,
                vm = setupTeamViewModel,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    homeViewModel.setScreen(false)
                    navController.popBackStack()
                    //navController.navigate(TEAMS_SCREEN)
                })
        }
    }
}