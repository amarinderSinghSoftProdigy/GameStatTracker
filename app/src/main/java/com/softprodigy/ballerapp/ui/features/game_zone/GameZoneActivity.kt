package com.softprodigy.ballerapp.ui.features.game_zone

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