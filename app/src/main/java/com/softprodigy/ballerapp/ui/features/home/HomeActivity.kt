package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.Route.EVENTS_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAMS_SCREEN
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.components.CommonTabView
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.home.teams.TeamsScreen
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
            BallerAppMainTheme {
                val navController = rememberNavController()
                val showDialog = remember { mutableStateOf(false) }
                val userType = remember { mutableStateOf(BottomNavKey.HOME) }

                Scaffold(
                    backgroundColor = MaterialTheme.appColors.material.primary,
                    topBar = {
                        if (userType.value != BottomNavKey.HOME) {
                            TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                CommonTabView(
                                    canMoveBack = false,
                                    user = userType.value,
                                    label = stringResource(id = R.string.coach_label),
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
                            navController = navController,
                            showDialog = showDialog.value,
                            dismissDialog = {
                                showDialog.value = it
                            })
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController) {
                            userType.value = it
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun NavControllerComposable(
    showDialog: Boolean = false,
    dismissDialog: (Boolean) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = HOME_SCREEN) {
        composable(route = HOME_SCREEN) {
            HomeScreen(name = "")
        }
        composable(route = TEAMS_SCREEN) {
            TeamsScreen(name = "", showDialog = showDialog, dismissDialog = dismissDialog)
        }
        composable(route = EVENTS_SCREEN) {
            EventsScreen(name = "")
        }
    }
}