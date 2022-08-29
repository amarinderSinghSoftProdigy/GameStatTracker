package com.softprodigy.ballerapp.ui.features.sign_up

import android.graphics.Bitmap
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
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.softprodigy.ballerapp.data.request.GlobalRequest
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.EditFields
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.confirm_phone.ConfirmPhoneScreen
import com.softprodigy.ballerapp.ui.features.confirm_phone.ConfirmPhoneViewModel
import com.softprodigy.ballerapp.ui.features.confirm_phone.VerifyPhoneChannel
import com.softprodigy.ballerapp.ui.features.confirm_phone.VerifyPhoneUIEvent
import com.softprodigy.ballerapp.ui.features.user_type.UserTypeViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileSetUpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: ConfirmPhoneViewModel = hiltViewModel()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val phoneNumber = remember {
        mutableStateOf(viewModel.profileData.phoneNumber)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is VerifyPhoneChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                else -> Unit
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
                phoneNumber = phoneNumber.value
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

            var imageUri by remember(false) {
                mutableStateOf<Uri?>(viewModel.profileData.image)
            }
            val context = LocalContext.current
            val bitmap = remember(false) {
                mutableStateOf<Bitmap?>(null)
            }

            val fName = remember {
                mutableStateOf(viewModel.profileData.fName)
            }
            val lName = remember {
                mutableStateOf(viewModel.profileData.lName)
            }
            val email = remember {
                mutableStateOf(viewModel.profileData.email)
            }


            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                    imageUri = uri
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
                            imageUri?.let {
                                Image(
                                    painter = rememberImagePainter(data = Uri.parse(it.toString())),
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
                            fName,
                            stringResource(id = R.string.first_name),
                            isError = !validName(fName.value) && fName.value.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_first_name)
                        )

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            lName,
                            stringResource(id = R.string.last_name),
                            isError = !validName(lName.value) && lName.value.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_last_name)
                        )
                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            email,
                            stringResource(id = R.string.email),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = (!email.value.isValidEmail() && email.value.isNotEmpty()),
                            errorMessage = stringResource(id = R.string.email_error)
                        )
                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            phoneNumber,
                            stringResource(id = R.string.phone_num),
                            KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = (!validPhoneNumber(phoneNumber.value) && phoneNumber.value.isNotEmpty()),
                            errorMessage = stringResource(id = R.string.valid_phone_number)
                        )

                        if (validPhoneNumber(phoneNumber.value)) {

                            AppText(
                                text = stringResource(id = R.string.verify),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = dimensionResource(id = R.dimen.size_20dp))
                                    .clickable {
                                        scope.launch {
                                            viewModel.onEvent(VerifyPhoneUIEvent.Submit("+" + phoneNumber.value))
                                            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                        }
                                    },
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

                BottomButtons(
                    onBackClick = { onBack() },
                    onNextClick = {
                        onNext()
                        val request =
                            GlobalRequest.SetupProfile(
                                fName = fName.value,
                                lName = lName.value,
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                image = imageUri
                            )
                        viewModel.saveProfileData(request = request)
                    },
                    enableState = validName(fName.value)
                            && validName(lName.value)
                            && validPhoneNumber(phoneNumber.value)
                            && email.value.isValidEmail() && imageUri != null,
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next)
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            }
        }
    }
}



