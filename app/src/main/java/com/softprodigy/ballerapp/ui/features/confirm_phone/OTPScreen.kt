package com.softprodigy.ballerapp.ui.features.confirm_phone

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.SwapUser
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.Text
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpChannel
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayDark
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(
    viewModel: SignUpViewModel,
    onSuccess: (profilesCount:Int,profileUserIFSingle:SwapUser?) -> Unit,
    onTokenSelectionSuccess:()->Unit
) {
    // create variable for value
    var value by remember {
        mutableStateOf(1f)
    }
    // create variable for current time
    var currentTime by remember {
        mutableStateOf(60L * 1000L)
    }

    val totalTime = 60L * 1000L
    val context = LocalContext.current
    val (editValue, setEditValue) = remember { mutableStateOf("") }
    val otpLength = remember { 6 }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    var otp by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.signUpUiState.value

    LaunchedEffect(key1 = currentTime) {
        if (currentTime > 0) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnSuccess -> {
                    onSuccess(uiEvent.count,uiEvent.profileIdIfSingle)
                }
                is SignUpChannel.OnProfileUpdateSuccess -> {
                    onTokenSelectionSuccess.invoke()
                }
                    else -> Unit
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
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
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AppText(
                    text = stringResource(id = R.string.verification_code),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                AppOutlineTextField(
                    value = editValue,
                    onValueChange = {
                        if (it.length <= otpLength) {
                            setEditValue(it)
                            otp = it
                            if (otpLength == otp.length) {
                                keyboardController?.hide()
                                viewModel.onEvent(
                                    SignUpUIEvent.OnConfirmNumber(
                                        phoneNumber = state.signUpData.phone,
                                        otp = otp
                                    )
                                )
                            }
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
                                .height(dimensionResource(id = R.dimen.size_45dp))
                                .weight(1f)
                                .clickable {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                }
                                .border(
                                    width = dimensionResource(id = R.dimen.size_1dp),
                                    color = Color.LightGray,
                                    RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp))
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
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_10dp)))
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))

                Box(modifier = Modifier.fillMaxWidth()) {
                    AppText(
                        style = MaterialTheme.typography.h6,
                        text = stringResource(id = R.string.did_not_recieve_sms),
                        modifier = Modifier.align(Alignment.CenterStart),
                        color = ColorBWGrayLight
                    )


                    val annotatedText = buildAnnotatedString {
                        pushStringAnnotation(
                            tag = stringResource(id = R.string.code),
                            annotation = stringResource(id = R.string.code)
                        )
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(stringResource(id = R.string.code))
                        }

                        pop()

                    }
                    val timer = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            )
                        ) {
                            append("00:" + if ((currentTime / 1000L).toString().length == 1) "0" + (currentTime / 1000L).toString() else (currentTime / 1000L).toString())
                        }
                    }
                    if (currentTime <= 0L) {
                        ClickableText(
                            text = annotatedText,
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            onClick = {
                                annotatedText.getStringAnnotations(
                                    tag = "Resend Code",
                                    start = it,
                                    end = it
                                ).forEach { _ ->
                                    viewModel.onEvent(
                                        SignUpUIEvent.OnVerifyNumber
                                    )
                                }
                                currentTime = 60L * 1000L
                            },
                            style = MaterialTheme.typography.h6.copy(color = ColorBWGrayDark)
                        )
                    } else {

                        Text(
                            style = MaterialTheme.typography.h6,
                            color = ColorBWGrayDark,
                            text = timer,
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                        )
                    }
                }
            }
        }

        if (state.isLoading) {
            CommonProgressBar()
        }
    }
}
