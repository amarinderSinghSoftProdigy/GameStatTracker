package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.theme.BallerAppMainTheme
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
    teams: ArrayList<Team>
) {
    BallerAppMainTheme {
        AlertDialog(
            modifier = Modifier.clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))).fillMaxHeight(0.8f),
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
                            .fillMaxSize(0.8f)
                    ) {
                        item {
                            teams.forEach {
                                TeamListItem(team = it, selected = selected == it) { team ->
                                    onSelectionChange.invoke(team)
                                }
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
        color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
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
