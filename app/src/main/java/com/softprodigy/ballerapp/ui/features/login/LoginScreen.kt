package com.softprodigy.ballerapp.ui.features.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.*
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SocialLoginSection
import com.softprodigy.ballerapp.ui.theme.appColors
import timber.log.Timber

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (UserInfo?) -> Unit,
    onForgetPasswordClick: () -> Unit,
) {

    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val callbackManager = LocalFacebookCallbackManager.current

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
                }
                else{
                    Timber.i("gsa null")
                    Toast.makeText(context, "gsa null", Toast.LENGTH_SHORT).show()
                }

            } catch (e: ApiException) {
                Timber.i(e.toString())
            }

        }

    LaunchedEffect(key1 = Unit) {
        vm.loginChannel.collect { uiEvent ->
            when (uiEvent) {
                is LoginChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
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
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp))
                .verticalScroll(rememberScrollState()),
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
                modifier = Modifier.fillMaxWidth(),
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
                placeholder = { Text(text = stringResource(id = R.string.your_email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = (!email.isValidEmail() && email.isNotEmpty()),
                errorMessage = stringResource(id = R.string.email_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
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
                placeholder = { Text(text = stringResource(id = R.string.your_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = (!password.isValidPassword() && password.isNotEmpty()),
                errorMessage = stringResource(id = R.string.password_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility

                    }) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = email.isValidEmail() && password.isValidPassword(),
                onClick = {
                    vm.onEvent(LoginUIEvent.Submit(email, password))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp)),
                text = stringResource(id = R.string.login),
                icon = painterResource(id = R.drawable.ic_circle_next)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))

            AppText(
                text = stringResource(id = R.string.forgot_password),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onForgetPasswordClick)
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_100dp)))

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
                }) {
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))

        }
        if (loginState.isDataLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}