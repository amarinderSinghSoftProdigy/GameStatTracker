package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.response.ParentDetails
import com.softprodigy.ballerapp.data.response.PlayerDetails
import com.softprodigy.ballerapp.data.response.SwapUser
import com.softprodigy.ballerapp.data.response.UserRoles
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.home.HomeViewModel
import com.softprodigy.ballerapp.ui.features.home.events.DivisionData
import com.softprodigy.ballerapp.ui.features.home.events.NoteType
import com.softprodigy.ballerapp.ui.features.profile.tabs.DetailItem
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.InviteObject
import com.softprodigy.ballerapp.ui.theme.*
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries

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
                        onClick = {
                            onDelete(item)
                            onDismiss.invoke()
                        },
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
    onConfirmClick: (String, String) -> Unit,
    onSelectionChange: (Team) -> Unit,
    selected: Team?,
    showLoading: Boolean,
    teams: ArrayList<Team>,
    onCreateTeamClick: () -> Unit,
    showCreateTeamButton: Boolean = false,
) {
    val homeViewModel : HomeViewModel = hiltViewModel()
    val teamId = remember {
        mutableStateOf(UserStorage.teamId)
    }
    val teamName = remember {
        mutableStateOf(UserStorage.teamName)
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
                            tint = ColorGreyLighter,
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
                            .height(dimensionResource(id = R.dimen.size_200dp))
                            .padding(
                                bottom = dimensionResource(
                                    id = R.dimen.size_10dp
                                )
                            ),
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
                                    teamName.value = team.name
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
                                onConfirmClick.invoke(teamId.value, teamName.value)
                                onDismiss.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
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
    parentDetails: ParentDetails,
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
                        AppText(
                            text = stringResource(id = R.string.parent),
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
                                },
                            tint = MaterialTheme.appColors.buttonColor.textDisabled
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

                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + parentDetails.parent.profileImage,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_200dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                            onError = { Placeholder(R.drawable.ic_profile_placeholder) }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = "${parentDetails.parent.firstName} ${parentDetails.parent.lastName}",
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack,
                            fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                        AppText(
                            text = parentDetails.parentType,
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.W500,
                            color = ColorBWGrayLight
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                        Row(
                            modifier = Modifier
                        ) {
                            DetailItem(
                                stringResource(id = R.string.email),
                                parentDetails.parent.email
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            DetailItem(
                                stringResource(id = R.string.number),
                                parentDetails.parent.phone
                            )
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListItem(user: SwapUser, selected: Boolean, onClick: (String) -> Unit) {
    /* val selected = remember {
         mutableStateOf(selected)
     }*/

    Surface(
        onClick = {
            onClick(user._Id)
        },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp)),
        elevation = if (selected) dimensionResource(id = R.dimen.size_10dp) else 0.dp,
        color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
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
                src = BuildConfig.IMAGE_SERVER + user.profileImage,
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
                text = user.firstName + " " + user.lastName,
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
    roleList: List<UserRoles>,
    userName: String,
    userLogo: String,
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
                            fontWeight = FontWeight.W500,
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

                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.appColors.material.primary,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .padding(dimensionResource(id = R.dimen.size_12dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CoilImage(
                            src = userLogo,
                            modifier = Modifier
                                .size(
                                    dimensionResource(id = R.dimen.size_44dp)
                                )
                                .clip(CircleShape)
                                .border(
                                    dimensionResource(id = R.dimen.size_2dp),
                                    MaterialTheme.colors.surface,
                                    CircleShape,
                                ),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                            onError = { Placeholder(R.drawable.ic_profile_placeholder) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            AppText(
                                text = userName,
                                style = MaterialTheme.typography.h3,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

                            AppText(
                                text = stringResource(
                                    R.string.select_your_role_on_specific_team
                                ),
                                style = MaterialTheme.typography.h6,
                                color = ColorMainPrimary,
                                textAlign = TextAlign.Start
                            )
                        }
                    }


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
                                role = role.value,
                                isSelected = selected == role.key,
                                onItemClick = {
                                    onSelectionChange.invoke(role.key)
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
                                /*onDismiss.invoke()*/
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
    selectedRole: String,
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onChildNotListedCLick: () -> Unit,
    onSelectionChange: (String) -> Unit,
    selected: String?,
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
                            text = if (selectedRole == "guardian") stringResource(R.string.select_the_players_guardian) else stringResource(
                                R.string.confirm_your_guardin
                            ),

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
                         text = if (selectedRole == "guardian") stringResource(R.string.child_not_listed) else stringResource(
                             R.string.my_guardian_not_listed
                         ),
                         onClick = onChildNotListedCLick,
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

@Composable
fun SwitchTeamDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (teams: Team) -> Unit,
    title: String,
    teams: ArrayList<Team>,
    teamSelect: Team?,
) {
    val teamSelected = remember {
        mutableStateOf(teamSelect)
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
                        modifier = Modifier.height(dimensionResource(id = R.dimen.size_200dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(teams) { team ->
                            Column(
                                modifier = Modifier
                                    .padding(bottom = dimensionResource(id = R.dimen.size_8dp))
                                    .background(
                                        color = if (teamSelected.value?._id == team._id) MaterialTheme.appColors.material.primary else Color.White,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_8dp)),
                                    verticalAlignment = Alignment.CenterVertically
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
                                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = team.name,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    CustomCheckBox(team._id == teamSelected.value?._id) {
                                        teamSelected.value = team
                                    }
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
                                teamSelected.value?.let { onConfirmClick.invoke(it) }
//                                onConfirmClick.invoke(teamSelected.value)
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


@Composable
fun SwitchPlayerDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (teams: ArrayList<String>) -> Unit,
    title: String,
    teams: ArrayList<PlayerDetails>,
    player: ArrayList<String>,
) {
    val teamSelected = remember {
        mutableStateOf(player)
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
                    val list = teamSelected.value
                    LazyColumn(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(teams) { team ->
                            val status = remember {
                                mutableStateOf(list.contains(team.id))
                            }
                            Column(
                                modifier = Modifier
                                    .padding(bottom = dimensionResource(id = R.dimen.size_8dp))
                                    .background(
                                        color = if (list.contains(team.id)) MaterialTheme.appColors.material.primary else Color.White,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_12dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CoilImage(
                                        src = BuildConfig.IMAGE_SERVER + team.memberDetails.profileImage,
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
                                        modifier = Modifier.weight(1f),
                                        text = team.memberDetails.name,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    CustomCheckBox(status.value) {
                                        status.value = !status.value
                                        if (list.contains(team.id)) {
                                            list.remove(team.id)
                                        } else {
                                            list.add(team.id)
                                        }
                                        teamSelected.value = list
                                    }
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
                                onConfirmClick.invoke(teamSelected.value)
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

@Composable
fun SelectDivisionDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (teams: DivisionData) -> Unit,
    title: String,
    teams: List<DivisionData>,
    division: DivisionData,
) {
    val divisionSelected = remember {
        mutableStateOf(division)
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
                                        color = if (divisionSelected.value._id == team._id) MaterialTheme.appColors.material.primary else Color.White,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                    )
                            ) {
                                Row(
                                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_12dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = team.divisionName,
                                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    CustomCheckBox(divisionSelected.value._id == team._id) {
                                        divisionSelected.value = team
                                    }
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
                                onConfirmClick.invoke(divisionSelected.value)
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
    placeholderText : String = ""
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
                        maxLines = 1,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = if(placeholderText.isEmpty()) stringResource(id = R.string.reason_not_going)  else placeholderText,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                textAlign = TextAlign.Start
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
                            enabled = reason.trim().length > 3
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun SwapProfile(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
    showLoading: Boolean,
    users: List<SwapUser>,
    onCreatePlayerClick: () -> Unit,
    showCreatePlayerButton: Boolean = false,
) {

    val selectedUser = remember {
        mutableStateOf(SwapUser(_Id = UserStorage.userId))
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
                            text = stringResource(id = R.string.swap_profiles),
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross_1),
                            contentDescription = "",
                            tint = ColorGreyLighter,
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            if (showLoading) {
                                CommonProgressBar()
                            }
                        }
                        item {
                            users.forEach { item ->
                                UserListItem(
                                    user = item,
                                    selectedUser.value._Id == item._Id
                                ) {
                                    if (selectedUser.value._Id != it) {
                                        selectedUser.value = item
                                    }
                                }
                            }
                        }
                    }
                    //  Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    if (showCreatePlayerButton) {
                        ButtonWithLeadingIcon(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.add_new_profile),
                            onClick = { onCreatePlayerClick.invoke() },
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
                                onConfirmClick.invoke(selectedUser.value._Id)
                                onDismiss.invoke()
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

@Composable
fun AddPlayer(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    val playerId = remember {
        mutableStateOf(UserStorage.playerId)
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
                            text = stringResource(id = R.string.invite),
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
                            AppOutlineTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = "",
                                onValueChange = {

                                },
                                placeholder = {
                                    AppText(
                                        text = stringResource(id = R.string.name),
                                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                                ),
                                errorMessage = stringResource(id = R.string.valid_team_name)
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                            AppOutlineTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = "",
                                onValueChange = {

                                },
                                placeholder = {
                                    AppText(
                                        text = stringResource(id = R.string.email),
                                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                                ),
                                errorMessage = stringResource(id = R.string.valid_team_name)
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.size_56dp))
                                    .clickable {
                                    }
                                    .onGloballyPositioned {
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
                                    Text(
                                        text = stringResource(id = R.string.user_type),
                                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                        color = MaterialTheme.appColors.textField.label,
                                    )
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

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))

                        }
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
                            text = stringResource(R.string.invite),
                            onClick = {
                                onConfirmClick.invoke(playerId.value)
                                onDismiss.invoke()
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNoteDialog(
    onDismiss: (NoteType) -> Unit,
    onConfirmClick: (NoteType, String) -> Unit,
    note: String,
    noteType: NoteType?,
    onNoteChange: (String) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                noteType?.let {
                    onDismiss.invoke(it)
                }
            },
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
                            text = if (noteType == NoteType.PRE) stringResource(id = R.string.add_pre_note)
                            else if (noteType == NoteType.POST) stringResource(
                                id = R.string.add_post_note
                            ) else "",
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
                                    noteType?.let {
                                        onDismiss.invoke(it)
                                    }
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))
                    AppOutlineTextField(
                        value = note,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onValueChange = {
                            onNoteChange(it)
                        },
                        maxLines = 1,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.add_note),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                textAlign = TextAlign.Start
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
                            onClick = {
                                noteType?.let {
                                    onDismiss.invoke(it)
                                }
                            },
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
                                noteType?.let { noteType ->
                                    onConfirmClick.invoke(noteType, note)
                                    onDismiss.invoke(noteType)
                                }
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = false,
                            enabled = note.length > 4
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun InviteTeamMembersDialog(
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onIndexChange: (Int) -> Unit,
    inviteList: SnapshotStateList<InviteObject>,
    onNameValueChange: (Int, String) -> Unit,
    onEmailValueChange: (Int, String) -> Unit,
    onInviteCountValueChange: (addIntent: Boolean) -> Unit,
    OnCountryValueChange: (Int, String) -> Unit,
    roles: List<UserRoles>,
    onRoleValueChange: (Int, UserRoles) -> Unit
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }




    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {

            },
            buttons = {
                val showInfoBox = remember {
                    mutableStateOf(true)
                }
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
                            text = stringResource(id = R.string.invite_team_member),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontWeight = FontWeight.W500
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp)))

                    if (showInfoBox.value) {
                        InfoBox(
                            title = "Info box",
                            content = stringResource(id = R.string.info_text),
                            color = ColorMainPrimary,
                            onCancelClick = {
//                                showInfoBox.value = false
                            })
                    }

                    UserFlowBackground(
                        color = MaterialTheme.appColors.buttonColor.textEnabled, modifier = Modifier
                            .height(
                                dimensionResource(id = R.dimen.size_150dp)
                            )
                            .fillMaxWidth(), padding = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {
                            inviteList.forEachIndexed { index, item ->
                                val roleObject = remember { mutableStateOf(UserRoles()) }
                                var expanded by remember { mutableStateOf(false) }
                                var textFieldSize by remember { mutableStateOf(Size.Zero) }
                                var defaultLang by rememberSaveable {
                                    mutableStateOf(
                                        getDefaultLangCode(context)
                                    )
                                }
                                val getDefaultPhoneCode = getDefaultPhoneCode(context)


                                LaunchedEffect(key1 = Unit) {
                                    OnCountryValueChange.invoke(index, getDefaultPhoneCode)

                                }


                                Column(Modifier.fillMaxSize()) {
                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AppSearchOutlinedTextField(
                                            modifier = Modifier
                                                .weight(0.6f)
                                                .focusRequester(focusRequester),
                                            value = item.name,
                                            onValueChange = { name ->
                                                if (name.length <= 30)
                                                    onNameValueChange.invoke(index, name)
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
                                            isError = !validName(item.name)
                                                    && item.name.isNotEmpty(),
                                            errorMessage = stringResource(id = R.string.valid_first_name),
                                        )
                                        Spacer(
                                            modifier = Modifier.width(
                                                dimensionResource(
                                                    id = R.dimen.size_8dp
                                                )
                                            )
                                        )
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            EditFields(
                                                roleObject.value.value,
                                                textStyle = TextStyle().copy(textAlign = TextAlign.Start),
                                                onValueChange = {},
                                                head = "",
                                                keyboardOptions = KeyboardOptions(
                                                    imeAction = ImeAction.Next,
                                                    keyboardType = KeyboardType.Text
                                                ),
                                                placeholder = {
                                                    Text(
                                                        text = stringResource(id = R.string.select_role),
                                                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                                        color = MaterialTheme.appColors.textField.label,
                                                    )
                                                },
                                                modifier = Modifier
                                                    .onGloballyPositioned {
                                                        textFieldSize = it.size.toSize()
                                                    }
                                                    .border(
                                                        shape = RoundedCornerShape(
                                                            dimensionResource(id = R.dimen.size_8dp)
                                                        ),
                                                        width = dimensionResource(id = R.dimen.size_1dp),
                                                        color = ColorBWGrayBorder
                                                    ),
                                                trailingIcon = {
                                                    Icon(
                                                        painterResource(id = R.drawable.ic_arrow_down),
                                                        contentDescription = null,
                                                        modifier = Modifier.clickable {
                                                            expanded = !expanded
                                                        })
                                                },
                                                enabled = true
                                            )
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                                modifier = Modifier
                                                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                                                    .background(MaterialTheme.colors.background)
                                            ) {
                                                roles.forEach { label ->
                                                    DropdownMenuItem(onClick = {
                                                        roleObject.value = label
                                                        onRoleValueChange.invoke(
                                                            index,
                                                            label
                                                        )
                                                        expanded = false
                                                    }) {
                                                        Text(
                                                            text = label.value,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }

                                        }
                                    }

                                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .border(
                                                1.dp,
                                                color = MaterialTheme.appColors.editField.borderUnFocused,
                                                shape = RoundedCornerShape(
                                                    dimensionResource(id = R.dimen.size_8dp)
                                                )
                                            )
                                            .padding(horizontal = dimensionResource(id = R.dimen.size_8dp)),
                                        verticalAlignment = Alignment.CenterVertically,

                                        ) {

                                        TogiCountryCodePicker(
                                            modifier = Modifier
                                                .weight(1f)
                                                .focusRequester(focusRequester),
                                            pickedCountry = {
                                                OnCountryValueChange(index, it.countryPhoneCode)
                                                defaultLang = it.countryCode
                                            },
                                            defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            dialogAppBarTextColor = Color.Black,
                                            showCountryFlag = false,
                                            dialogAppBarColor = Color.White,
                                            error = true,
                                            text = item.contact,
                                            onValueChange = { mobileNumber ->
                                                if (mobileNumber.length <= 10)
                                                    onEmailValueChange(index, mobileNumber)

                                            },
                                            readOnly = false,
                                            cursorColor = Color.Black,
                                            placeHolder = {
                                                Text(
                                                    text = stringResource(id = R.string.mobile_number),
                                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                                    color = MaterialTheme.appColors.textField.label,
                                                )
                                            },
                                            content = {

                                            },
                                            textStyle = TextStyle(textAlign = TextAlign.Start)
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_add),
                                            contentDescription = "",
                                            tint = Color.Unspecified,
                                            modifier = Modifier
                                                .clickable { onIndexChange.invoke(index) }
                                        )
                                    }

                                }
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))


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
                            text = stringResource(R.string.invite),
                            onClick = {
                                onConfirmClick.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = inviteList.isNotEmpty() &&
                                    inviteList.all { it.name.isNotEmpty() && it.contact.isNotEmpty() },
                            onlyBorder = false,
                        )
                    }
                }
            })
    }
}

@Composable
fun InfoBox(title: String, content: String, color: Color, onCancelClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .border(
                width = dimensionResource(id = R.dimen.size_1dp),
                color = color,
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_8dp)
                )
            )
            .background(
                color = color.copy(0.1f),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            )
            .padding(dimensionResource(id = R.dimen.size_16dp))

    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "",
                    modifier = Modifier.size(
                        dimensionResource(id = R.dimen.size_18dp)
                    ),
                    tint = color
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                AppText(
                    text = title,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontFamily = rubikFamily
                )

            }


            Icon(
                painter = painterResource(id = R.drawable.ic_cross_1),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.size_12dp)
                    )
                    .clickable {
                        onCancelClick.invoke()
                    },
                tint = color
            )

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
        AppText(
            text = content,
            color = ColorBWGrayDark,
            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            fontFamily = rubikFamily
        )

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InvitationSuccessfullySentDialog(
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    teamLogo: String,
    teamName: String,
    playerName: String,
) {
/*   InvitationSuccessfullySentDialog(
            onDismiss = { },
            onConfirmClick = {

            },
            teamLogo = BuildConfig.IMAGE_SERVER + "teamLogo/1666259687828-IMG_20220805_120020_710.jpg",
            teamName ="My name",
            playerName ="My player"
        )*/
    val keyboardController = LocalSoftwareKeyboardController.current
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                onDismiss.invoke()
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.appColors.material.surface)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    CoilImage(
                        src = teamLogo,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_160dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                        onError = { Placeholder(R.drawable.ic_profile_placeholder) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    AppText(
                        text = stringResource(
                            id = R.string.success_player_has_been_added_to_the_team,
                            playerName,
                            teamName
                        ),
                        fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontFamily = rubikFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    DialogButton(
                        text = stringResource(R.string.invite_others_to_the_team),
                        onClick = {
                            onConfirmClick.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = true,
                        onlyBorder = false,
                    )
                }
            },
        )
    }
}
