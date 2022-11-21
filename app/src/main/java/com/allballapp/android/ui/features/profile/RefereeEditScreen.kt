package com.allballapp.android.ui.features.profile


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.allballapp.android.R
import com.allballapp.android.common.ComposeFileProvider
import com.allballapp.android.common.isValidEmail
import com.allballapp.android.common.validName
import com.allballapp.android.common.validPhoneNumber
import com.allballapp.android.data.response.TeamDetails
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.error
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefereeEditScreen(
    vm: ProfileViewModel,
    onBackClick: () -> Unit,
    OnNextGameStaffClick: () -> Unit,
    onUpdateSuccess: () -> Unit
) {
    val state = vm.state.value
    val maxChar = 30
    val maxEmailChar = 45
    val maxPhoneNumber = 13
    val maxAddress = 100

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
            uri?.let {
                vm.onEvent(ProfileEvent.OnProfileImageSelected(imageUri.toString()))
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success)
                vm.onEvent(ProfileEvent.OnProfileImageSelected(imageUri.toString()))
        }
    )


    /* remember {
         vm.onEvent(ProfileEvent.GetProfile)
     }*/
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        vm.channel.collect {
            when (it) {
                is ProfileChannel.OnUpdateSuccess -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                    onUpdateSuccess.invoke()
                }

                is ProfileChannel.OnProfileImageUpload -> {
                    vm.onEvent(ProfileEvent.OnSaveUserDetailsClick)
                }

                is ProfileChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        it.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val ageRange =
        listOf("10 - 16", "17 - 23", "24 - 29")

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val selected = remember {
        mutableStateOf(true)
    }
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {

            Spacer(modifier = Modifier.height(1.dp))

            ImagePickerBottomSheet(
                title = stringResource(id = R.string.select_image_from),
                onCameraClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                },
                onGalleryClick = {
                    imagePicker.launch("image/*")
                },
                onDismiss = {
                    scope.launch { modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden) }
                })
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.size_16dp),
            topEnd = dimensionResource(id = R.dimen.size_16dp)
        ),
        sheetBackgroundColor = colorResource(id = R.color.white),
    ) {

        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.appColors.material.surface)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                UserFlowBackground(modifier = Modifier.fillMaxWidth(),/* color = Color.White*/) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
                        Box(contentAlignment = Alignment.BottomEnd) {
                            CoilImage(
                                src = if (state.selectedImage.isEmpty()) com.allballapp.android.BuildConfig.IMAGE_SERVER + state.user.profileImage else Uri.parse(
                                    state.selectedImage
                                ),
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.size_200dp))
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                isCrossFadeEnabled = false,
                                onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                                onError = { Placeholder(R.drawable.ic_user_profile_icon) },
                                contentScale = ContentScale.Fit
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_top_add),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.size_40dp))
                                    .padding(
                                        end = dimensionResource(id = R.dimen.size_20dp),
                                        bottom = dimensionResource(
                                            id = R.dimen.size_20dp
                                        )
                                    )
                                    .clickable {
                                        scope.launch {
                                            modalBottomSheetState.animateTo(
                                                ModalBottomSheetValue.Expanded
                                            )
                                        }
                                    },
                                tint = MaterialTheme.appColors.material.primaryVariant
                            )
                        }

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
                    color = MaterialTheme.appColors.textField.labelColor,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.size_16dp))
                        .fillMaxWidth(),
                )

                UserFlowBackground(modifier = Modifier.fillMaxWidth(),/* color = Color.White*/) {
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
                            textColor = MaterialTheme.appColors.textField.labelColor,
                            placeholderColor = MaterialTheme.appColors.textField.label,
                            cursorColor = MaterialTheme.appColors.textField.labelColor
                        ),
                        singleLine = false,
                        maxLines = 6
                    )
                }


                if (state.user.teamDetails.isNotEmpty()) {
                    AppText(
                        text = stringResource(id = R.string.teams),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.appColors.textField.labelColor,
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.size_16dp))
                            .fillMaxWidth(),
                    )


                    UserFlowBackground(modifier = Modifier.fillMaxWidth(), /*color = Color.White*/) {
                        state.user.teamDetails.forEach {
                            RefereeTeams(
                                teams = it,
                            )
                        }
                    }
                }
                AppText(
                    text = stringResource(id = R.string.refereeing_experience),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.appColors.textField.labelColor,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.size_16dp))
                        .fillMaxWidth()
                )
                UserFlowBackground(modifier = Modifier.fillMaxWidth(), /*color = Color.White*/) {
                    DistanceItem(
                        if (state.user.userDetails.refereeningExperience.isEmpty()) 0
                        else
                            state.user.userDetails.refereeningExperience.toInt(),
                        vm
                    )

                    DividerCommon()

                    Column(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
                        AppText(
                            text = stringResource(id = R.string.about_exp),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.textField.labelColor,
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
                                textColor = MaterialTheme.appColors.textField.labelColor,
                                placeholderColor = MaterialTheme.appColors.textField.label,
                                cursorColor = MaterialTheme.appColors.textField.labelColor
                            ),
                            singleLine = false,
                            maxLines = 6
                        )
                    }
                }

                AppText(
                    text = stringResource(id = R.string.team_age_preference),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.appColors.textField.labelColor,
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.size_16dp))
                        .fillMaxWidth(),
                )

                UserFlowBackground(modifier = Modifier.fillMaxWidth(),/* color = Color.White*/) {

                    EditProfileFields(
                        state.user.userDetails.teamAgePerference,
                        onValueChange = {
                            vm.onEvent(ProfileEvent.OnAgeRangeChanges(it))
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
                        fontWeight = FontWeight.W800,
                        modifier = Modifier
                            .onGloballyPositioned {
                                textFieldSize = it.size.toSize()
                            }
                            .clickable { expanded = !expanded },
                        enabled = false,
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                            .background(MaterialTheme.colors.background)
                    ) {
                        ageRange.forEach { label ->
                            DropdownMenuItem(onClick = {
                                vm.onEvent(ProfileEvent.OnAgeRangeChanges(label))
                                expanded = false
                            }) {
                                Text(
                                    text = label,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.appColors.textField.labelColor
                                )
                            }
                        }
                    }

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
                        color = MaterialTheme.appColors.textField.labelColor,
                    )

                    Icon(
                        painterResource(id = R.drawable.ic_info),
                        "",
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }

                UserFlowBackground(modifier = Modifier.fillMaxWidth(),/* color = Color.White*/) {

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
                            color = MaterialTheme.appColors.textField.labelColor,
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                OnNextGameStaffClick()
                            }) {

                            state.user.userDetails.preferredPartner?.let {
                                if (it.name.isNotEmpty()) {
                                    AppText(
                                        text = it.name,
                                        style = MaterialTheme.typography.h5,
                                        color = ColorBWBlack
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                                    CoilImage(
                                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + it.profileImage,
                                        modifier = Modifier
                                            .size(dimensionResource(id = R.dimen.size_24dp))
                                            .clip(CircleShape),
                                        isCrossFadeEnabled = false,
                                        onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                                        onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                                }
                            }
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
                            color = MaterialTheme.appColors.textField.labelColor,
                        )

                        Switch(
                            modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
                            checked = state.user.userDetails.refereeWithPartner,
                            onCheckedChange = {
                                vm.onEvent(ProfileEvent.OnRefereeWithPartner(it))
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
                        if (state.selectedImage.isNullOrEmpty()) {
                            vm.onEvent(ProfileEvent.OnSaveUserDetailsClick)
                        } else {
                            vm.onEvent(ProfileEvent.ProfileUpload)
                        }
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

}


@Composable
fun RefereeTeams(
    teams: TeamDetails,
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
                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + teams.teamId.logo,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_24dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        onError = { Placeholder(R.drawable.ic_team_placeholder) }
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    AppText(
                        text = teams.teamId.name,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.appColors.textField.labelColor
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
                teams.role,
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
fun DistanceItem(age: Int, vm: ProfileViewModel) {

    val distance = remember {
        mutableStateOf(age)
    }

    vm.onEvent(ProfileEvent.OnReferringExperience(distance.value.toString()))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
            .background(color = MaterialTheme.appColors.material.background)
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_12dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = stringResource(id = R.string.years),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.appColors.textField.labelColor,
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
            color = MaterialTheme.appColors.textField.labelColor,
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

