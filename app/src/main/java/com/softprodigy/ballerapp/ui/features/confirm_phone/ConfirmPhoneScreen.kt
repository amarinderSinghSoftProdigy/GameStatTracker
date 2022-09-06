package com.softprodigy.ballerapp.ui.features.confirm_phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalTime::class)
@Composable
fun ConfirmPhoneScreen(
    viewModel: SignUpViewModel,
    phoneNumber: String,
    onDismiss:()->Unit
) {

    val (editValue, setEditValue) = remember { mutableStateOf("") }
    val otpLength = remember { 6 }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var otp by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var ticks by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(1.seconds)
            ticks++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_20dp),
                vertical = dimensionResource(id = R.dimen.size_20dp)
            )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //   verticalArrangement = Arrangement.Center
        ) {

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Icon(
                    painter = painterResource(id = com.softprodigy.ballerapp.R.drawable.ic_close_color_picker),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_16dp))
                        .clickable {
                            onDismiss.invoke()
                        }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_100dp)))

            Image(
                painter = painterResource(id = com.softprodigy.ballerapp.R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_95dp))
                    .width(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_160dp)),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_60dp)))


            AppText(
                text = stringResource(id = com.softprodigy.ballerapp.R.string.enter_otp),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_20dp)))

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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = otp.isNotEmpty() && otp.length >= 6,
                onClick = {
                    keyboardController?.hide()
                    viewModel.onEvent(
                        SignUpUIEvent.OnConfirmNumber(
                            phoneNumber = phoneNumber,
                            otp = otp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp)),
                text = stringResource(id = R.string.verify),
                icon = painterResource(id = R.drawable.ic_circle_next)
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            val annotatedText = buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    )
                ) {
                    append(stringResource(id = R.string.did_not_recieve_sms))
                }
                append(" ")
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

            ClickableText(
                text = annotatedText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
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
                },
                style = MaterialTheme.typography.h4
            )

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
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}