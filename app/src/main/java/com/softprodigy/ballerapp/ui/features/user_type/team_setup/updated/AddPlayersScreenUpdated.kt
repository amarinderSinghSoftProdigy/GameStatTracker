package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.flowlayout.FlowRow
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.*
import timber.log.Timber

@SuppressLint("MutableCollectionMutableState")
@Composable
fun AddPlayersScreenUpdated(
    teamId: String? = "",
    vm: SetupTeamViewModelUpdated,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit,
    onInvitationSuccess: () -> Unit
) {
    val context = LocalContext.current as Activity
    val state = vm.teamSetupUiState.value
    val focusRequester = remember { FocusRequester() }
    val maxChar = 45
    Timber.i("AddPlayersScreenUpdated-- teamId--$teamId")
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val email = dataStoreManager.getEmail.collectAsState(initial = "Kaushal")
    val expanded = remember { mutableStateOf(false) }

    vm.onEvent(
        TeamSetupUIEventUpdated.OnCoachEmailChange(
            email.value
        )
    )

    BackHandler {
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
            verticalArrangement = Arrangement.Top
        ) {
            UserFlowBackground(color = MaterialTheme.appColors.buttonColor.textEnabled) {
                FoldableItem(
                    expanded = expanded.value,
                    headerBackground = MaterialTheme.appColors.material.surface,
                    headerBorder = BorderStroke(0.dp, Color.Transparent),
                    header = { isExpanded ->
                        TeamMemberItem("Springfield Bucks", "", "3", isExpanded, true)
                    },
                    childItems = state.teamInviteList,
                    hasItemLeadingSpacing = false,
                    hasItemTrailingSpacing = false,
                    itemSpacing = 0.dp,
                    itemHorizontalPadding = 0.dp,
                    itemsBackground = MaterialTheme.appColors.material.primary,
                    item = { match, index ->
                        TeamMemberItem(
                            "George",
                            "",
                            "Pending",
                        )
                    }
                )

                AppDivider()

                Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {
                    FlowRow {
                        state.inviteList.forEachIndexed { index, item ->
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
                                                .weight(0.8f)
                                                .focusRequester(focusRequester),
                                            value = item.name,
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
                                            isError = !validName(item.name)
                                                    && item.name.isNotEmpty(),
                                            errorMessage = stringResource(id = R.string.valid_first_name),
                                        )
                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                                        AppSearchOutlinedTextField(
                                            modifier = Modifier
                                                .weight(1f)
                                                .focusRequester(focusRequester),
                                            value = item.contact,
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
                                                    text = stringResource(id = R.string.phone_num),
                                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                                    color = MaterialTheme.appColors.textField.label,
                                                )
                                            },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Next,
                                                keyboardType = KeyboardType.Phone

                                            ),
                                            isError = item.contact.isNotEmpty() && item.contact.length != 10
                                                    && !validPhoneNumber(item.contact),
                                            errorMessage = stringResource(id = R.string.valid_phone_number)
                                        )

                                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                                    }
                                    if (item.name.isEmpty()
                                        && item.contact.isEmpty()
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
                                                    updateItem(index, false)
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                }
                AppDivider()
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (hasContactPermission(context)) {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            context.startActivityForResult(
                                intent,
                                AppConstants.REQUEST_CONTACT_CODE,
                                null
                            )
                        } else {
                            requestContactPermission(context, context)
                        }
                    }) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_button),
                            contentDescription = "",
                            tint = ColorGreyLighter,
                            modifier = Modifier.size(
                                dimensionResource(id = R.dimen.size_10dp)
                            )

                        )
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.add_from_contacts),
                            color = MaterialTheme.appColors.buttonColor.textDisabled,
                            style = MaterialTheme.typography.button
                        )
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
                enableState = true,
                /*state.inviteList.isNotEmpty() &&
                        state.inviteList.all { it.name.isNotEmpty() && it.contact.isNotEmpty() },*/
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

@Composable
fun TeamMemberItem(
    name: String,
    url: String,
    status: String,
    isExpanded: Boolean = false,
    header: Boolean = false
) {

    Box(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_16dp)
            ),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + url,
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
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
            Text(
                text = name,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            val color = when (status) {
                "Pending" -> {
                    MaterialTheme.appColors.textField.labelDark
                }
                "Declined" -> {
                    ColorButtonRed
                }
                "Accepted" -> {
                    GreenColor
                }
                else -> {
                    MaterialTheme.appColors.buttonColor.bckgroundEnabled
                }
            }

            Text(
                text = if (header) "${status} ${stringResource(id = R.string.invited)}" else status,
                color = color,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.SemiBold
            )
            if (header) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                    contentDescription = "",
                    modifier = Modifier
                        .size(
                            height = dimensionResource(id = R.dimen.size_12dp),
                            width = dimensionResource(id = R.dimen.size_12dp)
                        )
                        .then(
                            if (!isExpanded) Modifier else Modifier.rotate(180f)
                        ),
                    tint = ColorGreyLighter
                )
            }
        }
    }
}

fun hasContactPermission(context: Context): Boolean {
    // on below line checking if permission is present or not.
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED;
}

fun requestContactPermission(context: Context, activity: Activity) {
    // on below line if permission is not granted requesting permissions.
    if (!hasContactPermission(context)) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CONTACTS), 1)
    }
}
