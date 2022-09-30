package com.softprodigy.ballerapp.ui.features.game_zone

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.common.IntentData
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameZoneActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val fromSplash = intent.getBooleanExtra(IntentData.FROM_SPLASH, false)
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
    val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = hiltViewModel()
    NavHost(navController, startDestination = Route.GAME_ZONE_SCREEN) {
        composable(route = Route.GAME_ZONE_SCREEN) {
            OverviewScreen()
        }
    }
}