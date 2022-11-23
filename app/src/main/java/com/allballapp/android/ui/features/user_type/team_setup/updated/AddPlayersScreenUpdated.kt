package com.allballapp.android.ui.features.user_type.team_setup.updated

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.getCustomColorCode
import com.allballapp.android.common.validName
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.request.Members
import com.allballapp.android.data.response.UserRoles
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.features.home.home_screen.HomeScreenEvent
import com.allballapp.android.ui.features.home.teams.TeamUIEvent
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.theme.*
import com.allballapp.android.ui.utils.CommonUtils
import com.google.accompanist.flowlayout.FlowRow
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries

val maxChar = 45

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreenUpdated(
    teamData: TeamViewModel? = null,
    teamId: String? = "",
    vm: SetupTeamViewModelUpdated,
    homeVm: HomeViewModel,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit,
    onInvitationSuccess: () -> Unit,
    addProfileClick: () -> Unit,
) {
    val context = LocalContext.current as Activity
    val teamState = teamData?.teamUiState?.value
    val homeState = homeVm.state.value
    val state = vm.teamSetupUiState.value
    val focusRequester = remember { FocusRequester() }
    val showSwapDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val profileName = remember {
        mutableStateOf("")
    }
    val expanded = remember {
        mutableStateOf(false)
    }
    val roleKey = rememberSaveable {
        mutableStateOf("")
    }
    remember {
        vm.onEvent(TeamSetupUIEventUpdated.GetRoles)
        if (state.inviteList.isEmpty())
            vm.initialInviteCount(2)

        if (!teamId.isNullOrEmpty()) {
            if (teamData != null) {
                teamData.onEvent(TeamUIEvent.GetTeam(teamId))

            }
            vm.onEvent(TeamSetupUIEventUpdated.GetInvitedTeamPlayers(teamId))
        }
        homeVm.onEvent(HomeScreenEvent.OnSwapClick())
    }
    BackHandler {
        onBackClick.invoke()
        vm.onEvent(TeamSetupUIEventUpdated.OnBackButtonClickFromPlayerScreen)
    }

    fun updateItem(index: Int? = null, addIntent: Boolean) {
        if (addIntent) {
            vm.onEvent(TeamSetupUIEventUpdated.OnInviteCountValueChange(addIntent = true))
        } else {
            index?.let {
                vm.onEvent(
                    TeamSetupUIEventUpdated.OnInviteCountValueChange(
                        index = index,
                        addIntent = false
                    )
                )
            }
        }
    }
    LaunchedEffect(key1 = Unit) {
        vm.teamSetupChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamSetupChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is TeamSetupChannel.OnLogoUpload -> {
                    vm.onEvent(TeamSetupUIEventUpdated.OnLogoUploadSuccess)
                }
                is TeamSetupChannel.OnShowDialog -> {
                    showSwapDialog.value = true
                }
                is TeamSetupChannel.OnTeamCreate -> {
                    Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG)
                        .show()
                    onNextClick.invoke(uiEvent.message)
                }
                is TeamSetupChannel.OnInvitationSuccess -> {
                    onInvitationSuccess.invoke()
                }
                else -> Unit
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            UserFlowBackground(color = MaterialTheme.appColors.buttonColor.textEnabled) {
                if (state.memberList.isNotEmpty() && teamData != null) {
                    FoldableItem(
                        expanded = expanded.value,
                        headerBackground = MaterialTheme.appColors.material.surface,
                        headerBorder = BorderStroke(0.dp, Color.Transparent),
                        header = { isExpanded ->
                            TeamMemberItem(
                                state.memberList.size,
                                teamState?.member ?: Members(
                                    teamId ?: "",
                                    "",
                                    teamState?.teamName ?: "",
                                    profileImage = teamState?.logo ?: ""
                                ),
                                isExpanded,
                                true
                            )
                        },
                        childItems = state.memberList,
                        hasItemLeadingSpacing = false,
                        hasItemTrailingSpacing = false,
                        itemSpacing = 0.dp,
                        itemHorizontalPadding = 0.dp,
                        itemsBackground = MaterialTheme.appColors.material.primary,
                        item = { match, index ->
                            TeamMemberItem(
                                0,
                                match,
                                expanded.value,
                                false,
                            )
                        }
                    )

                    AppDivider()
                }

                Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {
                    FlowRow {
                        state.inviteList.forEachIndexed { index, item ->
                            Column {
                                InviteItem(
                                    item,
                                    index,
                                    vm,
                                    focusRequester,
                                    context,
                                    state.roles,
                                ) { it, it1 ->
                                    updateItem(it, it1)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                    //Add New Row Button - Commented as per the client.
                    InviteTeamMemberButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.add),
                        onClick = {
                            updateItem(addIntent = true)
                        },
                        painter = painterResource(
                            id = R.drawable.ic_add_button
                        ),
                        isTransParent = true
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
            BottomButtons(
                secondText = stringResource(id = R.string.finish),
                onBackClick = {
                    onBackClick.invoke()
                    vm.onEvent(TeamSetupUIEventUpdated.OnBackButtonClickFromPlayerScreen)
                },
                onNextClick = {
                    if (!state.isLoading) {
                        if (teamId.isNullOrEmpty()) {
                            vm.onEvent(TeamSetupUIEventUpdated.OnAddPlayerScreenNext)
                        } else {
                            val role = CommonUtils.getCheck(homeState.user.phone, state.inviteList)
                            if (role.isNotEmpty()) {
                                roleKey.value = role//state.inviteList[0].role.key
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.SetRequestData(
                                        roleKey.value,
                                        teamId.ifEmpty { UserStorage.teamId }
                                    )
                                )
                                showSwapDialog.value = true
                                homeVm.onEvent(HomeScreenEvent.OnSwapClick())
                            } else {
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.OnInviteTeamMembers(
                                        teamId,
                                        type = AppConstants.TYPE_CREATE_TEAM,
                                    )
                                )
                            }
                        }
                    }

                    /*if (!state.isLoading) {
                        if (teamId.isNullOrEmpty()) {
                            vm.onEvent(TeamSetupUIEventUpdated.OnAddPlayerScreenNext)
                        } else {
                            if (homeState.user.phone != state.inviteList[0].countryCode + state.inviteList[0].contact) {
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.OnInviteTeamMembers(
                                        teamId,
                                        type = AppConstants.TYPE_CREATE_TEAM,
                                    )
                                )
                            } else {
                                roleKey.value = state.inviteList[0].role.key
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.SetRequestData(
                                        roleKey.value,
                                        teamId.ifEmpty { UserStorage.teamId }
                                    )
                                )
                                showSwapDialog.value = true
                                homeVm.onEvent(HomeScreenEvent.OnSwapClick())
                            }
                        }
                    }*/
                },
                enableState = !state.isLoading && state.inviteList.isNotEmpty() &&
                        state.inviteList.all { it.name.isNotEmpty() && it.contact.isNotEmpty() && it.contact.length >= 10 },
                themed = true,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
        }
    }

    if (state.showDialog) {
        DeleteDialog(
            item = state.removePlayer,
            message = stringResource(id = R.string.alert_remove_player),
            onDismiss = {
                vm.onEvent(TeamSetupUIEventUpdated.OnDismissDialogCLick(false))
            },
            onDelete = {
                if (state.removePlayer != null) {
                    vm.onEvent(TeamSetupUIEventUpdated.OnRemovePlayerConfirmClick(state.removePlayer))
                }
            }
        )
    }
    if (showSwapDialog.value) {
        val index = CommonUtils.getIndex(homeState.user.phone, state.inviteList)
        val name = if (index == -1) {
            profileName.value.ifEmpty { "user" }
        } else state.inviteList[index].name
        SwapProfile(
            defaultCase = true,
            title = stringResource(id = R.string.select_profile_title).replace(
                "user", name
            ),
            actionButtonText = stringResource(id = R.string.invite),
            users = CommonUtils.filterUsers(
                state.inviteList,
                state.memberList,
                homeState.swapUsers as ArrayList
            ),
            onDismiss = { showSwapDialog.value = false },
            onConfirmClick = { swapUser ->
                if (index != -1) {
                    vm.onEvent(
                        TeamSetupUIEventUpdated.AddInviteTeamMembers(
                            homeState.user.phone,
                            index,
                            userType = state.role,
                            teamId = state.teamId,
                            member = swapUser,
                            role = roleKey.value,
                        )
                    )
                } else {
                    vm.onEvent(
                        TeamSetupUIEventUpdated.OnInviteTeamMembers(
                            state.teamId,
                            type = AppConstants.TYPE_CREATE_TEAM,
                        )
                    )
                }
                /*showSwapDialog.value = false
                homeVm.onEvent(HomeScreenEvent.HideSwap(false))*/
                /* vm.onEvent(
                       TeamSetupUIEventUpdated.OnInviteTeamMembers(
                           state.teamId,
                           userType = state.role,
                           type = AppConstants.TYPE_CREATE_TEAM,
                           profilesSelected = true,
                           member = Members(
                               name = swapUser.firstName,
                               mobileNumber = swapUser._Id,
                               role = roleKey.value
                           )
                       )
                   )*/
                showSwapDialog.value = false
                homeVm.onEvent(HomeScreenEvent.HideSwap(false))
            },
            showLoading = homeState.isDataLoading,
            onCreatePlayerClick = {
                profileName.value = name
                addProfileClick()
            },
            showCreatePlayerButton = true
        )
    }

    if (state.isLoading) {
        CommonProgressBar()
    }
}


@Composable
fun InviteItem(
    item: InviteObject,
    index: Int,
    vm: SetupTeamViewModelUpdated,
    focusRequester: FocusRequester,
    context: Activity,
    roles: List<UserRoles>,
    updateItem: (Int, Boolean) -> Unit
) {
    val roleObject = remember { mutableStateOf(UserRoles()) }
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val getDefaultPhoneCode = getDefaultPhoneCode(context)

    LaunchedEffect(key1 = Unit) {
        vm.onEvent(
            TeamSetupUIEventUpdated.OnCountryValueChange(
                index = index,
                code = getDefaultPhoneCode
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppSearchOutlinedTextField(
                modifier = Modifier
                    .weight(0.7f)
                    .focusRequester(focusRequester),
                value = item.name,
                onValueChange = { name ->
                    if (name.length <= maxChar)
                        vm.onEvent(
                            TeamSetupUIEventUpdated.OnNameValueChange(
                                index = index,
                                name
                            )
                        )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ColorBWGrayBorder,
                    unfocusedBorderColor = ColorBWGrayBorder,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    backgroundColor = MaterialTheme.appColors.material.background
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.name),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = MaterialTheme.appColors.textField.label,
                    )
                },
                singleLine = true,
                isError = !validName(item.name)
                        && item.name.isNotEmpty(),
                errorMessage = stringResource(id = R.string.valid_first_name),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    EditFields(
                        roleObject.value.value,
                        textStyle = TextStyle().copy(textAlign = TextAlign.Start),
                        onValueChange = {},
                        head = "",
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.select_role),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                color = MaterialTheme.appColors.textField.label,
                            )
                        },
                        modifier = Modifier
                            .onGloballyPositioned {
                                textFieldSize = it.size.toSize()
                            }
                            .border(
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                width = dimensionResource(id = R.dimen.size_1dp),
                                color = ColorBWGrayBorder
                            ),
                        trailingIcon = {
                            Icon(
                                painterResource(id = R.drawable.ic_arrow_down),
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
                        roles.forEach { label ->
                            DropdownMenuItem(onClick = {
                                roleObject.value = label

                                vm.onEvent(
                                    TeamSetupUIEventUpdated.OnRoleValueChange(
                                        index = index,
                                        role = label
                                    )
                                )
                                expanded = false
                            }) {
                                Text(
                                    text = label.value,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
            Box(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_40dp))
                    .background(
                        MaterialTheme.appColors.material.primary,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable {
                        updateItem(index, false)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "",
                    tint = Color.Unspecified,
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        Row(
            Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    color = MaterialTheme.appColors.editField.borderUnFocused,
                    shape = RoundedCornerShape(
                        dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(horizontal = dimensionResource(id = R.dimen.size_8dp)),
            verticalAlignment = Alignment.CenterVertically,

            ) {

            TogiCountryCodePicker(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                pickedCountry = {
                    vm.onEvent(
                        TeamSetupUIEventUpdated.OnCountryValueChange(
                            index = index,
                            code = it.countryPhoneCode
                        )
                    )
                    defaultLang = it.countryCode
                },
                defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                dialogAppBarTextColor = Color.Black,
                showCountryFlag = false,
                dialogAppBarColor = Color.White,
                error = true,
                text = item.contact,
                onValueChange = { contact ->
                    if (contact.length <= 10)
                        vm.onEvent(
                            TeamSetupUIEventUpdated.OnEmailValueChange(
                                index = index,
                                contact
                            )
                        )

                },
                readOnly = false,
                cursorColor = Color.Black,
                placeHolder = {
                    Text(
                        text = stringResource(id = R.string.phone_num),
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = MaterialTheme.appColors.textField.label,
                    )
                },
                content = {
                },
                textStyle = TextStyle(textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_40dp))
                    .clickable {
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
                            context.startActivityForResult(
                                intent,
                                AppConstants.REQUEST_CONTACT_CODE,
                                null
                            )
                        } else {
                            requestContactPermission(context, context)
                        }
                    }
            )
        }
    }
}

@Composable
fun AddRemoveButton(icon: Painter, teamColor: String, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.size_20dp))
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                color = Color(android.graphics.Color.parseColor("#${getCustomColorCode(teamColor)}"))
            )
    ) {

        Icon(
            painter = icon, contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(dimensionResource(id = R.dimen.size_20dp))
                .clickable(onClick = { onItemClick() }),
            tint = Color.White
        )
    }
}

@Composable
fun TeamMemberItem(
    count: Int,
    data: Members,
    isExpanded: Boolean = false,
    header: Boolean = false
) {

    Box(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_16dp)
            ),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + data.profileImage,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .border(
                        dimensionResource(id = R.dimen.size_2dp),
                        MaterialTheme.colors.surface,
                        CircleShape,
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                onError = { Placeholder(R.drawable.ic_team_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
            Text(
                text = data.name,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            val color = when (data.status) {
                "Pending" -> {
                    MaterialTheme.appColors.textField.labelDark
                }
                "Declined" -> {
                    ColorButtonRed
                }
                "Accepted" -> {
                    GreenColor
                }
                else -> {
                    MaterialTheme.appColors.buttonColor.bckgroundEnabled
                }
            }

            Text(
                text = if (header) "$count ${stringResource(id = R.string.invited)}" else data.status,
                color = color,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.SemiBold
            )
            if (header) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                    contentDescription = "",
                    modifier = Modifier
                        .size(
                            height = dimensionResource(id = R.dimen.size_12dp),
                            width = dimensionResource(id = R.dimen.size_12dp)
                        )
                        .then(
                            if (!isExpanded) Modifier else Modifier.rotate(180f)
                        ),
                    tint = ColorGreyLighter
                )
            }
        }
    }
}
