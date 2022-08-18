package com.softprodigy.ballerapp.ui.features.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SocialLoginSection
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun LoginScreen(
    vm: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (LoginResponse?) -> Unit,
    onForgetPasswordClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val loginState = vm.loginUiState.value
    val context = LocalContext.current

    val isError = rememberSaveable { mutableStateOf(false) }

//    val authResultLauncher =
//        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
//            try {
//                val gsa = task?.getResult(ApiException::class.java)
//                Timber.i("gsa: $gsa")
//
//                if (gsa != null) {
//                    val googleUser = GoogleUserModel(
//                        email = gsa.email,
//                        name = gsa.displayName,
//                        id = gsa.id,
//                        token = gsa.idToken
//                    )
//                    vm.onEvent(LoginUIEvent.OnGoogleClick(googleUser))
//                } else {
//                    isError.value = true
//                }
//            } catch (e: ApiException) {
//                Timber.i("LoginScreen: $e")
//            }
//        }


    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val passwordVisibility by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        vm.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is LoginChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is LoginChannel.OnLoginSuccess -> {
                    onLoginSuccess.invoke(uiEvent.loginResponse)
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_ball_center),
                contentDescription = "Login Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                AppText(
                    text = stringResource(id = R.string.baller),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                AppText(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                AppOutlineTextField(
                    value = email,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        email = it
                    },
                    placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = (!email.isValidEmail() && email.length >= 6),
                    errorMessage = stringResource(id = R.string.email_error),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                AppText(
                    text = stringResource(id = R.string.password),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                AppOutlineTextField(
                    value = password,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        password = it
                    },
                    placeholder = { Text(text = stringResource(id = R.string.your_password)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = (!password.isValidPassword() && password.length >= 4),
                    errorMessage = stringResource(id = R.string.password_error),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

                AppButton(
                    enabled = email.isValidEmail() && password.isValidPassword(),
                    onClick = {
                        onLoginSuccess(null)
//                    vm.onEvent(
//                        LoginUIEvent.Submit(email, password)
//                    )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    text = stringResource(id = R.string.login),
                    icon = painterResource(id = R.drawable.ic_circle_next)
                ) {

                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

                AppText(
                    text = stringResource(id = R.string.forgot_password),
                    color = Color.Black,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable(onClick = onForgetPasswordClick)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                SocialLoginSection(
                    modifier = Modifier.fillMaxSize(),
                    headerText = stringResource(id = R.string.or_sign_in_with),
                    onAppleClick = { },
                    onFacebookClick = { }) {
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            }
        }

        if (loginState.isDataLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}


@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "rectangle", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    BallerAppTheme {
        Surface {
        }
    }
}