package com.softprodigy.deliveryapp.ui.features.welcome

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.android.gms.common.api.ApiException
import com.softprodigy.deliveryapp.LocalFacebookCallbackManager
import com.softprodigy.deliveryapp.R
import com.softprodigy.deliveryapp.common.CustomFBManager
import com.softprodigy.deliveryapp.common.FacebookUserProfile
import com.softprodigy.deliveryapp.data.FacebookUserModel
import com.softprodigy.deliveryapp.data.GoogleUserModel
import com.softprodigy.deliveryapp.data.response.LoginResponse
import com.softprodigy.deliveryapp.ui.features.components.AppButton
import com.softprodigy.deliveryapp.ui.features.components.AppOutlinedButton
import com.softprodigy.deliveryapp.ui.features.components.AppText
import com.softprodigy.deliveryapp.ui.features.components.SocialSection
import com.softprodigy.deliveryapp.ui.features.components.Text
import com.softprodigy.deliveryapp.ui.features.login.GoogleApiContract
import com.softprodigy.deliveryapp.ui.theme.DeliveryProjectStructureDemoTheme
import com.softprodigy.deliveryapp.ui.theme.spacing
import timber.log.Timber

//
//@Composable
//fun WelcomeScreen(
//    vm: WelcomeViewModel = hiltViewModel(),
//    onCreateAccountCLick: () -> Unit,
//    onSkipCLick: () -> Unit,
//    onGoogleLogin: (LoginResponse) -> Unit,
//    onFacebookClick: () -> Unit,
//    onLoginClick: () -> Unit,
//
//    ) {
//
//    val callbackManager = LocalFacebookCallbackManager.current
//    val context = LocalContext.current
//    val welcomeUIState = vm.welcomeUiState.value
//
//    DisposableEffect(Unit) {
//
//        LoginManager.getInstance().registerCallback(
//            callbackManager,
//            object : FacebookCallback<LoginResult> {
//                override fun onSuccess(loginResult: LoginResult) {
//
//                    CustomFBManager.getFacebookUserProfile(loginResult.accessToken, object : FacebookUserProfile {
//                        override fun onFacebookUserFetch(fbUser: FacebookUserModel) {
//                            Timber.i("FacebookUserModel-- $fbUser")
//                        }
//                    })
//                }
//
//                override fun onCancel() {
//                    println("onCancel")
//                }
//
//                override fun onError(exception: FacebookException) {
//                    println("onError $exception")
//                }
//            }
//        )
//        onDispose {
//            LoginManager.getInstance().unregisterCallback(callbackManager)
//        }
//    }
//    val authResultLauncher =
//        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
//            vm.welcomeUiState.value.isDataLoading = true
//            try {
//                val gsa = task?.getResult(ApiException::class.java)
//                if (gsa != null) {
//                    val googleUser = GoogleUserModel(
//                        email = gsa.email,
//                        name = gsa.displayName,
//                        id = gsa.id,
//                        token = gsa.idToken
//                    )
//                    vm.onEvent(WelcomeUIEvent.OnGoogleClick(googleUser))
//                }
//
//            } catch (e: ApiException) {
//                Timber.i(e.toString())
//            }
//            vm.welcomeUiState.value.isDataLoading = false
//
//        }
//    LaunchedEffect(Unit) {
//        vm.welcomeChannel.collect { welcomeChannel ->
//            when (welcomeChannel) {
//                is WelcomeChannel.OnGoogleLoginSuccess -> {
//                    onGoogleLogin.invoke(welcomeChannel.loginResponse)
//                }
//                is WelcomeChannel.OnFailure -> {
//                    Toast.makeText(context, welcomeChannel.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 20.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_welcome),
//                contentDescription = "Welcome Icon",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(163.dp)
//            )
//            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
//            AppText(
//                text = stringResource(id = R.string.welcome_to_the_app),
//                style = MaterialTheme.typography.h1,
//                color = MaterialTheme.colors.onSurface
//            )
//            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
//            AppText(
//                text = stringResource(id = R.string.create_free_account),
//                style = MaterialTheme.typography.h2,
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
//            AppButton(
//                onClick = { onCreateAccountCLick.invoke() },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                androidx.compose.material.Text(text = stringResource(id = R.string.create_new_account))
//            }
//            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
//            AppOutlinedButton(
//                onClick = { onSkipCLick.invoke() },
//                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                androidx.compose.material.Text(text = stringResource(id = R.string.skip))
//            }
//
//            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
//            SocialSection(modifier = Modifier.fillMaxWidth(), onGoogleClick = {
//                vm.welcomeUiState.value.isDataLoading = true
//                authResultLauncher.launch(0)
//            }, onFacebookClick = {
//                LoginManager.getInstance()
//                    .logInWithReadPermissions(
//                        context as ActivityResultRegistryOwner,
//                        callbackManager,
//                        listOf("public_profile", "email")
//                    )
//            }, onFooterClick = {
//                onLoginClick.invoke()
//            })
//
//        }
//        if (welcomeUIState.isDataLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
//    }
//}
//
//@Preview("default", "rectangle")
//@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview("large font", "rectangle", fontScale = 2f)
//@Composable
//private fun RectangleButtonPreview() {
//    DeliveryProjectStructureDemoTheme {
//        Surface {
////            WelcomeScreen()
//        }
//    }
//}

data class WelcomeScreenData(val image: Int, val title: String)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen() {

    val items = ArrayList<WelcomeScreenData>()
    items.add(WelcomeScreenData(R.drawable.ball, "Balling App"))
    items.add(WelcomeScreenData(R.drawable.ball, "Balling App"))
    items.add(WelcomeScreenData(R.drawable.ball, "Balling App"))
    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffScreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = items[page].image),
                        contentDescription = items[page].title,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                    AppText(
                        text = items[page].title,
                        style = MaterialTheme.typography.h1,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                }
            }

            PagerIndicator(size = items.size, currentPage = pagerState.currentPage)

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerState(
    @androidx.annotation.IntRange(from = 0) pageCount: Int,
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @androidx.annotation.IntRange(from = 1) initialOffScreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {

    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffScreenLimit,
        infiniteLoop = infiniteLoop
    )

}

@Composable
fun PagerIndicator(size: Int, currentPage: Int) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }
    }

}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 15.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(10.dp)
            .width(width = width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.Black else Color.Gray)
    )
}
