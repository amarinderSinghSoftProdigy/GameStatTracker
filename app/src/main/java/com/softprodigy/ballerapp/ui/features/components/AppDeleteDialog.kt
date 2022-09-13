package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.data.response.Player
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.profile.tabs.DetailItem
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
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
    onConfirmClick: () -> Unit,
    onSelectionChange: (Team) -> Unit,
    selected: Team?,
    showLoading: Boolean,
    teams: ArrayList<Team>
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
                            text = stringResource(id = R.string.pick_team),
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
                            .height(dimensionResource(id = R.dimen.size_150dp)),
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
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    ButtonWithLeadingIcon(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.create_new_team),
                        onClick = { },
                        painter = painterResource(id = R.drawable.ic_add_button),
                        isTransParent = true

                    )
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
            AsyncImage(
                model = BuildConfig.IMAGE_SERVER + team.logo,
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .background(color = MaterialTheme.appColors.material.secondaryVariant)
                    .border(
                        dimensionResource(id = R.dimen.size_2dp),
                        MaterialTheme.colors.surface,
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = team.name,
                fontWeight = FontWeight.W400,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                modifier = Modifier.weight(1f),
                color = if (selected) {
                    MaterialTheme.appColors.buttonColor.textEnabled
                } else {
                    MaterialTheme.appColors.buttonColor.textDisabled
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
            AsyncImage(
                model = BuildConfig.IMAGE_SERVER + player.profileImage,
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
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
