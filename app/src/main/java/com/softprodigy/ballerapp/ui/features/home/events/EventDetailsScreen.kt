package com.softprodigy.ballerapp.ui.features.home.events

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.apiToUIDateFormat2
import com.softprodigy.ballerapp.common.convertServerUtcDateToLocal
import com.softprodigy.ballerapp.common.uiToAPiDate
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.venue.Location
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.appColors
import timber.log.Timber

@Composable
fun EventDetailsScreen(vm: EventViewModel, eventId: String) {

    val state = vm.eventState.value
    val context = LocalContext.current
    remember {
        vm.onEvent(EvEvents.RefreshEventDetailsScreen(eventId))
    }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White),
    ) {
        Timber.i("EventDetailsScreen-- $eventId")

        LaunchedEffect(key1 = state.event.serverDate) {
            val arrivalTime =
                uiToAPiDate("${apiToUIDateFormat2(state.event.date)} ${state.event.arrivalTime}")
            val endTime =
                uiToAPiDate("${apiToUIDateFormat2(state.event.date)} ${state.event.endTime}")
            val serverDateTime = convertServerUtcDateToLocal(state.event.serverDate)

            /*Comparing arrival time and server time to show pre note button*/
            val preCompare = arrivalTime?.compareTo(serverDateTime)
            if (preCompare != null) {
                when {
                    preCompare > 0 -> {
                        Timber.i("DateCompare-- arrivalTime($arrivalTime) is after serverDate($serverDateTime)")
                        vm.onEvent(EvEvents.PreNoteTimeSpan(showPreNoteButton = true))
                    }
                    else -> {
                        Timber.i("DateCompare-- arrivalTime($arrivalTime) is before serverDate($serverDateTime)")
                        vm.onEvent(EvEvents.PreNoteTimeSpan(showPreNoteButton = false))
                    }
                }
            } else {
                Timber.i("DateCompare-- cmp is null")
            }

            /*Comparing end time and server time to show post note button*/
            val postCompare = endTime?.compareTo(serverDateTime)
            if (postCompare != null) {
                when {
                    postCompare > 0 -> {
                        Timber.i("DateCompare-- EndTime($endTime) is after serverDate($serverDateTime)")
                        vm.onEvent(EvEvents.PostNoteTimeSpan(showPostNoteButton = false))
                    }
                    else -> {
                        Timber.i("DateCompare-- EndTime($endTime) is before serverDate($serverDateTime)")
                        vm.onEvent(EvEvents.PostNoteTimeSpan(showPostNoteButton = true))
                    }
                }
            } else {
                Timber.i("DateCompare-- cmp is null")
            }
        }

        LaunchedEffect(key1 = Unit) {
            vm.eventChannel.collect { uiEvent ->
                when (uiEvent) {
                    is EventChannel.ShowEventDetailsToast -> {
                        Toast.makeText(
                            context,
                            uiEvent.message.asString(context),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    is EventChannel.OnUpdateNoteSuccess -> {
                        Toast.makeText(
                            context,
                            uiEvent.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        vm.onEvent(EvEvents.RefreshEventDetailsScreen(eventId = eventId))
                    }
                }
            }
        }


        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            AppText(
                text = stringResource(id = R.string.events_info),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
            Row {
                AppText(
                    text = stringResource(id = R.string.date),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.8f)
                )
                AppText(
                    text = stringResource(id = R.string.arrival_time),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.5f)
                )
                AppText(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.8f)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            Row {
                Text(
                    text = apiToUIDateFormat2(state.event.date),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.8f)
                )
                Text(
                    text = state.event.arrivalTime,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.5f)

                )
                Text(
                    text = "${state.event.startTime} - ${state.event.endTime}",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.8f)
                )
            }
            LocationBlock(
                Location(
                    state.event.landmarkLocation,
                    state.event.address.street,
                    "",
                    state.event.address.zip,
                ),
                padding = 0.dp
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        AppDivider(color = MaterialTheme.appColors.material.primary)

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.rsvp),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
        }

        LazyRow {
            itemsIndexed(state.event.invites) { index, item ->
                Column(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
                    CoilImage(
                        src = BuildConfig.IMAGE_SERVER + item.profileImage,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(dimensionResource(id = R.dimen.size_44dp)),
                        onError = {
                            Placeholder(R.drawable.ic_profile_placeholder)
                        },
                        onLoading = { Placeholder(R.drawable.ic_profile_placeholder) },
                        isCrossFadeEnabled = false,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    Text(
                        text = item.name.ifEmpty { stringResource(id = R.string.na) },
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500
                    )

                    Text(
                        text = item.status,
                        color = if (item.status.equals(
                                EventStatus.GOING.status,
                                ignoreCase = true
                            )
                        )
                            ColorButtonGreen
                        else if (item.status.equals(
                                EventStatus.NOT_GOING.status,
                                ignoreCase = true
                            )
                        )
                            ColorButtonRed
                        else
                            MaterialTheme.appColors.textField.labelDark,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

        AppDivider(color = MaterialTheme.appColors.material.primary)

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = stringResource(id = R.string.jersey_color),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = state.event.jerseyColor,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }

        AppDivider(color = MaterialTheme.appColors.material.primary)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))


        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.isPrePracticeTimeSpan) {
                    Text(
                        text = stringResource(id = R.string.pre_practive_head),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500
                    )
                }
                if (state.isPrePracticeTimeSpan && state.event.prePractice.isEmpty() && state.event.createdBy == UserStorage.userId) {
                    Text(
                        text = stringResource(id = R.string.add_note),
                        color = MaterialTheme.appColors.buttonColor.textEnabled,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier
                            .shadow(
                                dimensionResource(id = R.dimen.size_4dp),
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .background(
                                color = MaterialTheme.appColors.material.primaryVariant,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .clickable {
                                vm.onEvent(
                                    EvEvents.ShowPrePostPracticeAddNoteDialog(
                                        true,
                                        NoteType.PRE
                                    )
                                )
                            }
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.size_12dp),
                                vertical = dimensionResource(id = R.dimen.size_10dp)
                            )

                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

            if (state.event.prePractice.isNotEmpty() && state.isPrePracticeTimeSpan) {
                Text(
                    text = state.event.prePractice,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + state.event.coachId.profileImage,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(dimensionResource(id = R.dimen.size_24dp)),
                            onError = {
                                Placeholder(R.drawable.ic_coach_place_holder)
                            },
                            onLoading = { Placeholder(R.drawable.ic_coach_place_holder) },
                            isCrossFadeEnabled = false,
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        AppText(
                            text = state.event.coachId.name.ifEmpty { stringResource(id = R.string.na) },
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            style = MaterialTheme.typography.h6
                        )
                    }

                    Row(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.size_32dp))
                            .background(
                                color = MaterialTheme.appColors.material.primaryVariant,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tick),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp))
                        )
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.read_the_note),
                            color = Color.White,
                            style = MaterialTheme.typography.button
                        )
                        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                    }
                }
            }

        }

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state.isPostPracticeTimeSpan) {
                    Text(
                        text = stringResource(id = R.string.post_practive_head),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500
                    )
                }
                if (state.isPostPracticeTimeSpan && state.event.postPractice.isEmpty() && state.event.createdBy == UserStorage.userId) {
                    Text(
                        text = stringResource(id = R.string.add_note),
                        color = MaterialTheme.appColors.buttonColor.textEnabled,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier
                            .shadow(
                                dimensionResource(id = R.dimen.size_4dp),
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .background(
                                color = MaterialTheme.appColors.material.primaryVariant,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .clickable {
                                vm.onEvent(
                                    EvEvents.ShowPrePostPracticeAddNoteDialog(
                                        true,
                                        NoteType.POST
                                    )
                                )
                            }
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.size_12dp),
                                vertical = dimensionResource(id = R.dimen.size_10dp)
                            )

                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

            if (state.event.postPractice.isNotEmpty() && state.isPostPracticeTimeSpan) {
                Text(
                    text = state.event.postPractice,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + state.event.coachId.profileImage,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(dimensionResource(id = R.dimen.size_24dp)),
                            onError = {
                                Placeholder(R.drawable.ic_coach_place_holder)
                            },
                            onLoading = { Placeholder(R.drawable.ic_coach_place_holder) },
                            isCrossFadeEnabled = false,
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        AppText(
                            text = state.event.coachId.name.ifEmpty { stringResource(id = R.string.na) },
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            style = MaterialTheme.typography.h6
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }

        if (state.showPrePostNoteDialog) {
            AddNoteDialog(
                onDismiss = { noteType ->
                    vm.onEvent(EvEvents.ShowPrePostPracticeAddNoteDialog(false, noteType))
                },
                onConfirmClick = { noteType, note ->
                    vm.onEvent(EvEvents.OnAddNoteConfirmClick(noteType, note, eventId))
                },
                note = state.note,
                noteType = state.noteType,
                onNoteChange = { note -> vm.onEvent(EvEvents.OnNoteChange(note)) }
            )
        }

        if (state.showLoading) {
            CommonProgressBar()
        }

    }
}

