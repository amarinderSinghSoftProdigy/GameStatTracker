package com.softprodigy.ballerapp

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
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.NEW_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.OTP_VERIFICATION_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.ui.features.create_new_password.NewPasswordScreen
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.home.HomeScreen
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.otp_verification.OTPVerificationScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
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
                    color = MaterialTheme.colors.background
                ) {
                    CompositionLocalProvider(
                        LocalFacebookCallbackManager provides callbackManager
                    ) {
                        NavControllerComposable()

                    }
                }
            }
        }
    }
}

@Composable
fun NavControllerComposable() {
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
                onLoginSuccess = { loginResponse ->
                    navController.navigate(SELECT_USER_TYPE)
                    /*if (loginResponse.userInfo.isEmailVerified) {
                        navController.navigate(HOME_SCREEN + "/${loginResponse?.userInfo?.firstName}") {
                            popUpTo(WELCOME_SCREEN) {
                                inclusive = true
                            }
                        }
                    } else {
                        val isResetIntent = "false"
                        navController.navigate(
                            OTP_VERIFICATION_SCREEN + "/${loginResponse?.verifyToken}"
                                    + "/${loginResponse?.userInfo?.email}"
                                    + "/${isResetIntent}"

                        )
                    }*/
                },
                onCreateAccountClick = {
                },
                onForgetPasswordClick = { navController.navigate(FORGOT_PASSWORD_SCREEN) },
                onFacebookClick = {})
        }
        composable(route = PROFILE_SETUP_SCREEN) {
            val context = LocalContext.current

            ProfileSetUpScreen(onNext = { navController.navigate(TEAM_SETUP_SCREEN) }, onBack = {
                navController.popBackStack()
            })
        }
        composable(route = FORGOT_PASSWORD_SCREEN) {
            val context = LocalContext.current
            ForgotPasswordScreen(
                onOtpClick = { forgotPasswordResponse ->
                    Timber.d("NavControllerComposable: " + forgotPasswordResponse.userInfo.email)
                    val isResetIntent = "true"
                    navController.navigate(
                        OTP_VERIFICATION_SCREEN + "/${forgotPasswordResponse.verifyToken}"
                                + "/${forgotPasswordResponse.userInfo.email}"
                                + "/${isResetIntent}"

                    )
                },
                onSuccess = { forgotPasswordResponse ->
                    Toast.makeText(context, forgotPasswordResponse.message, Toast.LENGTH_LONG)
                        .show()
                },

                onLoginClick = { navController.navigate(LOGIN_SCREEN) })

        }

        composable(route = "$OTP_VERIFICATION_SCREEN/{token}/{email}/{isResetIntent}") {
            val token = it.arguments?.getString("token")
            val email = it.arguments?.getString("email")
            val isResetIntent = it.arguments?.getString("isResetIntent") ?: "false"

            val context = LocalContext.current
            OTPVerificationScreen(
                token = token!!,
                email = email!!,
                onSuccess = { verifyOtpResponse ->
                    Toast.makeText(context, verifyOtpResponse.message, Toast.LENGTH_LONG)
                        .show()
                    if (isResetIntent == "true") {
                        navController.navigate(Route.NEW_PASSWORD_SCREEN + "/$token")
                    } else {
                        navController.navigate(HOME_SCREEN + "/${verifyOtpResponse.userInfo.firstName}") {
                            popUpTo(WELCOME_SCREEN) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

        composable(route = "$NEW_PASSWORD_SCREEN/{token}") {
            val token = it.arguments?.getString("token")
            val context = LocalContext.current
            NewPasswordScreen(
                token = token!!,
                OnLoginScreen = {
                    navController.navigate(LOGIN_SCREEN) {
                        popUpTo(WELCOME_SCREEN)
                    }
                },
                OnSuccess = { resetPasswordResponse ->
                    Toast.makeText(context, resetPasswordResponse.message, Toast.LENGTH_LONG)
                        .show()
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
            AddPlayersScreen(onBackClick = { navController.popBackStack() }, onNextClick = {})
        }
    }
}