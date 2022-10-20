package com.softprodigy.ballerapp.ui.features.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpChannel
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpUIEvent
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpViewModel
import com.softprodigy.ballerapp.ui.theme.appColors
import com.togitech.ccp.data.utils.getDefaultPhoneCode

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    signUpViewModel: SignUpViewModel,
    onSuccess: () -> Unit,
) {
    val state = signUpViewModel.signUpUiState.value
    val context = LocalContext.current
    val phone = rememberSaveable { mutableStateOf(false) }
     val keyboardController = LocalSoftwareKeyboardController.current
    val maxEmailChar = 10
    LaunchedEffect(key1 = Unit) {
        signUpViewModel.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnOTPScreen -> {
                    onSuccess()
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                AppButton(
                    singleButton = true,
                    enabled = true,
                    onClick = {
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
                     },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_56dp)),
                    text = stringResource(id = R.string.next),
                    icon = painterResource(id = R.drawable.ic_circle_next),
                )
            }
            if (state.isLoading) {
                CommonProgressBar()
            }
        }
    }
}