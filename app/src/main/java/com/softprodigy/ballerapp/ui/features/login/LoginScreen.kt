package com.softprodigy.ballerapp.ui.features.login

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
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
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

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
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(95.dp)
                    .width(160.dp),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            AppText(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
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
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = password,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                onValueChange = {
                    password = it
                },
                placeholder = { Text(text = stringResource(id = R.string.re_enter_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = stringResource(id = R.string.login),
                icon = painterResource(id = R.drawable.ic_circle_next)
            ) {}
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

            AppText(
                text = stringResource(id = R.string.create_now),
                color = Color.Black,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(10.dp)
                    .clickable(onClick = onCreateAccountClick)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AppText(
                text = stringResource(id = R.string.forgot_password),
                color = Color.Black,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onForgetPasswordClick)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraXLarge))


            SocialLoginSection(
                headerText = stringResource(id = R.string.or_sign_in_with),
                onAppleClick = { },
                onFacebookClick = { }) {
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