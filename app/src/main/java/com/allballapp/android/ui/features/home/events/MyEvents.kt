package com.allballapp.android.ui.features.home.events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.data.UserStorage
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun MyEvents(
    vm: EventViewModel,
    moveToPracticeDetail: (String, String) -> Unit,
    moveToGameDetail: (String) -> Unit,
    moveToEventDetail: (String) -> Unit
) {

    val state = vm.eventState.value
    remember {
        vm.onEvent(EvEvents.RefreshEventScreen)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.upcomingAndGameData.size > 0 || state.pastEvents.size > 0) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                    var text = buildAnnotatedString {
                        append(stringResource(id = R.string.upcoming_Events))
                        val startIndex = length
                        append(" ( ")
                        append("" + state.upcomingAndGameData.size)
                        append(" )")
                        addStyle(
                            SpanStyle(color = MaterialTheme.appColors.textField.label),
                            startIndex,
                            length,
                        )
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h2,
                        color = ColorBWBlack,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                    FlowRow(Modifier.fillMaxWidth()) {
                        state.upcomingAndGameData.forEach { data ->

                            when (data) {
                                is Events -> {
                                    EventItem(events = data, onAcceptCLick = {
                                        vm.onEvent(EvEvents.OnGoingCLick(it))
                                    }, onDeclineCLick = {
                                        vm.onEvent(EvEvents.OnDeclineCLick(it))
                                    }, moveToPracticeDetail = moveToPracticeDetail,
                                        moveToGameDetail = moveToGameDetail,
                                        isPast = false,
                                        isSelfCreatedEvent = data.createdBy == UserStorage.userId
                                    )
                                }
                                is PublishedGames -> {
                                    GameDataItem(
                                        publishedGames = data,
                                        moveToEventDetail = { gameData ->
                                            vm.onEvent(EvEvents.SetEventId(gameData.Id))
                                            moveToEventDetail.invoke(gameData.name)
                                        })
                                }
                            }


                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.past_event))
                        val startIndex = length
                        append(" ( ")
                        append("" + state.pastEvents.size)
                        append(" )")
                        addStyle(
                            SpanStyle(color = MaterialTheme.appColors.textField.label),
                            startIndex,
                            length,
                        )
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h2,
                        color = ColorBWBlack,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                    Box {
                        FlowRow(Modifier.fillMaxWidth()) {
                            state.pastEvents.forEach {
                                EventItem(events = it, onAcceptCLick = {
                                }, onDeclineCLick = {
                                    vm.onEvent(EvEvents.OnDeclineCLick(it))
                                }, moveToPracticeDetail = moveToPracticeDetail,
                                    moveToGameDetail = moveToGameDetail,
                                    isPast = true
                                )
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                }


                if (state.showGoingDialog) {
                    DeleteDialog(
                        item = state.selectedEvent,
                        message = stringResource(id = R.string.alert_going_event_confirm),
                        onDismiss = {
                            vm.onEvent(EvEvents.OnGoingDialogClick(false))
                        },
                        onDelete = { event ->
                            if (event.id.isNotEmpty()) {
                                vm.onEvent(EvEvents.OnConfirmGoing)
                            }
                        }
                    )
                }
                if (state.showDeclineDialog) {
                    DeclineEventDialog(
                        title = stringResource(id = R.string.decline_head),
                        onDismiss = {
                            vm.onEvent(EvEvents.onCancelDeclineDialog(false))
                        },
                        onConfirmClick = {
                            vm.onEvent(EvEvents.OnConfirmDeclineClick)
                        },
                        onReasonChange = {
                            vm.onEvent(EvEvents.OnDeclineReasonChange(it))
                        },
                        reason = state.declineReason
                    )
                }

            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_events_large),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
                Text(
                    color = MaterialTheme.appColors.textField.label,
                    text = stringResource(id = R.string.no_upcoming_events),
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    color = MaterialTheme.appColors.textField.label,
                    text = stringResource(id = R.string.add_events_to_use_app),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
        }
        if (state.showLoading) {
            CommonProgressBar()
        }
    }
}

@Composable
fun GameDataItem(
    modifier: Modifier = Modifier,
    publishedGames: PublishedGames,
    moveToEventDetail: (GameData) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                moveToEventDetail.invoke(publishedGames.gameData)
            }
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface,
                    shape = RoundedCornerShape(
                        dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            CoilImage(
                src = BuildConfig.IMAGE_SERVER + publishedGames.gameData.logo,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.appColors.material.onSurface,
                        CircleShape
                    ),
                onError = {
                    Placeholder(R.drawable.ic_team_placeholder)
                },
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                isCrossFadeEnabled = false
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = publishedGames.gameData.name,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                color = MaterialTheme.appColors.buttonColor.backgroundEnabled
            )
        }
    }
}