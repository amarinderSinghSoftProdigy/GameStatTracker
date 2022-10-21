package com.softprodigy.ballerapp.ui.features.home.events.opportunities

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventChannel
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.md_theme_light_onSurface

@Composable
fun EventRefereeRegistrationScreen(vm: EventViewModel, onNextScreen: () -> Unit) {
    val state = vm.eventState.value
    val context = LocalContext.current

    val showRoleDialog = remember {
        mutableStateOf(false)
    }

    remember {
        vm.onEvent(EvEvents.GetRoles)
    }

    LaunchedEffect(key1 = Unit)
    {
        vm.eventChannel.collect { uiEvent ->
            when (uiEvent) {
                is EventChannel.ShowToast -> {
                     Toast.makeText(
                         context,
                         uiEvent.message.asString(context),
                         Toast.LENGTH_LONG
                     ).show()
                }
                is EventChannel.OnSuccess -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                    onNextScreen()
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        ),
                        color = Color.White
                    )
            ) {
                RegisterItem(
                    stringResource(id = R.string.register_as),
                    if (state.selectedRole.isNotEmpty()) state.selectedRole else stringResource(
                        id = R.string.select_role
                    ),
                    updated = state.registerGameStaff.role.isNotEmpty()
                ) {
                    showRoleDialog.value = true
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        ),
                        color = Color.White
                    )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    ) {
                        CustomCheckBox(
                            state.registerRequest.termsAndCondition
                        ) {
                            vm.onEvent(EvEvents.RegisterTerms(!state.registerRequest.termsAndCondition))
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = stringResource(id = R.string.i_agree) + " ",
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = md_theme_light_onSurface,
                        )
                        AppText(
                            text = stringResource(id = R.string.terms_cond),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = ColorMainPrimary,
                        )
                    }
                    DividerCommon()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    ) {
                        CustomCheckBox(
                            state.registerRequest.privacy
                        ) {
                            vm.onEvent(EvEvents.RegisterPrivacy(!state.registerRequest.privacy))
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = stringResource(id = R.string.privacy),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = ColorMainPrimary,
                        )
                    }
                }
            }
        }
        AppButton(
            enabled = true,
            icon = null,
            themed = true,
            onClick = {
                var message = ""
                if (!state.registerRequest.termsAndCondition || !state.registerRequest.privacy) {
                    message = context.getString(R.string.please_accept_tems)
                } else if(state.selectedRole.isEmpty()){
                    message = context.getString(R.string.select_role_first)
                }
                if (message.isNotEmpty()) {
                    vm.onEvent(EvEvents.ShowToast(message))
                } else {
                    vm.onEvent(EvEvents.RegisterGameStaff)
                }

            },
            text = stringResource(id = R.string.register),
            isForceEnableNeeded = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_16dp)
                )
                .align(Alignment.BottomCenter)
        )
    }

    if (showRoleDialog.value) {
        SelectInvitationRoleDialog(
            onDismiss = {
                showRoleDialog.value = false
            },
            onConfirmClick = {
                showRoleDialog.value = false
            },
            onSelectionChange = { vm.onEvent(EvEvents.OnRoleClick(it)) },
            title = stringResource(id = R.string.select_role),
            selected = state.selectedRole,
            showLoading = state.showLoading,
            roleList = state.roles,
            "",""
        )
    }
}
