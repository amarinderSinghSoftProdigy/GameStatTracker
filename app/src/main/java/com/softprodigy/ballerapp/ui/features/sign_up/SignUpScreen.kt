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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.api.ApiException
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.isValidFullName
import com.softprodigy.ballerapp.common.isValidPassword
import com.softprodigy.ballerapp.common.passwordMatches
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.GoogleUserModel

import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineDateField
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SocialLoginSection
import com.softprodigy.ballerapp.ui.features.login.LoginUIEvent

import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.spacing
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.TogiRoundedPicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import timber.log.Timber
import java.util.*

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit) {
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
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val getDefaultPhoneCode = getDefaultPhoneCode(context)
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode) }

    var verifyPhoneCode by rememberSaveable {
        mutableStateOf(getDefaultPhoneCode)
    }
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
    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp))
                .verticalScroll(rememberScrollState()),
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
                modifier = Modifier.fillMaxWidth(),
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
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                isError = (!email.isValidEmail() && email.isNotEmpty()),
                errorMessage = stringResource(id = R.string.email_error),
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.address),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = address,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_100dp)),
                onValueChange = {
                    address = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.your_address),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                errorMessage = stringResource(id = R.string.address_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                singleLine = false,
                maxLines = 5,
                isError = (address.isNotEmpty() && address.length <= 4),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            AppText(
                text = stringResource(id = R.string.gender),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = gender,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    gender = it
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.select_gender),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                singleLine = false,
                maxLines = 5,
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.birthdate),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.fillMaxWidth(),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                modifier = Modifier.fillMaxWidth(),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = email.isValidEmail() && birthday.isNotEmpty() && validPhoneNumber(
                    phoneNumber.value
                ) && confirmPassword.value.passwordMatches(password) && address.isNotEmpty(),
                onClick = {
                    onSignUpSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp)),
                text = stringResource(id = R.string.create_now),
                icon = painterResource(id = R.drawable.ic_circle_next)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        }
    }
}

