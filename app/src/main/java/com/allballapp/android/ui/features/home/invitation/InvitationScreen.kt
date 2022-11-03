package com.allballapp.android.ui.features.home.invitation

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.apiToUIDateFormat
import com.allballapp.android.data.request.Members
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.HomeChannel
import com.allballapp.android.ui.features.home.HomeViewModel
import com.allballapp.android.ui.features.home.home_screen.HomeScreenEvent
import com.allballapp.android.ui.features.sign_up.SignUpChannel
import com.allballapp.android.ui.features.sign_up.SignUpViewModel
import com.allballapp.android.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.allballapp.android.ui.features.user_type.team_setup.updated.TeamSetupChannel
import com.allballapp.android.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.allballapp.android.ui.theme.ColorButtonGreen
import com.allballapp.android.ui.theme.ColorButtonRed
import com.allballapp.android.ui.theme.appColors
import timber.log.Timber

@Composable
fun InvitationScreen(
    refreshProfileList: String,
    vmSetupTeam: SetupTeamViewModelUpdated,
    homeVm: HomeViewModel,
    signUpViewModel: SignUpViewModel,
    onNewProfileIntent: (countryCode: String, mobileNumber: String) -> Unit,
    onInvitationSuccess: () -> Unit,
    addProfileClick: () -> Unit,
    onInviteClick: (teamId: String) -> Unit,
) {
    val vm: InvitationViewModel = hiltViewModel()
    val state = vm.invitationState.value
    val context = LocalContext.current
    val teamState = vmSetupTeam.teamSetupUiState.value
    val homeState = homeVm.state.value

    val showSwapDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val roleKey = rememberSaveable {
        mutableStateOf("")
    }

    if (vmSetupTeam.teamSetupUiState.value.isLoading) {
        CommonProgressBar()
    }

    if (refreshProfileList == true.toString()) {
        LaunchedEffect(key1 = refreshProfileList, block = {
            homeVm.onEvent(HomeScreenEvent.OnSwapClick)

        })
    }
    LaunchedEffect(key1 = Unit) {
        vmSetupTeam.teamSetupChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamSetupChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is TeamSetupChannel.OnInvitationSuccess -> {
                    vm.onEvent(InvitationEvent.OnRoleConfirmClick)
                    vm.onEvent(InvitationEvent.OnRoleDialogClick(true))
                    vm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                    vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))


                }
                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        signUpViewModel.signUpChannel.collect { uiEvent ->
            when (uiEvent) {
                is SignUpChannel.OnProfileUpdateSuccess -> {
                    homeVm.onEvent(HomeScreenEvent.OnSwapClick)
                    Timber.i("HomeChannel.OnProfileUpdateSuccess")

                }
                else -> Unit
            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        homeVm.homeChannel.collect { uiEvent ->
            when (uiEvent) {

                is HomeChannel.OnSwapListSuccess -> {
                    vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
                    showSwapDialog.value = true
                    Timber.i("HomeChannel.OnSwapListSuccess")
                }

            }
        }
    }

    if (state.invitations.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.invitations) { invitation ->
                        InvitationItem(invitation = invitation, onAcceptCLick = {
                            vm.onEvent(InvitationEvent.OnRoleClick(roleKey = it.role))
                            vm.onEvent(InvitationEvent.OnAcceptCLick(it))
                        }, onDeclineCLick = {
                            vm.onEvent(InvitationEvent.OnDeclineCLick(it))
                        })
                    }
                }
            }

        }
    } else if (!state.showLoading) {
        EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))

    }
    if (state.showLoading) {
        CommonProgressBar()
    }
    if (state.showRoleDialog) {
        SelectInvitationRoleDialog(
            onDismiss = {
                vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                vm.onEvent(InvitationEvent.OnClearValues)
            },
            onConfirmClick = { /*vm.onEvent(InvitationEvent.OnRoleConfirmClick)*/

                if (state.selectedRoleKey == UserType.PARENT.key || (state.selectedRoleKey == UserType.PLAYER.key)) {
                    vm.onEvent(InvitationEvent.OnRoleConfirmClick)
                    vm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                } else {
                    vm.onEvent(InvitationEvent.OnInvitationConfirm(homeState.user.gender))
                    vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                }
            },
            onSelectionChange = { vm.onEvent(InvitationEvent.OnRoleClick(roleKey = it)) },
            title = stringResource(
                id = R.string.what_is_your_role,
                state.selectedInvitation.team.name
            ),
            selected = state.selectedRoleKey,
            showLoading = state.showLoading,
            roleList = state.roles,
            userName = "${homeState.user.firstName} ${homeState.user.lastName}",
            userLogo = com.allballapp.android.BuildConfig.IMAGE_SERVER + homeState.user.profileImage,
        )
    }

    if (state.showGuardianDialog) {
        SelectGuardianRoleDialog(
            state.selectedRoleKey,
            onBack = {
                vm.onEvent(InvitationEvent.OnRoleDialogClick(true))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
            },
            onConfirmClick = {
                vm.onEvent(InvitationEvent.OnInvitationConfirm(homeState.user.gender))
                vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
            },
            onSelectionChange = { vm.onEvent(InvitationEvent.OnGuardianClick(guardian = it)) },
            selected = state.selectedGuardian,
            guardianList = if (state.selectedRoleKey == UserType.PARENT.key)
                state.playerDetails.filter { member -> member.role == UserType.PLAYER.key }
            else state.playerDetails.filter { member -> member.role != UserType.PLAYER.key },
            onValueSelected = {
                vm.onEvent(InvitationEvent.OnValuesSelected(it))
            },
            onDismiss = {
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                vm.onEvent(InvitationEvent.OnClearGuardianValues)
            },
            onChildNotListedCLick = {
                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(true))
            },
            dontHaveChildClick = {
                vm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(true))

            }
        )
    }

    if (state.showGuardianOnlyConfirmDialog) {
        ConfirmDialog(
            title = stringResource(id = R.string.join_team_without_child_selection), onDismiss = {
                vm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(false))
            },
            onConfirmClick = {
                vm.onEvent(InvitationEvent.OnInvitationConfirm(homeState.user.gender))
                vm.onEvent(InvitationEvent.ConfirmGuardianWithoutChildAlert(false))
                vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
            })
    }


    if (state.showDeclineDialog) {
        DeleteDialog(
            item = state.selectedInvitation,
            message = stringResource(id = R.string.alert_decline_invitation),
            onDismiss = {
                vm.onEvent(InvitationEvent.OnDeleteDialogClick(false))
            },
            onDelete = {
                if (state.selectedInvitation.id.isNotEmpty()) {
                    vm.onEvent(InvitationEvent.OnDeclineConfirmClick(state.selectedInvitation))
                }
            }
        )
    }

    if (state.showAddPlayerDialog) {
        vmSetupTeam.initialInviteCount(1)

        InviteTeamMembersDialog(
            onBack = {
                vm.onEvent(InvitationEvent.OnRoleDialogClick(true))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
            },
            onConfirmClick = {
//                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))

                if (state.selectedInvitation.team._id.isNotEmpty()) {

                    if (teamState.inviteList.isNotEmpty()) {
                        /*Check if user is entering his own number or not*/
                        if (homeState.user.phone != teamState.inviteList[0].countryCode + teamState.inviteList[0].contact) {
                            vmSetupTeam.onEvent(
                                TeamSetupUIEventUpdated.OnInviteTeamMembers(
                                    teamId = state.selectedInvitation.team._id,
                                    userType = state.selectedRoleKey,
                                    type = AppConstants.TYPE_ACCEPT_INVITATION
                                )
                            )
                        } else {
                            /* show option to select his own profile*/
                            homeVm.onEvent(HomeScreenEvent.OnSwapClick)
                        }
                    }
                }
            },
            onIndexChange = { index ->
                vmSetupTeam.onEvent(
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
            inviteList = teamState.inviteList,
            onNameValueChange = { index, name ->
                vmSetupTeam.onEvent(
                    TeamSetupUIEventUpdated.OnNameValueChange(
                        index = index,
                        name
                    )
                )

            },
            onEmailValueChange = { index, email ->
                vmSetupTeam.onEvent(
                    TeamSetupUIEventUpdated.OnEmailValueChange(
                        index = index,
                        email
                    )
                )

            },
            onInviteCountValueChange = { addIntent ->
                vmSetupTeam.onEvent(TeamSetupUIEventUpdated.OnInviteCountValueChange(addIntent = true))
            },
            OnCountryValueChange = { index, code ->
                vmSetupTeam.onEvent(
                    TeamSetupUIEventUpdated.OnCountryValueChange(
                        index = index,
                        code = code
                    )
                )
            }, roles = state.roles, onRoleValueChange = { index, role ->
                vmSetupTeam.onEvent(
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
                vmSetupTeam.onEvent(
                    TeamSetupUIEventUpdated.OnInviteTeamMembers(
                        state.selectedInvitation.team._id,
                        userType = state.selectedRoleKey,
                        type = AppConstants.TYPE_ACCEPT_INVITATION,
                        profilesSelected = true,
                        member = Members(
                            name = swapUser.firstName,
                            mobileNumber = swapUser._Id,
                            role = roleKey.value
                        )
                    )
                )

                showSwapDialog.value = false
            },
            showLoading = homeState.isDataLoading,
            onCreatePlayerClick = {
                addProfileClick()
            },
            showCreatePlayerButton = true
        )
    }

    /* if(state.showPlayerAddedSuccessDialog){
         InvitationSuccessfullySentDialog(
             onDismiss = {
                 vm.onEvent(InvitationEvent.OnPlayerAddedSuccessDialog(false))
             },
             onConfirmClick = {
                 onInviteClick.invoke(state.selectedInvitation.team._id)
                 vm.onEvent(InvitationEvent.OnPlayerAddedSuccessDialog(false))
                 vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                 vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))

             },
             teamLogo = BuildConfig.IMAGE_SERVER + state.selectedInvitation.team.logo,
             teamName = state.selectedInvitation.team.name,
             playerName = if (teamState.inviteList.isNotEmpty()) teamState.inviteList[0].name else ""
         )
     }*/
}


@Composable
fun InvitationItem(
    modifier: Modifier = Modifier,
    invitation: Invitation,
    onAcceptCLick: (Invitation) -> Unit,
    onDeclineCLick: (Invitation) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface, shape = RoundedCornerShape(
                        topStart = dimensionResource(
                            id = R.dimen.size_8dp
                        ),
                        topEnd = dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + invitation.team.logo,
                modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .clip(CircleShape)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                onError = { Placeholder(R.drawable.ic_team_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            Column(Modifier.weight(1f)) {
                Text(
                    text = invitation.team.name,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = " ${stringResource(id = R.string.sent_by)} ${invitation.createdBy.name}",
                        color = MaterialTheme.appColors.textField.label,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )

                    Text(
                        text = apiToUIDateFormat(invitation.createdAt),
                        color = MaterialTheme.appColors.textField.label,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        }
        if (invitation.status.equals(InvitationStatus.PENDING.status, ignoreCase = true)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                        shape = RoundedCornerShape(
                            bottomStart = dimensionResource(
                                id = R.dimen.size_8dp
                            ),
                            bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .clickable {
                            onDeclineCLick.invoke(invitation)
                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross_2),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                        tint = MaterialTheme.appColors.material.primaryVariant
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = stringResource(id = R.string.decline),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,

                        )
                }

                Row(
                    Modifier
                        .weight(1f)
                        .clickable {
                            onAcceptCLick.invoke(invitation)
                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                        tint = MaterialTheme.appColors.material.primaryVariant
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = stringResource(id = R.string.accept),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }

            }
        } else if (invitation.status.equals(InvitationStatus.ACCEPT.status, ignoreCase = true) ||
            invitation.status.equals(InvitationStatus.ACCEPTED.status, ignoreCase = true)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorButtonGreen,
                        shape = RoundedCornerShape(
                            bottomStart = dimensionResource(
                                id = R.dimen.size_8dp
                            ),
                            bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                        )
                    )
                    .clickable {

                    }
                    .padding(dimensionResource(id = R.dimen.size_14dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                    tint = MaterialTheme.appColors.buttonColor.textEnabled,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = stringResource(id = R.string.accepted),
                    color = MaterialTheme.appColors.buttonColor.textEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W500,
                )
            }
        } else if (invitation.status.equals(InvitationStatus.REJECT.status, ignoreCase = true)
            || invitation.status.equals(InvitationStatus.DECLINED.status, ignoreCase = true)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = ColorButtonRed,
                        shape = RoundedCornerShape(
                            bottomStart = dimensionResource(
                                id = R.dimen.size_8dp
                            ),
                            bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                        )
                    )
                    .clickable {

                    }
                    .padding(dimensionResource(id = R.dimen.size_14dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cross_2),
                    contentDescription = "",
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                    tint = MaterialTheme.appColors.buttonColor.textEnabled,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = stringResource(id = R.string.declined),
                    color = MaterialTheme.appColors.buttonColor.textEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W500,
                )
            }
        }
    }
}
