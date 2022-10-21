package com.softprodigy.ballerapp.ui.features.profile


import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.*
import com.softprodigy.ballerapp.data.response.CheckBoxData
import com.softprodigy.ballerapp.data.response.TeamDetails
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.error
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileEditScreen(
    vm: ProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUpdateSuccess: () -> Unit
) {
    val state = vm.state.value
    val maxChar = 30
    val maxEmailChar = 45
    val maxPhoneNumber = 13
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
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


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

    remember {
        vm.onEvent(ProfileEvent.GetProfile)
    }
    val scope = rememberCoroutineScope()
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

                        Box(contentAlignment = Alignment.BottomEnd) {
                            CoilImage(
                                src = if (state.selectedImage.isEmpty()) BuildConfig.IMAGE_SERVER + state.user.profileImage else Uri.parse(
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
                        DividerCommon()
                        EditProfileFields(
                            data = if (state.user.birthdate != null) apiToUIDateFormat2(state.user.birthdate) else "",
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
                            if (it.length <= 5)
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
                            if (it.length <= 3)
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
                            if (it.length <= 3)
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
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Email
                        ),
                    )
                    DividerCommon()

                    EditProfileFields(
                        state.favProfessionalTeam,
                        onValueChange = {
                            if (it.length <= maxChar)
                                vm.onEvent(ProfileEvent.OnNbaTeamChange(it))
                        },
                        stringResource(id = R.string.favorite_proff_team),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Email
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
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Email
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
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Email
                        ),
                    )
                }

                AppButton(
                    enabled = true /*validName(state.user.firstName)
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
                        && state.favAllTimePlayer.isNotEmpty()*/,
                    icon = null,
                    themed = true,
                    onClick = {
                        vm.onEvent(ProfileEvent.ProfileUpload)
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
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    verticalAlignment = Alignment.CenterVertically
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
                            text = teamDetails.teamId.name.capitalize(),
                            color = ColorBWBlack,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                    ClickableText(
                        style = TextStyle(color = error),
                        text = AnnotatedString(stringResource(id = R.string.remove_team)),
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

