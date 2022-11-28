package com.allballapp.android.ui.features.user_type.team_setup.updated

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.getCustomColorCode
import com.allballapp.android.common.validTeamName
import com.allballapp.android.data.request.Address
import com.allballapp.android.data.request.Members
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.features.home.home_screen.HomeScreenEvent
import com.allballapp.android.ui.features.home.invitation.Invitation
import com.allballapp.android.ui.features.home.invitation.InvitationEvent
import com.allballapp.android.ui.features.home.invitation.InvitationViewModel
import com.allballapp.android.ui.theme.*
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeamSetupScreenUpdated(
    venue: String = "",
    onBackClick: () -> Unit,
    onNextClick: (String?) -> Unit,
    onVenueClick: () -> Unit,
    addProfileClick: () -> Unit,
    vm: SetupTeamViewModelUpdated,
    homeVm: HomeViewModel,
    inviteVm: InvitationViewModel = hiltViewModel()
) {

    val maxTeamChar = 30
    var currentColorType: ColorType? by remember {
        mutableStateOf(null)
    }
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val showSwapDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val showNoMessage = remember {
        mutableStateOf(false)
    }
    val roleKey = rememberSaveable {
        mutableStateOf("")
    }

    val state = vm.teamSetupUiState.value
    val homeState = homeVm.state.value
    val inviteState = inviteVm.invitationState.value
    remember {
        inviteVm.onEvent(InvitationEvent.GetRoles)
        homeVm.onEvent(HomeScreenEvent.OnSwapClick())
    }
    val playerName = rememberSaveable {
        mutableStateOf("")
    }
    val role = remember {
        mutableStateOf("")
    }

    val teamId = remember {
        mutableStateOf("")
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null)
                vm.onEvent(TeamSetupUIEventUpdated.OnImageSelected(uri.toString()))
        }
    val controller = rememberColorPickerController()
    val selectionUpdated = remember {
        mutableStateOf("")
    }
    if (selectionUpdated.value.isNotEmpty()) {
        UpdateColor()
    }
    LaunchedEffect(key1 = venue, block = {
        vm.onEvent(TeamSetupUIEventUpdated.OnVenueChange(venue))
    })
    val context = LocalContext.current
    val placePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { activityResult ->
            val addressReq = Address()

            if (activityResult.resultCode == Activity.RESULT_OK) {
                activityResult.data?.let {
                    val place = activityResult.data?.let { data ->
                        Autocomplete.getPlaceFromIntent(
                            data
                        )
                    }
                    val address = place?.address ?: ""
                    val latLng = place?.latLng
                    val lat = latLng?.latitude ?: 0.0
                    val long = latLng?.longitude ?: 0.0

                    addressReq.street = address
                    addressReq.lat = lat
                    addressReq.long = long

                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(lat, long, 1)
                        val stateName: String = addresses?.get(0)?.adminArea ?: ""
                        val cityName: String = addresses?.get(0)?.locality ?: ""
                        val countryName: String = addresses?.get(0)?.countryName ?: ""
                        val zip: String = addresses?.get(0)?.postalCode ?: ""

                        addressReq.state = stateName
                        addressReq.city = cityName
                        addressReq.country = countryName
                        addressReq.zip = zip


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            if (addressReq.street.isNotEmpty()) {
                vm.onEvent(TeamSetupUIEventUpdated.OnAddressChanged(addressReq))
            }
            if (activityResult.resultCode == AutocompleteActivity.RESULT_ERROR) {
                activityResult.let {
                    val status = it.data?.let { it1 -> Autocomplete.getStatusFromIntent(it1) }
                    Timber.i("RESULT_ERROR ${status?.statusMessage}--$status")
                }
            }

        }
    )


    LaunchedEffect(key1 = Unit) {
        vm.teamSetupChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamSetupChannel.ShowToast -> {
                    if (inviteState.showAddPlayerDialog) {
                        inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
                    }
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is TeamSetupChannel.OnLogoUpload -> {
                    vm.onEvent(TeamSetupUIEventUpdated.OnLogoUploadSuccess)
                }
                is TeamSetupChannel.OnTeamCreate -> {
                    teamId.value = uiEvent.id
                    Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG)
                        .show()
                    vm.onEvent(TeamSetupUIEventUpdated.SetRequestData(role.value, teamId.value))
                    inviteVm.onEvent(InvitationEvent.SetTeamId(teamId.value))
                    inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                    inviteVm.onEvent(InvitationEvent.OnRoleConfirmClick)
                    inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                }
                is TeamSetupChannel.OnInvitationSuccess -> {
                    inviteVm.onEvent(InvitationEvent.OnRoleConfirmClick)
                    inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                    inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
                }
                is TeamSetupChannel.OnInvitationDone -> {
                    inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                    inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                    inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
                    inviteVm.onEvent(InvitationEvent.OnPlayerAddedSuccessDialog(true))
                }
                else -> Unit
            }
        }
    }
    if (inviteState.showRoleDialog) {
        SelectInvitationRoleDialog(
            onDismiss = {
                inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                inviteVm.onEvent(InvitationEvent.OnClearValues)
            },
            onConfirmClick = {
                vm.onEvent(TeamSetupUIEventUpdated.OnRole(inviteState.selectedRoleKey))
                role.value = inviteState.selectedRoleKey
                vm.onEvent(TeamSetupUIEventUpdated.OnAddPlayerScreenNext)
                inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
            },
            onSelectionChange = { inviteVm.onEvent(InvitationEvent.OnRoleClick(roleKey = it)) },
            title = stringResource(
                id = R.string.what_is_your_role,
                "${homeState.user.firstName} ${homeState.user.lastName}",
                inviteState.selectedInvitation.team.name.ifEmpty {
                    state.teamName.ifEmpty {
                        context.getString(
                            R.string.the_team
                        )
                    }
                }
            ),
            selected = inviteState.selectedRoleKey,
            showLoading = inviteState.showLoading,
            roleList = inviteState.roles,
            userName = "${homeState.user.firstName} ${homeState.user.lastName}",
            userLogo = com.allballapp.android.BuildConfig.IMAGE_SERVER + homeState.user.profileImage,
        )
    }

    if (inviteState.showGuardianDialog) {
        SelectGuardianRoleDialog(
            loading = state.isLoading,
            inviteState.selectedRoleKey,
            onBack = {
                showNoMessage.value = true
                inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                vm.onEvent(TeamSetupUIEventUpdated.MoveBack(true))
            },
            onConfirmClick = {
                //inviteVm.onEvent(InvitationEvent.OnInvitationConfirm(homeState.user.gender))
                inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                vm.onEvent(TeamSetupUIEventUpdated.MoveBack(true))
            },
            onSelectionChange = { inviteVm.onEvent(InvitationEvent.OnGuardianClick(guardian = it)) },
            //selected = inviteState.selectedGuardian,
            selected = inviteState.selectedIds,
            guardianList = if (state.role != UserType.PLAYER.key)
                inviteState.playerDetails.filter { member -> member.role == UserType.PLAYER.key }
            else inviteState.playerDetails.filter { member -> member.role != UserType.PLAYER.key },
            onValueSelected = {
                playerName.value = it.memberDetails.name
                inviteVm.onEvent(InvitationEvent.OnValuesSelected(it))
            },
            onDismiss = {
                inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                inviteVm.onEvent(InvitationEvent.OnClearGuardianValues)
            },
            onChildNotListedCLick = {
                inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(true))
            },
            dontHaveChildClick = {
                inviteVm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(true))
            }
        )
    }

    if (inviteState.showGuardianOnlyConfirmDialog) {
        ConfirmDialog(
            title = stringResource(id = R.string.join_team_without_child_selection), onDismiss = {
                showNoMessage.value = false
                inviteVm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(false))
            },
            onConfirmClick = { //inviteVm.onEvent(InvitationEvent.OnInvitationConfirm(homeState.user.gender))
                showNoMessage.value = true
                inviteVm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(false))
                inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                vm.onEvent(TeamSetupUIEventUpdated.MoveBack(false))
            })
    }
    if (inviteState.showAddPlayerDialog) {
        vm.initialInviteCount(1)
        InviteTeamMembersDialog(
            role = if (state.role == UserType.PLAYER.key) UserType.PARENT.key else UserType.PLAYER.key,
            onBack = {
                //inviteVm.onEvent(InvitationEvent.OnRoleDialogClick(true))
                inviteVm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
            },
            onConfirmClick = {
                if (state.teamId.isNotEmpty()) {
                    if (state.inviteList.isNotEmpty()) {
                        if (homeState.phone != state.inviteList[0].countryCode + state.inviteList[0].contact) {
                            inviteVm.onEvent(
                                InvitationEvent.SetData(
                                    state.teamImageServerUrl,
                                    state.inviteList[0].name
                                )
                            )
                            vm.onEvent(
                                TeamSetupUIEventUpdated.OnInviteTeamMembers(
                                    teamId = state.teamId,
                                    userType = state.role,
                                    type = AppConstants.TYPE_ACCEPT_INVITATION
                                )
                            )
                        } else {
                            showSwapDialog.value = true
                            homeVm.onEvent(HomeScreenEvent.OnSwapClick())
                        }
                    }
                }
                inviteVm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
            },
            onIndexChange = { index ->
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnIndexChange(
                        index = index
                    )
                )

                if (hasContactPermission(context)) {
                    val intent =
                        Intent(Intent.ACTION_GET_CONTENT)
                    intent.type =
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    (context as Activity).startActivityForResult(
                        intent,
                        AppConstants.REQUEST_CONTACT_CODE,
                        null
                    )
                } else {
                    requestContactPermission(context, (context as Activity))
                }
            },
            inviteList = state.inviteList,
            onNameValueChange = { index, name ->
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnNameValueChange(
                        index = index,
                        name
                    )
                )

            },
            onEmailValueChange = { index, email ->
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnEmailValueChange(
                        index = index,
                        email
                    )
                )

            },
            onInviteCountValueChange = { addIntent ->
                vm.onEvent(TeamSetupUIEventUpdated.OnInviteCountValueChange(addIntent = true))
            },
            OnCountryValueChange = { index, code ->
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnCountryValueChange(
                        index = index,
                        code = code
                    )
                )
            }, roles = if (state.role != UserType.PLAYER.key)
                inviteState.roles.filter { member -> member.key == UserType.PLAYER.key }
            else inviteState.roles.filter { member -> member.key != UserType.PLAYER.key },
            onRoleValueChange = { index, role ->
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnRoleValueChange(
                        index = index,
                        role = role
                    )
                )
                roleKey.value = role.key
            }
        )
    }

    if (showSwapDialog.value) {
        SwapProfile(
            title = stringResource(id = R.string.invite_from_your_profile),
            actionButtonText = stringResource(id = R.string.invite),
            users = homeState.swapUsers,
            onDismiss = { showSwapDialog.value = false },
            onConfirmClick = { swapUser ->
                inviteVm.onEvent(
                    InvitationEvent.SetData(
                        state.teamImageServerUrl,
                        swapUser.firstName
                    )
                )
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnInviteTeamMembers(
                        state.teamId,
                        userType = state.role,
                        type = AppConstants.TYPE_ACCEPT_INVITATION,
                        profilesSelected = true,
                        member = Members(
                            name = swapUser.firstName,
                            mobileNumber = swapUser._Id,
                            role = roleKey.value,
                            profilesSelected = "true"
                        )
                    )
                )
                homeVm.onEvent(HomeScreenEvent.HideSwap(false))
            },
            showLoading = homeState.isDataLoading,
            onCreatePlayerClick = {
                addProfileClick()
            },
            showCreatePlayerButton = true
        )
    }

    if (inviteState.showPlayerAddedSuccessDialog) {
        InvitationSuccessfullySentDialog(
            onDismiss = {
                inviteVm.onEvent(InvitationEvent.OnPlayerAddedSuccessDialog(false))
                //vm.onEvent(TeamSetupUIEventUpdated.Clear)
                onBackClick()
            },
            onConfirmClick = {
                inviteVm.onEvent(InvitationEvent.OnPlayerAddedSuccessDialog(false))
                homeVm.onEvent(HomeScreenEvent.HideSwap(false))
                //vm.onEvent(TeamSetupUIEventUpdated.Clear)
                onNextClick(
                    inviteState.teamId.ifEmpty {
                        ""
                    }
                )
            },
            teamName = state.teamName,
            teamLogo = inviteState.selectedLogo.ifEmpty {
                (state.teamImageUri ?: "").ifEmpty { "" }
            },
            playerName = if (showNoMessage.value) stringResource(id = R.string.no_player)
            else if (inviteState.selectedIds.isNotEmpty()) {
                if (inviteState.selectedIds.size == 1) stringResource(id = R.string.no_player).replace(
                    "No",
                    inviteState.selectedIds.size.toString() + ""
                )
                else inviteState.selectedIds.size.toString() + " " + stringResource(id = R.string.members)
            } else stringResource(id = R.string.no_player)
        )
    }

    ModalBottomSheetLayout(

        sheetContent = {
            ColorPickerBottomSheet(controller, colorEnvelope = { colorEnvelope ->
                if (!colorEnvelope.hexCode.contentEquals(AppConstants.PICKER_DEFAULT_COLOR)) {
                    selectionUpdated.value = colorEnvelope.hexCode
                    when (currentColorType) {
                        ColorType.PRIMARY -> {
                            AppConstants.SELECTED_COLOR = colorEnvelope.color
                            vm.onEvent(TeamSetupUIEventUpdated.OnColorSelected(colorEnvelope.hexCode))
                        }
                        ColorType.SECONDARY -> {
                            vm.onEvent(TeamSetupUIEventUpdated.OnSecColorSelected(colorEnvelope.hexCode))

                        }
                        ColorType.TERTIARY -> {
                            vm.onEvent(TeamSetupUIEventUpdated.OnTerColorSelected(colorEnvelope.hexCode))
                        }
                    }
                }
            }, onDismiss = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            })
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.size_16dp),
            topEnd = dimensionResource(id = R.dimen.size_16dp)
        ),
        sheetBackgroundColor = colorResource(id = R.color.white)
    ) {


        Box(Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                UserFlowBackground {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_name),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamName,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamSetupUIEventUpdated.OnTeamNameChange(it))
                            },
                            placeholder = {
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            isError = !validTeamName(state.teamName) && state.teamName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_jerseys),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnJerseys,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamSetupUIEventUpdated.OnTeamNameJerseyChange(it))
                            },
                            placeholder = {
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            isError = !validTeamName(state.teamNameOnJerseys) && state.teamNameOnJerseys.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_tournament),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnTournaments,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(
                                        TeamSetupUIEventUpdated.OnTeamNameTournamentsChange(
                                            it
                                        )
                                    )
                            },
                            placeholder = {

                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                            isError = !validTeamName(state.teamNameOnTournaments) && state.teamNameOnTournaments.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )
                    }

                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_logo),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        if (state.teamImageUri != null) {
                            Text(
                                text = stringResource(id = R.string.change),
                                color = ColorBWGrayLight,
                                modifier = Modifier.clickable {
                                    if (state.teamImageUri != null) {
                                        scope.launch {
                                            modalBottomSheetState.hide()
                                        }
                                        launcher.launch("image/*")
                                    }
                                },
                                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp
                            )
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensionResource(id = R.dimen.size_16dp),
                                end = dimensionResource(id = R.dimen.size_16dp)
                            )
                            .height(
                                if (state.teamImageUri == null) dimensionResource(id = R.dimen.size_80dp) else dimensionResource(
                                    id = R.dimen.size_200dp
                                )
                            )
                            .background(

                                color = if (state.teamImageUri == null) ColorPrimaryTransparent
                                else MaterialTheme.appColors.material.surface,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .clickable {
                                if (state.teamImageUri == null) {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
                                }
                            }

                    ) {
                        if (state.teamImageUri == null) {
                            Row(modifier = Modifier.align(Alignment.Center)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_upload),
                                    contentDescription = null,
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                                AppText(
                                    text = stringResource(id = R.string.upload_files),
                                    style = MaterialTheme.typography.h6,
                                    color = ColorMainPrimary
                                )
                            }
                        }
                        state.teamImageUri?.let {
                            CoilImage(
                                src = Uri.parse(it),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(dimensionResource(id = R.dimen.size_160dp))
                                    .clip(CircleShape)
                                    .align(Alignment.Center)
                                    .background(
                                        color = MaterialTheme.appColors.material.onSurface,
                                        CircleShape
                                    ),
                                isCrossFadeEnabled = false,
                                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                onError = { Placeholder(R.drawable.ic_team_placeholder) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    AppDivider()
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.primary_team_color),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            style = MaterialTheme.typography.h6
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.PRIMARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))

                                    )
                                    .background(
                                        color = Color.Transparent, shape = RoundedCornerShape(
                                            dimensionResource(id = R.dimen.size_16dp)
                                        )
                                    )
                                    .padding(dimensionResource(id = R.dimen.size_1dp))
                            ) {
                                AppText(
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = if (state.teamColorPrimary.isNotEmpty()) {
                                        "#" + state.teamColorPrimary
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled


                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorPrimary.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(
                                        android.graphics.Color.parseColor(
                                            "#${
                                                getCustomColorCode(
                                                    state.teamColorPrimary
                                                )
                                            }"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }
                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.secondary_team_color),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.SECONDARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        /*BorderStroke(
                                        dimensionResource(id = R.dimen.size_1dp),
                                        ColorBWGrayBorder
                                    )*/
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                                    )
                                    .background(
                                        color = Color.Transparent, shape = RoundedCornerShape(
                                            dimensionResource(id = R.dimen.size_16dp)
                                        )
                                    )
                                    .padding(dimensionResource(id = R.dimen.size_1dp))
                            ) {
                                AppText(
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = if (state.teamColorSec.isNotEmpty()) {
                                        "#" + state.teamColorSec
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled


                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorSec.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(
                                        android.graphics.Color.parseColor(
                                            "#${
                                                getCustomColorCode(
                                                    state.teamColorSec
                                                )
                                            }"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }
                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.tertiary_team_color),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.TERTIARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .border(
                                        /* BorderStroke(
                                         dimensionResource(id = R.dimen.size_1dp),
                                         ColorBWGrayBorder
                                     )*/
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))

                                    )
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))

                                    .background(
                                        color = Color.Transparent, shape = RoundedCornerShape(
                                            dimensionResource(id = R.dimen.size_16dp)
                                        )
                                    )
                                    .padding(dimensionResource(id = R.dimen.size_1dp))
                            ) {
                                AppText(
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = if (state.teamColorThird.isNotEmpty()) {
                                        "#" + state.teamColorThird
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorThird.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(
                                        android.graphics.Color.parseColor(
                                            "#${
                                                getCustomColorCode(
                                                    state.teamColorThird
                                                )
                                            }"
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }

                }

                AppText(
                    text = stringResource(id = R.string.home_court),
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                )

                UserFlowBackground() {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.size_16dp))
                    ) {

//                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.name_of_venue),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable { onVenueClick.invoke() }
                                .border(
                                    width = dimensionResource(id = R.dimen.divider),
                                    color = MaterialTheme.appColors.editField.borderUnFocused,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                                )
                                .padding(dimensionResource(id = R.dimen.size_16dp))
                        )
                        {
                            if (venue.isEmpty()) {
                                AppText(
                                    text = "",
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.textField.labelDark,
                                    fontWeight = FontWeight.W400,
                                    fontFamily = rubikFamily
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            } else {
                                AppText(
                                    text = venue,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    fontWeight = FontWeight.W400,
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.address),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!Places.isInitialized()) {
                                        Places.initialize(
                                            context.applicationContext,
                                            com.allballapp.android.BuildConfig.MAPS_API_KEY
                                        )
                                    }
                                    val fields = listOf(
                                        Place.Field.NAME,
                                        Place.Field.ADDRESS,
                                        Place.Field.LAT_LNG
                                    )
                                    placePicker.launch(
                                        Autocomplete
                                            .IntentBuilder(
                                                AutocompleteActivityMode.FULLSCREEN,
                                                fields
                                            )
                                            .build(context)
                                    )
                                }
                                .border(
                                    width = dimensionResource(id = R.dimen.divider),
                                    color = MaterialTheme.appColors.editField.borderUnFocused,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                                )
                                .padding(dimensionResource(id = R.dimen.size_16dp))
                        )
                        {
                            if (state.selectedAddress.street.isEmpty()) {
                                AppText(
                                    text = "",
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.textField.labelDark,
                                    fontWeight = FontWeight.W400,
                                    fontFamily = rubikFamily
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            } else {
                                AppText(
                                    text = state.selectedAddress.street,
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    fontWeight = FontWeight.W400,
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                val enable = !state.isLoading &&
                        validTeamName(state.teamName)
                        && state.teamImageUri != null
                        && state.teamColorPrimary.isNotEmpty()
                        && state.teamColorSec.isNotEmpty()
                        && state.teamColorThird.isNotEmpty()
                        && state.teamName.isNotEmpty()
                        && state.teamNameOnJerseys.isNotEmpty()
                        && state.teamNameOnTournaments.isNotEmpty()
                        /* && validTeamName(state.venueName)
                         && state.venueName.isNotEmpty()*/
                        && state.selectedAddress.street.isNotEmpty()
                        && state.selectedAddress.street.length >= 4
                BottomButtons(
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next),
                    onBackClick = onBackClick,
                    onNextClick = {
                        inviteVm.onEvent(InvitationEvent.OnAcceptCLick(Invitation()))
                    },
                    enableState = enable,
                    showOnlyNext = true,
                    themed = false, //Just set to true to show selected color as background
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

               /* if (enable) {
                    BackHandler {}
                }*/
            }
        }
    }

    if (state.isLoading || inviteState.showLoading) {
        CommonProgressBar()
    }
}

@Composable
fun ColorPickerBottomSheet(
    controller: ColorPickerController,
    colorEnvelope: (ColorEnvelope) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.size_40dp))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_25dp))
        ) {
            AppText(
                text = stringResource(id = R.string.colors),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_close_color_picker),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(dimensionResource(id = R.dimen.size_16dp))
                    .clickable {
                        onDismiss.invoke()
                    }
            )
        }
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_300dp))
                .padding(dimensionResource(id = R.dimen.size_8dp)),
            controller = controller,
            onColorChanged = colorEnvelope
        )
    }

}

enum class ColorType {
    PRIMARY, SECONDARY, TERTIARY
}

@Composable
fun UpdateColor() {
    MaterialTheme.appColors.material.copy(primaryVariant = AppConstants.SELECTED_COLOR)
}
