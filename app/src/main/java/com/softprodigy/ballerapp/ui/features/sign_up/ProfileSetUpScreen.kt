package com.softprodigy.ballerapp.ui.features.sign_up

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.EditFields
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.confirm_phone.ConfirmPhoneScreen
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSetUpScreen(
    signUpData: SignUpData?,
    onNext: () -> Unit,
    onBack: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val state = signUpViewModel.signUpUiState.value

    val context = LocalContext.current

    if (signUpData?.email?.isNotEmpty() == true) {
        state.email = signUpData.email!!
    }

    val verified = signUpViewModel.verified.value

    LaunchedEffect(key1 = Unit) {

        signUpViewModel.uiEvent.collect { uiEvent ->

            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }

                is SignUpChannel.OnProfileUpload -> {
                    signUpViewModel.onEvent(SignUpUIEvent.OnImageUploadSuccess)
                }

                is SignUpChannel.OnNextScreen -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                    onNext()
                }

                is SignUpChannel.OnOTPScreen -> {
                    scope.launch { modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded) }
                }

                is SignUpChannel.OnSuccess -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            ConfirmPhoneScreen(
                onDismiss = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                phoneNumber = state.phoneNumber
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.size_16dp),
            topEnd = dimensionResource(id = R.dimen.size_16dp)
        ),
        sheetBackgroundColor = colorResource(id = R.color.white),
    ) {

        Box(
            Modifier
                .fillMaxWidth()

        ) {
            CoachFlowBackground()


            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                    signUpViewModel.onEvent(SignUpUIEvent.OnImageSelected(uri.toString()))
                }

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
                AppText(
                    text = stringResource(id = R.string.set_your_profile),
                    style = MaterialTheme.typography.h3,
                    color = ColorBWBlack,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))

                        Box(
                            Modifier
                                .width(dimensionResource(id = R.dimen.size_200dp))
                                .height(dimensionResource(id = R.dimen.size_200dp))
                                .clip(shape = CircleShape)
                                .background(color = ColorPrimaryTransparent)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                                .align(Alignment.CenterHorizontally),

                            contentAlignment = Alignment.Center

                        ) {
                            Row(modifier = Modifier.align(Alignment.Center)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_camera),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.size_35dp))
                                        .height(dimensionResource(id = R.dimen.size_35dp))
                                )
                            }
                            state.profileImageUri.let {
                                Image(
                                    painter = rememberImagePainter(data = Uri.parse(it)),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.size_300dp))
                                        .clip(CircleShape)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.firstName,
                            onValueChange = {
                                signUpViewModel.onEvent(
                                    SignUpUIEvent.OnFirstNameChanged(
                                        it
                                    )
                                )
                            },
                            stringResource(id = R.string.first_name),
                            isError = !validName(state.firstName) && state.firstName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_first_name)
                        )

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.lastName,
                            onValueChange = {
                                signUpViewModel.onEvent(SignUpUIEvent.OnLastNameChanged(it))
                            },
                            stringResource(id = R.string.last_name),
                            isError = !validName(state.lastName) && state.lastName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_last_name)
                        )
                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.email,
                            onValueChange = {
                                signUpViewModel.onEvent(SignUpUIEvent.OnEmailChanged(it))

                            },
                            stringResource(id = R.string.email),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = (!state.email.isValidEmail() && state.email.isNotEmpty()),
                            errorMessage = stringResource(id = R.string.email_error)
                        )
                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.phoneNumber,
                            onValueChange = {
                                signUpViewModel.onEvent(SignUpUIEvent.OnPhoneNumberChanged(it))
                            },
                            stringResource(id = R.string.phone_num),
                            KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = (!validPhoneNumber(state.phoneNumber) && state.phoneNumber.isNotEmpty()),
                            errorMessage = stringResource(id = R.string.valid_phone_number),
                            enabled = !verified
                        )

                        if (validPhoneNumber(state.phoneNumber) && !verified) {

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                AppText(
                                    text = stringResource(id = R.string.verify),
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    modifier = Modifier
                                        .padding(end = dimensionResource(id = R.dimen.size_20dp))
                                        .clickable {
                                            scope.launch {
                                                signUpViewModel.onEvent(
                                                    SignUpUIEvent.OnVerifyNumber
                                                )
                                            }
                                        },
                                    textAlign = TextAlign.End
                                )
                            }
                        }

                        if (verified) {
                            AppText(
                                text = stringResource(id = R.string.verified),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = dimensionResource(id = R.dimen.size_20dp)),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

                BottomButtons(
                    onBackClick = { onBack() },
                    onNextClick = {

                        val signUpDataRequest = SignUpData(
                            phoneVerified = verified,
                            gender = signUpData?.gender,
                            birthdate = signUpData?.birthdate,
                            role = signUpData?.role,
                            password = signUpData?.password,
                            repeatPassword = signUpData?.repeatPassword,
                            address = signUpData?.address
                        )
                        signUpViewModel.onEvent(SignUpUIEvent.OnSignUpDataSelected(signUpDataRequest))
                        signUpViewModel.onEvent(SignUpUIEvent.OnScreenNext)
                    },

                    enableState = validName(state.firstName)
                            && validName(state.lastName)
                            && validPhoneNumber(state.phoneNumber)
                            && state.email.isValidEmail() && state.profileImageUri != null && verified,
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next)
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
            }
        }
    }
}



