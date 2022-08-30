package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.Route.EVENTS_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAMS_SCREEN
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.*
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
                val vm: HomeViewModel = hiltViewModel()
                val navController = rememberNavController()
                val showDialog = remember { mutableStateOf(false) }
                val dataStoreManager = DataStoreManager(this)
                val userType = remember { mutableStateOf(BottomNavKey.HOME) }
                val context= LocalContext.current
                //UserType.COACH//dataStoreManager.userInfo.collectAsState(initial = "").value

                LaunchedEffect(key1 = Unit) {
                    vm.homeChannel.collect { uiEvent ->
                        when (uiEvent) {
                            is HomeChannel.ShowToast -> {
                                Toast.makeText(
                                    context,
                                    uiEvent.message.asString(context),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                }
                Scaffold(
                    backgroundColor = MaterialTheme.appColors.material.primary,
                    topBar = {
                        if (userType.value != BottomNavKey.HOME) {
                            TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                CommonTabView(
                                    false,
                                    userType.value,
                                    stringResource(id = R.string.coach_label),
                                    painterResource(id = R.drawable.ic_settings), {
                                        showDialog.value = true
                                    }, {

                                    })
                            }
                        }
                    },
                    content = {

                        NavControllerComposable(navController = navController)
                        if (showDialog.value) {
                            SelectTeamDialog(
                                teams = vm.homeUiState.value.teams,
                                onDismiss = { showDialog.value = false },
                                onClick = {})
                        }
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
fun NavControllerComposable(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = HOME_SCREEN) {
        composable(route = HOME_SCREEN) {
            HomeScreen(name = "")
        }
        composable(route = TEAMS_SCREEN) {
            TeamsScreen(name = "")
        }
        composable(route = EVENTS_SCREEN) {
            EventsScreen(name = "")
        }
    }
}