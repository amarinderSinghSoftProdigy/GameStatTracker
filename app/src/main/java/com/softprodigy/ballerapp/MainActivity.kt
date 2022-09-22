package com.softprodigy.ballerapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.IntentData
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_PROFILE
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.twitter_login.TwitterConstants
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.select_profile.SelectProfileScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.AddPlayersScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupScreenUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryOrange
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import twitter4j.Twitter
import twitter4j.TwitterException
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

val LocalFacebookCallbackManager =
    staticCompositionLocalOf<CallbackManager> { error("No CallbackManager provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var callbackManager = CallbackManager.Factory.create()
    lateinit var twitterDialog: Dialog
    lateinit var twitter: Twitter
    var accToken: AccessToken? = null
    private var user = "login"
    private var id = ""
    private var handle = ""
    private var name = ""
    private var email = ""
    private var profilePicURL = ""
    private var accessToken = ""
    val twitterUser = mutableStateOf<SocialUserModel?>(SocialUserModel())
    val twitterUserRegister = mutableStateOf<SocialUserModel?>(SocialUserModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val state = mainViewModel.state.value
            BallerAppMainTheme(
                customColor = state.color ?: MaterialTheme.appColors.material.primary
            ) {
                val navController = rememberNavController()
                if (!state.showAppBar) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.appColors.material.primary
                    ) {
                        CompositionLocalProvider(
                            LocalFacebookCallbackManager provides callbackManager
                        ) {
                            NavControllerComposable(this, mainViewModel, navController)
                        }
                    }
                } else {
                    Scaffold(
                        backgroundColor = MaterialTheme.appColors.material.primary,
                        topBar = {
                            if (state.showAppBar) {
                                TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                    CommonTabView(
                                        topBarData = state.topBar,
                                        userRole = "",
                                        backClick = {
                                            mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar =false))
                                            navController.popBackStack()
                                        },
                                        labelClick = {
                                        },
                                        iconClick = {
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
                                CompositionLocalProvider(
                                    LocalFacebookCallbackManager provides callbackManager
                                ) {
                                    NavControllerComposable(
                                        this@MainActivity,
                                        mainViewModel,
                                        navController
                                    )
                                }
                                /*  com.softprodigy.ballerapp.ui.features.home.NavControllerComposable(
                                      homeViewModel,
                                      teamViewModel,
                                      eventViewModel,
                                      navController = navController,
                                      showDialog = state.showDialog,
                                      fromSplash = fromSplash
                                  )*/
                            }
                        },
                        /* bottomBar = {
                             BottomNavigationBar(
                                 state.bottomBar,
                                 navController = navController,
                                 selectionColor = state.color ?: Color.Black
                             ) {
                                 homeViewModel.setBottomNav(it)
                                 if (it == BottomNavKey.HOME) {
                                     homeViewModel.setAppBar(false)
                                 } else {
                                     homeViewModel.setAppBar(true)
                                 }
                             }
                         },*/
                    )
                }


            }
        }
    }

    fun getRequestToken(type: String) {
        user = type
        lifecycleScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConstants.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConstants.CONSUMER_SECRET)
                .setIncludeEmailEnabled(true)
            val config = builder.build()
            val factory = TwitterFactory(config)
            twitter = factory.instance
            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebviewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    // Show twitter login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebviewDialog(url: String) {
        twitterDialog = Dialog(this, android.R.style.Theme)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString()
                    .startsWith(TwitterConstants.CALLBACK_URL, ignoreCase = true)
            ) {
                Timber.e("Authorization URL: ", "" + request?.url.toString())
                handleUrl(request?.url.toString())
                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString()
                        .contains(TwitterConstants.CALLBACK_URL, ignoreCase = true)
                ) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(TwitterConstants.CALLBACK_URL, ignoreCase = true)) {
                Timber.d("Authorization URL: ", url)
                lifecycleScope.launch(Dispatchers.Main) {
                    handleUrl(url)
                }

                // Close the dialog after getting the oauth_verifier
                if (url.contains(TwitterConstants.CALLBACK_URL, ignoreCase = true)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        accToken = twitter.getOAuthAccessToken(oauthVerifier)
                        getUserProfile()
                    }
//                    getUserProfile()

                } catch (e: TwitterException) {
                    e.printStackTrace()
                }
            }
        }

        private suspend fun getUserProfile() {
            val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
            val twitterId = usr.id.toString()
            Timber.d("Twitter Id: ", twitterId)
            id = twitterId
            val twitterHandle = usr.screenName
            Timber.d("Twitter Handle: ", twitterHandle)
            handle = twitterHandle
            val twitterName = usr.name
            Timber.d("Twitter Name: ", twitterName)
            name = twitterName
            val twitterEmail = usr.email
            email = twitterEmail
                ?: "'Request email address from users' on the Twitter dashboard is disabled"
            val twitterProfilePic = usr.profileImageURLHttps.replace("_normal", "")
            Timber.d("Twitter Profile URL: ", twitterProfilePic)
            profilePicURL = twitterProfilePic
            Timber.d("Twitter Access Token", accToken?.token ?: "")
            accessToken = accToken?.token ?: ""
            val socialUserModel = SocialUserModel(name = name, id = twitterId, email = twitterEmail)
            if (user == "login") {
                twitterUser.value = socialUserModel
            } else {
                twitterUserRegister.value = socialUserModel
            }
        }

    }
}

@Composable
fun NavControllerComposable(
    activity: MainActivity,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
//    val navController = rememberNavController()
    val setupTeamViewModelUpdated: SetupTeamViewModelUpdated = viewModel()
    val signUpViewModel: SignUpViewModel = viewModel()
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(activity)
    val userToken = dataStoreManager.userToken.collectAsState(initial = "")
    UserStorage.token = userToken.value
    val getRole = dataStoreManager.getRole.collectAsState(initial = "")
    val email = dataStoreManager.getEmail.collectAsState(initial = "")
    val scope = rememberCoroutineScope()
    val color = dataStoreManager.getWalkThrough.collectAsState(initial = "")
    NavHost(navController, startDestination = SPLASH_SCREEN) {
        composable(route = SPLASH_SCREEN) {
            SplashScreen {
                scope.launch {
                    if (userToken.value.isNotEmpty()) {
                        if (getRole.value.equals(AppConstants.USER_TYPE_USER, ignoreCase = true)) {
                            signUpViewModel.signUpUiState.value.signUpData =
//                                SignUpData(token = userToken.value)
                                SignUpData(token = userToken.value, email = email.value)
                        }
                        checkRole(
                            getRole.value.equals(AppConstants.USER_TYPE_USER, ignoreCase = true),
                            navController,
                            activity,
                            fromSplash = true
                        )
                    } else if (color.value.isNotEmpty()) {
                        navController.popBackStack()
                        navController.navigate(LOGIN_SCREEN)
                    } else {
                        navController.popBackStack()
                        navController.navigate(WELCOME_SCREEN)
                    }
                }
            }
        }

        composable(route = SIGN_UP_SCREEN) {
            SignUpScreen(vm = signUpViewModel, onLoginScreen = {
                navController.popBackStack()
            }, onSignUpSuccess = {
                navController.navigate(SELECT_USER_TYPE)
            },
                onTwitterClick = {
                    scope.launch {
                        (context as MainActivity).getRequestToken("signup")
                    }
                },
                twitterUser = activity.twitterUserRegister.value,
                onSocialLoginSuccess = { userInfo ->
                    checkRole(
                        userInfo.user.role.equals(
                            AppConstants.USER_TYPE_USER,
                            ignoreCase = true
                        ), navController, activity
                    )
                }, onPreviousClick = {
                    navController.popBackStack()
                },
                onSocialLoginFailed = { activity.twitterUserRegister.value = null }
            )
        }

        composable(route = WELCOME_SCREEN) {
            WelcomeScreen {
                scope.launch {
                    dataStoreManager.skipWalkthrough(AppConstants.SKIP)
                }
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }

        composable(route = LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    UserStorage.token = it?.token.toString()
                    if (it?.user?.role.equals(AppConstants.USER_TYPE_USER, ignoreCase = true)) {
                        signUpViewModel.signUpUiState.value.signUpData =
                            SignUpData(
                                token = UserStorage.token,
                                email = it?.user?.email ?: "",
                                firstName = it?.user?.firstName ?: "",
                                lastName = it?.user?.lastName ?: "",
                            )
                        signUpViewModel.signUpUiState.value.isSocialUser =true
                    }
                    val check =
                        it?.user?.role.equals(AppConstants.USER_TYPE_USER, ignoreCase = true)
                    if (check) {
                        checkRole(
                            check,
                            navController,
                            activity
                        )
                    } else {
                        navController.navigate(SELECT_PROFILE) {
                            navController.popBackStack()
                        }
                    }
                },
                onRegister = {
                    navController.navigate(SIGN_UP_SCREEN)
                },
                onForgetPasswordClick = {
                    navController.navigate(FORGOT_PASSWORD_SCREEN)
                },
                onTwitterClick = {
                    scope.launch {
                        activity.getRequestToken("login")
                    }
                },
                twitterUser = activity.twitterUser.value,
                onLoginFail = {
                    activity.twitterUser.value = null
                }
            )
        }

        composable(route = FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(onNextClick = {
                navController.navigate(LOGIN_SCREEN) {
                    navController.popBackStack()

                }
            }, onPreviousClick = {
                navController.popBackStack()
            })
        }

        composable(route = PROFILE_SETUP_SCREEN) {

            ProfileSetUpScreen(
                signUpViewModel = signUpViewModel,
                onNext = {
                    if (UserStorage.role.equals(UserType.COACH.key, ignoreCase = true)) {
                        navController.navigate(TEAM_SETUP_SCREEN) {
                            navController.popBackStack()
                        }
                    } else {
                        moveToHome(activity)
                    }
                },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable(route = SELECT_USER_TYPE) {
            mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar =false))

            BackHandler {}

            UserTypeScreen(
                signUpvm = signUpViewModel,
                onNextClick = { userType ->
                    UserStorage.role = userType

                    if (userType.equals(UserType.COACH.key, ignoreCase = true)
                        || userType.equals(UserType.PLAYER.key, ignoreCase = true)
                    ) {
                        navController.navigate(PROFILE_SETUP_SCREEN)
                    } else {
                        Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        composable(route = TEAM_SETUP_SCREEN) {
            mainViewModel.onEvent(
                MainEvent.OnTopBarChanges(
                    showAppBar = true, TopBarData(
                        topBar = TopBar.CREATE_TEAM,
                    )
                )
            )
            mainViewModel.onEvent(MainEvent.OnColorChanges(ColorPrimaryOrange))

            BackHandler {
                mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar =false))
                navController.popBackStack()
            }

            TeamSetupScreenUpdated(
                vm = setupTeamViewModelUpdated,
                onBackClick = {
                    mainViewModel.onEvent(MainEvent.OnShowTopBar(showAppBar =false))
                    navController.popBackStack() },
                onNextClick = {
                    navController.navigate(ADD_PLAYER_SCREEN)
                })
        }

        composable(route = ADD_PLAYER_SCREEN) {
            mainViewModel.onEvent(
                MainEvent.OnTopBarChanges(
                    showAppBar = true, TopBarData(
                        topBar = TopBar.INVITE_TEAM_MEMBERS,
                    )
                )
            )
            mainViewModel.onEvent(MainEvent.OnColorChanges(AppConstants.SELECTED_COLOR))

            AddPlayersScreenUpdated(
                vm = setupTeamViewModelUpdated,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    moveToHome(activity)
                }, onInvitationSuccess = {

                })
        }
        composable(route = SELECT_PROFILE) {
            SelectProfileScreen(onNextClick = { moveToHome(activity) })
        }

    }
}


private fun moveToHome(activity: MainActivity, fromSplash: Boolean = false) {
    val intent = Intent(activity, HomeActivity::class.java)
    if (fromSplash) {
        intent.putExtra(IntentData.FROM_SPLASH, fromSplash)
    }
    activity.startActivity(intent)
    activity.finish()
}

private fun checkRole(
    check: Boolean,
    navController: NavController,
    activity: MainActivity,
    fromSplash: Boolean = false
) {
    if (check) {
        navController.navigate(SELECT_USER_TYPE) {
            navController.popBackStack()
        }
    } else {
        moveToHome(activity, fromSplash)
    }
}
