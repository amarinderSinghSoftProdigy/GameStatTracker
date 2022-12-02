package com.allballapp.android.ui.features.home

//import com.softprodigy.ballerapp.ui.features.home.events.NewEventScreen
//import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.allballapp.android.MainActivity
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.IntentData
import com.allballapp.android.common.Route
import com.allballapp.android.common.argbToHexString
import com.allballapp.android.common.connectivity_helper.ConnectionState
import com.allballapp.android.common.connectivity_helper.connectivityState
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.events.*
import com.allballapp.android.ui.features.home.events.division.divisionTab.DivisionScreenTab
import com.allballapp.android.ui.features.home.events.game.GameDetailsTab
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
import com.allballapp.android.ui.features.home.teams.chat.ChatViewModel
import com.allballapp.android.ui.features.home.teams.chat.NewConversationScreen
import com.allballapp.android.ui.features.home.teams.chat.TeamsChatDetailScreen
import com.allballapp.android.ui.features.home.teams.chat.TeamsChatScreen
import com.allballapp.android.ui.features.home.teams.roaster.RoasterProfileDetails
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
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.models.*
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.cometchat.pro.uikit.ui_components.chats.CustomCometListener
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageList
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

val animeDuration = 500

@AndroidEntryPoint
class HomeActivity : FragmentActivity(), CustomCometListener {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    val cometChat = CometChatUI()
    var setupTeamViewModelUpdated: SetupTeamViewModelUpdated? = null
    lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        cometChat.context = this
        cometChat.setConversationClickListener()
//        CometChatConversationList.newCustomCometListener = this
//        CometChatMessageList.newCustomCometListener = this
        setContent {
            //val fromSplash = intent.getBooleanExtra(IntentData.FROM_SPLASH, false)
            homeViewModel = hiltViewModel()
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

            cometChat.setPrimaryColor(AppConstants.SELECTED_COLOR.toArgb().argbToHexString())

            /* Register for unread chat count broadcast*/
            registerForTeamChatCountBroadcast(homeViewModel)

            /*Check for internet availability*/
            ListenForConnectionAvailability(homeViewModel)

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
                                    selectedTeamCreatedBy = teamViewModel.teamUiState.value.createdBy,
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
                                if (it != homeViewModel.state.value.bottomBar) {
                                    homeViewModel.setBottomNav(it)
                                    if (it == BottomNavKey.HOME) {
                                        homeViewModel.setTopAppBar(false)
                                    } else {
                                        homeViewModel.setTopAppBar(true)
                                    }
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

                if (state.showNoInternetDialog) {
                    NoInternetDialog(
                        title = stringResource(id = R.string.no_internet_message),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    private fun ListenForConnectionAvailability(homeViewModel: HomeViewModel) {

        // This will cause re-composition on every network state change
        val connection by connectivityState()
        val isConnected = connection === ConnectionState.Available

        LaunchedEffect(key1 = isConnected, block = {
            if (isConnected) {
                Timber.i("connectivityState--- connected")
                homeViewModel.onEvent(HomeScreenEvent.OnNetworkAvailable(isAvailable = true))

            } else {
                Timber.i("connectivityState--- disconnected")
                homeViewModel.onEvent(HomeScreenEvent.OnNetworkAvailable(isAvailable = false))

            }
        })
    }

    private fun registerForTeamChatCountBroadcast(homeViewModel: HomeViewModel) {

        // on below line we are creating a new broad cast.
        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            // we will receive data updates in onReceive method.
            override fun onReceive(context: Context?, intent: Intent) {
                Timber.i("onReceive")
                homeViewModel.getUnreadMessageCount()
            }
        }
        // on below line we are registering our local broadcast manager.
        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver, IntentFilter(IntentData.COMET_CHAT_READ_COUNT)
        )
    }

    override fun onResume() {
        super.onResume()
        if (::homeViewModel.isInitialized) {
            homeViewModel.getUnreadMessageCount()
        }

        /* Listener for updating badge count on new conversation receive*/
        addConversationListener()

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

    override fun onTeamIDChange(teamId: String) {
        Timber.i("setResult--- $teamId")
    }

    private fun addConversationListener() {
        CometChat.addMessageListener("HomeActivity", object : CometChat.MessageListener() {
            override fun onTextMessageReceived(message: TextMessage) {
                if (::homeViewModel.isInitialized) {
                    homeViewModel.getUnreadMessageCount()
                }
            }

            override fun onMediaMessageReceived(message: MediaMessage) {
                if (::homeViewModel.isInitialized) {
                    homeViewModel.getUnreadMessageCount()
                }
            }

            override fun onCustomMessageReceived(message: CustomMessage) {
                if (::homeViewModel.isInitialized) {
                    homeViewModel.getUnreadMessageCount()
                }
            }
        })
        CometChat.addGroupListener("HomeActivity", object : CometChat.GroupListener() {

            override fun onMemberAddedToGroup(
                action: Action,
                addedby: User,
                userAdded: User,
                addedTo: Group
            ) {
                if (::homeViewModel.isInitialized) {
                    homeViewModel.getUnreadMessageCount()
                }
            }

            override fun onGroupMemberJoined(action: Action, joinedUser: User, joinedGroup: Group) {
                if (::homeViewModel.isInitialized) {
                    homeViewModel.getUnreadMessageCount()
                }
            }
        })
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
    val chatViewModel: ChatViewModel = hiltViewModel()
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
    var userid by rememberSaveable {
        mutableStateOf("")
    }

    var name by rememberSaveable {
        mutableStateOf("")
    }

    AnimatedNavHost(navController, startDestination = Route.HOME_SCREEN) {
        composable(route = Route.HOME_SCREEN, enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) { backStackEntry ->

            /*Getting required refreshTeamListing in home screen if new team added*/
            val refreshTeamListing: String = backStackEntry
                .savedStateHandle.get<String>("refreshTeamListing") ?: ""

            homeViewModel.setBottomNav(BottomNavKey.HOME)
            homeViewModel.setTopAppBar(false)
            HomeScreen(
                role,
                onOpportunityClick = {
                    navController.navigate(Route.OPPORTUNITIES_SCREEN + "/" + it)
                },
                onEventsClick = {
                    navController.navigate(Route.MY_EVENTS)
                },
                onLeagueClick = {
                    navController.navigate(Route.MY_LEAGUE + "/" + it)
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
                    homeViewModel.setBottomNav(BottomNavKey.TEAMS)
                    navController.navigate(Route.TEAMS_SCREEN)
                },
                refreshTeamListing = refreshTeamListing,
            )
        }
        composable(route = Route.PROFILE_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.PROFILE,
                )
            )
            ProfileScreen(updateTopBar = { homeViewModel.setTopBar(it) }, vm = profileViewModel)
        }

        composable(route = Route.ADD_PROFILE_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
                    homeViewModel.onEvent(HomeScreenEvent.OnSwapClick())
                    signUpViewModel.onEvent(SignUpUIEvent.ClearPhoneField)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }, moveToTermsAndConditions = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                },
                moveToPrivacyAndPolicy = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                }
            )
        }

        /*composable(route = Route.ADD_PROFILE_SCREEN + "/{countryCode}/{mobileNumber}",
            arguments = listOf(
                navArgument("countryCode") {
                    type = NavType.StringType
                }, navArgument("mobileNumber") {
                    type = NavType.StringType
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
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
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }, moveToTermsAndConditions = { urL ->
                    url = urL
                    navController.navigate(Route.WEB_VIEW)
                }, moveToPrivacyAndPolicy = {
                    url = it
                    navController.navigate(Route.WEB_VIEW)
                })

        }*/
        composable(route = Route.PROFILE_EDIT_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
                    },
                    teamViewModel = teamViewModel
                )
            }


        }
        composable(route = Route.TEAMS_SCREEN) {
            homeViewModel.setBottomNav(BottomNavKey.TEAMS)
            homeViewModel.setTopBar(
                TopBarData(
                    label = teamViewModel.teamUiState.value.teamName,
                    topBar = TopBar.TEAMS,
                )
            )
            BackHandler {
                homeViewModel.setScreen(false)
                navController.popBackStack()
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
                    homeViewModel.showBottomAppBar(false)
                    navController.navigate(Route.HOME_SCREEN)
                }, homeVm = homeViewModel,
                chatViewModel = chatViewModel,
                onProfileDetailScreen = { userId, userName ->
                    userid = userId
                    name = userName
                    navController.navigate(Route.ROASTER_PROFILE_VIEW)
                }
            )
        }

        composable(
            route = Route.CREATE_NEW_CHAT_CONVO + "/{teamId}",
            arguments = listOf(
                navArgument("teamId") {
                    type = NavType.StringType
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
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
                }, chatVM = chatViewModel
            )

        }


        composable(route = Route.EVENTS_SCREEN) {
            homeViewModel.setBottomNav(BottomNavKey.EVENTS)
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
                moveToGameDetail = { gameId, gameName ->
                    eventTitle = gameName
                    navController.navigate(Route.GAME_DETAIL_SCREEN + "/$gameId")
                },
                moveToOppDetails = {
                    eventTitle = it
                    navController.navigate(Route.OPP_DETAIL_SCREEN)
                },
                updateTopBar = {
                    homeViewModel.setTopBar(it)
                }, moveToEventDetail = {
                    eventTitle = it
                    navController.navigate(Route.OPP_DETAIL_SCREEN)
                }
            )
        }

        composable(route = Route.GAME_DETAIL_SCREEN + "/{gameId}",
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.StringType
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_DETAILS,
                    label = eventTitle
                )
            )
            /* GameDetailsScreen(eventViewModel, moveToGameRules = {
                 navController.navigate(Route.GAME_RULES_SCREENS)
             })*/

            val gameId = it.arguments?.getString("gameId") ?: ""

            GameDetailsTab(gameId, eventViewModel, moveToGameRules = {
                navController.navigate(Route.GAME_RULES_SCREENS)
            })
        }
        composable(route = Route.OPP_DETAIL_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS,
                    label = eventTitle
                )
            )
            remember {
                eventViewModel.onEvent(EvEvents.ClearOpportunities)
            }
            OppEventDetails(eventViewModel, moveToRegistration = {
                //eventViewModel.onEvent(EvEvents.ClearRegister)
                navController.navigate(Route.EVENT_REGISTRATION)
            })
        }
        composable(route = Route.EVENT_REGISTRATION,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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

        composable(route = Route.EVENT_REGISTRATION_SUCCESS,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
        composable(route = Route.LEAGUE_DETAIL_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
                    //Commented the Team screen
                    /*eventTitle = title
                    teamLogo = logo
                    navController.navigate(Route.TEAM_TAB)*/
                },
                eventViewModel = eventViewModel
            )
        }
        composable(route = Route.EVENTS_FILTER_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.EVENT_DETAILS, label = eventTitle
                )
            )
            val eventId = it.arguments?.getString("eventId") ?: ""
            EventDetailsScreen(eventViewModel, eventId)
        }
        composable(route = Route.GAME_RULES_SCREENS,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    topBar = TopBar.GAME_RULES, label = eventTitle
                )
            )
            GameRuleScreen(eventViewModel)
        }
        composable(route = Route.MANAGED_TEAM_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) { backStackEntry ->

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


        composable(route = Route.ADD_PLAYER_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
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
                vmSetupTeam = setupTeamViewModelUpdated,
                /*onNewProfileIntent = { countryCode, mobileNumber ->
                    navController.navigate(Route.ADD_PROFILE_SCREEN + "/$countryCode/$mobileNumber")

                },
                onInvitationSuccess = {
                    navController.popBackStack()
                },*/
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
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Route.TEAM_SETUP_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) { backStackEntry ->

            // get data passed back from next stack
            val venue: String = backStackEntry
                .savedStateHandle.get<String>("venue") ?: ""
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
                setupTeamViewModelUpdated.onEvent(TeamSetupUIEventUpdated.Clear)
            }
            TeamSetupScreenUpdated(
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

        composable(route = Route.NEW_EVENT,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) { backStackEntry ->

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
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }

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
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }

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

        composable(route = Route.TEAM_TAB,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.TEAM_TAB,
                    logo = teamLogo
                )
            )
            EventTeamTabs(vm = teamViewModel, eventVm = eventViewModel)
        }
        composable(route = Route.MY_CHAT_DETAIL,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
//            Timber.e("data " + CometChatUI.convo)
            TeamsChatDetailScreen()
        }
        composable(route = Route.TEAMS_CHAT_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = eventTitle,
                    topBar = TopBar.TEAM_TAB,
                    logo = teamLogo
                )
            )
            TeamsChatScreen(
                "",
                vm = chatViewModel,
                homeVm = homeViewModel,
                onTeamItemClick = {

                },
                onCreateNewConversationClick = { teamId ->
                    navController.navigate(Route.CREATE_NEW_CHAT_CONVO + "/$teamId")
                }
            )
        }
        composable(route = Route.SELECT_VENUE,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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

        composable(route = Route.WEB_VIEW,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = "",
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            CommonWebView(url)
        }

        composable(route = Route.GAME_STAFF_SCREEN,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
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

        composable(route = Route.OPPORTUNITIES_SCREEN + "/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {

            val type = it.arguments?.getString("type")
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.events_label),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            remember {
                eventViewModel.onEvent(EvEvents.ClearList)
            }
            OpportunitiesScreen(type = type ?: "", vm = eventViewModel) {
                eventTitle = it
                navController.navigate(Route.OPP_DETAIL_SCREEN)
            }
        }

        composable(route = Route.MY_LEAGUE + "/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }),
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.leagues),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            remember {
                eventViewModel.onEvent(EvEvents.ClearListLeague)
            }
            val type = it.arguments?.getString("type") ?: ""
            MyLeagueScreen(type = type, eventViewModel) {
                eventMainTitle = it
                navController.navigate(Route.LEAGUE_DETAIL_SCREEN)
            }
        }

        composable(route = Route.MY_EVENTS,
            enterTransition = { slideInHorizont(animeDuration) },
            exitTransition = { exitTransition(animeDuration) },
            popExitTransition = { slideOutHorizont(animeDuration) }
        ) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = stringResource(id = R.string.events_label),
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )
            remember {
                eventViewModel.onEvent(EvEvents.ClearListEvents)
            }
            MyEvents(eventViewModel,
                moveToPracticeDetail = { eventId, eventName ->
                    eventTitle = eventName
                    navController.navigate(Route.EVENTS_DETAIL_SCREEN + "/$eventId")
                },
                moveToGameDetail = { gameId, gameName ->
                    eventTitle = gameName
                    navController.navigate(Route.GAME_DETAIL_SCREEN + "/$gameId")
                }, moveToEventDetail = {
                    eventTitle = it
                    navController.navigate(Route.OPP_DETAIL_SCREEN)
                })
        }

        composable(route = Route.ROASTER_PROFILE_VIEW) {
            homeViewModel.setTopBar(
                TopBarData(
                    label = name,
                    topBar = TopBar.SINGLE_LABEL_BACK,
                )
            )

            RoasterProfileDetails(vm = profileViewModel, userId = userid)

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

