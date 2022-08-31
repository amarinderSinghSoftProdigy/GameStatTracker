package com.softprodigy.ballerapp.common

import androidx.compose.ui.graphics.Color

object AppConstants {
    const val ANDROID = "android"
    const val GOOGLE = "google"
    const val KEY_PASSWORD = "password"
    const val KEY_USERS = "users"
    const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
    const val USER_TYPE_PLAYER = "Player"
    const val USER_TYPE_REFEREE = "Referee"
    const val USER_TYPE_COACH = "Coach"
    const val USER_TYPE_PARENT = "Parent"
    const val USER_TYPE_GAME_STAFF = "Game Staff"
    const val USER_TYPE_PROGRAM_STAFF = "Program Staff"
    const val USER_TYPE_FAN = "Fan"
    const val PUBLIC_PROFILE="public_profile"
    const val EMAIL="email"
    var SELECTED_COLOR: Color = Color(0xFF0177C1)
}

object Route {
    const val WELCOME_SCREEN = "welcomeScreen"
    const val LOGIN_SCREEN = "loginScreen"
    const val SIGN_UP_SCREEN = "signupScreen"
    const val FORGOT_PASSWORD_SCREEN = "forgotPasswordScreen"
    const val OTP_VERIFICATION_SCREEN = "otpVerificationScreen"
    const val NEW_PASSWORD_SCREEN = "newPasswordScreen"
    const val HOME_SCREEN = "homeScreen"
    const val EVENTS_SCREEN = "eventsScreen"
    const val TEAMS_SCREEN = "teamsScreen"
    const val SPLASH_SCREEN = "splashScreen"
    const val SELECT_USER_TYPE = "selectUserTypeScreen"
    const val TEAM_SETUP_SCREEN = "teamSetupScreen"
    const val ADD_PLAYER_SCREEN = "addPlayerScreen"
    const val PROFILE_SETUP_SCREEN = "profileSetupScreen"
}

object ApiConstants {
    const val BASE_URL = "http://baller-app.softprodigyphp.in"
    const val LOGIN = "v1/auth/login"
    const val SOCIAL_LOGIN = "/social_login"
    const val SIGNUP = "/register"
    const val FORGOT_PASSWORD = "/forgot/password"
    const val VERIFY_OTP = "/verifyOtp/"
    const val RESET_PASS = "/reset/password/"
    const val RESEND_OTP = "/resendOtp"
    const val first_name="first_name"
    const val last_name="lastName"
    const val mobile="mobile"
    const val EMAIL="email"
    const val FACEBOOK="facebook"
    const val GOOGLE="google"
    const val TWITTER="google"
    const val email = "email"
    const val token = "token"
}

object RequestCode{
    const val GOOGLE_ACCESS = 100

}