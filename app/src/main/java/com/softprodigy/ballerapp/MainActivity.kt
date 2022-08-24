package com.softprodigy.ballerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.home.HomeScreen
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
    NavHost(navController, startDestination = SPLASH_SCREEN) {

        composable(route = SPLASH_SCREEN) {
            SplashScreen {
                navController.popBackStack()
                navController.navigate(WELCOME_SCREEN)
            }
        }

        composable(route = WELCOME_SCREEN) {
            WelcomeScreen {
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }
        composable(route = LOGIN_SCREEN) {
            val context = LocalContext.current
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(SELECT_USER_TYPE)
                },

                onForgetPasswordClick = { },
            )
        }
        composable(route = PROFILE_SETUP_SCREEN) {
            val context = LocalContext.current

            ProfileSetUpScreen(onNext = { navController.navigate(TEAM_SETUP_SCREEN) }, onBack = {
                navController.popBackStack()
            })
        }
        composable(route = "$HOME_SCREEN/{name}") {
            val name = it.arguments?.getString("name")
            HomeScreen(name = name)
        }
        composable(route = SELECT_USER_TYPE) {
            UserTypeScreen(onNextClick = { userType ->
                Timber.i("onNextClick-- $userType")
                when (userType) {
                    AppConstants.USER_TYPE_COACH -> {
                        navController.navigate(PROFILE_SETUP_SCREEN)
                    }
                    AppConstants.USER_TYPE_PLAYER -> {

                    }
                    AppConstants.USER_TYPE_REFEREE -> {

                    }

                }
            })
        }
        composable(route = TEAM_SETUP_SCREEN) {
            TeamSetupScreen(onBackClick = { navController.popBackStack() }, onNextClick = {
                navController.navigate(ADD_PLAYER_SCREEN)
            })
        }
        composable(route = ADD_PLAYER_SCREEN) {
            AddPlayersScreen(onBackClick = { navController.popBackStack() }, onNextClick = {
                val intent = Intent(activity, HomeActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            })
        }
    }
}