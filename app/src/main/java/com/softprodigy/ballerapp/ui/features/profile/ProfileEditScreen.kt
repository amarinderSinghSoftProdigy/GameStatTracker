package com.softprodigy.ballerapp.ui.features.profile


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.apiToUIDateFormat2
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.response.CheckBoxData
import com.softprodigy.ballerapp.data.response.TeamDetails
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.error
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ProfileEditScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state = vm.state.value
    val maxChar = 30
    val maxEmailChar = 45
    val maxPhoneNumber = 13


    val genderList =
        listOf(stringResource(id = R.string.male), stringResource(id = R.string.female))
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    val context = LocalContext.current
    val focusRequester = FocusRequester()


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


    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateString: String = format.format(calendar.time)
            vm.onEvent(ProfileEvent.OnBirthdayChange(dateString))
        }, mYear, mMonth, mDay
    )
    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))

                    CoilImage(
                        src = BuildConfig.IMAGE_SERVER + state.user.profileImage,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                        onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))
                    DividerCommon()
                    EditProfileFields(
                        state.user.firstName,
                        onValueChange = {
                            if (it.length <= maxChar)
                                vm.onEvent(ProfileEvent.OnFirstNameChange(it))
                        },
                        stringResource(id = R.string.first_name),
                        errorMessage = stringResource(id = R.string.valid_first_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        isError = !validName(state.user.firstName) && state.user.firstName.isNotEmpty() || state.user.firstName.length > 30,

                        )
                    DividerCommon()
                    EditProfileFields(
                        state.user.lastName,
                        onValueChange = {
                            if (it.length <= maxChar)
                                vm.onEvent(ProfileEvent.OnLastNameChange(it))
                        },
                        stringResource(id = R.string.last_name),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Email

                        ),
                        isError = !validName(state.user.lastName) && state.user.lastName.isNotEmpty() || state.user.lastName.length > 30,
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.email,
                        onValueChange = {
                            if (it.length <= maxEmailChar)
                                vm.onEvent(ProfileEvent.OnEmailChange(it))

                        },
                        stringResource(id = R.string.email),
                        isError = (!state.user.email.isValidEmail() && state.user.email.isNotEmpty()),
                        errorMessage = stringResource(id = R.string.enter_valid_email),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        readOnly = true
                    )
                    DividerCommon()
                    EditProfileFields(
                        state.user.phone,
                        onValueChange = {
                            if (it.length <= maxPhoneNumber)
                                vm.onEvent(ProfileEvent.OnPhoneChange(it))

                        },
                        stringResource(id = R.string.phone_num),
                        isError = validPhoneNumber(state.user.phone),
                        errorMessage = stringResource(id = R.string.valid_phone_number),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    DividerCommon()
                    EditProfileFields(
                        data = apiToUIDateFormat2(state.user.birthdate),
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnBirthdayChange(it))

                        },
                        readOnly = true,
                        head = stringResource(id = R.string.birthday),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ), enabled = false,
                        modifier = Modifier.clickable {
                            mDatePickerDialog.show()
                        }
                    )
                    DividerCommon()

                    EditProfileFields(
                        state.user.userDetails.classOf,
                        onValueChange = {
                            if (it.length <= maxChar)
                                vm.onEvent(ProfileEvent.OnClassChange(it))

                        },
                        stringResource(id = R.string.classof),
                        errorMessage = stringResource(id = R.string.valid_last_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                    )
                    DividerCommon()
                }
            }

            AppText(
                text = stringResource(id = R.string.positons),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            )

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Row(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
                    state.positionPlayed.forEachIndexed { index, item ->
                        CheckBoxItem(item = item, onCheckedChange = {
                            vm.onEvent(ProfileEvent.OnPositionPlayedChanges(index, it))
                        })
                    }
                }
            }

            AppText(
                text = stringResource(id = R.string.teams_label),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                Teams(
                    teams = state.user.teamDetails,
                    onLeaveTeamClick = { index, teamId ->
                        vm.onEvent(ProfileEvent.OnLeaveTeamCLick(index, teamId))
                    },
                    onPositionChange = { index, position ->
                        vm.onEvent(ProfileEvent.OnPositionChange(index, position))
                    },
                    onRoleChange = { index, role ->
                        vm.onEvent(ProfileEvent.OnRoleChange(index, role))
                    },
                    onJerseyNumberChange = { index, number ->
                        vm.onEvent(ProfileEvent.OnJerseyChange(index, number))
                    }
                )
            }

            AppText(
                text = stringResource(id = R.string.jersey_pref),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                EditProfileFields(
                    data =
                    state.jerseyNumerPerferences,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnPrefJerseyNoChange(it))
                    },
                    stringResource(id = R.string.jersey_number),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Number
                    ),
                )
                DividerCommon()

                Column {
                    EditProfileFields(
                        state.user.gender,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnGenderChange(it))

                        },
                        stringResource(id = R.string.gender),
                        errorMessage = stringResource(id = R.string.valid_first_name),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        modifier = Modifier
                            .onGloballyPositioned {
                                textFieldSize = it.size.toSize()
                            }
                            .clickable { expanded = !expanded },
                        readOnly = true,
                        enabled = false,
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
                                vm.onEvent(ProfileEvent.OnGenderChange(label))

                                expanded = false
                            }) {
                                androidx.compose.material.Text(
                                    text = label,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                DividerCommon()

                EditProfileFields(
                    state.shirtSize,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnShirtChange(it))

                    },
                    stringResource(id = R.string.shirt_size),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.waistSize,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnWaistChange(it))

                    },
                    stringResource(id = R.string.waist_size),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Number
                    ),
                )
            }
            AppText(
                text = stringResource(id = R.string.fun_facts),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {


                EditProfileFields(
                    state.favCollegeTeam,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnCollegeTeamChange(it))
                    },
                    stringResource(id = R.string.favorite_college_team),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.favProfessionalTeam,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnNbaTeamChange(it))
                    },
                    stringResource(id = R.string.favorite_nba_team),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.favActivePlayer,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnActivePlayerChange(it))

                    },
                    stringResource(id = R.string.favorite_active_player),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
                DividerCommon()

                EditProfileFields(
                    state.favAllTimePlayer,
                    onValueChange = {
                        if (it.length <= maxChar)
                            vm.onEvent(ProfileEvent.OnAllTimeFavChange(it))

                    },
                    stringResource(id = R.string.favoritea_all_time_tlayer),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                )
            }

            AppButton(
                enabled = validName(state.user.firstName)
                        && validName(state.user.lastName)
                        && state.user.email.isValidEmail()
                        && state.user.phone.isNotEmpty()
                        && state.user.userDetails.classOf.isNotEmpty()
                        && state.jerseyNumerPerferences.isNotEmpty()
                        && state.user.gender.isNotEmpty()
                        && state.shirtSize.isNotEmpty()
                        && state.waistSize.isNotEmpty()
                        && state.favCollegeTeam.isNotEmpty()
                        && state.favProfessionalTeam.isNotEmpty()
                        && state.favActivePlayer.isNotEmpty()
                        && state.favAllTimePlayer.isNotEmpty(),
                icon = null,
                themed = true,
                onClick = {
                    vm.onEvent(ProfileEvent.OnSaveUserDetailsClick)
                },
                text = stringResource(id = R.string.save),
                isForceEnableNeeded = true
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
        }

        if (state.isLoading) {
            CommonProgressBar()
        }

        if (state.showRemoveFromTeamDialog) {
            DeleteDialog(
                item = state.selectedTeamId,
                message = stringResource(id = R.string.alert_remove_from_team),
                onDismiss = {
                    vm.onEvent(ProfileEvent.OnLeaveDialogClick(false))
                },
                onDelete = {
                    if (state.selectedTeamId.isNotEmpty()) {
                        vm.onEvent(ProfileEvent.OnLeaveConfirmClick(state.selectedTeamId))
                    }
                }
            )
        }
    }

}



@Composable
fun Teams(
    teams: SnapshotStateList<TeamDetails>,
    onLeaveTeamClick: (Int, String) -> Unit,
    onPositionChange: (Int, String) -> Unit,
    onRoleChange: (Int, String) -> Unit,
    onJerseyNumberChange: (Int, String) -> Unit,
) {

    teams.forEachIndexed { index, teamDetails ->
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.appColors.material.primary)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .fillMaxSize()
                    .background(color = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + teamDetails.teamId.logo,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_24dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                            onError = { Placeholder(R.drawable.ic_team_placeholder) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                        AppText(
                            text = teamDetails.teamId.name,
                            style = MaterialTheme.typography.h5,
                            color = ColorBWBlack
                        )
                    }
                    ClickableText(
                        style = TextStyle(color = error),
                        text = AnnotatedString(stringResource(id = R.string.leave_team)),
                        onClick = {
                            onLeaveTeamClick.invoke(index, teamDetails.teamId.Id)
                        })
                }
                DividerCommon()

                EditProfileFields(
                    teamDetails.role,
                    onValueChange = {
                        onRoleChange.invoke(index, it)

                    },
                    stringResource(id = R.string.role),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = true
                )
                DividerCommon()

                EditProfileFields(
                    teamDetails.position,
                    onValueChange = {
                        onPositionChange.invoke(index, it)
                    },
                    stringResource(id = R.string.position),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = true

                )
                DividerCommon()

                EditProfileFields(
                    teamDetails.jersey,
                    onValueChange = {
                        if (it.length <= 3)
                            onJerseyNumberChange.invoke(index, it)
                    },
                    stringResource(id = R.string.jersey_number),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Number,
                    ),
                    readOnly = true

                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

        }
    }

}

@Composable
fun CheckBoxItem(item: CheckBoxData, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
            end = dimensionResource(id = R.dimen.size_12dp),
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {
                    onCheckedChange.invoke(!item.isChecked)
                }
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
                .size(
                    dimensionResource(id = R.dimen.size_16dp)
                )
                .background(
                    color =
                    if (item.isChecked) {
                        MaterialTheme.appColors.material.primaryVariant
                    } else Color.White
                )
                .border(
                    width =
                    if (item.isChecked) {
                        0.dp
                    } else dimensionResource(id = R.dimen.size_1dp),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                    color = if (item.isChecked)
                        Color.Transparent
                    else
                        MaterialTheme.appColors.buttonColor.bckgroundDisabled
                )
        ) {
            if (item.isChecked)
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.appColors.buttonColor.textEnabled
                )
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        AppText(
            text = item.label,
            style = MaterialTheme.typography.body1,
            color = ColorBWBlack,
        )
    }
}

