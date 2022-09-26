package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.response.PlayerDetails
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.profile.tabs.DetailItem
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayMedium
import com.softprodigy.ballerapp.ui.theme.appColors

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
fun ShowParentDialog(

    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
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
                            text = stringResource(id = R.string.parent),
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

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.size_16dp))
                            .background(color = Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_200dp))
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = "George Will",
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                            fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                        )
                        AppText(
                            text = "Mother",
                            style = MaterialTheme.typography.h4,
                            color = ColorBWGrayLight
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                        Row(
                            modifier = Modifier
                        ) {
                            DetailItem("email", "joe@gmail.com")
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            DetailItem("number", "9888834352")
                        }

                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ButtonWithLeadingIconGrayed(
                            text = stringResource(R.string.message),
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f),
                            painter = painterResource(id = R.drawable.ic_message),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        ButtonWithLeadingIcon(
                            text = stringResource(R.string.call),
                            onClick = {
                                onConfirmClick.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            iconSize = dimensionResource(id = R.dimen.size_20dp),
                            painter = painterResource(id = R.drawable.ic_call),
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

                        Row() {
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
                icon,
                tintColor = teamColor,
                selected = selected
            ) { onItemClick(player) }
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
}

@Composable
fun CheckBoxButton(icon: Painter, tintColor: String, selected: Boolean, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
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
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontWeight = FontWeight.W500
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
                            .height(dimensionResource(id = R.dimen.size_300dp)),
                        /* verticalArrangement = Arrangement.Center,*/
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
                    AppDivider()
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

@Composable
fun SelectGuardianRoleItem(
    id: String,
    name: String,
    profile: String,
    onItemClick: (String) -> Unit,
    isSelected: Boolean
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = dimensionResource(id = R.dimen.size_10dp))
            .clickable { onItemClick(id) }
    ) {

        Box(
            modifier = Modifier.border(
                width = if (isSelected) dimensionResource(id = R.dimen.size_4dp) else 0.dp,
                color = if (isSelected) MaterialTheme.appColors.material.primaryVariant else Color.White,
                shape = CircleShape
            )
        ) {
            CoilImage(
                src = profile,
                modifier =
                Modifier
                    .padding(all = dimensionResource(id = R.dimen.size_4dp))
                    .border(
                        width = dimensionResource(id = R.dimen.size_3dp),
                        color = Color.White,
                        shape = CircleShape
                    )
                    .size(dimensionResource(id = R.dimen.size_80dp))
                    .clip(CircleShape),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                onError = { Placeholder(R.drawable.ic_user_profile_icon) }
            )
        }


        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        AppText(
            text = name.capitalize(),
            color = if (isSelected) MaterialTheme.appColors.buttonColor.bckgroundEnabled else ColorBWGrayMedium,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SelectGuardianRoleDialog(
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onSelectionChange: (String) -> Unit,
    title: String,
    selected: String?,
    showLoading: Boolean,
    onDismiss: () -> Unit,
    guardianList: ArrayList<PlayerDetails>,
    onValueSelected: (PlayerDetails) -> Unit
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
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontWeight = FontWeight.W500
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    CompositionLocalProvider(
                        LocalOverScrollConfiguration provides null
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.height(dimensionResource(id = R.dimen.size_300dp)),
                            content = {

                                items(guardianList) {

                                    SelectGuardianRoleItem(
                                        name = it.memberDetails!!.firstName,
                                        profile = it.memberDetails.profileImage,
                                        onItemClick = { guardian ->
                                            onSelectionChange(guardian)
                                            onValueSelected(it)
                                        },
                                        isSelected = selected == it.id,
                                        id = it.id
                                    )
                                }
                            })
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    DialogButton(
                        text = stringResource(R.string.child_not_listed),
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    AppDivider()

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
                            text = stringResource(R.string.back),
                            onClick = onBack,
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
fun SwitchTeamDialogg(
    teams: ArrayList<Team>,
    title: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onConfirm: (Team) -> Unit,
) {
    BallerAppMainTheme {
        AlertDialog(
            onDismissRequest = onDismiss,
            backgroundColor = Color.White,
            title = title,
            text = {
                Text(
                    text = stringResource(id = (R.string.switch_teams)),
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
                        onClick = { },
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwitchTeamDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (teams: ArrayList<Team>) -> Unit,
    title: String,
    teams: ArrayList<Team>,
) {
    val selectedTeams = remember {
        mutableStateOf(false)
    }
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .background(
                    Color.White,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                )
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
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(teams) { team ->

                            Column(
                                modifier = Modifier
                                    .padding(bottom = dimensionResource(id = R.dimen.size_8dp))
                                    .background(
                                        color = if (selectedTeams.value) MaterialTheme.appColors.material.primary else Color.White,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_8dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.user_demo),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(dimensionResource(id = R.dimen.size_32dp))
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = team.name,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    CustomCheckBox(
                                        selectedTeams.value
                                    ) { selectedTeams.value = !selectedTeams.value }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    AppDivider()
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
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
                                //TODO add list to be sent back in callback
                                onConfirmClick.invoke(ArrayList())
                                onDismiss.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = false,
                        )
                    }
                }
            },
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeclineEventDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    title: String,
    reason: String,
    onReasonChange: (String) -> Unit,
) {

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
                    AppOutlineTextField(
                        value = reason,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = {
                            onReasonChange(it)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.reason_not_going) + "\n\n\n",
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                textAlign = TextAlign.Center
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                            unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                            backgroundColor = MaterialTheme.appColors.material.background,
                            textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            placeholderColor = MaterialTheme.appColors.textField.label,
                            cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        }),
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
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
                            onlyBorder = false,
                        )
                    }
                }
            },
        )
    }
}