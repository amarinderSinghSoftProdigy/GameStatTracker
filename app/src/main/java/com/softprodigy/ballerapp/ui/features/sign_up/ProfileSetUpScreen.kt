package com.softprodigy.ballerapp.ui.features.sign_up

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.ComposeFileProvider
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.confirm_phone.ConfirmPhoneScreen
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import kotlinx.coroutines.launch
import java.util.*


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileSetUpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    signUpViewModel: SignUpViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var currentBottomSheet: BottomSheetType? by remember {
        mutableStateOf(null)
    }

    val maxChar = 30
    val maxEmailChar = 45
    val maxPhoneNumber = 10

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
            uri?.let {
                signUpViewModel.onEvent(SignUpUIEvent.OnImageSelected(imageUri.toString()))
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success)
                signUpViewModel.onEvent(SignUpUIEvent.OnImageSelected(imageUri.toString()))
        }
    )

    val scope = rememberCoroutineScope()
    val state = signUpViewModel.signUpUiState.value
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val context = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
    mCalendar.time = Date()

    val focus = LocalFocusManager.current

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            signUpViewModel.onEvent(SignUpUIEvent.OnBirthdayChanged("$year-${month + 1}-$dayOfMonth"))
        }, mYear, mMonth, mDay
    )
    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    //val phoneNumber = rememberSaveable { mutableStateOf("") }
    val getDefaultPhoneCode = getDefaultPhoneCode(context)
    //var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode) }

    val genderList =
        listOf(stringResource(id = R.string.male), stringResource(id = R.string.female))


    LaunchedEffect(key1 = Unit) {

        signUpViewModel.signUpChannel.collect { uiEvent ->

            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }

                is SignUpChannel.OnProfileImageUpload -> {
                    signUpViewModel.onEvent(SignUpUIEvent.OnImageUploadSuccess)
                }

                is SignUpChannel.OnProfileUpdateSuccess -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                    onNext()
                }

                is SignUpChannel.OnOTPScreen -> {
                    currentBottomSheet = BottomSheetType.OTP
                    scope.launch {
                        modalBottomSheetState.animateTo(
                            ModalBottomSheetValue.Expanded
                        )
                    }
                }

                is SignUpChannel.OnSuccess -> {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
                else -> Unit
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {

            Spacer(modifier = Modifier.height(1.dp))

            currentBottomSheet?.let { bottomSheetType ->
                /* sheet content */
                SheetLayout(
                    bottomSheetType = bottomSheetType,
                    onDismiss = {
                        scope.launch { modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden) }
                    },
                    imagePickerTitle = stringResource(id = R.string.select_image_from),
                    onCameraClick = {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }, onGalleryCLick = {
                        imagePicker.launch("image/*")
                    },
                    phone = state.signUpData.phone,
                    signUpViewModel = signUpViewModel
                )
            }
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
                                    keyboardController?.hide()
                                    currentBottomSheet = BottomSheetType.IMAGE_PICKER
                                    scope.launch {
                                        modalBottomSheetState.animateTo(
                                            ModalBottomSheetValue.Expanded
                                        )
                                    }

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
                            state.signUpData.profileImageUri?.let {
                                CoilImage(
                                    src = Uri.parse(it),
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.size_300dp))
                                        .background(
                                            color = MaterialTheme.appColors.material.onSurface,
                                            CircleShape
                                        )
                                        .align(Alignment.Center),
                                    isCrossFadeEnabled = false,
                                    onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                                    onError = { Placeholder(R.drawable.ic_profile_placeholder) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.signUpData.firstName,
                            onValueChange = {
                                if (it.length <= maxChar)
                                    signUpViewModel.onEvent(
                                        SignUpUIEvent.OnFirstNameChanged(
                                            it
                                        )
                                    )
                            },
                            stringResource(id = R.string.first_name),
                            isError = !validName(state.signUpData.firstName) && state.signUpData.firstName.isNotEmpty() || state.signUpData.firstName.length > 30,
                            errorMessage = stringResource(id = R.string.valid_first_name),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            state.signUpData.lastName,
                            onValueChange = {
                                if (it.length <= maxChar)
                                    signUpViewModel.onEvent(SignUpUIEvent.OnLastNameChanged(it))
                            },
                            stringResource(id = R.string.last_name),
                            isError = !validName(state.signUpData.lastName) && state.signUpData.lastName.isNotEmpty() || state.signUpData.lastName.length > 30,
                            errorMessage = stringResource(id = R.string.valid_last_name),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            )

                        )
                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        EditFields(
                            data = state.signUpData.email ?: "",
                            onValueChange = {
                                if (it.length <= maxEmailChar)
                                    signUpViewModel.onEvent(SignUpUIEvent.OnEmailChanged(it))
                            },
                            stringResource(id = R.string.email),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            isError = ((state.signUpData.email?.isValidEmail() != true
                                    || ((state.signUpData.email?.length) ?: 0) > 45)),
                            errorMessage = stringResource(id = R.string.email_error),
                            enabled = true
                        )
                        state.signUpData.token?.let { _ ->
                            Divider(thickness = dimensionResource(id = R.dimen.divider))

                            EditFields(
                                state.signUpData.address,
                                onValueChange = {
                                    signUpViewModel.onEvent(SignUpUIEvent.OnAddressChanged(it))
                                },
                                stringResource(id = R.string.address),
                                KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                                isError = (state.signUpData.address.isNotEmpty() && state.signUpData.address.length <= 4),
                                errorMessage = stringResource(id = R.string.address_error),
                            )
                            Divider(thickness = dimensionResource(id = R.dimen.divider))

                            Column {
                                EditFields(
                                    state.signUpData.gender,
                                    onValueChange = {
                                        signUpViewModel.onEvent(SignUpUIEvent.OnGenderChange(it))
                                    },
                                    stringResource(id = R.string.gender),
                                    KeyboardOptions(
                                        imeAction = ImeAction.Next,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    modifier = Modifier.onGloballyPositioned {
                                        textFieldSize = it.size.toSize()
                                    },
                                    trailingIcon = {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            modifier = Modifier.clickable { expanded = !expanded })
                                    },
                                    enabled = true
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    genderList.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            signUpViewModel.onEvent(
                                                SignUpUIEvent.OnGenderChange(
                                                    label
                                                )
                                            )
                                            expanded = false
                                        }) {
                                            Text(text = label, textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                            }
                            Divider(thickness = dimensionResource(id = R.dimen.divider))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = dimensionResource(id = R.dimen.size_16dp),
                                        end = dimensionResource(id = R.dimen.size_16dp)
                                    )
                                    .height(dimensionResource(id = R.dimen.size_56dp))
                            ) {
                                AppText(
                                    text = stringResource(id = R.string.birthdate),
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                                    textAlign = TextAlign.Start
                                )
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clickable { mDatePickerDialog.show() },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = state.signUpData.birthdate,
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp))
                                    )
                                }
                            }
                        }

                        Divider(thickness = dimensionResource(id = R.dimen.divider))

                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.size_56dp)),
                                /* verticalAlignment = Alignment.CenterVertically,
                                 horizontalArrangement = Arrangement.SpaceBetween*/
                            ) {

                                val customTextSelectionColors = TextSelectionColors(
                                    handleColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    backgroundColor = Color.Transparent
                                )
                                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                                    /* state.phoneCode = getDefaultPhoneCode*/
                                    signUpViewModel.onEvent(
                                        SignUpUIEvent.OnCountryCode(
                                            getDefaultPhoneCode
                                        )
                                    )
                                    TogiCountryCodePicker(
                                        pickedCountry = {
                                            /*  state.phoneCode = it.countryPhoneCode*/
                                            signUpViewModel.onEvent(SignUpUIEvent.OnCountryCode(it.countryCode))
                                            defaultLang = it.countryCode
                                        },
                                        defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        dialogAppBarTextColor = Color.Black,
                                        dialogAppBarColor = Color.White,
                                        error = state.signUpData.phone.length > 10,
                                        text = state.signUpData.phone,
                                        onValueChange = {
                                            if (it.length <= maxPhoneNumber)
                                                signUpViewModel.onEvent(
                                                    SignUpUIEvent.OnPhoneNumberChanged(
                                                        it
                                                    )
                                                )
                                        },
                                        readOnly = state.signUpData.phoneVerified,
                                        cursorColor = Color.Black,
                                        content = {
                                            AppText(
                                                text = stringResource(id = R.string.phone_num),
                                                style = MaterialTheme.typography.h6,
                                                color = ColorBWBlack,
                                                modifier = Modifier
                                                    .align(Alignment.CenterStart)
                                                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                                            )
                                        }
                                    )
                                }
                            }

                            if ((!validPhoneNumber(state.signUpData.phone) && state.signUpData.phone.isNotEmpty())) {
                                Text(
                                    text = stringResource(id = R.string.valid_phone_number),
                                    color = MaterialTheme.colors.error,
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                        if (validPhoneNumber(state.signUpData.phone) && !state.signUpData.phoneVerified) {

                            Column(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clickable {
                                        scope.launch {
                                            signUpViewModel.onEvent(
                                                SignUpUIEvent.OnVerifyNumber
                                            )
                                            focus.clearFocus()
                                        }
                                    },
                                horizontalAlignment = Alignment.End
                            ) {
                                AppText(
                                    text = stringResource(id = R.string.verify),
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_20dp)),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                        if (state.signUpData.phoneVerified) {
                            AppText(
                                text = stringResource(id = R.string.verified),
                                style = MaterialTheme.typography.h6,
                                color = Color.Green,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = dimensionResource(id = R.dimen.size_20dp)),
                                textAlign = TextAlign.End
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

                val check = state.signUpData.token?.let {
                    state.signUpData.birthdate.isNotEmpty()
                            && state.signUpData.address.isNotEmpty()
                            && state.signUpData.gender.isNotEmpty()
                } ?: true
                BottomButtons(
                    onBackClick = { onBack() },
                    onNextClick = {
                        if (state.registered) {
                            onNext()
                        } else {
                            signUpViewModel.onEvent(SignUpUIEvent.OnScreenNext)
                        }
                    },
                    enableState = validName(state.signUpData.firstName)
                            && validName(state.signUpData.lastName)
                            && validPhoneNumber(state.signUpData.phone)
                            && (state.signUpData.email ?: "".isValidEmail()) != true
                            && state.signUpData.profileImageUri != null
                            && state.signUpData.phoneVerified && check,
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

enum class BottomSheetType() {
    OTP, IMAGE_PICKER
}

@Composable
fun SheetLayout(
    bottomSheetType: BottomSheetType,
    onDismiss: () -> Unit,
    phone: String,
    signUpViewModel: SignUpViewModel,
    imagePickerTitle: String,
    onCameraClick: () -> Unit,
    onGalleryCLick: () -> Unit,
) {

    when (bottomSheetType) {
        BottomSheetType.IMAGE_PICKER ->
            ImagePickerBottomSheet(
                title = imagePickerTitle,
                onCameraClick = onCameraClick,
                onGalleryClick = onGalleryCLick,
                onDismiss = onDismiss,
            )

        BottomSheetType.OTP ->
            ConfirmPhoneScreen(
                phoneNumber = phone,
                viewModel = signUpViewModel,
                onDismiss = onDismiss,
            )

    }
}