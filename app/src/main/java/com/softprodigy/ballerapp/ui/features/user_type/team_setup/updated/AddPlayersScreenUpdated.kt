package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.appColors
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
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Timber.i("AddPlayersScreenUpdated-- teamId--$teamId")

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
        CoachFlowBackground(
            colorCode = state.teamColorPrimary,
            teamLogo = state.teamImageUri
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp)),
                text = stringResource(id = R.string.invite_team_member),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

            UserFlowBackground(modifier = Modifier.weight(1F)) {
                Column(
                    Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {

                    LazyColumn(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(state.inviteMemberCount) { index ->
                            Column(Modifier.fillMaxSize()) {

                                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppSearchOutlinedTextField(
                                        modifier = Modifier
                                            .width(dimensionResource(id = R.dimen.size_100dp))
                                            .focusRequester(focusRequester),
                                        value = state.inviteMemberName[index],
                                        onValueChange = { name ->
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
                                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                            )
                                        },
                                        singleLine = true,
                                        isError = !validName(state.inviteMemberName[index])
                                                && state.inviteMemberName[index].isNotEmpty()
                                                || state.inviteMemberName[index].length > 30,
                                        errorMessage = stringResource(id = R.string.valid_first_name),
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                                    AppSearchOutlinedTextField(
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(focusRequester),
                                        value = state.inviteMemberEmail[index],
                                        onValueChange = { email ->
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
                                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
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
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_remove),
                                        contentDescription = "",
                                        tint = Color.Unspecified,
                                        modifier = Modifier
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


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
                BottomButtons(
                    onBackClick = { onBackClick.invoke() },
                    onNextClick = {
                        if (teamId.isNullOrEmpty()) {
                            vm.onEvent(TeamSetupUIEventUpdated.OnAddPlayerScreenNext)
                        } else {
                            vm.onEvent(TeamSetupUIEventUpdated.OnInviteTeamMembers(teamId))

                        }
                    },
                    enableState =
                    state.inviteMemberName.isNotEmpty() &&
                            state.inviteMemberName.all() { it.isNotEmpty() } &&
                            state.inviteMemberName.all() { validName(it) }
                            && state.inviteMemberEmail.all() { it.isValidEmail() },
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