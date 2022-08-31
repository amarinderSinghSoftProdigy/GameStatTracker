package com.softprodigy.ballerapp

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.softprodigy.ballerapp.common.Route.ADD_PLAYER_SCREEN
import com.softprodigy.ballerapp.common.Route.LOGIN_SCREEN
import com.softprodigy.ballerapp.common.Route.PROFILE_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.SELECT_USER_TYPE
import com.softprodigy.ballerapp.common.Route.SIGN_UP_SCREEN
import com.softprodigy.ballerapp.common.Route.SPLASH_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAM_SETUP_SCREEN
import com.softprodigy.ballerapp.common.Route.WELCOME_SCREEN
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.twitter_login.TwitterConstants
import com.softprodigy.ballerapp.ui.features.home.HomeActivity
import com.softprodigy.ballerapp.ui.features.login.LoginScreen
import com.softprodigy.ballerapp.ui.features.sign_up.ProfileSetUpScreen
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpScreen
import com.softprodigy.ballerapp.ui.features.splash.SplashScreen
import com.softprodigy.ballerapp.ui.features.user_type.TeamSetupScreen
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeScreen
import com.softprodigy.ballerapp.ui.features.user_type.add_player.AddPlayersScreen
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
                        NavControllerComposable(this, twitterUser.value)
                    }
                }
            }
        }
    }

    private fun getRequestToken() {
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
        twitterDialog = Dialog(this,android.R.style.Theme)
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
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(TwitterConstants.CALLBACK_URL, ignoreCase = true)) {
                Log.d("Authorization URL: ", url)
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

            //Twitter Id
            val twitterId = usr.id.toString()
            Log.d("Twitter Id: ", twitterId)
            id = twitterId

            //Twitter Handle
            val twitterHandle = usr.screenName
            Log.d("Twitter Handle: ", twitterHandle)
            handle = twitterHandle

            //Twitter Name
            val twitterName = usr.name
            Log.d("Twitter Name: ", twitterName)
            name = twitterName

            //Twitter Email
            val twitterEmail = usr.email
            Log.d(
                "Twitter Email: ",
                twitterEmail
                    ?: "'Request email address from users' on the Twitter dashboard is disabled"
            )
            email = twitterEmail
                ?: "'Request email address from users' on the Twitter dashboard is disabled"

            // Twitter Profile Pic URL
            val twitterProfilePic = usr.profileImageURLHttps.replace("_normal", "")
            Log.d("Twitter Profile URL: ", twitterProfilePic)
            profilePicURL = twitterProfilePic


            // Twitter Access Token
            Log.d("Twitter Access Token", accToken?.token ?: "")
            accessToken = accToken?.token ?: ""


            // Save the Access Token (accToken.token) and Access Token Secret (accToken.tokenSecret) using SharedPreferences
            // This will allow us to check user's logging state every time they open the app after cold start.
//        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
//        sharedPref.edit().putString("oauth_token", accToken?.token ?: "").apply()
//        sharedPref.edit().putString("oauth_token_secret", accToken?.tokenSecret ?: "").apply()

//        openDetailsActivity()
            val socialUserModel = SocialUserModel(id = twitterId, email = twitterEmail)
//        twitterUser.onTwitterUserFetch(socialUserModel)
            twitterUser.value = socialUserModel
        }

    }

    @Composable
    fun NavControllerComposable(activity: MainActivity, twitterUser: SocialUserModel) {
        val navController = rememberNavController()
        val context = LocalContext.current
        NavHost(navController, startDestination = SPLASH_SCREEN) {

            composable(route = SPLASH_SCREEN) {
                SplashScreen {
                    navController.popBackStack()
                    navController.navigate(WELCOME_SCREEN)
                }
            }

            composable(route = SIGN_UP_SCREEN) {
                SignUpScreen(onSignUpSuccess = {
                    navController.navigate(SELECT_USER_TYPE)
                })
            }

            composable(route = WELCOME_SCREEN) {
                WelcomeScreen {
                    navController.popBackStack()
                    navController.navigate(LOGIN_SCREEN)
                }
            }

        composable(route = LOGIN_SCREEN) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(SELECT_USER_TYPE) {
                        navController.popBackStack()
                    }
                },
                onRegister = { navController.navigate(SIGN_UP_SCREEN) },
                onForgetPasswordClick = { },
                onTwitterClick = {
                    scope.launch {
                        (context as MainActivity).getRequestToken()
                    }

                },
                twitterUser = twitterUser
            )
        }
        composable(route = PROFILE_SETUP_SCREEN) {
            ProfileSetUpScreen(onNext = { navController.navigate(TEAM_SETUP_SCREEN) }, onBack = {
                navController.popBackStack()
            })
        }
        composable(route = SELECT_USER_TYPE) {

            BackHandler(true) {

            }

            UserTypeScreen(onNextClick = { userType ->
                Timber.i("onNextClick-- $userType")
                navController.navigate(PROFILE_SETUP_SCREEN)
            })
        }
            composable(route = TEAM_SETUP_SCREEN) {
                TeamSetupScreen(onBackClick = { navController.popBackStack() }, onNextClick = {
                    navController.navigate(ADD_PLAYER_SCREEN)
                })
            }
            composable(route = ADD_PLAYER_SCREEN) {
                AddPlayersScreen(onBackClick = { navController.popBackStack() }, onNextClick = {
                    val intent = Intent(activity, HomeActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                })
            }
        }
    }
}
