package com.allballapp.android.ui.features.home.events.opportunities

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.common.validTeamName
import com.allballapp.android.data.UserStorage
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventChannel
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.features.home.teams.TeamUIEvent
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.theme.*

@Composable
fun EventRegistraionDetails(
    vm: EventViewModel,
    teamVm: TeamViewModel,
    moveToPrivacy: (String) -> Unit,
    moveToTermsAndConditions: (String) -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state = vm.eventState.value
    val teamState = teamVm.teamUiState.value
    val maxCash = 10
    var toast : Toast? = null
    val paymentOption = remember {
        mutableStateOf(false)
    }
    remember {
        vm.onEvent(EvEvents.GetDivisions(state.selectedEventId))
    }
    val showError = remember {
        mutableStateOf(false)
    }
    val showDialog = remember {
        mutableStateOf(false)
    }
    val showPlayerDialog = remember {
        mutableStateOf(false)
    }
    val showDivisionDialog = remember {
        mutableStateOf(false)
    }
    val showPaymentDialog = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        vm.eventChannel.collect { uiEvent ->
            when (uiEvent) {
                is EventChannel.ShowToast -> {
                    if(toast != null)
                        toast?.cancel()

                     toast = Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    )
                    toast?.show()
                }
                is EventChannel.OnSuccess -> {
                    if(toast != null)
                        toast?.cancel()
                     toast = Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    )
                    toast?.show()
                    onSuccess()
                }
            }
        }
    }

    LaunchedEffect(true) {
        if (teamVm.teamUiState.value.teams.isNotEmpty()) {
            teamVm.teamUiState.value.teams.forEach {
                if (UserStorage.teamId == it._id) {
                    vm.onEvent(EvEvents.RegisterTeam(it))
                    teamVm.onEvent(TeamUIEvent.OnTeamIdSelected(it._id))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Heading(stringResource(id = R.string.team_info))
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.size_56dp))
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.team),
                        style = MaterialTheme.typography.caption,
                        color = ColorBWBlack,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Row(
                        modifier = Modifier.clickable { showDialog.value = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (state.registerRequest.team.isNotEmpty()) {
                            CoilImage(
                                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + state.team.logo,
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
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                            Text(
                                text = state.team.name,
                                style = MaterialTheme.typography.h6,
                                color = ColorBWBlack,
                                fontSize = dimensionResource(R.dimen.txt_size_14).value.sp,
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.choose_team),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.appColors.textField.label,
                            )
                        }
                    }
                }
                DividerCommon()
                RegisterItem(
                    stringResource(id = R.string.players),
                    if (state.registerRequest.players.isNotEmpty()) state.registerRequest.players.size.toString() else stringResource(
                        id = R.string.choose_playerr
                    ),
                    updated = state.registerRequest.players.isNotEmpty()
                ) {
                    if (state.registerRequest.team.isEmpty()) {
                        vm.onEvent(EvEvents.ShowToast(context.getString(R.string.no_team_selected)))
                    } else if (teamState.playersList.isEmpty()) {
                        vm.onEvent(EvEvents.ShowToast(context.getString(R.string.no_players_change_selection)))
                    } else {
                        showPlayerDialog.value = true
                    }
                }
                DividerCommon()
                RegisterItem(
                    stringResource(id = R.string.division),
                    if (state.registerRequest.division.isNotEmpty()) state.divisionData.divisionName
                    else stringResource(id = R.string.choose_division),
                    updated = state.registerRequest.division.isNotEmpty()
                ) {
                    showDivisionDialog.value = true
                }
                DividerCommon()

                RegisterItem(
                    stringResource(id = R.string.payment_options),
                    if (state.registerRequest.paymentOption.isNotEmpty()) state.registerRequest.paymentOption else stringResource(
                        id = R.string.payment_method
                    ),
                    updated = state.registerRequest.paymentOption.isNotEmpty()
                ) {
                    showPaymentDialog.value = true
                }
                AppOutlineTextField(
                    isError = showError.value && !validTeamName(state.registerRequest.payment) && state.registerRequest.payment.isNotEmpty(),
                    /* leadingIcon = {
                         *//*AppText(
                            text = stringResource(id = R.string.dollar),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                        )*//*
                    },*/
                    value = state.registerRequest.payment,
                    onValueChange = {
                        if (state.registerRequest.paymentOption.isEmpty() && !paymentOption.value) {
                            vm.onEvent(EvEvents.ShowToast(context.getString(R.string.please_select_payment_method)))
                            paymentOption.value = true
                        } else if (state.registerRequest.paymentOption.isNotEmpty()) {
                            if (it.isNotEmpty() || it != "0") {
                                showError.value = false
                                if (it.length <= maxCash) {
                                    vm.onEvent(EvEvents.RegisterCash(getValidatedNumber(it)))
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Decimal
                    ),
                    placeholder = {
                        AppText(
                            text = if (state.registerRequest.paymentOption.isEmpty()) stringResource(
                                id = R.string.payment_details
                            ) else state.registerRequest.paymentOption + " details",
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = ColorBWGrayBorder,
                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    ),
                    errorMessage = stringResource(id = R.string.cash_message)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            /*}
            Heading(stringResource(id = R.string.division))
            UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {*/
            /* Column(
                 modifier = Modifier
                     .fillMaxSize(),
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 *//* RegisterItem(
                     stringResource(id = R.string.division),
                     "Division 1"
                 )*//*
                DividerCommon()
                Row(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.size_56dp))
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.send_push_notification),
                        style = MaterialTheme.typography.caption,
                        color = ColorBWBlack,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Switch(
                        checked = state.registerRequest.sendPushNotification,
                        onCheckedChange = {
                            vm.onEvent(EvEvents.RegisterNotification(it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                        )
                    )
                }
            }*/
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            color = Color.White,
            elevation = 1.dp,
            shape = RoundedCornerShape(
                dimensionResource(id = R.dimen.size_8dp)
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
                        modifier = Modifier.clickable {
                            moveToTermsAndConditions("")
                        }
                    )
                }
                DividerCommon()
                Surface(color = Color.White)
                {

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
                            text = stringResource(id = R.string.loren_ipsum) + " ",
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = md_theme_light_onSurface,
                        )
                        AppText(
                            text = stringResource(id = R.string.privacy),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = ColorMainPrimary,
                            modifier = Modifier.clickable {
                                moveToPrivacy("")
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

        AppButton(
            enabled = true,
            icon = null,
            themed = true,
            onClick = {
                var message = ""
                if (state.registerRequest.team.isEmpty()) {
                    message = context.getString(R.string.no_team_selected)
                } else if (state.registerRequest.division.isEmpty()) {
                    message = context.getString(R.string.please_select_division)
                } else if (state.registerRequest.players.isEmpty()) {
                    message = context.getString(R.string.please_select_player)
                } else if (state.registerRequest.payment.isEmpty() || state.registerRequest.payment == "0") {
                    showError.value = true
                } else if (!state.registerRequest.termsAndCondition || !state.registerRequest.privacy) {
                    message = context.getString(R.string.please_accept_tems)
                } else if (state.registerRequest.payment.toDouble() < state.opportunitiesDetail.standardPrice.toDouble()) {
                    message = context.getString(R.string.valid_cash_message)
                }
                if (message.isNotEmpty()) {
                    vm.onEvent(EvEvents.ShowToast(message))
                } else if (!showError.value) {
                    vm.onEvent(EvEvents.RegisterForEvent)
                }
            },
            text = stringResource(id = R.string.register),
            isForceEnableNeeded = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
    }

    if (showDialog.value) {
        SwitchTeamDialog(
            teamSelect = state.team,
            teams = teamVm.teamUiState.value.teams,
            title = stringResource(id = R.string.select_team),
            onDismiss = {
                showDialog.value = false
            },
            onConfirmClick = {
                vm.onEvent(EvEvents.RegisterTeam(it))
                teamVm.onEvent(TeamUIEvent.OnTeamIdSelected(it._id))
                showDialog.value = false

                if (state.team != it) {
                    vm.onEvent(EvEvents.ClearRegisterData)
                }
            }
        )
    }
    if (showPlayerDialog.value) {
        SwitchPlayerDialog(
            player = state.registerRequest.players,
            teams = teamVm.teamUiState.value.playersList,
            title = stringResource(id = R.string.select_player),
            onDismiss = {
                showPlayerDialog.value = false
//                vm.onEvent(EvEvents.ClearPlayer)
            },
            onConfirmClick = {
                vm.onEvent(EvEvents.RegisterPlayer(it))
                showPlayerDialog.value = false
            },
        )
    }

    if (showPaymentDialog.value) {
        PaymentPickerDialog(onDismiss = {
            showPaymentDialog.value = false
        }, onConfirmClick = {
            vm.onEvent(EvEvents.PaymentOption(it))
            showPaymentDialog.value = false
        },
            selectedOption = state.registerRequest.paymentOption
        )
    }

    if (showDivisionDialog.value && state.eventDivision.isNotEmpty()) {
        SelectDivisionDialog(
            division = state.divisionData,
            teams = state.eventDivision,
            title = stringResource(id = R.string.select_division),
            onDismiss = {
                showDivisionDialog.value = false
             /*   vm.onEvent(EvEvents.ClearDivisionData)*/
            },
            onConfirmClick = {
                vm.onEvent(EvEvents.RegisterDivision(it))
                showDivisionDialog.value = false
            },
            onDivisionSelection = {
                vm.onEvent(EvEvents.RegisterDivision(it))
            }
        )
    }

    if (state.isLoading || teamState.isLoading) {
        CommonProgressBar()
    }
}

@Composable
fun Heading(title: String) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {
        Text(
            text = title,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun RegisterItem(
    title: String,
    value: String,
    showIcon: Boolean = true,
    updated: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_56dp))
            .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = title,
            style = MaterialTheme.typography.caption,
            color = ColorBWBlack,
            modifier = Modifier
                .weight(0.8f)
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F)
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            AppText(
                text = value,
                textAlign = TextAlign.End,
                style = if (!updated) MaterialTheme.typography.h5 else MaterialTheme.typography.h2,
                color = if (!updated) MaterialTheme.appColors.textField.label else ColorBWBlack,
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_12dp))
            )
            if (showIcon)
                Icon(
                    modifier = Modifier
                        .clickable {
                            onClick()
                        },
                    painter = painterResource(id = R.drawable.ic_forward),
                    contentDescription = "", tint = ColorGreyLighter
                )
        }
    }
}