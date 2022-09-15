package com.softprodigy.ballerapp.ui.features.home.invitation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.apiToUIDateFormat
import com.softprodigy.ballerapp.ui.features.components.DeleteDialog
import com.softprodigy.ballerapp.ui.features.components.SelectInvitationRoleDialog
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayStatus
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@Composable
fun InvitationScreen(vm: InvitationViewModel = hiltViewModel()) {
    val state = vm.invitationState.value
    val coroutineScope = rememberCoroutineScope()
    remember {
        coroutineScope.launch {
            vm.getAllInvitation()
        }
    }
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
//                    vm.onEvent(InvitationEvent.OnDeclineInvitationClick(invitation = it))

                    })
                }
            }

        }
        if (state.showLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.appColors.material.primaryVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
    if (state.showRoleDialog) {
        SelectInvitationRoleDialog(
            onDismiss = { vm.onEvent(InvitationEvent.OnRoleDialogClick(false)) },
            onConfirmClick = { vm.onEvent(InvitationEvent.OnRoleConfirmClick) },
            onSelectionChange = { vm.onEvent(InvitationEvent.OnRoleClick(role = it)) },
            title = stringResource(id = R.string.what_is_your_role),
            selected = state.selectedRole,
            showLoading = state.showLoading,
            roleList = state.roles
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
            AsyncImage(
                model = BuildConfig.IMAGE_SERVER + invitation.team.logo, contentDescription = "",
                modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .clip(CircleShape)
                    .background(
                        color = ColorBWGrayStatus,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.FillBounds
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
                    text = " ${stringResource(id = R.string.sent_by)} ${invitation.name}",
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
        } else if (invitation.status.equals(InvitationStatus.ACCEPT.status, ignoreCase = true)) {
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
        } else if (invitation.status.equals(InvitationStatus.REJECT.status, ignoreCase = true)) {
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