package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.response.TeamDetails
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.error


@Composable
fun RefereeEditScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUpdateSuccess: () -> Unit
) {
    val state = vm.state.value
    val maxChar = 30
    val maxEmailChar = 45
    val maxPhoneNumber = 13
    val maxAddress = 100

    val selected = remember {
        mutableStateOf(true)
    }
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
                }
            }

            AppText(
                text = stringResource(id = R.string.mailing_address),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                AppOutlineTextField(
                    value = state.user.address,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = {
                        if (it.length <= maxAddress)
                            vm.onEvent(ProfileEvent.OnAddressChange(it))
                    },
                    placeholder = {

                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    isError = false,
                    errorMessage = stringResource(id = R.string.email_error),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        backgroundColor = MaterialTheme.appColors.material.background,
                        textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        placeholderColor = MaterialTheme.appColors.textField.label,
                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    ),
                    singleLine = false,
                    maxLines = 6
                )
            }


            AppText(
                text = stringResource(id = R.string.teams),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                RefereeTeams(
                    teams = state.user.teamDetails,
                )
            }

            AppText(
                text = stringResource(id = R.string.refereeing_experience),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            )
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
                DistanceItem(state.user.age)

                DividerCommon()

                Column(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
                    AppText(
                        text = stringResource(id = R.string.about_exp),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                    AppOutlineTextField(
                        value = state.user.userDetails.aboutExperience,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnExperienceChange(it))
                        },
                        placeholder = {
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        isError = false,
                        errorMessage = stringResource(id = R.string.email_error),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                            unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                            backgroundColor = MaterialTheme.appColors.material.background,
                            textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            placeholderColor = MaterialTheme.appColors.textField.label,
                            cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        ),
                        singleLine = false,
                        maxLines = 6
                    )
                }
            }

            AppText(
                text = stringResource(id = R.string.team_age_preference),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth(),
            )

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {

                EditProfileFields(
                    state.user.userDetails.teamAgePerference,
                    onValueChange = {

                    },
                    stringResource(id = R.string.age_range),
                    errorMessage = stringResource(id = R.string.valid_first_name),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_next), "",
                            tint = ColorGreyLighter
                        )
                    },
                    fontWeight = FontWeight.W600
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    text = stringResource(id = R.string.prefered_partner),
                    style = MaterialTheme.typography.h2,
                    color = ColorBWBlack,
                )

                Icon(
                    painterResource(id = R.drawable.ic_info),
                    "",
                    tint = MaterialTheme.colors.primaryVariant
                )
            }

            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.select_partner),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppText(
                            text = state.user.userDetails.perferredPartner,
                            style = MaterialTheme.typography.h5,
                            color = ColorBWBlack
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_24dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                            onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                        Icon(
                            painterResource(id = R.drawable.ic_next),
                            "",
                            tint = ColorGreyLighter
                        )
                    }
                }

                DividerCommon()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AppText(
                        text = stringResource(id = R.string.referee_only_with_this_partner),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                    )

                    Switch(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
                        checked = selected.value,
                        onCheckedChange = {
                            selected.value = !selected.value
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                        )
                    )
                }
            }

            AppButton(
                enabled = validName(state.user.firstName)
                        && validName(state.user.lastName)
                        && state.user.email.isValidEmail()
                        && state.user.phone.isNotEmpty()
                        && state.user.address.isNotEmpty()
                        && selected.value,
                icon = null,
                themed = true,
                onClick = {
                    onUpdateSuccess()
                },
                text = stringResource(id = R.string.save_changes),
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
fun RefereeTeams(
    teams: SnapshotStateList<TeamDetails>,
) {

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
                        src = BuildConfig.IMAGE_SERVER + teams[0].teamId.logo,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_24dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        onError = { Placeholder(R.drawable.ic_team_placeholder) }
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    AppText(
                        text = teams[0].teamId.name,
                        style = MaterialTheme.typography.h5,
                        color = ColorBWBlack
                    )
                }
                ClickableText(
                    style = TextStyle(color = error),
                    text = AnnotatedString(stringResource(id = R.string.remove_team)),
                    onClick = {

                    })
            }
            DividerCommon()

            EditProfileFields(
                teams[0].role,
                onValueChange = {

                },
                stringResource(id = R.string.role),
                errorMessage = stringResource(id = R.string.valid_first_name),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_next), "",
                        tint = ColorGreyLighter
                    )
                }
            )
        }
    }
}

@Composable
fun DistanceItem(age: String) {

    val distance = remember {
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
            .background(color = Color.White)
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_12dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = stringResource(id = R.string.years),
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            modifier = Modifier.weight(1F)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_sub),
            contentDescription = "",
            modifier = Modifier
                .clickable { if (distance.value != 0) distance.value = distance.value - 1 }
                .size(dimensionResource(id = R.dimen.size_25dp))
                .clip(CircleShape)
        )
        Text(
            text = distance.value.toString(),
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.size_12dp))
        )
        Image(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "",
            modifier = Modifier
                .clickable { distance.value = distance.value + 1 }
                .size(dimensionResource(id = R.dimen.size_25dp))
                .clip(CircleShape)
        )
    }
}

