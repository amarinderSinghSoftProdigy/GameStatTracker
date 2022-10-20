package com.softprodigy.ballerapp.ui.features.home.invitation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.apiToUIDateFormat
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.EmptyScreen
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.SetupTeamViewModelUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.TeamSetupUIEventUpdated
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.hasContactPermission
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.requestContactPermission
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun InvitationScreen(vmSetupTeam: SetupTeamViewModelUpdated,onNewProfileIntent:(countryCode:String,mobileNumber:String)-> Unit) {
    val vm: InvitationViewModel = hiltViewModel()
    val state = vm.invitationState.value
    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        vm.invitationChannel.collect {
            when (it) {
                is InvitationChannel.ShowToast -> {
                    Toast.makeText(context, it.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                is InvitationChannel.Success -> {
                    Toast.makeText(context, it.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                    vm.getAllInvitation()
                }
            }
        }
    }

    if(state.invitations.isNotEmpty()){
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(state.invitations) { invitation ->
                    InvitationItem(invitation = invitation, onAcceptCLick = {
                        vm.onEvent(InvitationEvent.OnAcceptCLick(it))
                    }, onDeclineCLick = {
                        vm.onEvent(InvitationEvent.OnDeclineCLick(it))
                    })
                }
            }
        }

    }
} else if(!state.showLoading){
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
                if (state.selectedRole.value == "Guardian") {
                    vm.onEvent(InvitationEvent.OnRoleConfirmClick)
                    vm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                } else {
                    vm.onEvent(InvitationEvent.OnInvitationConfirm)
                    vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                }
            },
            onSelectionChange = { vm.onEvent(InvitationEvent.OnRoleClick(role = it)) },
            title = stringResource(id = R.string.what_is_your_role),
            selected = state.selectedRole,
            showLoading = state.showLoading,
            roleList = state.roles
        )
    }

    if (state.showGuardianDialog) {
        SelectGuardianRoleDialog(onBack = {
            vm.onEvent(InvitationEvent.OnRoleDialogClick(true))
            vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
        },
            onConfirmClick = {
                vm.onEvent(InvitationEvent.OnInvitationConfirm)
                vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
            },
            onSelectionChange = { vm.onEvent(InvitationEvent.OnGuardianClick(guardian = it)) },
            title = stringResource(
                R.string.select_the_players_guardian
            ),
            selected = state.selectedGuardian,
            showLoading = state.showLoading,
            guardianList = state.playerDetails,
            onValueSelected = {
                vm.onEvent(InvitationEvent.OnValuesSelected(it))
            },
            onDismiss = {
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
                vm.onEvent(InvitationEvent.OnClearGuardianValues)
            },
            onChildNotListedCLick = {
                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(true))
            }
        )
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
        InviteTeamMembersDialog(
            onBack = {
                vm.onEvent(InvitationEvent.OnRoleDialogClick(true))
                vm.onEvent(InvitationEvent.OnGuardianDialogClick(true))
                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
            },
            onConfirmClick = {
//                onNewProfileIntent.invoke("+12","8888888888")
//                vm.onEvent(InvitationEvent.OnRoleDialogClick(false))
//                vm.onEvent(InvitationEvent.OnGuardianDialogClick(false))
//                vm.onEvent(InvitationEvent.OnAddPlayerDialogClick(false))
            },
            onIndexChange = { index ->
                vmSetupTeam.onEvent(
                    TeamSetupUIEventUpdated.OnIndexChange(
                        index = index
                    )
                )

               /* when (PackageManager.PERMISSION_GRANTED) {
                    //First time asking for permission ... to be granted by user
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_CONTACTS
                    ) -> {
                        launchContactForResult.launch(contactIntent)
                    }
                    else -> {
                        //If permission has been already granted
                        launchContactPermission.launch(Manifest.permission.READ_CONTACTS)
                    }
                }*/
                if (hasContactPermission(context)) {
                    val intent =
                        Intent(Intent.ACTION_GET_CONTENT)
                    intent.type =
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    (context as Activity ).startActivityForResult(
                        intent,
                        AppConstants.REQUEST_CONTACT_CODE,
                        null
                    )
                } else {
                    requestContactPermission(context, (context as Activity ))
                }
            },
            inviteList = vmSetupTeam.teamSetupUiState.value.inviteList,
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
            }
        )
    }
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
                src = BuildConfig.IMAGE_SERVER + invitation.team.logo,
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

                Text(
                    text = " ${stringResource(id = R.string.sent_by)} ${invitation.createdBy.name}",
                    color = MaterialTheme.appColors.textField.label,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Box(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_44dp))
            ) {
                Text(
                    text = apiToUIDateFormat(invitation.createdAt),
                    color = MaterialTheme.appColors.textField.label,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
            }
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