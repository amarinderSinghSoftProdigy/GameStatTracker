package com.softprodigy.ballerapp.ui.features.user_type.team_setup
/*
import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.ui.features.components.AppSearchOutlinedTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.DeleteDialog
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreen(
    teamId: String? = "",
    vm: SetupTeamViewModel,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit
) {
    val context = LocalContext.current
    val state = vm.teamSetupUiState.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        vm.teamSetupChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamSetupChannel.ShowToast -> {
                    Toast.makeText(context, uiEvent.message.asString(context), Toast.LENGTH_LONG)
                        .show()
                }
                TeamSetupChannel.OnLogoUpload -> {
                    vm.onEvent(TeamSetupUIEvent.OnLogoUploadSuccess)
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
            colorCode = state.teamColor,
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
                text = stringResource(id = R.string.invite_players),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

            UserFlowBackground(modifier = Modifier.weight(1F)) {
                Column(
                    Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        AppSearchOutlinedTextField(
                            modifier = Modifier
                                .weight(1.0f)
                                .focusRequester(focusRequester),
                            value = state.search,
                            onValueChange = {
                                vm.onEvent(TeamSetupUIEvent.OnSearchPlayer(it))
//                                search.value = it
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = ""
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
                                    text = stringResource(id = R.string.search_by_name_or_email),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            singleLine = true
                        )
                        *//*Icon(
                            painter = painterResource(id = R.drawable.ic_scanner),
                            contentDescription = "",
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp)),
                            tint = Color.Unspecified
                        )*//*
                    }

                    if (!validName(state.search) && state.search.isNotEmpty()) {
                        Text(
                            text = stringResource(id = R.string.valid_search),
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.size_4dp))
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (state.search.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                        PlayerListUI(state.players, modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.size_200dp)),
                            searchedText = state.search,
                            teamColor = state.teamColor,
                            onPlayerClick = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                vm.onEvent(TeamSetupUIEvent.OnAddPlayerClick(it))
                            })
                        Divider(
                            modifier = Modifier
                                .layout { measurable, constraints ->
                                    val placeable = measurable.measure(
                                        constraints.copy(
                                            maxWidth = constraints.maxWidth + (context.resources.getDimension(
                                                R.dimen.size_32dp
                                            )).dp.roundToPx(),
                                        )
                                    )
                                    layout(placeable.width, placeable.height) {
                                        placeable.place(0, 0)
                                    }
                                })
                    }

                    if (state.selectedPlayers.isNotEmpty() || state.search.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row {
                                AppText(
                                    text = stringResource(id = R.string.invited_player),
                                    fontWeight = FontWeight.W500,
                                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                    color = ColorBWBlack
                                )

                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                                if (state.selectedPlayers.isNotEmpty()) {
                                    AppText(
                                        text = state.selectedPlayers.size.toString(),
                                        fontWeight = FontWeight.W500,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                        color = MaterialTheme.appColors.textField.label
                                    )
                                }
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = "",
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.size_10dp)),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
//                        items(selectedPlayer) { filteredCountry ->
                        items(state.selectedPlayers) { filteredCountry ->
                            PlayerListItem(
                                painterResource(id = R.drawable.ic_remove),
                                player = filteredCountry,
                                teamColor = state.teamColor,
                                onItemClick = { player ->
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                    vm.onEvent(TeamSetupUIEvent.OnRemovePlayerClick(player))
                                },
                                showBackground = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))
            BottomButtons(
                onBackClick = { onBackClick.invoke() },
                onNextClick = {
                    if (teamId.isNullOrEmpty()) {
                        vm.onEvent(TeamSetupUIEvent.OnAddPlayerScreenNext)
                    } else {
                    }
                },
                enableState = state.selectedPlayers.isNotEmpty(),
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
                vm.onEvent(TeamSetupUIEvent.OnDismissDialogCLick(false))
            },
            onDelete = {
                if (state.removePlayer != null) {
                    vm.onEvent(TeamSetupUIEvent.OnRemovePlayerConfirmClick(state.removePlayer))
                }
            }
        )
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
}*/
