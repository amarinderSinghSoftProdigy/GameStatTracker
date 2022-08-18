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
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.HOME_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.NEW_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.OTP_VERIFICATION_SCREEN
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.ui.features.create_new_password.NewPasswordScreen
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.home.HomeScreen
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.otp_verification.OTPVerificationScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
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
//            WelcomeScreen(onCreateAccountCLick = {
//                navController.navigate(SIGN_UP_SCREEN)
//            }, onSkipCLick = {
//                navController.navigate(HOME_SCREEN + "/${null}") {
//                    popUpTo(WELCOME_SCREEN) {
//                        inclusive = true
//                    }
//                }
//            },
//                onLoginClick = {
//                    navController.navigate(LOGIN_SCREEN)
//                },
//                onFacebookClick = {
//
//                },
//                onGoogleLogin = { userResponse ->
//                    navController.navigate(HOME_SCREEN + "/${userResponse.userInfo.firstName}") {
//                        popUpTo(WELCOME_SCREEN) {
//                            inclusive = true
//                        }
//                    }
//                })

            WelcomeScreen {
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }
        composable(route = LOGIN_SCREEN) {
            val context = LocalContext.current
            LoginScreen(
                onLoginSuccess = { loginResponse ->

                    if (loginResponse.userInfo.isEmailVerified) {
                        navController.navigate(HOME_SCREEN + "/${loginResponse.userInfo.firstName}") {
                            popUpTo(WELCOME_SCREEN) {
                                inclusive = true
                            }
                        }
                    } else {
                        val isResetIntent = "false"
                        navController.navigate(
                            OTP_VERIFICATION_SCREEN + "/${loginResponse.verifyToken}"
                                    + "/${loginResponse.userInfo.email}"
                                    + "/${isResetIntent}"

                        )
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(SIGN_UP_SCREEN) {
                        popUpTo(WELCOME_SCREEN)
                    }
                },
                onForgetPasswordClick = { navController.navigate(FORGOT_PASSWORD_SCREEN) },
                onFacebookClick = {})
        }
        composable(route = SIGN_UP_SCREEN) {
            val context = LocalContext.current
            SignUpScreen(onSuccessfulSignUp = { signUpResponse ->
                val isResetIntent = "false"
                navController.navigate(
                    OTP_VERIFICATION_SCREEN + "/${signUpResponse.verifyToken}"
                            + "/${signUpResponse.userInfo.email}"
                            + "/${isResetIntent}"

                )
            },
                onGoogleClick = { name ->
                    navController.navigate(HOME_SCREEN + "/${name}") {
                        popUpTo(WELCOME_SCREEN) {
                            inclusive = true
                        }
                    }
                },
                onFacebookClick = {},
                onLoginClick = {
                    navController.navigate(LOGIN_SCREEN) {
                        popUpTo(WELCOME_SCREEN)
                    }
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
    }
}