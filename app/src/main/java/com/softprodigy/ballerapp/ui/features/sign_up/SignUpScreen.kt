package com.softprodigy.ballerapp.ui.features.sign_up

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidFullName
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.common.passwordMatches
import com.softprodigy.ballerapp.data.GoogleUserModel
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.data.response.SignUpResponse
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineDateField
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SocialLoginSection
import com.softprodigy.ballerapp.ui.features.login.GoogleApiContract
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.TogiRoundedPicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import timber.log.Timber
import java.util.*

@Composable
fun SignUpScreen(
    vm: SignupViewModel = hiltViewModel(),
    onSuccessfulSignUp: (SignUpResponse) -> Unit,
    onGoogleClick: (LoginResponse) -> Unit,
    onFacebookClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }

    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable {
        mutableStateOf("")
    }
    var birthday by rememberSaveable {
        mutableStateOf("Enter your Birthday")
    }
    var address by rememberSaveable {
        mutableStateOf("")
    }
    var mobileNumber by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val getDefaultPhoneCode = getDefaultPhoneCode(context)
    var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode) }

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

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            birthday = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    val googleUser = GoogleUserModel(
                        email = gsa.email,
                        name = gsa.displayName,
                        id = gsa.id,
                        token = gsa.idToken
                    )
                    vm.onEvent(SignUpUIEvent.OnGoogleClick(googleUser))
                }
            } catch (e: ApiException) {
                Timber.i(e.toString())
            }
        }

    LaunchedEffect(key1 = true) {
        vm.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.OnLoginSuccess -> {
                    onGoogleClick.invoke(uiEvent.loginResponse)
                }
                is SignUpChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is SignUpChannel.OnSignUpSuccess -> {
                    onSuccessfulSignUp.invoke(uiEvent.signUpResponse)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .height(95.dp)
                    .width(160.dp),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

            AppText(
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                onValueChange = {
                    name = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_your_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = (!name.isValidFullName() && name.isNotEmpty()),
                errorMessage = stringResource(id = R.string.enter_valid_full_name),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),

                )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = email,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                onValueChange = {
                    email = it
                },
                placeholder = { Text(text = stringResource(id = R.string.email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                isError = (!email.isValidEmail() && email.length >= 6),
                errorMessage = stringResource(id = R.string.email_error),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.birthday),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineDateField(value = birthday, onClick = { mDatePickerDialog.show() })

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.address),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = address,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color.White),
                onValueChange = {
                    address = it
                },
                placeholder = { Text(text = stringResource(id = R.string.your_address)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                errorMessage = stringResource(id = R.string.address_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = false,
                maxLines = 5,
                isError = (address.isNotEmpty() && address.length <= 4),
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.number),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            TogiRoundedPicker(
                pickedCountry = {
                    phoneCode = it.countryPhoneCode
                    defaultLang = it.countryCode
                },
                defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                focusedBorderColor = Color.LightGray,
                dialogAppBarTextColor = Color.Black,
                dialogAppBarColor = Color.White,
                value = phoneNumber.value,
                error = true,
                onValueChange = { phoneNumber.value = it },
                rowPadding = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                unFocusedBorderColor = Color.LightGray,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = password,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                onValueChange = {
                    password = it
                },
                placeholder = { Text(text = stringResource(id = R.string.your_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = (!password.isValidPassword() && password.length >= 4),
                errorMessage = stringResource(id = R.string.password_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppText(
                text = stringResource(id = R.string.confirm_password),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            AppOutlineTextField(
                value = confirmPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                onValueChange = {
                    confirmPassword = it
                },
                placeholder = { Text(text = stringResource(id = R.string.re_enter_password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = (!confirmPassword.isValidPassword() && confirmPassword.length >= 4 && confirmPassword.passwordMatches(
                    password
                )),
                errorMessage = stringResource(id = R.string.confirm_password_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

            AppButton(
                enabled = email.isValidEmail() && password.isValidPassword() && confirmPassword.passwordMatches(
                    password
                ) && birthday.isNotEmpty() && phoneNumber.value.isNotEmpty() && address.isNotEmpty(),
                onClick = {
                    onLoginClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = stringResource(id = R.string.create_now),
                icon = painterResource(id = R.drawable.ic_circle_next)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

        }
    }
}

@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "rectangle", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    BallerAppTheme {
        Surface {
//            SignUpScreen()
        }
    }
}