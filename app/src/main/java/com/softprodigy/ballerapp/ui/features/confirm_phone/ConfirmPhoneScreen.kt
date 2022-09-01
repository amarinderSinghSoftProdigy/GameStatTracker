package com.softprodigy.ballerapp.ui.features.confirm_phone

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R

import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.login.LoginChannel
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfirmPhoneScreen(
    onDismiss: () -> Unit,
    viewModel: SignUpViewModel,
    phoneNumber: String
) {

    val (editValue, setEditValue) = remember { mutableStateOf("") }
    val otpLength = remember { 6 }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var otp by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_20dp),
                vertical = dimensionResource(id = R.dimen.size_20dp)
            )
    ) {

        Icon(
            painter = painterResource(id = com.softprodigy.ballerapp.R.drawable.ic_close_color_picker),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(dimensionResource(id = com.softprodigy.ballerapp.R.dimen.size_16dp))
                .clickable {
                    onDismiss.invoke()
                }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //   verticalArrangement = Arrangement.Center
        ) {
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
                    viewModel.onEvent(
                        SignUpUIEvent.OnConfirmNumber(
                            phoneNumber = "+$phoneNumber",
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