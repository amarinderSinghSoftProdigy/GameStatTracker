package com.softprodigy.ballerapp.ui.features.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.CustomFBManager
import com.softprodigy.ballerapp.common.FacebookUserProfile
import com.softprodigy.ballerapp.common.GoogleApiContract
import com.softprodigy.ballerapp.common.RequestCode
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SocialLoginSection
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayDark
import com.softprodigy.ballerapp.ui.theme.appColors
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onRegister: () -> Unit,
    onLoginSuccess: (UserInfo?) -> Unit,
    onForgetPasswordClick: () -> Unit,
    onTwitterClick: () -> Unit,
    twitterUser: SocialUserModel?,
    onLoginFail: () -> Unit
) {

    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val callbackManager = LocalFacebookCallbackManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val loginState = vm.loginUiState.value
    DisposableEffect(Unit) {

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {

                    CustomFBManager.getFacebookUserProfile(result.accessToken, object :
                        FacebookUserProfile {
                        override fun onFacebookUserFetch(fbUser: SocialUserModel) {
                            Timber.i("FacebookUserModel-- $fbUser")
                            vm.onEvent(LoginUIEvent.OnFacebookClick(fbUser))

                        }
                    })
                }

                override fun onCancel() {
                    println("onCancel")
                }

                override fun onError(error: FacebookException) {
                    println("onError $error")
                }
            }
        )
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }
    LaunchedEffect(twitterUser) {
        twitterUser?.id?.let {
            if (it.isNotEmpty())
                vm.onEvent(LoginUIEvent.OnTwitterClick(twitterUser))
        }
    }

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    val googleUser = SocialUserModel(
                        email = gsa.email,
                        name = gsa.displayName,
                        id = gsa.id,
                        token = gsa.idToken
                    )
                    vm.onEvent(LoginUIEvent.OnGoogleClick(googleUser))
                } else {
//                    Toast.makeText(
//                        context,
//                        context.resources.getString(R.string.something_went_wrong),
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

            } catch (e: ApiException) {
                Timber.i(e.toString())
            }
        }

    LaunchedEffect(key1 = Unit) {
        vm.loginChannel.collect { uiEvent ->
            when (uiEvent) {
                is LoginChannel.OnLoginFailed -> {
                    onLoginFail.invoke()
                    Toast.makeText(
                        context,
                        uiEvent.errorMessage.asString(context),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                is LoginChannel.OnLoginSuccess -> {
                    onLoginSuccess.invoke(uiEvent.loginResponse)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_60dp)))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_95dp))
                    .width(dimensionResource(id = R.dimen.size_160dp)),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_60dp)))

            AppText(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = email,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.your_email),
                        fontSize = dimensionResource(
                            id =
                            R.dimen.txt_size_12
                        ).value.sp,
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                isError = (!email.isValidEmail() && email.isNotEmpty() || email.length > 45),
                errorMessage = stringResource(id = R.string.email_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = password,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.ur_password),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
               /* isError = (!password.isValidPassword() && password.isNotEmpty()),
                errorMessage = stringResource(id = R.string.password_error),*/
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
//                trailingIcon = {
//
//                    IconButton(onClick = {
//                        passwordVisibility = !passwordVisibility
//
//                    }) {
//                        Icon(
//                            imageVector = if (passwordVisibility)
//                                Icons.Filled.Visibility
//                            else
//                                Icons.Filled.VisibilityOff, ""
//                        )
//
//                    }
//                },
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = email.isValidEmail() && password.isValidPassword(),
                onClick = {
//                    onLoginSuccess(null)
                    vm.onEvent(LoginUIEvent.Submit(email, password))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp)),
                text = stringResource(id = R.string.login),
                icon = painterResource(id = R.drawable.ic_circle_next),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

            val annotatedText = buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    )
                ) {
                    append(stringResource(id = R.string.new_user))
                }
                append(" ")
                pushStringAnnotation(
                    tag = stringResource(id = R.string.sign_up),
                    annotation = stringResource(id = R.string.sign_up)
                )
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    )
                ) {
                    append(stringResource(id = R.string.create_account))
                }

                pop()

            }
            ClickableText(
                text = annotatedText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    annotatedText.getStringAnnotations(
                        tag = "Sign-up",
                        start = it,
                        end = it
                    ).forEach { _ ->
                        onRegister()
                    }
                },
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))

            AppText(
                text = stringResource(id = R.string.forgot_password),
                color = ColorBWGrayDark,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onForgetPasswordClick)
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_60dp)))

            SocialLoginSection(
                headerText = stringResource(id = R.string.or_sign_in_with),
                onGoogleClick = {
                    authResultLauncher.launch(RequestCode.GOOGLE_ACCESS)
                },
                onFacebookClick = {
                    LoginManager.getInstance()
                        .logInWithReadPermissions(
                            context as ActivityResultRegistryOwner,
                            callbackManager,
                            listOf(AppConstants.PUBLIC_PROFILE, AppConstants.EMAIL)
                        )
                }, onTwitterClick = {
                    onTwitterClick()
                })

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))

        }
        if (loginState.isDataLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
        }
    }
}