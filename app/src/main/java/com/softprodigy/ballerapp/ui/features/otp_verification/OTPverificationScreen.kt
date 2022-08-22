package com.softprodigy.ballerapp.ui.features.otp_verification

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baller_app.core.util.UiEvent

import com.softprodigy.ballerapp.data.response.VerifyOtpResponse
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.softprodigy.ballerapp.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OTPVerificationScreen(
    onSuccess: (VerifyOtpResponse) -> Unit,
    token: String,
    email: String,
    vm: VerifyOtpViewModel = hiltViewModel()
) {
    val (editValue, setEditValue) = remember { mutableStateOf("") }
    val otpLength = remember { 6 }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val verifyOtpState = vm.verifyOtpUiState.value
    val resendOtpState = vm.resendPassUiState.value
    val context = LocalContext.current
    var otp by rememberSaveable { mutableStateOf("") }
    var resendToken = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        vm.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {
                    vm.otpResponse?.let {
                        onSuccess(it)
                    }
                }
                is UiEvent.GenerateOtp -> {
                    vm.forgotResponse?.let {
                        vm.forgotResponse?.let {
                            resendToken.value = it.verifyToken
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
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
                painter = painterResource(id = R.drawable.ic_otp_verification),
                contentDescription = "Welcome Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            AppText(
                text = stringResource(id = R.string.otp_verifcation),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            AppText(
                text = stringResource(id = R.string.please_enter_4_digit_code, "your email"),
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            AppOutlineTextField(
                value = editValue,
                onValueChange = {
                    if (it.length <= otpLength) {
                        setEditValue(it)
                        otp = it
                    }
                },
                modifier = Modifier
                    .size(0.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (0 until otpLength).map { index ->
                    OtpCell(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                focusRequester.requestFocus()
                                keyboard?.show()
                            }
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                RoundedCornerShape(4.dp)
                            )
                            .graphicsLayer {
                                shadowElevation = 0.dp.toPx()
                                shape = CutCornerShape(5.dp)
                                clip = true
                            }
                            .background(color = MaterialTheme.colors.background),
                        value = editValue.getOrNull(index)?.toString() ?: "",
                        isCursorVisible = editValue.length == index
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            AppButton(
                onClick = {
                    vm.onEvent(
                        VerifyOtpUIEvent.Submit(
                            otp,
                            token = resendToken.value.ifEmpty { token }
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = otp.isNotEmpty() && otp.length >= 6,
                text = stringResource(id = R.string.verify)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
            AppText(
                text = stringResource(id = R.string.resend_code),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable {
                        setEditValue("")
                        vm.onEvent(
                            VerifyOtpUIEvent.ResendOtp(email = email)
                        )
                    }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        if (verifyOtpState.isLoading || resendOtpState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

    }
}


@Composable
fun OtpCell(
    modifier: Modifier,
    value: String,
    isCursorVisible: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = modifier
    ) {

        AppText(
            text = if (isCursorVisible) cursorSymbol else value,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
