package com.allballapp.android.ui.features.home.events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.common.apiToUIDateFormat
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.components.DeclineEventDialog
import com.allballapp.android.ui.features.components.DeleteDialog
import com.allballapp.android.ui.theme.*
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
        if (state.upcomingAndGameData.isNotEmpty() || state.pastEvents.isNotEmpty()) {
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
                                    EventItem(events = data, onAcceptCLick = { event ->
                                        vm.onEvent(EvEvents.OnGoingCLick(event.id,EventType.PRACTICE.type))
                                    }, onDeclineCLick = { event ->
                                        vm.onEvent(EvEvents.OnDeclineCLick(event.id,EventType.PRACTICE.type))
                                    }, moveToPracticeDetail = moveToPracticeDetail,
                                        moveToGameDetail = moveToGameDetail,
                                        isPast = false,
//                                        isSelfCreatedEvent = data.createdBy == UserStorage.userId
                                        isSelfCreatedEvent = false
                                    )
                                }
                                is PublishedGames -> {
                                    GameDataItem(
                                        publishedGames = data,
                                        moveToGameDetail = { gameId ->
//                                            moveToGameDetail.invoke(gameId)
                                        }, onAcceptCLick = { eventId ->
                                            vm.onEvent(EvEvents.OnGoingCLick(eventId,EventType.GAME.type))

                                        },
                                        onDeclineCLick = { eventId ->
                                            vm.onEvent(EvEvents.OnDeclineCLick(eventId,EventType.GAME.type))
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
                                }, onDeclineCLick = { eventId ->
                                    vm.onEvent(EvEvents.OnDeclineCLick(eventId.id,EventType.PRACTICE.type))
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
                        item = state.selectedMyEventId,
                        message = stringResource(id = R.string.alert_going_event_confirm),
                        onDismiss = {
                            vm.onEvent(EvEvents.OnGoingDialogClick(false))
                        },
                        onDelete = { eventId ->
                            if (eventId.isNotEmpty()) {
                                vm.onEvent(EvEvents.OnConfirmGoing(EventType.PRACTICE.type))
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
                            vm.onEvent(EvEvents.OnConfirmDeclineClick(EventType.PRACTICE.type))
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
    moveToGameDetail: (gameId: String) -> Unit,
    onAcceptCLick: (String) -> Unit,
    onDeclineCLick: (String) -> Unit,
) {

    Box(
        modifier = modifier
            .clickable {
                moveToGameDetail.invoke(publishedGames.Id)
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.appColors.material.surface,
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp),
                        )
                    )
                    .padding(
                        dimensionResource(id = R.dimen.size_16dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = publishedGames.pairname,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(
                                    color = ColorMainPrimary
                                )
                                .padding(dimensionResource(id = R.dimen.size_4dp))
                        ) {
                            Text(
                                text = EventType.GAME.type,
                                color = Color.White,
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_5dp))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = publishedGames.gameData.name,
                            color = MaterialTheme.appColors.textField.label,
                            style = MaterialTheme.typography.h4
                        )


                        Text(
                            text = "${apiToUIDateFormat(publishedGames.date)} ${publishedGames.timeslot.toUpperCase()}",
                            color = ColorBWGrayLight,
                            style = MaterialTheme.typography.h4
                        )
                    }


                }
            }
            if (publishedGames.invitationStatus.equals(
                    EventStatus.MAY_BE.status,
                    ignoreCase = true
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = ColorPrimaryTransparent,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .clickable {
                                onDeclineCLick.invoke(publishedGames.Id)
                            }
                            .padding(dimensionResource(id = R.dimen.size_14dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross_2),
                            contentDescription = "",
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        Text(
                            text = stringResource(id = R.string.decline),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,

                            )
                    }

                    Row(
                        Modifier
                            .weight(1f)
                            .clickable {
                                onAcceptCLick.invoke(publishedGames.Id)
                            }
                            .padding(dimensionResource(id = R.dimen.size_14dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = "",
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        Text(
                            text = stringResource(id = R.string.accept),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                        )
                    }

                }
            } else if (publishedGames.invitationStatus.equals(
                    EventStatus.GOING.status,
                    ignoreCase = true
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = ColorButtonGreen,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        )
                        .clickable {

                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                        tint = MaterialTheme.appColors.buttonColor.textEnabled,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = stringResource(id = R.string.going),
                        color = MaterialTheme.appColors.buttonColor.textEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }
            } else if (publishedGames.invitationStatus.equals(
                    EventStatus.NOT_GOING.status,
                    ignoreCase = true
                )
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = ColorButtonRed,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        )
                        .clickable {

                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross_2),
                            contentDescription = "",
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                            tint = MaterialTheme.appColors.buttonColor.textEnabled,
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        Text(
                            text = stringResource(id = R.string.not_going),
                            color = MaterialTheme.appColors.buttonColor.textEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = rubikFamily
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = publishedGames.reason,
                            color = MaterialTheme.appColors.buttonColor.textEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                        )
                    }
                }
            }
        }
    }
}
