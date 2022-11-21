package com.allballapp.android.ui.features.sign_up

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.android.TextLayout
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.allballapp.android.R
import com.allballapp.android.common.ComposeFileProvider
import com.allballapp.android.common.isValidEmail
import com.allballapp.android.common.validName
import com.allballapp.android.common.validPhoneNumber
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.confirm_phone.ConfirmPhoneScreen
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.theme.*
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("RememberReturnType")
@OptIn(
    ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ProfileSetUpScreen(
    isToAddProfile: Boolean = false,
    countryCode: String = "",
    mobileNumber: String = "",
    onNext: () -> Unit,
    onBack: () -> Unit,
    signUpViewModel: SignUpViewModel,
    moveToTermsAndConditions: (String) -> Unit,
    moveToPrivacyAndPolicy: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var currentBottomSheet: BottomSheetType? by remember {
        mutableStateOf(null)
    }

    BackHandler {
        signUpViewModel.onEvent(SignUpUIEvent.ClearPhoneField)
        onBack()
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

    LaunchedEffect(key1 = mobileNumber, key2 = countryCode) {

        if (mobileNumber.isNotEmpty()) {
            signUpViewModel.onEvent(
                SignUpUIEvent.OnPhoneNumberChanged(
                    mobileNumber
                )
            )
        }

        if (countryCode.isNotEmpty()) {
            signUpViewModel.onEvent(
                SignUpUIEvent.OnCountryCode(
                    countryCode
                )
            )
        }
    }
    LaunchedEffect(key1 = Unit) {

        signUpViewModel.signUpChannel.collect { uiEvent ->

            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }

                is SignUpChannel.OnProfileImageUpload -> {
                    signUpViewModel.onEvent(SignUpUIEvent.OnImageUploadSuccess(state.registered))
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
        CoachFlowBackground()
        {
            Box(
                Modifier
                    .fillMaxWidth()
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                    /*.verticalScroll(rememberScrollState())*/,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
                    AppText(
                        text = stringResource(id = R.string.set_your_profile),
                        style = MaterialTheme.typography.h3,
                        color = ColorBWBlack,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))

                    CompositionLocalProvider(
                        LocalOverScrollConfiguration provides null
                    ) {

                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                            UserFlowBackground(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White
                            ) {

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

                                    AppDivider()

                                    EditFields(
                                        data = state.signUpData.firstName,
                                        onValueChange = {
                                            if (it.length <= maxChar)
                                                signUpViewModel.onEvent(
                                                    SignUpUIEvent.OnFirstNameChanged(
                                                        it
                                                    )
                                                )
                                        },
                                        head = stringResource(id = R.string.first_name),
                                        isError = !validName(state.signUpData.firstName) && state.signUpData.firstName.isNotEmpty() || state.signUpData.firstName.length > 30,
                                        errorMessage = stringResource(id = R.string.valid_first_name),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.Sentences
                                        )
                                    )

                                    AppDivider()

                                    EditFields(
                                        state.signUpData.lastName,
                                        onValueChange = {
                                            if (it.length <= maxChar)
                                                signUpViewModel.onEvent(
                                                    SignUpUIEvent.OnLastNameChanged(
                                                        it
                                                    )
                                                )
                                        },
                                        stringResource(id = R.string.last_name),
                                        isError = !validName(state.signUpData.lastName) && state.signUpData.lastName.isNotEmpty() || state.signUpData.lastName.length > 30,
                                        errorMessage = stringResource(id = R.string.valid_last_name),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.Sentences
                                        ),
                                    )
                                    AppDivider()

                                    EditFields(
                                        data = state.signUpData.email ?: "",
                                        onValueChange = {
                                            signUpViewModel.onEvent(SignUpUIEvent.OnEmailChanged(it))
                                        },
                                        stringResource(id = R.string.email),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Done,
                                            capitalization = KeyboardCapitalization.Sentences
                                        ),
                                        keyboardActions = KeyboardActions(onDone = {
                                            keyboardController?.hideSoftwareKeyboard()
                                        }),
                                        isError = (state.signUpData.email
                                            ?: "").isNotEmpty() && state.signUpData.email?.isValidEmail() != true,
                                        errorMessage = stringResource(id = R.string.email_error),
                                        enabled = false
                                    )
                                    if (!isToAddProfile) {
                                        state.signUpData.token?.let { _ ->
                                            AppDivider()

                                            EditFields(
                                                state.signUpData.address,
                                                onValueChange = {
                                                    signUpViewModel.onEvent(
                                                        SignUpUIEvent.OnAddressChanged(
                                                            it
                                                        )
                                                    )
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
                                            AppDivider()

                                            Column {
                                                EditFields(
                                                    state.signUpData.gender,
                                                    onValueChange = {
                                                        signUpViewModel.onEvent(
                                                            SignUpUIEvent.OnGenderChange(
                                                                it
                                                            )
                                                        )
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
                                                            modifier = Modifier.clickable {
                                                                expanded = !expanded
                                                            })
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
                                                            Text(
                                                                text = label,
                                                                textAlign = TextAlign.Center
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            AppDivider()
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
                                                    Spacer(
                                                        modifier = Modifier.width(
                                                            dimensionResource(
                                                                id = R.dimen.size_5dp
                                                            )
                                                        )
                                                    )
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(
                                                            dimensionResource(
                                                                id = R.dimen.size_24dp
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        AppDivider()
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                AppText(
                                                    text = stringResource(id = R.string.phone_num),
                                                    style = MaterialTheme.typography.h6,
                                                    color = ColorBWBlack,
                                                    modifier = Modifier
                                                        .padding(start = dimensionResource(id = R.dimen.size_16dp))
                                                )
                                                Spacer(
                                                    modifier = Modifier.width(
                                                        dimensionResource(
                                                            id = R.dimen.size_10dp
                                                        )
                                                    )
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(IntrinsicSize.Min),
                                                    /* verticalAlignment = Alignment.CenterVertically,
                                                     horizontalArrangement = Arrangement.SpaceBetween*/
                                                ) {

                                                    val customTextSelectionColors =
                                                        TextSelectionColors(
                                                            handleColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                                            backgroundColor = Color.Transparent
                                                        )
                                                    CompositionLocalProvider(
                                                        LocalTextSelectionColors provides customTextSelectionColors
                                                    ) {
                                                        /* state.phoneCode = getDefaultPhoneCode*/
                                                        signUpViewModel.onEvent(
                                                            SignUpUIEvent.OnCountryCode(
                                                                getDefaultPhoneCode
                                                            )
                                                        )
                                                        TogiCountryCodePicker(
                                                            showCountryFlag = false,
                                                            pickedCountry = {
                                                                /*  state.phoneCode = it.countryPhoneCode*/
                                                                signUpViewModel.onEvent(
                                                                    SignUpUIEvent.OnCountryCode(
                                                                        it.countryCode
                                                                    )
                                                                )
                                                                defaultLang = it.countryCode
                                                            },
                                                            defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                                                            focusedBorderColor = Color.Transparent,
                                                            unfocusedBorderColor = Color.Transparent,
                                                            dialogAppBarTextColor = Color.Black,
                                                            dialogAppBarColor = Color.White,
                                                            error = true,
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
                                                            },
                                                            showDialog = false
                                                        )
                                                    }
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

                                    }

                                    /*if (validPhoneNumber(state.signUpData.phone) && !state.signUpData.phoneVerified) {

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
                                    }*/
                                    AppDivider()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        CustomCheckBox(
                                            state.signUpData.termsAndCondition
                                        ) {
                                            signUpViewModel.onEvent(
                                                SignUpUIEvent.OnTermsAndConditionChanged(
                                                    !state.signUpData.termsAndCondition
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

                                        val annotatedText = buildAnnotatedString {

                                            withStyle(
                                                style = SpanStyle(
                                                    color = md_theme_light_onSurface,
                                                )
                                            ) {
                                                append(stringResource(id = R.string.by_creating_an_account))
                                            }
                                            pushStringAnnotation(
                                                tag = stringResource(id = R.string.all_ball_terms_condition),
                                                annotation = stringResource(id = R.string.all_ball_terms_condition)
                                            )
                                            withStyle(
                                                style = SpanStyle(
                                                    color = ColorMainPrimary,
                                                )
                                            ) {
                                                append(stringResource(id = R.string.all_ball_terms_condition))
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    color = ColorMainPrimary,
                                                )
                                            ) {
                                                append(" ")
                                                append(stringResource(id = R.string.text_message_agreement))
                                            }
                                            pop()
                                            withStyle(
                                                style = SpanStyle(
                                                    color = md_theme_light_onSurface,
                                                )
                                            ) {
                                                append(" ")
                                                append(stringResource(id = R.string.authorizing_all_ball))
                                            }
                                            pushStringAnnotation(
                                                tag = stringResource(id = R.string.privacy_policy),
                                                annotation = stringResource(id = R.string.privacy_policy)
                                            )
                                            withStyle(
                                                style = SpanStyle(
                                                    color = ColorMainPrimary,
                                                )
                                            ) {
                                                append(" ")
                                                append(stringResource(id = R.string.privacy_policy))
                                            }
                                            pop()
                                            withStyle(
                                                style = SpanStyle(
                                                    color = md_theme_light_onSurface,
                                                )
                                            ) {
                                                append(" ")
                                                append(stringResource(id = R.string.including_child_online_privacy_protection))
                                            }
                                        }

                                        ClickableText(
                                            text = annotatedText,
                                            onClick = { offset ->
                                                annotatedText.getStringAnnotations(
                                                    tag = context.getString(R.string.all_ball_terms_condition),
                                                    start = offset,
                                                    end = offset
                                                ).firstOrNull()?.let {
                                                    moveToTermsAndConditions("")
                                                }
                                                annotatedText.getStringAnnotations(
                                                    tag = context.getString(R.string.privacy_policy),
                                                    start = offset,
                                                    end = offset
                                                ).firstOrNull()?.let {
                                                    moveToPrivacyAndPolicy("")
                                                }
                                            },
                                        )
                                    }

                                    AppDivider()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                        verticalAlignment = Alignment.Top,
                                    ) {
                                        CustomCheckBox(
                                            state.signUpData.privacyAndPolicy
                                        ) {
                                            signUpViewModel.onEvent(
                                                SignUpUIEvent.OnPrivacyAndPolicy(
                                                    !state.signUpData.privacyAndPolicy
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                                        val annotatedText = buildAnnotatedString {

                                            withStyle(
                                                style = SpanStyle(
                                                    color = md_theme_light_onSurface,
                                                )
                                            ) {
                                                append(stringResource(id = R.string.i_authorize_all_ball))
                                                append(" ")
                                            }
                                            pushStringAnnotation(
                                                tag = stringResource(id = R.string.all_ball_privacy_policy),
                                                annotation = stringResource(id = R.string.all_ball_privacy_policy)
                                            )
                                            withStyle(
                                                style = SpanStyle(
                                                    color = ColorMainPrimary,
                                                )
                                            ) {
                                                append(stringResource(id = R.string.all_ball_privacy_policy))
                                            }
                                            pop()
                                        }
                                        ClickableText(
                                            text = annotatedText,
                                            onClick = { offset ->
                                                annotatedText.getStringAnnotations(
                                                    tag = context.getString(R.string.all_ball_privacy_policy),
                                                    start = offset,
                                                    end = offset
                                                ).firstOrNull()?.let {
                                                    moveToPrivacyAndPolicy("")
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

                            /* val check = remember {
                                 mutableStateOf(true)
                             }*//*state.signUpData.token?.let {
                        state.signUpData.birthdate.isNotEmpty()
                                && state.signUpData.address.isNotEmpty()
                                && state.signUpData.gender.isNotEmpty()
                    } ?: true*/
                            BottomButtons(
                                onBackClick = {
                                    signUpViewModel.onEvent(SignUpUIEvent.ClearPhoneField)
                                    onBack()
                                },
                                onNextClick = {
                                    if (state.signUpData.profileImageUri.isNullOrEmpty()) {
                                        signUpViewModel.onEvent(
                                            SignUpUIEvent.ShowToast(
                                                context.getString(
                                                    R.string.please_select_profile
                                                )
                                            )
                                        )
                                    } else {
                                        if (state.registered) {
                                            signUpViewModel.onEvent(SignUpUIEvent.OnAddProfile)
                                        } else {
                                            signUpViewModel.onEvent(SignUpUIEvent.OnScreenNext)
                                        }
                                    }

                                    /*check.value = false*/
                                },
                                enableState = !state.isLoading && validName(state.signUpData.firstName)
                                        && validName(state.signUpData.lastName) && state.signUpData.termsAndCondition
                                        && state.signUpData.privacyAndPolicy && state.signUpData.profileImageUri != null
                                        && state.signUpData.email != null && state.signUpData.email?.isValidEmail() == true
                                        && if (isToAddProfile) true else validPhoneNumber(state.signUpData.phone)
                                /*&& check.value*/,
                                //&& (state.signUpData.email ?: "".isValidEmail()) != true
                                //&& state.signUpData.profileImageUri != null
                                //&& state.signUpData.phoneVerified
                                //&& check,
                                firstText = stringResource(id = R.string.back),
                                secondText = stringResource(id = R.string.next)
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                        }
                    }
                }
            }
        }
    }
    if (state.isLoading) {
        CommonProgressBar()
    }
}

enum class BottomSheetType {
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