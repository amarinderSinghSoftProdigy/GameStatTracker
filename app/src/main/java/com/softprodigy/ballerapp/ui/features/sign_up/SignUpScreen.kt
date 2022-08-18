package com.softprodigy.ballerapp.ui.features.sign_up

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidFullName
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.data.GoogleUserModel
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.data.response.SignUpResponse
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.login.GoogleApiContract
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing
import timber.log.Timber

@Composable
fun SignUpScreen(
    vm: SignupViewModel = hiltViewModel(),
    onSuccessfulSignUp: (SignUpResponse) -> Unit,
    onGoogleClick: (LoginResponse) -> Unit,
    onFacebookClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val signUpState = vm.signUpUIState.value
    val context = LocalContext.current


    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    val googleUser = GoogleUserModel(
                        email = gsa.email,
                        name = gsa.displayName,
                        id = gsa.id,
                        token = gsa.idToken
                    )
                    vm.onEvent(SignUpUIEvent.OnGoogleClick(googleUser))
                }
            } catch (e: ApiException) {
                Timber.i(e.toString())
            }
        }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var termsConditions by rememberSaveable { mutableStateOf(false) }
    var name by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        vm.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.OnLoginSuccess -> {
                    onGoogleClick.invoke(uiEvent.loginResponse)
                }
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnSignUpSuccess -> {
                    onSuccessfulSignUp.invoke(uiEvent.signUpResponse)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_sign_up),
                contentDescription = "Sign up Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            AppText(
                text = stringResource(id = R.string.sign_up),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            AppText(
                text = stringResource(id = R.string.create_free_account_and_join_us),
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            AppOutlineTextField(
                value = name,
                label = { Text(text = stringResource(id = R.string.your_name)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    name = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_your_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError = (!name.isValidFullName() && name.length > 4),
                errorMessage = stringResource(id = R.string.enter_valid_full_name)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            AppOutlineTextField(
                value = email,
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    email = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                isError = (!email.isValidEmail() && email.length >= 6),
                errorMessage = stringResource(id = R.string.email_error),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            AppOutlineTextField(
                value = password,
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                },
                placeholder = { Text(text = stringResource(id = R.string.create_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = (!password.isValidPassword() && password.length >= 4),
                errorMessage = stringResource(id = R.string.password_error),
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
                })
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = termsConditions,
                    onCheckedChange = {
                        termsConditions = !termsConditions
                    }
                )

                val annotatedString = buildAnnotatedString {
                    append(stringResource(id = R.string.by_creating_an_accound_you_agree))

                    pushStringAnnotation(tag = "terms", annotation = "https://google.com/terms")
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                        append(stringResource(id = R.string.terms_of_service))
                    }
                    pop()

                    append(" and ")

                    pushStringAnnotation(
                        tag = "policy",
                        annotation = "https://google.com/privacy"
                    )

                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                        append(stringResource(id = R.string.privacy_policy))
                    }
                    pop()
                }
                ClickableText(
                    text = annotatedString,
                    style = MaterialTheme.typography.h2,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "policy",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            Log.d("policy URL", it.item)
                            uriHandler.openUri(it.item)

                        }
                        annotatedString.getStringAnnotations(
                            tag = "terms",
                            start = offset,
                            end = offset
                        ).firstOrNull()?.let {
                            Log.d("terms URL", it.item)
                            uriHandler.openUri(it.item)

                        }
                    })

            }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                AppButton(
                    onClick = {
                        vm.onEvent(SignUpUIEvent.Submit(name, email, password))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = email.isValidEmail() && password.isValidPassword() && name.isValidFullName() && termsConditions
            ) {
                Text(text = stringResource(id = R.string.create_account))
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
//            SocialSection(
//                onGoogleClick = {
//                    authResultLauncher.launch(0)
//                },
//                onFacebookClick = { onFacebookClick.invoke() },
//                onFooterClick = { onLoginClick.invoke() })
            }
        if (signUpState.isLoading) {
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
//            SignUpScreen()
        }
    }
}