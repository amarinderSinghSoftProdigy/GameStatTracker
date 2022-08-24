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
    val confirmNumber = rememberSaveable {
        mutableStateOf("")
    }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val getDefaultPhoneCode = getDefaultPhoneCode(context)
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
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineTextField(
                value = name,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    name = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_full_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError = (!name.isValidFullName() && name.isNotEmpty()),
                errorMessage = stringResource(id = R.string.enter_valid_full_name),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

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
                placeholder = { Text(text = stringResource(id = R.string.enter_your_email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
                isError = (!email.isValidEmail() && email.isNotEmpty()),
                errorMessage = stringResource(id = R.string.email_error),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.birthday),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppOutlineDateField(value = birthday, onClick = { mDatePickerDialog.show() })

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
                placeholder = { Text(text = stringResource(id = R.string.your_address)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                errorMessage = stringResource(id = R.string.address_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
                singleLine = false,
                maxLines = 5,
                isError = (address.isNotEmpty() && address.length <= 4),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.number),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            /*  TogiRoundedPicker(
                  pickedCountry = {
                      phoneCode = it.countryPhoneCode
                      defaultLang = it.countryCode
                  },
                  defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                  focusedBorderColor = if (!validPhoneNumber(phoneCode + phoneNumber.value) && phoneNumber.value.length >= 5)
                      MaterialTheme.colors.error
                  else
                      MaterialTheme.appColors.editField.borderFocused,
                  dialogAppBarTextColor = Color.Black,
                  value = phoneNumber.value,
                  error = true,
                  onValueChange = { phoneNumber.value = it },
                  rowPadding = Modifier.fillMaxWidth(),
                  modifier = Modifier
                      .fillMaxWidth()
                      .background(
                          color = MaterialTheme.appColors.material.background,
                          shape = RoundedCornerShape(8.dp)
                      ),
                  unFocusedBorderColor = if (!validPhoneNumber(phoneCode + phoneNumber.value) && phoneNumber.value.length >= 5)
                      MaterialTheme.colors.error
                  else
                      MaterialTheme.appColors.editField.borderUnFocused,
                  color = Color.Transparent,
                  shape = RoundedCornerShape(8.dp)
              )*/

            AppOutlineTextField(
                value = phoneNumber.value,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    phoneNumber.value = it
                },
                placeholder = { Text(text = stringResource(id = R.string.enter_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = stringResource(id = R.string.valid_phone_number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
                singleLine = true,
                isError = (!validPhoneNumber(phoneNumber.value) && phoneNumber.value.isNotEmpty()),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            AppText(
                text = stringResource(id = R.string.verify_number),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            /*TogiRoundedPicker(
                pickedCountry = {
                    verifyPhoneCode = it.countryPhoneCode
                    defaultLang = it.countryCode
                },
                defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                focusedBorderColor = if (!(phoneCode + phoneNumber.value).passwordMatches(
                        verifyPhoneCode + confirmNumber.value
                    )
                )
                    MaterialTheme.colors.error
                else
                    MaterialTheme.appColors.editField.borderFocused,
                dialogAppBarTextColor = Color.Black,
                value = confirmNumber.value,
                onValueChange = {
                    confirmNumber.value = it

                },
                rowPadding = Modifier.fillMaxWidth(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.appColors.material.background,
                        shape = RoundedCornerShape(8.dp)
                    ),
                unFocusedBorderColor = if (!(phoneCode + phoneNumber.value).passwordMatches(
                        verifyPhoneCode + confirmNumber.value
                    )
                )
                    MaterialTheme.colors.error
                else
                    MaterialTheme.appColors.editField.borderUnFocused,
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                error = true
            )*/
            AppOutlineTextField(
                value = confirmNumber.value,
                modifier = Modifier
                    .fillMaxWidth(),
                onValueChange = {
                    confirmNumber.value = it
                },
                placeholder = { Text(text = stringResource(id = R.string.verify_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                errorMessage = stringResource(id = R.string.number_same_as_confirm),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label
                ),
                singleLine = true,
                isError = !(phoneNumber.value).passwordMatches(confirmNumber.value) && confirmNumber.value.isNotEmpty(),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            AppButton(
                singleButton = true,
                enabled = email.isValidEmail() && birthday.isNotEmpty() && validPhoneNumber(
                    phoneNumber.value
                ) && confirmNumber.value.passwordMatches(phoneNumber.value) && address.isNotEmpty(),
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

