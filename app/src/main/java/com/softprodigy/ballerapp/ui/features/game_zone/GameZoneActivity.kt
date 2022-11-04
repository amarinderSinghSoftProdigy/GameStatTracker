package com.softprodigy.ballerapp.ui.features.game_zone

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.softprodigy.ballerapp.common.IntentData
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameZoneActivity : ComponentActivity() {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val insetsController = this.window?.insetsController
            insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            val fromSplash = intent.getBooleanExtra(IntentData.FROM_SPLASH, false)
            val systemUiController: SystemUiController = rememberSystemUiController()
            systemUiController.isStatusBarVisible = false
            BallerAppMainTheme(customColor = Color.White) {
                val navController = rememberNavController()
                Scaffold(
                    backgroundColor = MaterialTheme.appColors.material.primary,
                    content = {
                        Box(
                            modifier = Modifier
                                .padding(it)
                                .background(
                                    color = MaterialTheme.appColors.material.primary
                                )
                        ) {
                            NavControllerComposable(
                                navController = navController,
                                fromSplash = fromSplash
                            )
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
    navController: NavHostController = rememberNavController(),
    fromSplash: Boolean = false
) {

    NavHost(navController, startDestination = Route.OVERVIEW_SCREEN) {
        composable(route = Route.OVERVIEW_SCREEN) {
            OverviewScreen(
                onSettingClick = {
                        //gameSetting: GameSettingsState -> gameSettingScreenNavigation(gameSetting)
                    when(it.id) {
                        0 -> { navController.popBackStack() }
                        1 -> { navController.navigate(Route.GAME_SETTINGS) }
                        2 -> { navController.navigate(Route.BOX_SCORE_SCREEN) }
                        3 -> { navController.navigate(Route.TIMEOUTS) }
                        else -> {}
                    }
                },
                onPointClick = {},
                onAddRosterClick= { navController.navigate(Route.ROASTER_SELECTION_SCREEN) }
            )
        }

        composable(route = Route.ROASTER_SELECTION_SCREEN) {
            RoasterSelectionScreen(
                onRoasterSelectionClose = { navController.popBackStack() }
            )
        }

        composable(route = Route.BOX_SCORE_SCREEN) {
            BoxScoreScreen(
                onBoxScoreClose = { navController.popBackStack() }
            )
        }

        composable(route = Route.GAME_SETTINGS) {
            GameSettingsScreen(
                onCloseSettings = { navController.popBackStack() }
            )
        }

        composable(route = Route.TIMEOUTS) {
            TimeoutsScreen(
                onTimeoutClose = { navController.popBackStack() }
            )
        }
    }
}