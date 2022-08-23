package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.EVENTS_SCREEN
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.NEW_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.OTP_VERIFICATION_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAMS_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.create_new_password.NewPasswordScreen
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.home.HomeScreen
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.otp_verification.OTPVerificationScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.BottomNavigationBar
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BallerAppTheme {
                val navController = rememberNavController()
                Surface(
                    color = MaterialTheme.appColors.material.primary
                ) {
                    // Scaffold Component
                    Scaffold(
                        topBar = {
                            TabBar {

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