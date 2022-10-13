package com.softprodigy.ballerapp.common

import androidx.compose.ui.graphics.Color

object AppConstants {
    const val ROLE = "role"
    const val TEAM_LOGO: String = "teamLogo"
    const val PROFILE_IMAGE: String = "profileImage"
    const val PICKER_DEFAULT_COLOR = "FFFFFFFF"
    const val ANDROID = "android"
    const val GOOGLE = "google"
    const val KEY_PASSWORD = "password"
    const val KEY_USERS = "users"
    const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
    const val USER_TYPE_PLAYER = "Player"
    const val USER_TYPE_REFEREE = "Referee"
    const val USER_TYPE_COACH = "Coach"
    const val USER_TYPE_USER = "user"
    const val USER_TYPE_PARENT = "Parent"
    const val USER_TYPE_GAME_STAFF = "Game Staff"
    const val USER_TYPE_PROGRAM_STAFF = "Program Staff"
    const val USER_TYPE_FAN = "Fan"
    const val PUBLIC_PROFILE = "public_profile"
    const val EMAIL = "email"
    const val SKIP = "skip"
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
    const val ROASTER_SCREEN = "roasterScreen"
    const val MANAGED_TEAM_SCREEN = "managedTeamScreen"
    const val SELECT_PROFILE = "selectProfile"
    const val INVITATION_SCREEN="invitationScreen"
    const val GAME_ZONE_SCREEN = "gameZoneScreen"
    const val OVERVIEW_SCREEN = "overviewScreen"
    const val BOX_SCORE_SCREEN = "boxScoreScreen"
    const val GAME_SETTINGS = "gameSettings"
}

object ApiConstants {
    const val BASE_URL = "http://baller-app.softprodigyphp.in"
    const val LOGIN = "v1/auth/login"
    const val GET_ALL_PLAYERS = "v1/player/getAllPlayers"
    const val UPLOAD_SINGLE_IMAGE = "v1/upload/single"
    const val CREATE_TEAM = "v1/team"
    const val GET_TEAMS = "v1/team"
    const val GET_TEAM_BY_ID = "v1/team/getTeamById"
    const val GET_LEADER_BOARD = "v1/team/getTeamLeaderBoard"
    const val COACH_PLAYER = "v1/team/coachPlayers"
    const val GET_TEAM_STANDING = "v1/team/standings"
    const val SEND_INVITATION = "v1/team/inviteTeamMembers"
    const val GET_ALL_INVITATION = "v1/team/getAllTeamsInvitations"
    const val ACCEPT_TEAM_INVITATION = "v1/team/acceptTeamInvitation"
    const val REJECT_TEAM_INVITATION = "v1/team/rejectTeamInvitation"

    const val SOCIAL_LOGIN = "/social_login"
    const val SIGNUP = "v1/auth/register"
    const val FORGOT_PASSWORD = "v1/auth/forgotPassword"
    const val UPDATE_PROFILE = "v1/auth/updateUserProfile"
    const val VERIFY_PHONE = "v1/auth/verifyPhone"
    const val CONFIRM_PHONE = "v1/auth/confirmPhone"
    const val RESET_PASS = "/reset/password/"
    const val RESEND_OTP = "/resendOtp"
    const val UPDATE_TEAM = "v1/team/update"
    const val GET_USER_DETAILS="v1/auth/getUserProfile"
    const val GET_HOME_PAGE_DETAILS="v1/coach/event/getHomePageDetails"
    const val first_name = "first_name"
    const val last_name = "lastName"
    const val mobile = "mobile"
    const val EMAIL = "email"
    const val FACEBOOK = "facebook"
    const val GOOGLE = "google"
    const val TWITTER = "twitter"
    const val email = "email"
    const val token = "token"
}

object RequestCode {
    const val GOOGLE_ACCESS = 100

}
object IntentData{
    const val FROM_SPLASH = "fromSplash"
}