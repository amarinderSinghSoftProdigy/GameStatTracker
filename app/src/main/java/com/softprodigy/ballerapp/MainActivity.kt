package com.softprodigy.ballerapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.google.gson.Gson
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.FORGOT_PASSWORD_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.twitter_login.TwitterConstants
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordScreen
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.AddPlayersScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.SetupTeamViewModel
import com.softprodigy.ballerapp.ui.features.welcome.WelcomeScreen
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

val LocalFacebookCallbackManager =
    staticCompositionLocalOf<CallbackManager> { error("No CallbackManager provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var callbackManager = CallbackManager.Factory.create();
    lateinit var twitterDialog: Dialog
    lateinit var twitter: Twitter
    var accToken: AccessToken? = null
    private var id = ""
    private var handle = ""
    private var name = ""
    private var email = ""
    private var profilePicURL = ""
    private var accessToken = ""
    val twitterUser = mutableStateOf(SocialUserModel())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BallerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.appColors.material.primary
                ) {
                    CompositionLocalProvider(
                        LocalFacebookCallbackManager provides callbackManager
                    ) {
                        NavControllerComposable(this)
                    }
                }
            }
        }
    }

    fun getRequestToken() {
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
                Log.e("ERROR: ", e.toString())
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
                Log.d("Authorization URL: ", request?.url.toString())
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
                accToken =
                    withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
                getUserProfile()
//            }
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
            val socialUserModel = SocialUserModel(name=name,id = twitterId, email = twitterEmail)
            twitterUser.value = socialUserModel
        }

    }
}

@Composable
fun NavControllerComposable(activity: MainActivity) {
    val navController = rememberNavController()
    val setupTeamViewModel: SetupTeamViewModel = viewModel()
    val signUpViewModel: SignUpViewModel = viewModel()
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(activity)
    val userToken = dataStoreManager.userToken.collectAsState(initial = "")
    val scope = rememberCoroutineScope()
    NavHost(navController, startDestination = SPLASH_SCREEN) {

        composable(route = SPLASH_SCREEN) {
            SplashScreen {
                if (userToken.value.isNotEmpty()) {
                    moveToHome(activity)
                } else {
                    navController.popBackStack()
                    navController.navigate(WELCOME_SCREEN)
                }
            }
        }

        composable(route = SIGN_UP_SCREEN) {
            SignUpScreen(vm = signUpViewModel, onLoginScreen = {
                navController.navigate(LOGIN_SCREEN)
            }, onSignUpSuccess = {
                navController.navigate(SELECT_USER_TYPE)
            },
                onTwitterClick = {
                    scope.launch {
                        (context as MainActivity).getRequestToken()
                    }

                },
                twitterUser = activity.twitterUser.value,
                onLoginSuccess = { userInfo ->
                    if (!userInfo.user.role.equals(
                            AppConstants.USER_TYPE_USER,
                            ignoreCase = true
                        )
                    ) {
                        moveToHome(activity)
                    } else {
                        navController.navigate(SELECT_USER_TYPE) {
                            navController.popBackStack()
                        }
                    }
                })
        }

        composable(route = WELCOME_SCREEN) {
            WelcomeScreen {
                navController.popBackStack()
                navController.navigate(LOGIN_SCREEN)
            }
        }
        composable(route = LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    UserStorage.token = it?.token.toString()
                    /*navController.navigate(SELECT_USER_TYPE){
                        navController.popBackStack()
                    }*/
                    moveToHome(activity)
                },
                onRegister = {
                    navController.navigate(SIGN_UP_SCREEN)
                },
                onForgetPasswordClick = {
                    navController.navigate(FORGOT_PASSWORD_SCREEN)
                },
                onTwitterClick = {
                    scope.launch {
                        (context as MainActivity).getRequestToken()
                    }

                },
                twitterUser = activity.twitterUser.value
            )
        }

        composable(route = FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(onNextClick = {
                navController.navigate(LOGIN_SCREEN) {
                    navController.popBackStack()

                }
            })

        }

        composable(
            route = PROFILE_SETUP_SCREEN,
        ) {
            ProfileSetUpScreen(
                signUpViewModel = signUpViewModel,
                onNext = {
                    navController.navigate(TEAM_SETUP_SCREEN) {
                        navController.popBackStack()

                    }
                },
                onBack = {
                    navController.popBackStack()
                })
        }

        composable(
            route = SELECT_USER_TYPE
        ) {
            UserTypeScreen(
                signUpvm = signUpViewModel,
                onNextClick = {
                    navController.navigate(PROFILE_SETUP_SCREEN)
                }
            )
        }

        composable(route = TEAM_SETUP_SCREEN) {
            TeamSetupScreen(
                vm = setupTeamViewModel,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(ADD_PLAYER_SCREEN)
                })
        }
        composable(route = ADD_PLAYER_SCREEN) {
            AddPlayersScreen(
                vm = setupTeamViewModel,
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    moveToHome(activity)
                })
        }
    }

}


fun moveToHome(activity: MainActivity){
    val intent = Intent(activity, HomeActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}