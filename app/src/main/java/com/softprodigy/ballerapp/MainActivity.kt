package com.softprodigy.ballerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.data.request.SignUpType
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.SetupTeamViewModel
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint

val LocalFacebookCallbackManager =
    staticCompositionLocalOf<CallbackManager> { error("No CallbackManager provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var callbackManager = CallbackManager.Factory.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BallerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.appColors.material.primary
                ) {
                    CompositionLocalProvider(
                        LocalFacebookCallbackManager provides callbackManager
                    ) {
                        NavControllerComposable(this)
                    }
                }
            }
        }
    }
}

@Composable
fun NavControllerComposable(activity: MainActivity) {
    val navController = rememberNavController()
    val setupTeamViewModel: SetupTeamViewModel = viewModel()
    val context = LocalContext.current

    NavHost(navController, startDestination = SPLASH_SCREEN) {

        composable(route = SPLASH_SCREEN) {
            SplashScreen {
                navController.popBackStack()
                navController.navigate(WELCOME_SCREEN)
            }
        }

        composable(route = SIGN_UP_SCREEN) {
            SignUpScreen(onLoginScreen = {
                navController.navigate(LOGIN_SCREEN)
            }, onSignUpSuccess = {
                val json = Uri.encode(Gson().toJson(it))
                navController.navigate("${SELECT_USER_TYPE}/${json}")
            })
        }

        composable(route = WELCOME_SCREEN) {
            WelcomeScreen {
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }
        composable(route = LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    UserStorage.token = it?.token.toString()
                    navController.navigate(SELECT_USER_TYPE){
                        navController.popBackStack()
                    }
                },
                onRegister = { navController.navigate(SIGN_UP_SCREEN) },
                onForgetPasswordClick = {
                    navController.navigate(FORGOT_PASSWORD_SCREEN)
                },
            )
        }

        composable(route = FORGOT_PASSWORD_SCREEN) {

            ForgotPasswordScreen(onNextClick = {
                navController.navigate(LOGIN_SCREEN) {
                    navController.popBackStack()

                }
            })

        }

        composable(
            route = "${PROFILE_SETUP_SCREEN}/{signUp}",
            arguments = listOf(navArgument("signUp") { type = SignUpType() })
        ) {
            val signUpData = it.arguments?.getParcelable<SignUpData>("signUp")

            ProfileSetUpScreen(
                signUpData,
                onNext = {
                    navController.navigate(TEAM_SETUP_SCREEN) {
                        navController.popBackStack()

                    }
                },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable(
            route = "${SELECT_USER_TYPE}/{signUp}",
            arguments = listOf(navArgument("signUp") { type = SignUpType() })
        ) {

            val mSignUpData = it.arguments?.getParcelable<SignUpData>("signUp")

            BackHandler(true) {
            }

            UserTypeScreen(mSignUpData, onNextClick = { userType, signUpData ->
                val json = Uri.encode(Gson().toJson(signUpData))
                navController.navigate("${PROFILE_SETUP_SCREEN}/${json}")
            })
        }

        composable(route = TEAM_SETUP_SCREEN) {
            TeamSetupScreen(
                vm = setupTeamViewModel,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(ADD_PLAYER_SCREEN)
                })
        }
        composable(route = ADD_PLAYER_SCREEN) {
            AddPlayersScreen(
                vm = setupTeamViewModel,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    val intent = Intent(activity, HomeActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                })
        }
    }
}