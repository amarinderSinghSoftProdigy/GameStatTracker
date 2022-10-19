package com.softprodigy.ballerapp.common

import androidx.compose.ui.graphics.Color
import com.softprodigy.ballerapp.ui.theme.Yellow700

object AppConstants {
    val REQUEST_CONTACT_CODE: Int = 1122
    const val ROLE = "role"
    const val TEAM_LOGO: String = "teamLogo"
    const val PROFILE_IMAGE: String = "profileImage"
    const val DOCUMENT: String = "document"
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
    const val PAGE = 1
    const val PAGE_LIMIT = 20
    var DEFAULT_COLOR = "FF923D"
    var SELECTED_COLOR: Color = Yellow700
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
    const val EVENTS_FILTER_SCREEN = "events_filter_screen"
    const val EVENTS_DETAIL_SCREEN = "events_detail_screen"
    const val GAME_RULES_SCREENS = "game_rules_screen"
    const val GAME_DETAIL_SCREEN = "game_detail_screen"
    const val OPP_DETAIL_SCREEN = "opp_detail_screen"
    const val EVENT_REGISTRATION = "event_registration"
    const val EVENT_REGISTRATION_SUCCESS = "eventRegistrationSuccess"
    const val TEAMS_SCREEN = "teamsScreen"
    const val SPLASH_SCREEN = "splashScreen"
    const val SELECT_USER_TYPE = "selectUserTypeScreen"
    const val TEAM_SETUP_SCREEN = "teamSetupScreen"
    const val ADD_PLAYER_SCREEN = "addPlayerScreen"
    const val PROFILE_SETUP_SCREEN = "profileSetupScreen"
    const val ROASTER_SCREEN = "roasterScreen"
    const val MANAGED_TEAM_SCREEN = "managedTeamScreen"
    const val SELECT_PROFILE = "selectProfile"
    const val SELECT_VENUE = "selectVenue"
    const val LEAGUE_DETAIL_SCREEN = "leagueDetailScreen"
    const val PROFILE_SCREEN = "profileScreen"
    const val ADD_PROFILE_SCREEN = "addProfileScreen"
    const val PROFILE_EDIT_SCREEN = "profileeditscreen"
    const val INVITATION_SCREEN = "invitationScreen"
    const val NEW_EVENT = "newEvent"
    const val MY_LEAGUE = "myLeague"
    const val MY_CHAT_DETAIL = "myChatDetail"
    const val OPEN_VENUE = "openVenue"
    const val DIVISION_TAB = "divisionTab"
    const val TEAM_TAB = "teamTab"
    const val WEB_VIEW = "webview"
}

object ApiConstants {
    const val BASE_URL = "http://baller-app.softprodigyphp.in"
    const val LOGIN = "v1/auth/login"
    const val GET_ALL_PLAYERS = "v1/player/getAllPlayers"
    const val UPLOAD_SINGLE_IMAGE = "v1/upload/single"
    const val CREATE_TEAM = "v1/team"
    const val GET_TEAMS_BY_COACH_ID = "v1/team/coach"
    const val GET_TEAMS_BY_USER_ID = "v1/team/getMyTeams"
    const val GET_TEAM_BY_ID = "v1/team/getTeamById"
    const val GET_LEADER_BOARD = "v1/team/getTeamLeaderBoard"
    const val COACH_PLAYER = "v1/team/coachPlayers"
    const val GET_TEAM_STANDING = "v1/team/standings"
    const val SEND_INVITATION = "v1/team/inviteTeamMembers"
    const val GET_ALL_INVITATION = "v1/team/getAllTeamsInvitations"
    const val GET_ALL_EVENTS = "v1/coach/event/getAllEvents"
    const val ACCEPT_TEAM_INVITATION = "v1/team/acceptTeamInvitation"
    const val REJECT_TEAM_INVITATION = "v1/team/rejectTeamInvitation"
    const val GET_USER_ROLE = "v1/team/getUserRoles"
    const val GET_TEAM_PLAYER_BY_ID = "v1/team/getTeamPlayersById"

    //Events
    const val CREATE_NEW_EVENT = "v1/coach/event/addEvent"
    const val ACCEPT_COACH_EVENT = "v1/coach/event/acceptCoachEvent"
    const val DECLINE_COACH_EVENT = "v1/coach/event/rejectCoachEvent"
    const val GET_EVENT_DETAILS = "v1/coach/event/getEventById"
    const val UPDATE_EVENT_NOTE = "v1/coach/event/updateEventNotes"
    const val GET_MY_LEAGUE = "v1/event/getMyLeagues"
    const val GET_DIVISION = "v1/event/getMyLeague/divisions"
    const val GET_VENUES = "v1/event/getMyLeague/venues"
    const val GET_ALL_TEAMS_BY_DIVISION_AND_LEAGUES = "v1/event/getMyLeague/allTeams/byDivisionId"
    const val GET_ALL_TEAMS_BY_LEAGUE_ID_All_DIVISIONS = "v1/event/getMyLeague/allTeams/byDivisions"
    const val GET_ALL_TEAMS_STANDING_BY_LEAGUE_AND_DIVISION =
        "v1/event/getMyLeague/allTeamsStanding/byDivisionId"
    const val GET_VENUE_DETAILS_BY_ID = "v1/event/getMyLeague/venueById"
    const val GET_EVENT_SCHEDULE = "v1/event/getMyLeagueSchedule"

    const val SOCIAL_LOGIN = "/social_login"
    const val SIGNUP = "v1/auth/register"
    const val FORGOT_PASSWORD = "v1/auth/forgotPassword"
    const val UPDATE_PROFILE = "v1/auth/updateUserProfile"
    const val VERIFY_PHONE = "v1/auth/verifyPhone"
    const val CONFIRM_PHONE = "v1/auth/confirmPhone"
    const val SWAP_PROFILE = "v1/auth/swapProfileList"
    const val SWAP_TOKEN = "v1/auth/swapProfileToken"
    const val SWAP_INITIAL_TOKEN = "v1/auth/initialSwapProfileToken"
    const val ADD_PROFILE = "v1/auth/addUserProfile"

    const val GET_CHATS = "v1/team/teamsChatDetails"

    const val RESET_PASS = "/reset/password/"
    const val RESEND_OTP = "/resendOtp"

    const val UPDATE_TEAM = "v1/team/update"
    const val GET_TEAM_INVITED = "v1/team/getInvitedTeamMembers"
    const val GET_USER_DETAILS = "v1/auth/getUserProfile"
    const val GET_HOME_PAGE_DETAILS = "v1/coach/event/getHomePageDetails"
    const val GET_USER_FULL_DETAILS = "v1/userDetails"
    const val UPDATE_USER_FULL_DETAILS = "v1/userDetails"
    const val EVENT_GET_DIVISIONS = "v1/division/getAllDivisions"
    const val EVENT_TEAM_REGISTRATION = "v1/event/team/registration"
    const val EVENT_UPDATE_FILTERS = "v1/event/updateFilterPreferences"

    const val LEAVE_TEAM = "v1/userDetails/leaveTeam"
    const val DOC_TYPES = "v1/userDetails/getUserDocuments"
    const val DELETE_DOC = "v1/userDetails/deleteDocument"
    const val UPDATE_DOC = "v1/userDetails/updateDocument"

    const val GET_ALL_VENUE = "v1/venue/getAllVenues"

    const val GET_ALL_OPPORTUNITIES = "v1/event/getOpportunitiesEvents"
    const val GET_OPPORTUNITY_ID = "v1/event/getEventRegisterById"
    const val GET_FILTERS = "v1/event/getFilterPreferences"

    const val first_name = "first_name"
    const val last_name = "lastName"
    const val mobile = "mobile"
    const val EMAIL = "email"
    const val FACEBOOK = "facebook"
    const val GOOGLE = "google"
    const val TWITTER = "twitter"
    const val email = "email"
    const val token = "token"


    const val AUTH_REGISTER_MOBILE = "v1/auth/registerMobile"
}


object FileExtensions {
    val allowedExtension: ArrayList<String> = arrayListOf(
        ".png",
        ".jpg",
        ".jpeg",
        ".jfif",
        ".pjpeg",
        ".pjp",
        ".avif",
        ".csv",
        ".pdf",
        ".xlsx",
    )
}

object RequestCode {
    const val GOOGLE_ACCESS = 100

}

object IntentData {
    const val FROM_SPLASH = "fromSplash"
    const val ROLE = "role"
}