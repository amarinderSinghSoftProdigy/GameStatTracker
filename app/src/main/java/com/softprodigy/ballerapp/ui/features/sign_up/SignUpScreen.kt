package com.softprodigy.ballerapp.ui.features.sign_up


import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.LocalFacebookCallbackManager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.*
import com.softprodigy.ballerapp.data.SocialUserModel
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.spacing
import timber.log.Timber
import java.util.*

@Composable
fun SignUpScreen(
    onPreviousClick: () -> Unit,
    vm: SignUpViewModel,
    onLoginScreen: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onLoginSuccess: (UserInfo) -> Unit,
    onTwitterClick: () -> Unit,
    twitterUser: SocialUserModel
) {

    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }

    var gender by rememberSaveable {
        mutableStateOf("")
    }
    var birthday by rememberSaveable {
        mutableStateOf("")
    }
    var address by rememberSaveable {
        mutableStateOf("")
    }
    val confirmPassword = rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisibility by rememberSaveable { mutableStateOf(false) }

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

    val mDate = remember { mutableStateOf("") }

    /*  Declaring DatePickerDialog and setting
      initial values as current values (present year, month and day)*/

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            birthday = "$mYear-${mMonth + 1}-$mDayOfMonth"
        }, mYear, mMonth, mDay
    )

    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var expanded by remember { mutableStateOf(false) }

    val genderList =
        listOf(stringResource(id = R.string.male), stringResource(id = R.string.female))

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val callbackManager = LocalFacebookCallbackManager.current

    val state = vm.signUpUiState.value
    DisposableEffect(Unit) {

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {

                    CustomFBManager.getFacebookUserProfile(result.accessToken, object :
                        FacebookUserProfile {
                        override fun onFacebookUserFetch(fbUser: SocialUserModel) {
                            Timber.i("FacebookUserModel-- $fbUser")
                            vm.onEvent(SignUpUIEvent.OnFacebookClick(fbUser))

                        }
                    })
                }

                override fun onCancel() {
                    println("onCancel")
                }

                override fun onError(error: FacebookException) {
                    println("onError $error")
                }
            }
        )
        onDispose {
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }
    LaunchedEffect(key1 = twitterUser) {
        twitterUser.id?.let {
            if (it.isNotEmpty())
                vm.onEvent(SignUpUIEvent.OnTwitterClick(twitterUser))
        }
    }
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    val googleUser = SocialUserModel(
                        email = gsa.email,
                        name = gsa.displayName,
                        id = gsa.id,
                        token = gsa.idToken
                    )
                    vm.onEvent(SignUpUIEvent.OnGoogleClick(googleUser))
                } else {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: ApiException) {
                Timber.i(e.toString())
            }

        }

    LaunchedEffect(key1 = Unit) {
        vm.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnLoginSuccess -> {
                    onLoginSuccess.invoke(uiEvent.loginResponse)
                }
                SignUpChannel.OnSignUpSelected -> {
                    onSignUpSuccess.invoke()
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_5dp), top = dimensionResource(
                        id = R.dimen.size_5dp
                    )
                )
                .clickable {
                    onPreviousClick()
                },
            alignment = Alignment.TopStart
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_60dp)))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_95dp))
                    .width(dimensionResource(id = R.dimen.size_160dp)),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_60dp)))

            AppText(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = email,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    email = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_your_email),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                isError = (!email.isValidEmail() && email.isNotEmpty() || email.length > 45),
                errorMessage = stringResource(id = R.string.email_error),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.address),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = address,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    address = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.your_address),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        textAlign = TextAlign.Center
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                errorMessage = stringResource(id = R.string.address_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                isError = (address.isNotEmpty() && address.length <= 4),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.gender),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),

                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clickable {
                            expanded = !expanded
                        }
                        .onGloballyPositioned {
                            textFieldSize = it.size.toSize()
                        },
                    border = BorderStroke(
                        0.5.dp,
                        MaterialTheme.appColors.editField.borderUnFocused
                    ),
                    backgroundColor = MaterialTheme.appColors.material.background,
                    shape = RoundedCornerShape(MaterialTheme.spacing.small),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 13.dp, end = 13.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (gender.isEmpty()) {
                            AppText(
                                text = stringResource(id = R.string.select_gender),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.appColors.textField.label,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                            )
                        } else {
                            AppText(
                                text = gender,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                style = androidx.compose.material.LocalTextStyle.current
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                        .background(MaterialTheme.colors.background)
                ) {
                    genderList.forEach { label ->
                        DropdownMenuItem(onClick = {
                            gender = label
                            expanded = false
                        }) {
                            Text(text = label, textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.birthdate),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineDateField(
                value = stringResource(id = R.string.select_birthdate),
                data = birthday,
                onClick = { mDatePickerDialog.show() })

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = password,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.your_password),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.Words
                ),
                isError = (!password.isValidPassword() && password.isNotEmpty()),
                errorMessage = stringResource(id = R.string.password_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility

                    }) {
                        Icon(
                            imageVector = if (passwordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.confirm_password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),

                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = confirmPassword.value,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    confirmPassword.value = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.confirm_your_password),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Password,
                ),
                isError = !password.passwordMatches(confirmPassword.value) && confirmPassword.value.isNotEmpty(),
                errorMessage = stringResource(id = R.string.confirm_password_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility

                    }) {
                        Icon(
                            imageVector = if (confirmPasswordVisibility)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff, ""
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = email.isValidEmail() && birthday.isNotEmpty() && confirmPassword.value.passwordMatches(
                    password
                ) && address.isNotEmpty() && gender.isNotEmpty() && password.isNotEmpty() && confirmPassword.value.isNotEmpty(),
                onClick = {
                    val signUpData = SignUpData(
                        email = email,
                        address = address,
                        birthdate = birthday,
                        gender = gender,
                        password = password,
                        repeatPassword = confirmPassword.value
                    )
                    vm.onEvent(SignUpUIEvent.OnSignUpDataSelected(signUpData = signUpData))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp)),
                text = stringResource(id = R.string.sign_up),
                icon = painterResource(id = R.drawable.ic_circle_next),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            val annotatedText = buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    )
                ) {
                    append(stringResource(id = R.string.already_registered))
                }
                append(" ")
                pushStringAnnotation(
                    tag = stringResource(id = R.string.signin),
                    annotation = stringResource(id = R.string.signin)
                )
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(stringResource(id = R.string.signin))
                }

                pop()

            }

            ClickableText(
                text = annotatedText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    annotatedText.getStringAnnotations(
                        tag = "SignIn",
                        start = it,
                        end = it
                    ).forEach { _ ->
                        onLoginScreen()
                    }
                },
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

            SocialLoginSection(
                headerText = stringResource(id = R.string.or_sign_up_with),
                onGoogleClick = {
                    authResultLauncher.launch(RequestCode.GOOGLE_ACCESS)
                },
                onFacebookClick = {
                    LoginManager.getInstance()
                        .logInWithReadPermissions(
                            context as ActivityResultRegistryOwner,
                            callbackManager,
                            listOf(AppConstants.PUBLIC_PROFILE, AppConstants.EMAIL)
                        )
                }, onTwitterClick = onTwitterClick
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))

        }
    }
}


