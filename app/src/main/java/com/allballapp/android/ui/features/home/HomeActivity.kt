package com.allballapp.android.ui.features.home

//import com.softprodigy.ballerapp.ui.features.home.events.NewEventScreen
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.allballapp.android.MainActivity
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.Route
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.events.*
import com.allballapp.android.ui.features.home.events.division.divisionTab.DivisionScreenTab
import com.allballapp.android.ui.features.home.events.game.GameDetailsScreen
import com.allballapp.android.ui.features.home.events.game.GameRuleScreen
import com.allballapp.android.ui.features.home.events.new_event.NewEventScreen
import com.allballapp.android.ui.features.home.events.opportunities.*
import com.allballapp.android.ui.features.home.events.team.team_tabs.EventTeamTabs
import com.allballapp.android.ui.features.home.events.venues.openVenue.OpenVenueTopTabs
import com.allballapp.android.ui.features.home.home_screen.HomeScreen
import com.allballapp.android.ui.features.home.home_screen.HomeScreenEvent
import com.allballapp.android.ui.features.home.invitation.InvitationScreen
import com.allballapp.android.ui.features.home.manage_team.MainManageTeamScreen
import com.allballapp.android.ui.features.home.teams.TeamUIEvent
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.home.teams.TeamsScreen
import com.allballapp.android.ui.features.home.teams.chat.NewConversationScreen
import com.allballapp.android.ui.features.home.teams.chat.TeamsChatDetailScreen
import com.allballapp.android.ui.features.home.teams.chat.TeamsChatScreen
import com.allballapp.android.ui.features.home.webview.CommonWebView
import com.allballapp.android.ui.features.profile.*
import com.allballapp.android.ui.features.sign_up.ProfileSetUpScreen
import com.allballapp.android.ui.features.sign_up.SignUpUIEvent
import com.allballapp.android.ui.features.sign_up.SignUpViewModel
import com.allballapp.android.ui.features.user_type.team_setup.updated.*
import com.allballapp.android.ui.features.venue.VenueListScreen
import com.allballapp.android.ui.theme.BallerAppMainTheme
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.utils.anims.exitTransition
import com.allballapp.android.ui.utils.anims.slideInHorizont
import com.allballapp.android.ui.utils.anims.slideOutHorizont
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

val animeDuration = 1050

@AndroidEntryPoint
class HomeActivity : FragmentActivity() {

    var dataStoreManager: DataStoreManager = DataStoreManager(this)
    val cometChat = CometChatUI()
    var setupTeamViewModelUpdated: SetupTeamViewModelUpdated? = null

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        cometChat.context = this
        cometChat.setConversationClickListener()
        setContent {
            //val fromSplash = intent.getBooleanExtra(IntentData.FROM_SPLASH, false)
            val homeViewModel: HomeViewModel = hiltViewModel()
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val teamViewModel: TeamViewModel = hiltViewModel()
            val eventViewModel: EventViewModel = hiltViewModel()
            setupTeamViewModelUpdated = hiltViewModel()
            val state = homeViewModel.state.value
            dataStoreManager = DataStoreManager(LocalContext.current)
            /* val userToken = dataStoreManager.userToken.collectAsState(initial = "")
             UserStorage.token = userToken.value
             */
            val color =
                dataStoreManager.getColor.collectAsState(initial = AppConstants.DEFAULT_COLOR)
            val teamId = dataStoreManager.getId.collectAsState(initial = "")
            val teamName = dataStoreManager.getTeamName.collectAsState(initial = "")
            val role = dataStoreManager.getRole.collectAsState(initial = "")
            UserStorage.teamId = teamId.value
            UserStorage.teamName = teamName.value
            AppConstants.SELECTED_COLOR =
                fromHex(color.value.ifEmpty { AppConstants.DEFAULT_COLOR })
            homeViewModel.setColor(AppConstants.SELECTED_COLOR)
            //homeViewModel.showBottomAppBar(true)
            BallerAppMainTheme(
                customColor = state.color ?: MaterialTheme.appColors.material.primaryVariant
            ) {
                val navController = rememberAnimatedNavController()
                Scaffold(
                    backgroundColor = MaterialTheme.appColors.material.primary,
                    topBar = {
                        if (state.showTopAppBar) {
                            TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                CommonTabView(
                                    topBarData = state.topBar,
                                    userRole = role.value,
                                    backClick = {
                                        if (state.topBar.topBar == TopBar.MY_EVENT) {
                                            homeViewModel.setDialog(true)
                                        } else {
                                            navController.popBackStack()
                                        }
                                    },
                                    labelClick = {
                                        homeViewModel.setDialog(true)
                                    },
                                    iconClick = {
                                        when (state.topBar.topBar) {
                                            TopBar.MY_EVENT -> {
                                                navController.navigate(Route.NEW_EVENT)
                                            }
                                            TopBar.TEAMS -> {
                                                navController.navigate(Route.MANAGED_TEAM_SCREEN)
                                            }
                                            TopBar.MANAGE_TEAM -> {
                                                if (!teamViewModel.teamUiState.value.isLoading)
                                                    teamViewModel.onEvent(TeamUIEvent.OnTeamUpdate)
                                            }
                                            TopBar.PROFILE -> {
                                                navController.navigate(Route.PROFILE_EDIT_SCREEN)
                                            }
                                            TopBar.EVENT_OPPORTUNITIES -> {
                                                navController.navigate(Route.EVENTS_FILTER_SCREEN)
                                            }
                                            else -> {}
                                            //Add events cases for tool bar icon clicks
                                        }
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
                                homeViewModel,
                                teamViewModel,
                                eventViewModel,
                                signUpViewModel,
                                navController = navController,
                                showDialog = state.showDialog,
                                role = role.value,
                                cometChat = cometChat,
                                setupTeamViewModelUpdated = setupTeamViewModelUpdated
                                    ?: hiltViewModel()
                            )
                        }
                    },
                    bottomBar = {
                        if (state.showBottomAppBar) {
                            BottomNavigationBar(
                                state.bottomBar,
                                navController = navController,
                                selectionColor = state.color ?: Color.Black,
                                badgeCount = state.unReadMessageCount
                            ) {
                                homeViewModel.setBottomNav(it)
                                if (it == BottomNavKey.HOME) {
                                    homeViewModel.setTopAppBar(false)
                                } else {
                                    homeViewModel.setTopAppBar(true)
                                }
                            }
                        }
                    },
                )
                //}
                if (state.showLogout) {
                    ConfirmDialog(
                        title = stringResource(id = R.string.logout_message),
                        onDismiss = { homeViewModel.setLogoutDialog(false) },
                        onConfirmClick = {
                            homeViewModel.clearToken()
                            moveToLogin(this)
                        })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    // on below line we are calling on activity result method.
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppConstants.REQUEST_CONTACT && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            startActivityForResult(
                intent,
                AppConstants.REQUEST_CONTACT_CODE,
                null
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavControllerComposable(
    homeViewModel: HomeViewModel,
    teamViewModel: TeamViewModel,
    eventViewModel: EventViewModel,
    signUpViewModel: SignUpViewModel,
    showDialog: Boolean = false,
    navController: NavHostController = rememberAnimatedNavController(),
    role: String = "",
    cometChat: CometChatUI,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated
) {
    val state = homeViewModel.state.value

    //val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    var eventTitle by rememberSaveable { mutableStateOf("") }
    var teamLogo by rememberSaveable {
        mutableStateOf("")
    }
    var eventMainTitle by rememberSaveable {
        mutableStateOf("")
    }
    var url by rememberSaveable {
        mutableStateOf("")
    }

    AnimatedNavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN) {
            homeViewModel.setTopAppBar(false)
            HomeScreen(
                role,
                onOpportunityClick = {
                    navController.navigate(Route.OPPORTUNITIES_SCREEN)
                },
                onEventsClick = {
                    navController.navigate(Route.MY_EVENTS)
                },
                onLeagueClick = {
                    navController.navigate(Route.MY_LEAGUE)
                },
                onInvitationCLick = {
                    navController.navigate(Route.INVITATION_SCREEN)
                },
                addProfileClick = {
                    navController.navigate(Route.ADD_PROFILE_SCREEN)
                },
                logoClick = {
                    homeViewModel.setLogoutDialog(true)
                },
                onTeamNameClick = {
                    homeViewModel.setDialog(true)
                },
                vm = homeViewModel,
                gotToProfile = {
                    navController.navigate(Route.PROFILE_SCREEN)
                },
                teamVm = teamViewModel,
                OnTeamDetailsSuccess = { teamId, teamName ->
                    UserStorage.teamId = teamId
                    UserStorage.teamName = teamName
                },
                showDialog = showDialog,
                dismissDialog = {
                    homeViewModel.setDialog(it)
                },
                onCreateTeamClick = {
                    homeViewModel.setDialog(false)
                    navController.navigate(Route.TEAM_SETUP_SCREEN) {
                        if (it != null)
                            setupTeamViewModelUpdated.onEvent(
                                TeamSetupUIEventUpdated.OnColorSelected(
                                    (it.colorCode).replace(
                                        "#", ""
                                    )
                                )
                            )
                    }
                },
                onInviteClick = {
                    setColorUpdate(
                        setupTeamViewModelUpdated,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )
                    navController.navigate(Route.ADD_MY_PLAYER_SCREEN + "/${UserStorage.teamId}")
                },
                setupTeamViewModelUpdated = setupTeamViewModelUpdated,
                onChatCLick = {
                    navController.navigate(Route.TEAMS_CHAT_SCREEN)
                }
            )
        }
        composable(route = Route.PROFILE_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.PROFILE,
                )
            )
            ProfileScreen(updateTopBar = { homeViewModel.setTopBar(it) }, vm = profileViewModel)
        }

        composable(route = Route.ADD_PROFILE_SCREEN) { backStackEntry ->
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.add_new_profile),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
//            val signUpViewModel: SignUpViewModel = hiltViewModel()
            signUpViewModel.onEvent(SignUpUIEvent.SetRegister)
            ProfileSetUpScreen(
                signUpViewModel = signUpViewModel,
                isToAddProfile = true,
                onNext = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refreshProfileList", "true")
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                })

        }

        composable(route = Route.ADD_PROFILE_SCREEN + "/{countryCode}/{mobileNumber}",
            arguments = listOf(
                navArgument("countryCode") {
                    type = NavType.StringType
                }, navArgument("mobileNumber") {
                    type = NavType.StringType
                })
        ) {

            val countryCode = it.arguments?.getString("countryCode") ?: ""
            val mobileNumber = it.arguments?.getString("mobileNumber") ?: ""

            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.add_new_profile),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
//            val signUpViewModel: SignUpViewModel = hiltViewModel()
            signUpViewModel.onEvent(SignUpUIEvent.SetRegister)
            ProfileSetUpScreen(
                mobileNumber = mobileNumber,
                countryCode = countryCode,
                isToAddProfile = true,
                signUpViewModel = signUpViewModel,
                onNext = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refreshProfileList", "true")
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                })

        }
        composable(route = Route.PROFILE_EDIT_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EDIT_PROFILE,
                )
            )
            if (UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
                RefereeEditScreen(
                    profileViewModel,
                    onBackClick = { navController.popBackStack() },
                    OnNextGameStaffClick = {
                        navController.navigate(Route.GAME_STAFF_SCREEN)
                    },
                ) {
                    navController.popBackStack()
                }
            } else {
                ProfileEditScreen(
                    onBackClick = { navController.popBackStack() },
                    onUpdateSuccess = {
                        navController.popBackStack()
                    }
                )
            }


        }
        composable(route = Route.TEAMS_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = teamViewModel.teamUiState.value.teamName,
                    topBar = TopBar.TEAMS,
                )
            )
            BackHandler {
                homeViewModel.setScreen(false)
            }
            TeamsScreen(
                teamViewModel,
                showDialog = showDialog,
                setupTeamViewModelUpdated = setupTeamViewModelUpdated,
                dismissDialog = { homeViewModel.setDialog(it) },
                OnTeamDetailsSuccess = { teamId, teamName ->
                    UserStorage.teamId = teamId
                    UserStorage.teamName = teamName
                },
                onCreateTeamClick = {
                    navController.navigate(Route.TEAM_SETUP_SCREEN) {
//                        navController.popBackStack()
                        setupTeamViewModelUpdated.onEvent(
                            TeamSetupUIEventUpdated.OnColorSelected(
                                (it?.colorCode ?: "").replace(
                                    "#", ""
                                )
                            )
                        )
                    }
                },
                onTeamItemClick = {
                    //navController.navigate(Route.MY_CHAT_DETAIL)

                }, onCreateNewConversationClick = { teamId ->
                    navController.navigate(Route.CREATE_NEW_CHAT_CONVO + "/$teamId")
                },
                onAddPlayerClick = {
                    setColorUpdate(
                        setupTeamViewModelUpdated,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )
                    navController.navigate(Route.ADD_MY_PLAYER_SCREEN + "/${UserStorage.teamId}")
                }, onHomeClick = {
                    navController.navigate(Route.HOME_SCREEN)
                }, homeVm = homeViewModel
            )
        }

        composable(
            route = Route.CREATE_NEW_CHAT_CONVO + "/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                })
        ) {

            val teamId = it.arguments?.getString("teamId") ?: ""

            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.new_chat),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )

            NewConversationScreen(
                cometChat = cometChat,
                teamId = teamId,
                onGroupCreateSuccess = {
                    navController.popBackStack()
                })

        }



        composable(route = Route.EVENTS_SCREEN) {
            /* homeViewModel.setTopBar(
                 TopBarData(
                     topBar = TopBar.MY_EVENT,
                 )
             )*/
            EventsScreen(
                teamViewModel,
                eventViewModel,
                showDialog = showDialog,
                dismissDialog = { homeViewModel.setDialog(it) },
                moveToDetail = {
                    eventMainTitle = it
                    navController.navigate(Route.LEAGUE_DETAIL_SCREEN)
                },
                moveToPracticeDetail = { eventId, eventName ->
                    eventTitle = eventName
                    navController.navigate(Route.EVENTS_DETAIL_SCREEN + "/$eventId")
                },
                moveToGameDetail = {
                    eventTitle = it
                    navController.navigate(Route.GAME_DETAIL_SCREEN)
                },
                moveToOppDetails = {
                    eventTitle = it
                    navController.navigate(Route.OPP_DETAIL_SCREEN)
                },
                updateTopBar = {
                    homeViewModel.setTopBar(it)
                }
            )
        }
        composable(route = Route.GAME_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_DETAILS,
                    label = eventTitle
                )
            )
            GameDetailsScreen(eventViewModel, moveToGameRules = {
                navController.navigate(Route.GAME_RULES_SCREENS)
            })
        }
        composable(route = Route.OPP_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS,
                    label = eventTitle
                )
            )
            OppEventDetails(eventViewModel, moveToRegistration = {
                //eventViewModel.onEvent(EvEvents.ClearRegister)
                navController.navigate(Route.EVENT_REGISTRATION)
            })
        }
        composable(route = Route.EVENT_REGISTRATION) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.registration_form),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )

            if (role == UserType.REFEREE.key) {
                EventRefereeRegistrationScreen(vm = eventViewModel) {
                    navController.navigate(Route.EVENT_REGISTRATION_SUCCESS)
                }

            } else {
                EventRegistraionDetails(eventViewModel, teamViewModel, moveToPrivacy = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                }, moveToTermsAndConditions = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                }) {
                    navController.navigate(Route.EVENT_REGISTRATION_SUCCESS)
                }
            }
        }



        composable(route = Route.EVENT_REGISTRATION_SUCCESS) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.registration_form),
                    topBar = TopBar.SINGLE_LABEL,
                )
            )
            EventRegisterSuccessScreen {
                navController.navigate(Route.EVENTS_SCREEN) {
                    popUpTo(Route.EVENT_REGISTRATION)
                }
            }
        }
        composable(route = Route.LEAGUE_DETAIL_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventMainTitle,
                    topBar = TopBar.MY_LEAGUE
                )
            )
            MyLeagueDetailScreen(
                moveToOpenDetails = {
                    eventTitle = it
                    navController.navigate(Route.GAME_DETAIL_SCREEN)

                }, moveToOpenVenues = { venueName, venueId ->
                    eventTitle = venueName
                    navController.navigate(Route.OPEN_VENUE + "/${venueId}")

                }, moveToOpenDivisions = { divisionName, divisionId ->
                    eventTitle = divisionName
                    navController.navigate(Route.DIVISION_TAB + "/${divisionId}")
                }, moveToOpenTeams = { title, logo ->
                    eventTitle = title
                    teamLogo = logo
                    navController.navigate(Route.TEAM_TAB)
                },
                eventViewModel = eventViewModel
            )
        }
        composable(route = Route.EVENTS_FILTER_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.FILTER_EVENT,
                )
            )
            /* if (role.value == UserType.REFEREE.key)
                 RefereeFiltersScreen(eventViewModel) {
                     navController.popBackStack()
                 }
             else*/
            FilterScreen(eventViewModel) {
                navController.popBackStack()
            }
        }
        composable(
            route = Route.EVENTS_DETAIL_SCREEN + "/{eventId}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                })
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS, label = eventTitle
                )
            )
            val eventId = it.arguments?.getString("eventId") ?: ""
            EventDetailsScreen(eventViewModel, eventId)
        }
        composable(route = Route.GAME_RULES_SCREENS) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_RULES, label = eventTitle
                )
            )
            GameRuleScreen(eventViewModel)
        }
        composable(route = Route.MANAGED_TEAM_SCREEN) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""

            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.manage_team),
                    topBar = TopBar.MANAGE_TEAM,
                )
            )
            MainManageTeamScreen(vm = teamViewModel, onSuccess = {
                navController.popBackStack()
            }, onAddPlayerCLick = {
                setColorUpdate(
                    setupTeamViewModelUpdated,
                    teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                )
                navController.navigate(Route.ADD_MY_PLAYER_SCREEN + "/${UserStorage.teamId}")
            }, venue = venue, onVenueClick = {
                navController.navigate(Route.SELECT_VENUE)
            })
        }


        composable(route = Route.ADD_PLAYER_SCREEN) { backStackEntry ->
            homeViewModel.setTopAppBar(true)
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.invite_team_member),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            BackHandler {
                homeViewModel.setTopAppBar(false)
                navController.popBackStack()
            }
            AddPlayersScreenUpdated(
                homeVm = homeViewModel,
                addProfileClick = {
                    navController.navigate(Route.ADD_PROFILE_SCREEN)
                },
                vm = setupTeamViewModelUpdated,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(state.bottomBar.route) {
                        popUpTo(Route.TEAM_SETUP_SCREEN) {
                            inclusive = true
                        }
                    }
                    //homeViewModel.setScreen(false)
                }, onInvitationSuccess = {
                })
        }

        composable(
            route = Route.ADD_MY_PLAYER_SCREEN + "/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                }),
        ) {
            homeViewModel.setTopAppBar(true)
            //homeViewModel.showBottomAppBar(false)
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.invite_team_member),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            BackHandler {
                homeViewModel.setTopAppBar(true)
                //homeViewModel.showBottomAppBar(true)
                moveBackFromAddPlayer(homeViewModel, navController)
            }
            val teamId = it.arguments?.getString("teamId")
            AddPlayersScreenUpdated(
                homeVm = homeViewModel,
                teamData = teamViewModel,
                teamId = teamId,
                vm = setupTeamViewModelUpdated,
                onBackClick = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                },
                onNextClick = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                    //navController.navigate(TEAMS_SCREEN)
                },
                onInvitationSuccess = {
                    moveBackFromAddPlayer(homeViewModel, navController)
                },
                addProfileClick = {
                    navController.navigate(Route.ADD_PROFILE_SCREEN)

                },
            )
        }

        composable(route = Route.INVITATION_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) { backStackEntry ->

            val refreshProfileList: String = backStackEntry
                .savedStateHandle.get<String>("refreshProfileList") ?: ""
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.invitation),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            BackHandler {
                homeViewModel.setTopAppBar(false)
                navController.popBackStack()
            }
            InvitationScreen(
                refreshProfileList = refreshProfileList,
                vmSetupTeam = setupTeamViewModelUpdated,
                onNewProfileIntent = { countryCode, mobileNumber ->
                    navController.navigate(Route.ADD_PROFILE_SCREEN + "/$countryCode/$mobileNumber")

                },
                onInvitationSuccess = {
                    navController.popBackStack()
                },
                homeVm = homeViewModel,
                addProfileClick = {
                    navController.navigate(Route.ADD_PROFILE_SCREEN)

                }, signUpViewModel = signUpViewModel,
                onInviteClick = { teamId ->
                    /*setColorUpdate(
                        setupTeamViewModelUpdated,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )*/
                    navController.navigate(Route.ADD_MY_PLAYER_SCREEN + "/${teamId}")
                })
        }
        composable(route = Route.TEAM_SETUP_SCREEN) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""
            val refreshProfileList: String = backStackEntry
                .savedStateHandle.get<String>("refreshProfileList") ?: ""

            homeViewModel.setTopAppBar(true)
            //homeViewModel.showBottomAppBar(false)
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.CREATE_TEAM,
                )
            )
            BackHandler {
                setColorToOriginalOnBack(
                    navController,
                    setupTeamViewModelUpdated,
                    homeViewModel,
                    teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                )
            }
            TeamSetupScreenUpdated(
                refreshProfileList,
                homeVm = homeViewModel,
                venue = venue,
                vm = setupTeamViewModelUpdated,
                addProfileClick = {
                    navController.navigate(Route.ADD_PROFILE_SCREEN)

                },
                onBackClick = {
                    setColorToOriginalOnBack(
                        navController,
                        setupTeamViewModelUpdated,
                        homeViewModel,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )
                },
                onNextClick = {
                    setColorUpdate(
                        setupTeamViewModelUpdated,
                        teamViewModel.teamUiState.value.selectedTeam?.colorCode ?: ""
                    )
                    homeViewModel.onEvent(HomeScreenEvent.HideSwap(false))
                    if (it.isNullOrEmpty()) {
                        navController.navigate(Route.ADD_PLAYER_SCREEN) {
                            navController.popBackStack()
                        }
                    } else {
                        navController.navigate(Route.ADD_MY_PLAYER_SCREEN + "/${it}") {
                            navController.popBackStack()
                        }
                    }
                }, onVenueClick = {
                    navController.navigate(Route.SELECT_VENUE)
                })
        }

        composable(route = Route.NEW_EVENT) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""

            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.new_event),
                    topBar = TopBar.NEW_EVENT,
                )
            )
            NewEventScreen(venue = venue, onVenueClick = {
                navController.navigate(Route.SELECT_VENUE)

            }, onEventCreationSuccess = {
                navController.popBackStack()
            })
        }


        composable(
            route = Route.OPEN_VENUE + "/{venueId}",
            arguments = listOf(
                navArgument("venueId") {
                    type = NavType.StringType
                })
        ) {

            val venueId = it.arguments?.getString("venueId") ?: ""
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.OPEN_VENUE,
                )
            )
            OpenVenueTopTabs(venueId, eventViewModel)
        }

        composable(
            route = Route.DIVISION_TAB + "/{divisionId}",
            arguments = listOf(
                navArgument("divisionId") {
                    type = NavType.StringType
                })
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.DIVISION_TAB,
                )
            )
            val divisionId = it.arguments?.getString("divisionId") ?: ""
            DivisionScreenTab(divisionId, eventViewModel)
        }

        composable(route = Route.TEAM_TAB) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.TEAM_TAB,
                    logo = teamLogo
                )
            )
            EventTeamTabs(vm = teamViewModel)
        }
        composable(route = Route.MY_CHAT_DETAIL) {
            Timber.e("data " + CometChatUI.convo)
            TeamsChatDetailScreen()
        }
        composable(route = Route.TEAMS_CHAT_SCREEN) {
            TeamsChatScreen(
                "",
                homeVm = homeViewModel,
                onTeamItemClick = {

                },
                onCreateNewConversationClick = { teamId ->
                    navController.navigate(Route.CREATE_NEW_CHAT_CONVO + "/$teamId")
                }
            )
        }
        composable(route = Route.SELECT_VENUE) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.select_venue),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            VenueListScreen(onVenueClick = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("venue", it)
                navController.popBackStack()
            })
        }

        composable(route = Route.WEB_VIEW) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = "",
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            CommonWebView(url)
        }

        composable(route = Route.GAME_STAFF_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = "",
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            SearchGameStaff(profileViewModel, onGameStaffClick = {
                navController.popBackStack()
            })
        }

        composable(route = Route.OPPORTUNITIES_SCREEN) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.events_label),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            OpportunitiesScreen(eventViewModel) {
                navController.popBackStack()
            }
        }

        composable(route = Route.MY_LEAGUE) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.events_label),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            MyLeagueScreen(eventViewModel) {
                eventMainTitle = it
                navController.navigate(Route.LEAGUE_DETAIL_SCREEN)
            }
        }

        composable(route = Route.MY_EVENTS) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.events_label),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            MyEvents(eventViewModel,
                moveToPracticeDetail = { eventId, eventName ->
                    eventTitle = eventName
                    navController.navigate(Route.EVENTS_DETAIL_SCREEN + "/$eventId")
                },
                moveToGameDetail = {
                    eventTitle = it
                    navController.navigate(Route.GAME_DETAIL_SCREEN)
                })
        }
    }

}

fun setColorToOriginalOnBack(
    navController: NavHostController,
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated,
    homeViewModel: HomeViewModel,
    colorCode: String
) {
    moveBackFromAddPlayer(homeViewModel, navController)
    if (colorCode.isNotEmpty()) {
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                colorCode.replace(
                    "#", ""
                )
            )
        )
    }
}

fun setColorUpdate(
    setupTeamViewModelUpdated: SetupTeamViewModelUpdated, colorCode: String
) {
    if (colorCode.isNotEmpty()) {
        setupTeamViewModelUpdated.onEvent(
            TeamSetupUIEventUpdated.OnColorSelected(
                colorCode.replace(
                    "#", ""
                )
            )
        )
    }
}

fun moveBackFromAddPlayer(homeViewModel: HomeViewModel, navController: NavHostController) {
    navController.popBackStack()
    homeViewModel.onEvent(HomeScreenEvent.HideSwap(false))
    homeViewModel.setScreen(false)
}

private fun moveToLogin(activity: HomeActivity) {
    val intent = Intent(activity, MainActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

fun getConvoType(conversation: Conversation): Any {
    return if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP)
        conversation.conversationWith as Group else
        conversation.conversationWith as User
}

