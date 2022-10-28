package com.softprodigy.ballerapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.IntentData
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.OTP_VERIFICATION_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_PROFILE
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SELECT_VENUE
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.confirm_phone.OtpScreen
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.select_profile.SelectProfileScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.*
import com.softprodigy.ballerapp.ui.features.venue.VenueListScreen
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryOrange
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var setupTeamViewModelUpdated: SetupTeamViewModelUpdated? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
//        startActivity(Intent(this, CometChatUI::class.java))

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            setupTeamViewModelUpdated = hiltViewModel()
            val state = mainViewModel.state.value
            BallerAppTheme {
                val navController = rememberNavController()
                if (!state.showAppBar) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.appColors.material.primary
                    ) {

                            NavControllerComposable(
                                this,
                                mainViewModel,
                                navController,
                                setupTeamViewModelUpdated ?: hiltViewModel()
                            )
                        }

                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (state.showAppBar) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        topBarData = state.topBar,
                                        userRole = "",
                                        backClick = {
                                            mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar = false))
                                            navController.popBackStack()
                                        },
                                        labelClick = {
                                        },
                                        iconClick = {
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
                                        this@MainActivity,
                                        mainViewModel,
                                        navController,
                                        setupTeamViewModelUpdated ?: hiltViewModel()
                                    )
                                }

                        },
                    )
                }
            }
        }
    }

    // on below line we are calling on activity result method.
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == AppConstants.REQUEST_CONTACT_CODE && data != null) {
            val contactData: Uri? = data.data
            val cursor: Cursor = managedQuery(contactData, null, null, null, null)
            cursor.moveToFirst()
            val number: String =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val name: String =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            setupTeamViewModelUpdated?.onEvent(
                TeamSetupUIEventUpdated.OnContactAdded(
                    InviteObject(
                        name,
                        number.replace(" ", "")
                    )
                )
            )

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        startActivityForResult(
            intent,
            AppConstants.REQUEST_CONTACT_CODE,
            null
        )
    }
}

@Composable
fun NavControllerComposable(
    activity: MainActivity,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated
) {
    val signUpViewModel: SignUpViewModel = viewModel()
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(activity)
    val userToken = dataStoreManager.userToken.collectAsState(initial = "")
    UserStorage.token = userToken.value
    val scope = rememberCoroutineScope()
    val color = dataStoreManager.getWalkThrough.collectAsState(initial = "")
    NavHost(navController, startDestination = SPLASH_SCREEN) {
        composable(route = SPLASH_SCREEN) {
            SplashScreen {
                scope.launch {
                    if (userToken.value.isNotEmpty()) {
                        moveToHome(activity, true)
                    } else if (color.value.isNotEmpty()) {
                        navController.popBackStack()
                        navController.navigate(LOGIN_SCREEN)
                    } else {
                        navController.popBackStack()
                        navController.navigate(WELCOME_SCREEN)
                    }
                }
            }
        }


        composable(route = WELCOME_SCREEN) {
            WelcomeScreen {
                scope.launch {
                    dataStoreManager.skipWalkthrough(AppConstants.SKIP)
                }
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }

        composable(route = LOGIN_SCREEN) {
            LoginScreen(
                signUpViewModel = signUpViewModel,
                onSuccess = {
                    navController.navigate(OTP_VERIFICATION_SCREEN)
                }
            )
        }

        composable(route = PROFILE_SETUP_SCREEN) {
            ProfileSetUpScreen(
                signUpViewModel = signUpViewModel,
                onNext = {
                    /*if (UserStorage.role.equals(UserType.COACH.key, ignoreCase = true)) {
                        navController.navigate(TEAM_SETUP_SCREEN) {
                            navController.popBackStack()
                        }
                    } else {*/
                    moveToHome(activity)
                    //}
                },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable(route = SELECT_USER_TYPE) {
            mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar = false))

            BackHandler {}

            UserTypeScreen(
                signUpvm = signUpViewModel,
                onNextClick = { userType ->
                    UserStorage.role = userType

                    if (userType.equals(UserType.COACH.key, ignoreCase = true)
                        || userType.equals(
                            UserType.PLAYER.key,
                            ignoreCase = true
                        ) || userType.equals(UserType.REFEREE.key, ignoreCase = true)
                    ) {
                        navController.navigate(PROFILE_SETUP_SCREEN)
                    } else {
                        Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        composable(route = TEAM_SETUP_SCREEN) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""

            mainViewModel.onEvent(
                MainEvent.OnTopBarChanges(
                    showAppBar = true, TopBarData(
                        topBar = TopBar.CREATE_TEAM,
                    )
                )
            )
            mainViewModel.onEvent(MainEvent.OnColorChanges(ColorPrimaryOrange))

            BackHandler {
                mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar = false))
                navController.popBackStack()
            }

            TeamSetupScreenUpdated(
                venue = venue,
                vm = setupTeamViewModelUpdated,
                onBackClick = {
                    mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar = false))
                    navController.popBackStack()
                },
                onNextClick = {
                    navController.navigate(ADD_PLAYER_SCREEN)
                }, onVenueClick = {
                    navController.navigate(SELECT_VENUE)

                })
        }

        composable(route = ADD_PLAYER_SCREEN) {
            mainViewModel.onEvent(
                MainEvent.OnTopBarChanges(
                    showAppBar = true, TopBarData(
                        topBar = TopBar.INVITE_TEAM_MEMBERS,
                    )
                )
            )
            mainViewModel.onEvent(MainEvent.OnColorChanges(AppConstants.SELECTED_COLOR))
            AddPlayersScreenUpdated(
                vm = setupTeamViewModelUpdated,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    //moveToHome(activity)
                }, onInvitationSuccess = {

                })
        }
        composable(route = SELECT_PROFILE) {
            mainViewModel.onEvent(MainEvent.OnColorChanges(AppConstants.SELECTED_COLOR))
            SelectProfileScreen(
                vm = signUpViewModel,
                onNextClick = { moveToHome(activity) })
        }

        composable(route = SELECT_VENUE) {
            VenueListScreen(onVenueClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("venue", it)
                navController.popBackStack()
            })
        }

        composable(route = OTP_VERIFICATION_SCREEN) {
            OtpScreen(
                viewModel = signUpViewModel,
                onSuccess = { profilesCount, profileIdIFSingle->
                    when (profilesCount) {
                        0 -> {
                            navController.popBackStack()
                            navController.navigate(PROFILE_SETUP_SCREEN)
                        }
                        1 -> {
                            profileIdIFSingle?.let {
                                signUpViewModel.onEvent(SignUpUIEvent.OnSwapUpdate(it))
                            }
                        }
                        else -> {
                            navController.popBackStack()
                            navController.navigate(SELECT_PROFILE)
                        }
                    }

                },
                onTokenSelectionSuccess = {
                    moveToHome(activity)
                }
            )
        }
    }
}


private fun moveToHome(activity: MainActivity, fromSplash: Boolean = false) {
    val intent = Intent(activity, HomeActivity::class.java)
    if (fromSplash) {
        intent.putExtra(IntentData.FROM_SPLASH, fromSplash)
    }
    activity.startActivity(intent)
    activity.finish()
}

/*
private fun checkRole(
    check: Boolean,
    navController: NavController,
    activity: MainActivity,
    fromSplash: Boolean = false
) {
    if (check) {
        navController.navigate(SELECT_USER_TYPE) {
            navController.popBackStack()
        }
    } else {
        moveToHome(activity, fromSplash)
    }
}
*/
