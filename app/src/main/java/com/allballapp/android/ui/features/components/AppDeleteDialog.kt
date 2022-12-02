package com.allballapp.android.ui.features.components

import android.content.Intent
import android.net.Uri
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.allballapp.android.R
import com.allballapp.android.common.*
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.response.*
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.ui.features.home.events.DivisionData
import com.allballapp.android.ui.features.home.events.NoteType
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.profile.tabs.DetailItem
import com.allballapp.android.ui.features.user_type.team_setup.updated.InviteObject
import com.allballapp.android.ui.theme.*
import com.allballapp.android.ui.utils.CommonUtils
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import timber.log.Timber
import java.util.*


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
    teamVm: TeamViewModel
) {
    val teamId = remember {
        mutableStateOf(if (UserStorage.teamId.isEmpty()) teamVm.teamUiState.value.teamId else UserStorage.teamId)
    }
    val teamName = remember {
        mutableStateOf(UserStorage.teamName)
    }

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*   onDismiss()   */
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
                                if (it != null)
                                    TeamListItem(team = it, selected = selected == it) { team ->
                                        onSelectionChange.invoke(team)
                                        teamId.value = team._id
                                        teamName.value = team.name
                                        onConfirmClick.invoke(teamId.value, teamName.value)
                                        onDismiss.invoke()
                                    }
                            }
                        }
                    }
                    //  Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    /* if (showCreateTeamButton) {*/
                    ButtonWithLeadingIcon(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.create_new_team),
                        onClick = { onCreateTeamClick.invoke() },
                        painter = painterResource(id = R.drawable.ic_add_button),
                        isTransParent = true

                    )
                    /*   }*/
                    /*  Row(
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
                      }*/
                }
            },
        )
    }
}

@Composable
fun ShowParentDialog(
    parentDetails: Parent,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    val context = LocalContext.current
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*onDismiss()*/
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
                            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + parentDetails.profileImage,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_200dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                            onError = { Placeholder(R.drawable.ic_profile_placeholder) }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = "${parentDetails.firstName} ${parentDetails.lastName}",
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
                            parentDetails.email.let {
                                DetailItem(
                                    stringResource(id = R.string.email),
                                    it
                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            parentDetails.phone.let {
                                DetailItem(
                                    stringResource(id = R.string.number),
                                    it
                                )
                            }
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
                            text = stringResource(R.string.message_),
                            onClick = {
                                val u = Uri.parse("sms:" + parentDetails.phone)
                                val i = Intent(Intent.ACTION_VIEW, u)
                                try {
                                    context.startActivity(i)
                                } catch (s: SecurityException) {
                                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                                        .show()
                                }
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f),
                            painter = painterResource(id = R.drawable.ic_message),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        ButtonWithLeadingIcon(
                            text = stringResource(R.string.call),
                            onClick = {

                                val u = Uri.parse("tel:" + parentDetails.phone)
                                val i = Intent(Intent.ACTION_DIAL, u)
                                try {
                                    context.startActivity(i)
                                } catch (s: SecurityException) {
                                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG)
                                        .show()
                                }
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
fun ConfirmDialog(
    heading: String = "",
    title: String,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier.clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /* onDismiss*/
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    if (heading.isNotEmpty()) {
                        Text(
                            text = heading,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))
                    }
                    Text(
                        text = title,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
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


@Composable
fun NoInternetDialog(
    title: String,
) {
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier.clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {},
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Text(
                        text = title,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    )
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
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + team.logo,
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
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + user.profileImage,
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
            onDismissRequest = {
                /* onDismiss()*/
            },
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
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + player.profileImage,
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
                    Color(android.graphics.Color.parseColor("#${getCustomColorCode(tintColor)}"))
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
            onDismissRequest = {
//                onDismiss
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
                                if ((selected ?: "").isNotEmpty())
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
                    .background(
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) MaterialTheme.appColors.material.primaryVariant.copy(
                            alpha = 0.4f
                        ) else Color.Transparent
                    )
                    .padding(dimensionResource(id = R.dimen.size_2dp))
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = if (!isSelected) 1.dp else 0.dp,
                            color = if (!isSelected) MaterialTheme.appColors.textField.label else Color.Transparent,
                            shape = RoundedCornerShape(50)
                        )
                        .background(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected)
                                MaterialTheme.appColors.material.primaryVariant
                            else {
                                Color.Transparent
                            },
                        )
                        .padding(dimensionResource(id = R.dimen.size_8dp))
                )
            }
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
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + profile,
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
    loading: Boolean = false,
    selectedRole: String,
    onBack: () -> Unit,
    onConfirmClick: () -> Unit,
    onChildNotListedCLick: () -> Unit,
    dontHaveChildClick: () -> Unit,
    onSelectionChange: (String) -> Unit,
    //selected: String?,
    selected: ArrayList<String>?,
    onDismiss: () -> Unit,
    guardianList: List<PlayerDetails>,
    onValueSelected: (PlayerDetails) -> Unit
) {
    Timber.e("selected " + selected)
    BallerAppMainTheme {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .padding(horizontal = dimensionResource(id = R.dimen.size_32dp))
                .wrapContentSize()
                .animateContentSize(),
            onDismissRequest = {
                /*   onDismiss*/
            },
            buttons = {
                Box {
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
                                text = if (selectedRole != UserType.PLAYER.key) stringResource(R.string.select_the_players_guardian) else stringResource(
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
                                    items(guardianList) { member ->
                                        SelectGuardianRoleItem(
                                            name = member.memberDetails.firstName,
                                            profile = member.memberDetails.profileImage,
                                            onItemClick = { guardian ->
                                                onSelectionChange(guardian)
                                                onValueSelected(member)
                                            },
                                            isSelected = (selected
                                                ?: arrayListOf()).contains(member.id),
                                            id = member.id
                                        )
                                    }
                                })

                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                        DialogButton(
                            text = if (selectedRole != UserType.PLAYER.key) stringResource(R.string.child_not_listed) else stringResource(
                                R.string.my_guardian_not_listed
                            ),
                            onClick = { if (!loading) onChildNotListedCLick() },
                            modifier = Modifier.fillMaxWidth(),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = true,
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                        if (selectedRole != UserType.PLAYER.key)
                            DialogButton(
                                text = stringResource(R.string.i_dont_have_child_on_this_team),
                                onClick = { if (!loading) dontHaveChildClick() },
                                modifier = Modifier.fillMaxWidth(),
                                border = ButtonDefaults.outlinedBorder,
                                onlyBorder = true,
                                enabled = false
                            )

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
                                    if (!selected.isNullOrEmpty() && !loading) {
                                        onConfirmClick.invoke()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f),
                                border = ButtonDefaults.outlinedBorder,
                                enabled = !selected.isNullOrEmpty(),
                                onlyBorder = false,
                            )
                        }
                    }
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.appColors.material.primaryVariant
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
            onDismissRequest = {
                /* onDismiss()*/
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
                                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + team.logo,
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
            onDismissRequest = {
                /*onDismiss()*/
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
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
                        modifier = Modifier.height(dimensionResource(id = R.dimen.size_200dp)),
                        verticalArrangement = Arrangement.Top,
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
                                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + team.memberDetails.profileImage,
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
                                        if (list.contains(team.memberDetails.id)) {
                                            list.remove(team.memberDetails.id)
                                        } else {
                                            list.add(team.memberDetails.id)
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
    var divisionSelected = remember {
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
            onDismissRequest = {
                /*onDismiss()*/
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
                            onClick = {
                                onDismiss()
                                divisionSelected.value = DivisionData()
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
    placeholderText: String = "",
    textLimit: Int? = null
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LocalView.current.viewTreeObserver.addOnWindowFocusChangeListener {
        if (it) focusRequester.requestFocus()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*onDismiss()*/
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
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        onValueChange = { reason ->
                            if ((textLimit != null)) {
                                if (reason.length <= textLimit) {
                                    onReasonChange(reason)
                                }
                            } else {
                                onReasonChange(reason)
                            }
                        },
                        maxLines = 1,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = if (placeholderText.isEmpty()) stringResource(id = R.string.reason_not_going) else placeholderText,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                textAlign = TextAlign.Start
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
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
                                if (reason.trim().length > 3) {
                                    onConfirmClick.invoke()
                                    onDismiss.invoke()
                                }
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
    title: String = "",
    actionButtonText: String = "",
    onDismiss: () -> Unit,
    onConfirmClick: (SwapUser) -> Unit,
    showLoading: Boolean,
    users: List<SwapUser>,
    onCreatePlayerClick: () -> Unit,
    showCreatePlayerButton: Boolean = false,
    defaultCase: Boolean = false,
) {

    val selectedUser = remember {
        mutableStateOf(SwapUser(_Id = if (defaultCase) "" else UserStorage.userId))
    }
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*  onDismiss()*/
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
                            text = title.ifEmpty { stringResource(id = R.string.swap_profiles) },
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_16dp))
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

                        }

                        item {
                            if (showLoading) {
                                CommonProgressBar()
                            } else {
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
                            text = actionButtonText.ifEmpty { stringResource(R.string.dialog_button_confirm) },
                            onClick = {
                                if(selectedUser.value._Id.isNotEmpty()) {
                                    onConfirmClick.invoke(selectedUser.value)
                                     onDismiss.invoke()
                                }
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = selectedUser.value._Id.isNotEmpty(),
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
            onDismissRequest = {
                /*onDismiss()*/
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
                /*noteType?.let {
                    onDismiss.invoke(it)
                }*/
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AgeConfirmDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (Int) -> Unit,
) {
    val selected = remember {
        mutableStateOf(-1)
    }
    BallerAppMainTheme {
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*  onDismiss.invoke()*/
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
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.age_conformation),
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))
                    Surface(
                        onClick = {
                            selected.value = 0
                        },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp)),
                        elevation = if (selected.value == 0) dimensionResource(id = R.dimen.size_10dp) else 0.dp,
                        color = if (selected.value == 0) MaterialTheme.appColors.material.primaryVariant else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        Text(
                            style = MaterialTheme.typography.h6,
                            color = if (selected.value == 0) Color.White else MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            text = stringResource(id = R.string.age_ok),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                        )
                    }
                    Surface(
                        onClick = {
                            selected.value = 1
                        },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp)),
                        elevation = if (selected.value == 1) dimensionResource(id = R.dimen.size_10dp) else 0.dp,
                        color = if (selected.value == 1) MaterialTheme.appColors.material.primaryVariant else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        Text(
                            style = MaterialTheme.typography.h6,
                            color = if (selected.value == 1) Color.White else MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            text = stringResource(id = R.string.age_zero),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    DialogButton(
                        text = stringResource(R.string.dialog_button_confirm),
                        onClick = {
                            if (selected.value != -1) {
                                onConfirmClick(selected.value)
                                onDismiss()
                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = false,
                        enabled = true
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GuardianAuthorizeDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val defaultCountryCode = remember { mutableStateOf(getDefaultPhoneCode(context)) }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    var phone = remember { mutableStateOf("") }
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /* onDismiss.invoke()*/
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
                    Text(
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        text = stringResource(id = R.string.authorize_message),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.appColors.material.surface,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
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
                                .focusRequester(focusRequester),
                            pickedCountry = {
                                defaultCountryCode.value = it.countryPhoneCode
                                defaultLang = it.countryCode
                            },
                            defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                            focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                            unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                            dialogAppBarTextColor = Color.Black,
                            showCountryFlag = false,
                            dialogAppBarColor = Color.White,
                            error = true,
                            text = phone.value,
                            onValueChange = {
                                phone.value = it
                            },
                            readOnly = false,
                            cursorColor = Color.Black,
                            placeHolder = {
                                Text(
                                    text = stringResource(id = R.string.your_phone_number),
                                    fontSize = dimensionResource(
                                        id =
                                        R.dimen.txt_size_12
                                    ).value.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.appColors.textField.labelDark
                                )
                            },
                            content = {
                            },
                            textStyle = TextStyle(
                                textAlign = TextAlign.Start,
                                color = Color.Black
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    DialogButton(
                        text = stringResource(R.string.dialog_button_confirm),
                        onClick = {
                            onConfirmClick(defaultCountryCode.value + phone.value)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = false,
                        enabled = validPhoneNumber(phone.value) && phone.value.length == 10 && phone.value.isNotEmpty()
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InviteTeamMembersDialog(
    role: String = "",
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
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .wrapContentSize()
                .animateContentSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_32dp)),
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
                                showInfoBox.value = false
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
                                val roleObject =
                                    remember {
                                        mutableStateOf(
                                            CommonUtils.getUserRole(
                                                role,
                                                roles
                                            )
                                        )
                                    }

                                remember {
                                    if (role.isNotEmpty()) {
                                        onRoleValueChange(0, roleObject.value)
                                    }
                                }
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
                                                    text = stringResource(id = R.string.phone_num),
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
                                if (inviteList.isNotEmpty() &&
                                    inviteList.all { it.name.isNotEmpty() && it.contact.isNotEmpty() && it.role.key.isNotEmpty() && it.contact.length == 10}
                                ) {
                                    onConfirmClick.invoke()
                                }
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            enabled = inviteList.isNotEmpty() &&
                                    inviteList.all { it.name.isNotEmpty() && it.contact.isNotEmpty() && it.role.key.isNotEmpty() && it.contact.length == 10 },
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
    BallerAppMainTheme {
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*onDismiss.invoke()*/
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

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CoilImage(
                            src = teamLogo,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_160dp))
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                            onError = { Placeholder(R.drawable.ic_profile_placeholder) }
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
                    val team = stringResource(id = R.string.team)
                    val user = stringResource(id = R.string.user)
                    AppText(
                        text = stringResource(
                            id = R.string.success_player_has_been_added_to_the_team/*,
                            playerName.ifEmpty { user },
                            teamName.ifEmpty { team }*/
                        ),
                        fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontFamily = rubikFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    AppText(
                        text = stringResource(
                            id = R.string.best_time_to_invite
                        ),
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontFamily = rubikFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    AppDivider()

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumberPickerDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
) {
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val selection = remember { mutableStateOf(mYear.toString()) }

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /* onDismiss.invoke()*/
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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_color_picker),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_12dp))
                                .align(Alignment.TopEnd)
                                .clickable {
                                    onDismiss.invoke()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = { context ->
                            NumberPicker(context).apply {
                                setOnValueChangedListener { numberPicker, i, i2 ->
                                    selection.value = i2.toString()
                                }
                                minValue = mYear - 100
                                maxValue = mYear
                                value = mYear
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        DialogButton(
                            text = stringResource(R.string.dialog_button_cancel),
                            onClick = {
                                onDismiss.invoke()
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
                                onConfirmClick.invoke(selection.value)
                                onDismiss.invoke()
                            },
                            modifier = Modifier
                                .weight(1f),
                            border = ButtonDefaults.outlinedBorder,
                            onlyBorder = false,
                            enabled = true
                        )
                    }
                }
            },
        )
    }
}

@Composable
fun PaymentPickerDialog(
    onDismiss: () -> Unit,
    onConfirmClick: (String) -> Unit,
    selectedOption: String
) {

    val paymentOptions = arrayListOf(
        stringResource(id = R.string.venmo),
        stringResource(id = R.string.cash),
        stringResource(id = R.string.check),
        stringResource(id = R.string.square_cash),
        stringResource(id = R.string.zelle),
        stringResource(id = R.string.apple_pay),
        stringResource(id = R.string.google_pay)
    )

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*onDismiss.invoke()*/
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText(
                            text = stringResource(id = R.string.select_payment_option),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            style = MaterialTheme.typography.h6
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_color_picker),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_12dp))
                                .clickable {
                                    onDismiss.invoke()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    paymentOptions.forEach {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(id = R.dimen.size_44dp))
                                .background(
                                    color = if (it == selectedOption) MaterialTheme.appColors.material.primaryVariant else Color.White,
                                    shape = RoundedCornerShape(
                                        dimensionResource(id = R.dimen.size_8dp)
                                    )
                                )
                                .clickable {
                                    onConfirmClick(it)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AppText(
                                text = it,
                                color = if (it == selectedOption) Color.White else MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_5dp))
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ShowImageUploaded(
    onDismiss: () -> Unit,
    image: String
) {

    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
            onDismissRequest = {
                /*onDismiss.invoke()*/
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_color_picker),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_12dp))
                                .clickable {
                                    onDismiss.invoke()
                                }
                        )
                    }
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_20dp)))

                    CoilImage(
                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + image,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_300dp)),
                        isCrossFadeEnabled = false,
                        onLoading = { PlaceholderRect(R.drawable.ic_events_placeholder) },
                        onError = { PlaceholderRect(R.drawable.ic_events_placeholder) }
                    )
                }
            }
        )
    }
}
