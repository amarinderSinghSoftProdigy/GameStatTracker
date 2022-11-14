package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.rating.RatingBar
import com.softprodigy.ballerapp.ui.features.game_zone.teamLogo
import com.softprodigy.ballerapp.ui.theme.*

@Composable
fun <T> DeleteDialog(
    item: T,
    message: String,
    title: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onDelete: (T) -> Unit,
) {
    BallerAppMainTheme {
        AlertDialog(
            onDismissRequest = onDismiss,
            backgroundColor = Color.White,
            title = title,
            text = {
                Text(
                    text = message,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_10dp),
                            vertical = dimensionResource(id = R.dimen.size_10dp)
                        )
                ) {
                    AppButton(
                        text = stringResource(R.string.dialog_button_cancel),
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                        border = ButtonDefaults.outlinedBorder,
                        singleButton = true
                    )
                    AppButton(
                        text = stringResource(R.string.dialog_button_confirm),
                        onClick = { onDelete(item) },
                        modifier = Modifier
                            .weight(1f),
                        border = ButtonDefaults.outlinedBorder,
                        enabled = true,
                        singleButton = true,
                        themed = true,
                        isForceEnableNeeded = true
                    )
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }
}

@Composable
fun SelectTeamDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
    onSelectionChange: (Team) -> Unit,
    selected: Team?,
    showLoading: Boolean,
    teams: ArrayList<Team>,
    onCreateTeamClick: () -> Unit,
    showCreateTeamButton: Boolean = false,
) {
    val teamId = remember {
        mutableStateOf(UserStorage.teamId)
    }
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = onDismiss,
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.pick_team),
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross_1),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.buttonColor.textDisabled,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_12dp))
                                .align(Alignment.TopEnd)
                                .clickable {
                                    onDismiss()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    LazyColumn(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.size_150dp))
                            .padding(
                                bottom = dimensionResource(
                                    id = R.dimen.size_10dp
                                )
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            if (showLoading) {
                                CircularProgressIndicator(
                                    color = AppConstants.SELECTED_COLOR
                                )
                            }
                        }
                        item {
                            teams.forEach {
                                TeamListItem(team = it, selected = selected == it) { team ->
                                    onSelectionChange.invoke(team)
                                    teamId.value = team._id
                                }
                            }
                        }
                    }
                    //  Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    if (showCreateTeamButton) {
                        ButtonWithLeadingIcon(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.create_new_team),
                            onClick = { onCreateTeamClick.invoke() },
                            painter = painterResource(id = R.drawable.ic_add_button),
                            isTransParent = true

                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(
                                vertical = dimensionResource(id = R.dimen.size_16dp)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DialogButton(
                            text = stringResource(R.string.dialog_button_cancel),
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = true,
                            enabled = false
                        )
                        DialogButton(
                            text = stringResource(R.string.dialog_button_confirm),
                            onClick = {
                                onConfirmClick.invoke(teamId.value)
                                onDismiss.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = (selected?.name ?: "").isNotEmpty(),
                            onlyBorder = false,
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier.clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = onDismiss,
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Text(
                        text = stringResource(id = R.string.logout_message),
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.W600,
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                    ) {
                        DialogButton(
                            text = stringResource(R.string.dialog_button_cancel),
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = true,
                            enabled = true
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                        DialogButton(
                            text = stringResource(R.string.dialog_button_confirm),
                            onClick = {
                                onConfirmClick()
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = true,
                            onlyBorder = false,
                        )
                    }
                }
            },
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeamListItem(team: Team, selected: Boolean, onClick: (Team) -> Unit) {
    Surface(
        onClick = { onClick(team) },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp)),
        elevation = if (selected) dimensionResource(id = R.dimen.size_10dp) else 0.dp,
        color = if (selected) AppConstants.SELECTED_COLOR else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        dimensionResource(id = R.dimen.size_12dp),
                        dimensionResource(id = R.dimen.size_12dp)
                    )
                ), verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + team.logo,
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
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = team.name,
                fontWeight = FontWeight.W500,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                modifier = Modifier.weight(1f),
                color = if (selected) {
                    MaterialTheme.appColors.buttonColor.textEnabled
                } else {
                    MaterialTheme.appColors.buttonColor.bckgroundEnabled
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    searchKey: String,
    onSearchKeyChange: (String) -> Unit,
    onSelectionChange: (Player) -> Unit,
//    selectedPlayer: Player,
    selectedPlayers: MutableList<Player>,
    matchedPlayers: MutableList<Player>
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = onDismiss,
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_team_members),
                                contentDescription = "",
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_22dp))
                                    .height(dimensionResource(id = R.dimen.size_18dp)),
                                tint = MaterialTheme.appColors.textField.label
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            Text(
                                text = stringResource(id = R.string.add_new_team_member),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.Bold,
                            )

                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_color_picker),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_14dp))
                                .clickable {
                                    onDismiss()
                                },
                            tint = MaterialTheme.appColors.textField.label
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    AppSearchOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = searchKey,
                        onValueChange = {
                            onSearchKeyChange.invoke(it)
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
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    LazyColumn(
                        modifier = Modifier.height(dimensionResource(id = R.dimen.size_100dp))
                    ) {
                        item {
                            matchedPlayers.forEach {
                                PlayerListDialogItem(
                                    painterResource(id = R.drawable.ic_check),
                                    player = it,
                                    teamColor = AppConstants.SELECTED_COLOR.toArgb()
                                        .argbToHexString().removePrefix("#"),
                                    onItemClick = { player ->
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        onSelectionChange.invoke(player)
                                    },
                                    selected = selectedPlayers.contains(it)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_12dp),
                            vertical = dimensionResource(id = R.dimen.size_16dp)
                        )
                ) {
                    DialogButton(
                        text = stringResource(R.string.dialog_button_cancel),
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = true,
                        enabled = false
                    )
                    DialogButton(
                        text = stringResource(R.string.add_player),
                        onClick = {
                            onConfirmClick.invoke()
                            onDismiss.invoke()
                        },
                        modifier = Modifier
                            .weight(1f),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = false,
                        enabled = selectedPlayers.isNotEmpty()
                    )

                }
            },
        )
    }
}


@Composable
fun PlayerListDialogItem(
    icon: Painter,
    player: Player,
    teamColor: String,
    onItemClick: (Player) -> Unit,
    selected: Boolean
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_48dp))
            .fillMaxWidth()
            .background(
                color = if (selected) {
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
                .padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + player.profileImage,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.appColors.material.onSurface,
                        CircleShape
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                onError = { Placeholder(R.drawable.ic_team_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = player.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            CheckBoxButton(
                modifier = Modifier,
                icon,
                tintColor = teamColor,
                selected = selected
            ) { onItemClick(player) }
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
}

@Composable
fun CheckBoxButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    tintColor: String,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(onClick = { onItemClick() })
            .border(
                width = 1.dp,
                color = MaterialTheme.appColors.textField.label,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp))
            )
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                color = if (selected)
                    Color(android.graphics.Color.parseColor("#$tintColor"))
                else {
                    Color.Transparent
                }
            )
            .padding(dimensionResource(id = R.dimen.size_4dp))
    ) {

        Icon(
            painter = icon, contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(dimensionResource(id = R.dimen.size_6dp)),
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingDialog(
    onDismiss: () -> Unit,
    matchedPlayers: MutableList<Player> = ArrayList(),
    selectedPlayers: MutableList<Player>,
    onSelectionChange: (Player) -> Unit,
    onConfirmClick: () -> Unit,
) {

    val isReferee = remember { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = { onDismiss.invoke() },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.confirm_game_result),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.size_12dp)),
                        textAlign = TextAlign.Start,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.Bold
                    )

                    AppText(
                        text = "Springville HS Gym A",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.size_4dp)),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        fontFamily = rubikFamily,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    )


                    AppText(
                        text = "Sep 21 ,6:00 PM",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = text_field_label,
                        fontWeight = FontWeight.W400,
                        fontFamily = rubikFamily,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    )


                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                teamLogo()
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                                AppText(
                                    text = "My Team",
                                    color = colorResource(id = R.color.text_color),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_8dp)),
                                    fontFamily = rubikFamily,
                                    fontWeight = FontWeight.W500,
                                )
                            }

                            AppText(
                                text = stringResource(id = R.string.vs),
                                modifier = Modifier,
                                textAlign = TextAlign.Center,
                                color = text_field_label,
                                fontWeight = FontWeight.W400,
                                fontFamily = rubikFamily,
                                fontSize = dimensionResource(id = R.dimen.txt_size_10).value.sp,
                            )

                            Row(
                                Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(
                                    text = "Other Team",
                                    color = colorResource(id = R.color.text_color),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_8dp)),
                                    fontFamily = rubikFamily,
                                    fontWeight = FontWeight.W500,
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                                teamLogo(logoColor = Color.Blue)
                            }
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DialogButton(
                                text = "85",
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f),
                                border = ButtonDefaults.outlinedBorder,
                                onlyBorder = true,
                                enabled = false
                            )

                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            DialogButton(
                                text = "100",
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f),
                                border = ButtonDefaults.outlinedBorder,
                                onlyBorder = true,
                                enabled = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
//                    Divider(color = text_field_label, thickness = 1.dp)
//                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    AppText(
                        modifier = Modifier.fillMaxWidth(),
                        text = if(isReferee.value) "Referee Rating" else "Coach Rating",
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = colorResource(id = R.color.text_color)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    LazyColumn(
                        modifier = Modifier
                    ) {
                        item {
                            matchedPlayers.forEach {
                                PlayerRateBox(
                                    isReferee = isReferee,
                                    painterResource(id = R.drawable.ic_check),
                                    player = it,
                                    teamColor = AppConstants.SELECTED_COLOR.toArgb()
                                        .argbToHexString().removePrefix("#"),
                                    onItemClick = { player ->
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        onSelectionChange.invoke(player)
                                    },
                                    selected = selectedPlayers.contains(it)
                                )
                            }
                        }
                    }

                    DialogButton(
                        text = stringResource(R.string.dialog_button_confirm),
                        onClick = {
                            if(isReferee.value){
                                isReferee.value = false
                                onConfirmClick.invoke()
                            }else{
                                onConfirmClick.invoke()
                                onDismiss.invoke()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedBorder,
                        enabled = true,
                        onlyBorder = false,
                    )
                }
            }
        )
    }
}


@Composable
fun PlayerRateBox(
    isReferee : MutableState<Boolean>,
    icon: Painter,
    player: Player,
    teamColor: String,
    onItemClick: (Player) -> Unit,
    selected: Boolean
) {
    val colors = MaterialTheme.appColors.buttonColor
    var rating: Float by rememberSaveable { mutableStateOf(1f) }
    val imageBackground = ImageVector.vectorResource(id = R.drawable.ic_inactive_star)
    val imageForeground = ImageVector.vectorResource(id = R.drawable.ic_active_star)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        contentColor = colors.textEnabled.copy(alpha = 1f),
        border = ButtonDefaults.outlinedBorder,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (selected) {
                        Color.White
                    } else {

                        MaterialTheme.appColors.material.primary
                    },
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
                ) {


                    Row(
                        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_8dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + player.profileImage,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_50dp))
                                .clip(CircleShape)
                                .background(
                                    color = MaterialTheme.appColors.material.onSurface,
                                    CircleShape
                                ),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                            onError = { Placeholder(R.drawable.ic_team_placeholder) }
                        )

                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            AppText(
                                text = "Jskob",
                                style = MaterialTheme.typography.h4,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.text_color)
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                            AppText(
                                text = "Title",
                                style = MaterialTheme.typography.h6,
                                color = text_field_label,
                                textAlign = TextAlign.Start
                            )
                        }

                    }


                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                    Text(
                        text = player.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                    CheckBoxButton(
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.size_10dp)),
                        icon,
                        tintColor = teamColor,
                        selected = selected
                    ) { onItemClick(player) }
                }

                if(isReferee.value){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_8dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            AppText(
                                text = stringResource(id = R.string.professionalism),
                                style = MaterialTheme.typography.h6,
                                color = colorResource(id = R.color.text_color_sub_title),
                                textAlign = TextAlign.Start
                            )
                            RatingBar(
                                rating = rating,
                                space = 2.dp,
                                imageVectorEmpty = imageBackground,
                                imageVectorFFilled = imageForeground,
                                animationEnabled = true,
                                gestureEnabled = true,
                                itemSize = 22.dp,
//                            tintEmpty = in_active_star,
//                            tintFilled = active_˳star,
                            ) {
                                rating = it
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            AppText(
                                text = stringResource(id = R.string.call_Quality),
                                style = MaterialTheme.typography.h6,
                                color = colorResource(id = R.color.text_color_sub_title),
                                textAlign = TextAlign.Start
                            )

                            RatingBar(
                                rating = rating,
                                space = 2.dp,
                                imageVectorEmpty = imageBackground,
                                imageVectorFFilled = imageForeground,
                                animationEnabled = true,
                                gestureEnabled = true,
                                itemSize = 22.dp,
//                            tintEmpty = in_active_star,
//                            tintFilled = active_˳star,
                            ) {
                                rating = it
                            }
                        }
                    }
                }else{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_8dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText(
                            text = stringResource(id = R.string.professionalism),
                            style = MaterialTheme.typography.h6,
                            color = colorResource(id = R.color.text_color_sub_title),
                            textAlign = TextAlign.Start
                        )
                        RatingBar(
                            rating = rating,
                            space = 2.dp,
                            imageVectorEmpty = imageBackground,
                            imageVectorFFilled = imageForeground,
                            animationEnabled = true,
                            gestureEnabled = true,
                            itemSize = 22.dp,
                        ) {
                            rating = it
                        }
                    }
                }

            }

        }

    }

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectInvitationRoleDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    onSelectionChange: (String) -> Unit,
    title: String,
    selected: String?,
    showLoading: Boolean,
    roleList: ArrayList<String>
) {

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = onDismiss,
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = title,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.W600,
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_color_picker),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_12dp))
                                .align(Alignment.TopEnd)
                                .clickable {
                                    onDismiss()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    LazyColumn(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.size_250dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            if (showLoading) {
                                CircularProgressIndicator(
                                    color = AppConstants.SELECTED_COLOR
                                )
                            }
                        }

                        items(roleList) { role ->
                            SelectInvitationRoleItem(
                                role = role,
                                isSelected = selected == role,
                                onItemClick = {
                                    onSelectionChange.invoke(it)
                                })
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(
                                vertical = dimensionResource(id = R.dimen.size_16dp)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DialogButton(
                            text = stringResource(R.string.dialog_button_cancel),
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = true,
                            enabled = false
                        )
                        DialogButton(
                            text = stringResource(R.string.dialog_button_confirm),
                            onClick = {
                                onConfirmClick.invoke()
                                onDismiss.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = (selected ?: "").isNotEmpty(),
                            onlyBorder = false,
                        )
                    }
                }

            },
        )
    }
}


@Composable
fun SelectInvitationRoleItem(
    role: String,
    onItemClick: (String) -> Unit,
    isSelected: Boolean
) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_48dp))
            .fillMaxWidth()
            .background(
                color = if (isSelected) {
                    MaterialTheme.appColors.material.primary
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            )
            .clickable(onClick = { onItemClick(role) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            Text(
                text = role,
                modifier = Modifier.weight(1f),
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W400
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.appColors.textField.label,
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        shape = RoundedCornerShape(50),
                        color = if (isSelected)
                            MaterialTheme.appColors.material.primaryVariant
                        else {
                            Color.Transparent
                        }
                    )
                    .padding(dimensionResource(id = R.dimen.size_8dp))
            )

        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNewPlayerDialog(
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    playerName: String,
    jerseyNumber: String,
    isEdit: Boolean = false,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .background(color = colorResource(id = R.color.game_dialog_bg_color))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = { },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.game_dialog_bg_color))
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row {
                            Text(
                                text = stringResource(id = R.string.new_player),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.W500,
                                fontFamily = rubikFamily,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_wrong_white),
                            contentDescription = "",
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_24dp))
                                .height(dimensionResource(id = R.dimen.size_24dp))
                                .clickable { onDismiss.invoke() },
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))

                    AppSearchOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.size_44dp))
                            .focusRequester(focusRequester),
                        value = playerName,
                        onValueChange = {
                            //onSearchKeyChange.invoke(it)
                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White.copy(alpha = 0.2f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            cursorColor = Color.White,
                            backgroundColor = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textColor = colorResource(id = R.color.game_timeouts_slot_selected_text_color),
                        ),
                        placeholder = {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                text = stringResource(id = R.string.name),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.W400,
                                color = colorResource(id = R.color.game_timeouts_slot_selected_text_color),
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                    AppSearchOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.size_44dp))
                            .focusRequester(focusRequester),
                        value = jerseyNumber,
                        onValueChange = {},
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White.copy(alpha = 0.2f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            cursorColor = Color.White,
                            backgroundColor = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textColor = colorResource(id = R.color.game_timeouts_slot_selected_text_color),
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.jersey_number),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.W400,
                                color = colorResource(id = R.color.game_timeouts_slot_selected_text_color),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        },
                        singleLine = true
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.game_dialog_bg_color))
                        .padding(all = dimensionResource(id = R.dimen.size_8dp))
                ) {
                    AppButton(
                        onClick = { onDismiss.invoke() },
                        text = stringResource(R.string.dialog_button_cancel),
                        colors = ButtonColor(
                            bckgroundEnabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            bckgroundDisabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(dimensionResource(id = R.dimen.size_48dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = dimensionResource(id = R.dimen.size_1dp),
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(color = colorResource(id = R.color.game_dialog_player_text_field_bg_color))
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                    AppButton(
                        onClick = { onSaveClick.invoke() },
                        text = stringResource(R.string.save),
                        colors = ButtonColor(
                            bckgroundEnabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            bckgroundDisabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(dimensionResource(id = R.dimen.size_48dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = dimensionResource(id = R.dimen.size_1dp),
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(color = Color.White.copy(alpha = 0.1f))
                    )
                }
            },
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfirmSubstitutionDialog(
    onDismissClick: () -> Unit,
    onOutPlayerClick: () -> Unit,
    onInPlayerClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .background(color = colorResource(id = R.color.game_dialog_bg_color))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = { },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.game_dialog_bg_color))
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row {
                            Text(
                                text = stringResource(id = R.string.player_subs),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.W500,
                                fontFamily = rubikFamily,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_wrong_white),
                            contentDescription = "",
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_24dp))
                                .height(dimensionResource(id = R.dimen.size_24dp))
                                .clickable { onDismissClick.invoke() },
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.game_dialog_bg_color))
                        .padding(all = dimensionResource(id = R.dimen.size_8dp))
                ) {
                    AppButton(
                        onClick = { onOutPlayerClick.invoke() },
                        text = stringResource(R.string.player_out),
                        colors = ButtonColor(
                            bckgroundEnabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            bckgroundDisabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(dimensionResource(id = R.dimen.size_48dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = dimensionResource(id = R.dimen.size_1dp),
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(color = colorResource(id = R.color.game_dialog_player_text_field_bg_color))
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                    AppButton(
                        onClick = { onInPlayerClick.invoke() },
                        text = stringResource(R.string.player_in),
                        colors = ButtonColor(
                            bckgroundEnabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            bckgroundDisabled = colorResource(id = R.color.game_dialog_player_text_field_bg_color),
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(dimensionResource(id = R.dimen.size_48dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = dimensionResource(id = R.dimen.size_1dp),
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(color = Color.White.copy(alpha = 0.1f))
                    )
                }
            },
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameSettingsDialog(
    onDismiss: () -> Unit,
) {
    var isTwoHalvesSelected = remember { mutableStateOf(true) }
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .background(color = colorResource(id = R.color.game_dialog_bg_color))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = { },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.game_dialog_bg_color))
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row {
                            Text(
                                text = stringResource(id = R.string.periods),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.W500,
                                fontFamily = rubikFamily,
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_wrong_white),
                            contentDescription = "",
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_24dp))
                                .height(dimensionResource(id = R.dimen.size_24dp))
                                .clickable { onDismiss.invoke() },
                            tint = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))

                    var backgroundColor =
                        if (isTwoHalvesSelected.value) colorResource(id = R.color.game_timeouts_slot_selected_bg_color) else Color.Transparent

                    AppButton(
                        onClick = { isTwoHalvesSelected.value = !isTwoHalvesSelected.value },
                        text = stringResource(R.string.two_halves),
                        colors = ButtonColor(
                            bckgroundEnabled = backgroundColor,
                            bckgroundDisabled = backgroundColor,
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.size_300dp))
                            .height(
                                dimensionResource(id = R.dimen.size_48dp)
                            )
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = if (!isTwoHalvesSelected.value) 1.dp else 0.dp,
                                if (isTwoHalvesSelected.value) Color.Red else Color.White,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(
                                color =
                                if (isTwoHalvesSelected.value) colorResource(id = R.color.game_timeouts_slot_selected_bg_color) else Color.Transparent
                            )
                            .clickable { }
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                    AppButton(
                        onClick = { isTwoHalvesSelected.value = !isTwoHalvesSelected.value },
                        text = stringResource(R.string.four_quarters),
                        colors = ButtonColor(
                            bckgroundEnabled = backgroundColor,
                            bckgroundDisabled = backgroundColor,
                            textEnabled = Color.White,
                            textDisabled = Color.White
                        ),
                        modifier = Modifier
                            .width(dimensionResource(id = R.dimen.size_300dp))
                            .height(dimensionResource(id = R.dimen.size_48dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .border(
                                width = if (isTwoHalvesSelected.value) 1.dp else 0.dp,
                                if (isTwoHalvesSelected.value) Color.White else colorResource(id = R.color.game_timeouts_slot_selected_bg_color),
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .background(
                                color =
                                if (!isTwoHalvesSelected.value) colorResource(id = R.color.game_timeouts_slot_selected_bg_color) else Color.Transparent
                            )
                    )
                }
            },
        )
    }
}