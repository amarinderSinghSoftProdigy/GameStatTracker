package com.softprodigy.deliveryapp.common

object AppConstants {
    const val ANDROID = "android"
    const val GOOGLE = "google"
    const val KEY_PASSWORD = "password"
    const val KEY_USERS = "users"
    const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
}

object Route {
    const val WELCOME_SCREEN = "welcomeScreen"
    const val LOGIN_SCREEN = "loginScreen"
    const val SIGN_UP_SCREEN = "signupScreen"
    const val FORGOT_PASSWORD_SCREEN = "forgotPasswordScreen"
    const val OTP_VERIFICATION_SCREEN = "otpVerificationScreen"
    const val NEW_PASSWORD_SCREEN = "newPasswordScreen"
    const val HOME_SCREEN = "homeScreen"
}

object ApiConstants {
    const val BASE_URL = "http://delivery-app.softprodigyphp.in"
    const val LOGIN = "/login"
    const val SOCIAL_LOGIN = "/social_login"
    const val SIGNUP = "/register"
    const val FORGOT_PASSWORD = "/forgot/password"
    const val VERIFY_OTP = "/verifyOtp/"
    const val RESET_PASS = "/reset/password/"
    const val RESEND_OTP = "/resendOtp"
    const val first_name="first_name"
    const val last_name="lastName"
    const val mobile="mobile"
    const val email="email"
    const val token="token"
}