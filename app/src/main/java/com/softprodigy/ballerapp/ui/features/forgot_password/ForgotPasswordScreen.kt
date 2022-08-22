package com.softprodigy.ballerapp.ui.features.forgot_password

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baller_app.core.util.UiEvent

import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.data.response.ForgotPasswordResponse
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun ForgotPasswordScreen(
    onOtpClick: (ForgotPasswordResponse) -> Unit,
    onSuccess: (ForgotPasswordResponse) -> Unit,
    onLoginClick: () -> Unit,
    vm: ForgotPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    val forgotPassState = vm.forgotPassUiState.value

    LaunchedEffect(key1 = true) {
        vm.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {
                    vm.forgotResponse?.let {
                        onSuccess(it)
                        onOtpClick(it)
                    }
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                else -> Unit
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
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraXLarge))

            Image(
                painter = painterResource(id = R.drawable.ic_forgot_password),
                contentDescription = "Welcome Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            AppText(
                text = stringResource(id = R.string.forgot_password_heading),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            AppText(
                text = stringResource(id = R.string.forgot_password_subtitle),
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            AppOutlineTextField(
                value = email,
                label = { Text(text = stringResource(id = R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    email = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = (!email.isValidEmail() && email.length >= 6),
                errorMessage = stringResource(id = R.string.email_error),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            AppButton(
                onClick = {
                    vm.onEvent(
                        ForgotPasswordUIEvent.Submit(email)
                    )

                },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = email.isValidEmail(),
                text = stringResource(id = R.string.send)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            AppText(
                text = stringResource(id = R.string.back_to_login),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable {
                        onLoginClick()
                    }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        }

        if (forgotPassState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

}