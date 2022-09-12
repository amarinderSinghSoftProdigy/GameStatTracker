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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreenUpdated(
    teamId: String? = "",
    vm: SetupTeamViewModelUpdated,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit
) {
    val context = LocalContext.current
    val state = vm.teamSetupUiState.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val nameTextFiled = remember {
        mutableStateListOf<String>("", "", "", "", "")
    }
    val emailTextField = remember {
        mutableStateListOf<String>("", "", "", "", "")
    }
    var rowCount by rememberSaveable {
        mutableStateOf(5)
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
                    onNextClick.invoke(uiEvent.teamId)
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
                        items(rowCount) { index ->
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
                                        value = nameTextFiled[index],
                                        onValueChange = { name ->
                                            nameTextFiled[index] = name
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
                                        isError = !validName(nameTextFiled[index]) && nameTextFiled[index].isNotEmpty() || nameTextFiled[index].length > 30,
                                        errorMessage = stringResource(id = R.string.valid_first_name),
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                                    AppSearchOutlinedTextField(
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(focusRequester),
                                        value = emailTextField[index],
                                        onValueChange = { email ->
                                            emailTextField[index] = email
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
                                        isError = (!emailTextField[index].isValidEmail() && emailTextField[index].isNotEmpty() || emailTextField[index].length > 45),
                                        errorMessage = stringResource(id = R.string.email_error)
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
                                    ++rowCount
                                    nameTextFiled.add("")
                                    emailTextField.add("")
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
                            //TODO Add uupdate team api and add the newly selected player in the api.
                        }
                    },
                    enableState =
                    nameTextFiled.all() { it.isNotEmpty() } &&
                            nameTextFiled.all() { validName(it) }
                            && emailTextField.all() { it.isValidEmail() },
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
                    || player.email.lowercase()
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