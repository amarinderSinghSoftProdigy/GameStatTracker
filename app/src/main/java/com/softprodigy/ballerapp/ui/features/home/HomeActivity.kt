package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.Route.EVENTS_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAMS_SCREEN
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.components.CommonTabView
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.components.UserType
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
                Surface(
                    color = MaterialTheme.appColors.material.primary
                ) {
                    val dataStoreManager = DataStoreManager(this)
                    val userType =
                        UserType.COACH//dataStoreManager.userInfo.collectAsState(initial = "").value
                    // Scaffold Component
                    Scaffold(
                        topBar = {
                            TabBar(color = MaterialTheme.appColors.material.primaryVariant){
                                CommonTabView(
                                    false,
                                    UserType.COACH,
                                    stringResource(id = R.string.coach_label),
                                    painterResource(id = R.drawable.ic_settings)
                                ) {

                                }
                            }
                        },
                        content = {
                            NavControllerComposable(navController = navController)
                        },
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun NavControllerComposable(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = EVENTS_SCREEN) {
        composable(route = EVENTS_SCREEN) {
            HomeScreen(name = "")
        }
        composable(route = TEAMS_SCREEN) {
            HomeScreen(name = "")
        }
    }
}