package com.allballapp.android

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.saveable.rememberSaveable
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.IntentData
import com.allballapp.android.common.Route
import com.allballapp.android.common.Route.LOGIN_SCREEN
import com.allballapp.android.common.Route.OTP_VERIFICATION_SCREEN
import com.allballapp.android.common.Route.PROFILE_SETUP_SCREEN
import com.allballapp.android.common.Route.SELECT_PROFILE
import com.allballapp.android.common.Route.SELECT_VENUE
import com.allballapp.android.common.Route.SPLASH_SCREEN
import com.allballapp.android.common.Route.WELCOME_SCREEN
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.ui.features.components.CommonTabView
import com.allballapp.android.ui.features.components.TabBar
import com.allballapp.android.ui.features.components.TopBar
import com.allballapp.android.ui.features.components.TopBarData
import com.allballapp.android.ui.features.confirm_phone.OtpScreen
import com.allballapp.android.ui.features.home.HomeActivity
import com.allballapp.android.ui.features.home.webview.CommonWebView
import com.allballapp.android.ui.features.login.LoginScreen
import com.allballapp.android.ui.features.select_profile.SelectProfileScreen
import com.allballapp.android.ui.features.sign_up.ProfileSetUpScreen
import com.allballapp.android.ui.features.sign_up.SignUpUIEvent
import com.allballapp.android.ui.features.sign_up.SignUpViewModel
import com.allballapp.android.ui.features.splash.SplashScreen
import com.allballapp.android.ui.features.user_type.team_setup.updated.InviteObject
import com.allballapp.android.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.allballapp.android.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.allballapp.android.ui.features.venue.VenueListScreen
import com.allballapp.android.ui.features.welcome.WelcomeScreen
import com.allballapp.android.ui.theme.BallerAppTheme
import com.allballapp.android.ui.theme.appColors
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
    val teamId = dataStoreManager.getId.collectAsState(initial = "")
    UserStorage.teamId = teamId.value
    val scope = rememberCoroutineScope()
    val color = dataStoreManager.getWalkThrough.collectAsState(initial = "")
    var url by rememberSaveable {
        mutableStateOf("")
    }
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

//            OtpScreenView(
//                signUpViewModel = signUpViewModel,
//                navController = navController,
//                activity = activity
//            )
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
                }, moveToTermsAndConditions = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)

                },
                moveToPrivacyAndPolicy = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                }
            )
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
            OtpScreenView(
                signUpViewModel = signUpViewModel,
                navController = navController,
                activity = activity
            )
        }
        composable(route = Route.WEB_VIEW) {
            CommonWebView(url)
        }
    }
}

@Composable
fun OtpScreenView(
    signUpViewModel : SignUpViewModel,
    navController : NavController,
    activity : MainActivity,
){
    OtpScreen(
        viewModel = signUpViewModel,
        onSuccess = { profilesCount, profileIdIFSingle ->
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
        }, onAuthorize = {
            navController.popBackStack()
        }
    )
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
