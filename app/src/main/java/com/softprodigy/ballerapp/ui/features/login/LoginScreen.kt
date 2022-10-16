package com.softprodigy.ballerapp.ui.features.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpChannel
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.appColors
import com.togitech.ccp.data.utils.getDefaultPhoneCode

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    signUpViewModel: SignUpViewModel,
    onRegister: () -> Unit,
    onLoginSuccess: (UserInfo?) -> Unit,
    onForgetPasswordClick: () -> Unit,
    onTwitterClick: () -> Unit,
    twitterUser: SocialUserModel?,
    onLoginFail: () -> Unit
) {

    val state = signUpViewModel.signUpUiState.value
    val context = LocalContext.current
    var phone = rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val callbackManager = LocalFacebookCallbackManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var maxPasswordChar = 16
    var maxEmailChar = 10
    val loginState = vm.loginUiState.value
    /*DisposableEffect(Unit) {

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
        }*/

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
    LaunchedEffect(key1 = Unit) {
        signUpViewModel.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnOTPScreen -> {
                    onRegister()
                }
                else -> Unit
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_all_ball_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.size_120dp))
                .size(dimensionResource(id = R.dimen.size_130dp)),
        )

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
                AppText(
                    text = stringResource(id = R.string.phone_num),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                AppOutlineTextField(
                    value = state.signUpData.phone,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = {
                        if (it.length <= maxEmailChar) {
                            phone.value = false
                            signUpViewModel.onEvent(
                                SignUpUIEvent.OnPhoneNumberChanged(
                                    it
                                )
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.your_phone_number),
                            fontSize = dimensionResource(
                                id =
                                R.dimen.txt_size_12
                            ).value.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Phone
                    ),
                    isError = (phone.value || !validPhoneNumber(state.signUpData.phone) && state.signUpData.phone.isNotEmpty()),
                    errorMessage = stringResource(id = R.string.valid_phone_number),
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
                    visualTransformation = MaskTransformation(),
                )

                /*AppText(
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
                        if (it.length <= maxPasswordChar)
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
                    *//* isError = (!password.isValidPassword() && password.isNotEmpty()),
                 errorMessage = stringResource(id = R.string.password_error),*//*
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))*/
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                AppButton(
                    singleButton = true,
                    enabled = true,
                    onClick = {
//                    onLoginSuccess(null)
                        if (!validPhoneNumber(state.signUpData.phone) || state.signUpData.phone.length < 10 || state.signUpData.phone.isEmpty()) {
                            phone.value = true
                        } else {
                            phone.value = false
                            signUpViewModel.onEvent(
                                SignUpUIEvent.OnCountryCode(getDefaultPhoneCode(context))
                            )
                            signUpViewModel.onEvent(
                                SignUpUIEvent.OnVerifyNumber
                            )
                        }
                        //vm.onEvent(LoginUIEvent.Submit(email, password))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_56dp)),
                    text = stringResource(id = R.string.next),
                    icon = painterResource(id = R.drawable.ic_circle_next),
                )

                //Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                /*val annotatedText = buildAnnotatedString {

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
    */
                //Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))

            }
            if (loginState.isDataLoading || state.isLoading) {
                CommonProgressBar()
            }
        }
    }
}