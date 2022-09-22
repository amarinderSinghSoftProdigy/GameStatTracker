package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.sign_up.ClearRippleTheme
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.spacing
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreenUpdated(
    teamId: String? = "",
    vm: SetupTeamViewModelUpdated,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit,
    onInvitationSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state = vm.teamSetupUiState.value
    val focusRequester = remember { FocusRequester() }
    val maxChar = 45
    Timber.i("AddPlayersScreenUpdated-- teamId--$teamId")
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val email = dataStoreManager.getEmail.collectAsState(initial = "Kaushal")


    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val coachRoleList =
        listOf(stringResource(id = R.string.head_coach), stringResource(id = R.string.coach_label))
    var role by rememberSaveable {
        mutableStateOf("")
    }


    vm.onEvent(
        TeamSetupUIEventUpdated.OnCoachEmailChange(
            email.value
        )
    )

    BackHandler() {
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
                TeamSetupChannel.OnLogoUpload -> {
                    vm.onEvent(TeamSetupUIEventUpdated.OnLogoUploadSuccess)
                }
                is TeamSetupChannel.OnTeamCreate -> {
                    Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG)
                        .show()
                    onNextClick.invoke(uiEvent.message)
                }
                is TeamSetupChannel.OnInvitationSuccess -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
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
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

            UserFlowBackground(
                modifier = Modifier.weight(1F),
                color = MaterialTheme.appColors.buttonColor.textEnabled
            ) {
                Column(
                    Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top
                    ) {
                        AppSearchOutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            value = state.coachName,
//                            readOnly=true,
                            onValueChange = { name ->
                                /*  if (name.length <= maxChar)
                                      vm.onEvent(
                                          TeamSetupUIEventUpdated.OnNameValueChange(
                                              index = index,
                                              name
                                          )
                                      )*/
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.OnCoachNameChange(
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
                                    text = stringResource(id = R.string.your_name_),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.textField.label,
                                )
                            },
                            singleLine = true,
                            /*    isError = !validName(state.inviteMemberName[index])
                                        && state.inviteMemberName[index].isNotEmpty(),*/
//                                                || state.inviteMemberName[index].length > 30,
                            errorMessage = stringResource(id = R.string.valid_first_name),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                        /*  AppSearchOutlinedTextField(
                              modifier = Modifier
                                  .weight(1f)
                                  .focusRequester(focusRequester),
                              value = state.coachRole,
                              onValueChange = { role ->
                                  *//*  if (email.length <= maxChar)
                                      vm.onEvent(
                                          TeamSetupUIEventUpdated.OnEmailValueChange(
                                              index = index,
                                              email
                                          )
                                      )*//*
                                vm.onEvent(
                                    TeamSetupUIEventUpdated.OnCoachRoleChange(
                                       role))
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = ColorBWGrayBorder,
                                unfocusedBorderColor = ColorBWGrayBorder,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                backgroundColor = MaterialTheme.appColors.material.background
                            ),
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.role),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Email

                            ),
//                            isError = (!state.inviteMemberEmail[index].isValidEmail()
//                                    && state.inviteMemberEmail[index].isNotEmpty()
//                                    || state.inviteMemberEmail[index].length > 45),
                            errorMessage = stringResource(id = R.string.email_error)
                        )*/
                        CompositionLocalProvider(
                            LocalRippleTheme provides ClearRippleTheme
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(dimensionResource(id = R.dimen.size_56dp))
                                        .clickable() {
                                            expanded = !expanded
                                        }
                                        .onGloballyPositioned {
                                            textFieldSize = it.size.toSize()
                                        },
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.appColors.editField.borderUnFocused
                                    ),
                                    backgroundColor = MaterialTheme.appColors.material.background,
                                    shape = RoundedCornerShape(MaterialTheme.spacing.small),
                                    elevation = 0.dp
                                ) {
                                    Row(
                                        modifier = Modifier
//                                            .fillMaxSize()
                                            .padding(
                                                start = dimensionResource(id = R.dimen.size_12dp),
                                                end = dimensionResource(id = R.dimen.size_12dp)
                                            ),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        if (role.isEmpty()) {
                                            Text(
                                                text = stringResource(id = R.string.your_role),
                                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                                color = MaterialTheme.appColors.textField.label,
                                            )
                                        } else {
                                            AppText(
                                                text = role,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                                fontSize = dimensionResource(
                                                    id = R.dimen.txt_size_12
                                                ).value.sp
//                                                style = androidx.compose.material.LocalTextStyle.current
                                            )
                                        }
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(
                                                    height = dimensionResource(id = R.dimen.size_8dp),
                                                    width = dimensionResource(id = R.dimen.size_10dp)
                                                ),
                                            tint = MaterialTheme.appColors.buttonColor.textDisabled
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    coachRoleList.forEach { label ->
                                        DropdownMenuItem(onClick = {
                                            role = label
                                            expanded = false
                                        }) {
                                            Text(
                                                text = label,
                                                textAlign = TextAlign.Center,
                                                fontSize = dimensionResource(
                                                    id = R.dimen.txt_size_12
                                                ).value.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))

                    AppSearchOutlinedTextField2(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        readOnly = true,
                        value = state.coachEmail,
                        onValueChange = { email ->
                            vm.onEvent(
                                TeamSetupUIEventUpdated.OnCoachEmailChange(
                                    email
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
                                text = stringResource(id = R.string.email),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                color = MaterialTheme.appColors.textField.label,
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email

                        ),
//                            isError = (!state.inviteMemberEmail[index].isValidEmail()
//                                    && state.inviteMemberEmail[index].isNotEmpty()
//                                    || state.inviteMemberEmail[index].length > 45),
                        errorMessage = stringResource(id = R.string.email_error)
                    )
//                }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    //Below divider is used to cover parent container and ignoring padding of parent
                    Divider(
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        maxWidth = constraints.maxWidth + (context.resources.getDimension(
                                            R.dimen.size_32dp //add the end padding 32dp
                                        )).dp.roundToPx(),
                                    )
                                )
                                layout(placeable.width, placeable.height) {
                                    placeable.place(0, 0)
                                }
                            })

                    LazyColumn(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(state.inviteMemberCount) { index ->
                            Column(Modifier.fillMaxSize()) {

                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        AppSearchOutlinedTextField(
                                            modifier = Modifier
                                                .width(dimensionResource(id = R.dimen.size_100dp))
                                                .focusRequester(focusRequester),
                                            value = state.inviteMemberName[index],
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
                                            isError = !validName(state.inviteMemberName[index])
                                                    && state.inviteMemberName[index].isNotEmpty(),
//                                                || state.inviteMemberName[index].length > 30,
                                            errorMessage = stringResource(id = R.string.valid_first_name),
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                                        AppSearchOutlinedTextField(
                                            modifier = Modifier
                                                .weight(1f)
                                                .focusRequester(focusRequester),
                                            value = state.inviteMemberEmail[index],
                                            onValueChange = { email ->
                                                if (email.length <= maxChar)
                                                    vm.onEvent(
                                                        TeamSetupUIEventUpdated.OnEmailValueChange(
                                                            index = index,
                                                            email
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
                                                    text = stringResource(id = R.string.email),
                                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                                    color = MaterialTheme.appColors.textField.label,
                                                )
                                            },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Next,
                                                keyboardType = KeyboardType.Email

                                                ),
                                                isError = (!state.inviteMemberEmail[index].isValidEmail()
                                                        && state.inviteMemberEmail[index].isNotEmpty()
                                                        || state.inviteMemberEmail[index].length > 45),
                                                errorMessage = stringResource(id = R.string.email_error)
                                            )

                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                                    }
                                    if (state.inviteMemberName[index].isEmpty()
                                        && state.inviteMemberEmail[index].isEmpty()
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_remove),
                                            contentDescription = "",
                                            tint = Color.Unspecified,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .background(
                                                    AppConstants.SELECTED_COLOR,
                                                    shape = RoundedCornerShape(50)
                                                )
                                                .padding(dimensionResource(id = R.dimen.size_2dp))
                                                .clickable {
                                                    /*Remove a specific  item from list*/
                                                    updateItem(index, false)
                                                }
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))

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
                        }
                    }
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
                    if (teamId.isNullOrEmpty()) {
                        vm.onEvent(TeamSetupUIEventUpdated.OnAddPlayerScreenNext)
                    } else {
                        vm.onEvent(TeamSetupUIEventUpdated.OnInviteTeamMembers(teamId))

                    }
                },
                enableState =
                state.inviteMemberName.isNotEmpty() &&
                        state.inviteMemberName.all { it.isNotEmpty() } &&
                        state.inviteMemberName.all { validName(it) }
                        && state.inviteMemberEmail.all { it.isValidEmail() },
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
}
if (state.isLoading) {
    CommonProgressBar()
}
}

@Composable
fun PlayerListUI(
    players: List<Player>,
    modifier: Modifier = Modifier,
    onPlayerClick: (Player) -> Unit,
    searchedText: String,
    teamColor: String
) {
    var filteredCountries: ArrayList<Player>
    LazyColumn(modifier = modifier) {
        filteredCountries = if (searchedText.isEmpty()) {
            ArrayList()
        } else {
            val resultList = ArrayList<Player>()
            for (player in players) {
                if (player.name.lowercase()
                        .contains(searchedText.lowercase())
                    || player.email!!.lowercase()
                        .contains(searchedText.lowercase())
                ) {
                    resultList.add(player)
                }
            }
            resultList
        }
        items(filteredCountries) { filteredCountry ->
            PlayerListItem(
                painterResource(id = R.drawable.ic_add_player),
                player = filteredCountry,
                teamColor = teamColor,
                onItemClick = { selectedPlayer ->
                    onPlayerClick.invoke(selectedPlayer)
                },
                showBackground = false
            )
        }
    }
}

@Composable
fun PlayerListItem(
    icon: Painter,
    player: Player,
    teamColor: String,
    onItemClick: (Player) -> Unit,
    showBackground: Boolean
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_48dp))
            .fillMaxWidth()
            .background(
                color = if (showBackground) {
                    MaterialTheme.appColors.material.primary
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_demo),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(androidx.compose.foundation.shape.CircleShape),
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = player.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            AddRemoveButton(icon, teamColor = teamColor) { onItemClick(player) }
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
}

@Composable
fun AddRemoveButton(icon: Painter, teamColor: String, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.size_20dp))
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                color = Color(android.graphics.Color.parseColor("#$teamColor"))
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