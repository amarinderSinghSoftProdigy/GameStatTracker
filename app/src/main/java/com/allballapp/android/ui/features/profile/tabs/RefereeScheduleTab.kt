package com.allballapp.android.ui.features.profile.tabs

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.argbToHexString
import com.allballapp.android.data.response.ScheduleResponseObject
import com.allballapp.android.ui.features.components.AppButton
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.profile.ProfileChannel
import com.allballapp.android.ui.features.profile.ProfileEvent
import com.allballapp.android.ui.features.profile.ProfileViewModel
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.ColorButtonRed
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.utils.CommonUtils
import com.google.accompanist.flowlayout.FlowRow
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*

@Composable
fun RefereeScheduleTab(vm: ProfileViewModel) {

    val state = vm.state.value
    val clearCalendar = remember {
        mutableStateOf(false)
    }
    val selectedDates = remember {
        mutableStateOf(ArrayList<Date>())
    }

    remember {
        vm.onEvent(ProfileEvent.GetStaffSchedule)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        vm.channel.collect {
            when (it) {
                is ProfileChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        it.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            AppText(
                text = stringResource(id = R.string.selectDates),
                style = MaterialTheme.typography.h5,
                color = ColorBWBlack,
                fontWeight = FontWeight.W500
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RectangleShape)
            ) {
                AndroidView(
                    { MaterialCalendarView(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.size_16dp),
                            start = dimensionResource(id = R.dimen.size_16dp),
                            end = dimensionResource(id = R.dimen.size_16dp)
                        ),
                    update = { views ->
                        views.selectionMode = MaterialCalendarView.SELECTION_MODE_MULTIPLE
                        if (clearCalendar.value) {
                            views.clearSelection()
                        }
                        views.setOnDateChangedListener { widget, date, selected ->
                            try {
                                if (selected) {
                                    selectedDates.value.add(date.date)
                                } else if (selectedDates.value.contains(date.date)) {
                                    selectedDates.value.remove(date.date)
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        if ((clearCalendar.value && state.scheduleList.isNotEmpty()) || (state.scheduleList.isNotEmpty() && selectedDates.value.isEmpty() && !clearCalendar.value)) {
                            views.selectionColor = android.graphics.Color.parseColor(
                                ColorButtonRed.toArgb().argbToHexString()
                            )
                            state.scheduleList.forEach {
                                if (it.date.isNotEmpty()) {
                                    val calendar = Calendar.getInstance()
                                    val date = CommonUtils.getDateString(it.date).split("-")
                                    calendar.set(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt()
                                    )
                                    views.selectedDate = CalendarDay.from(calendar)
                                }
                            }
                        } else {
                            views.selectionColor = android.graphics.Color.parseColor(
                                AppConstants.SELECTED_COLOR.toArgb().argbToHexString()
                            )
                        }
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_16dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    AppButton(
                        text = stringResource(R.string.dialog_button_cancel),
                        onClick = {
                            clearCalendar.value = true
                            selectedDates.value = ArrayList()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                        border = ButtonDefaults.outlinedBorder,
                        singleButton = true
                    )
                    AppButton(
                        text = stringResource(R.string.update),
                        onClick = {
                            if (selectedDates.value.isNotEmpty()) {
                                vm.onEvent(ProfileEvent.UpdateScheduleStaff(selectedDates.value))
                                clearCalendar.value = false
                                selectedDates.value = ArrayList()
                            }
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

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))

            if (state.scheduleList.isNotEmpty()) {
                AppText(
                    text = stringResource(id = R.string.unavailable_dates),
                    style = MaterialTheme.typography.h5,
                    color = ColorBWBlack,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(id = R.dimen.size_16dp)),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
                FlowRow {
                    state.scheduleList.forEach {
                        UnavailableItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun UnavailableItem(data: ScheduleResponseObject) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_56dp))
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_16dp),
                vertical = dimensionResource(id = R.dimen.size_4dp)
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_8dp)
                )
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        AppText(
            text = CommonUtils.formatDateSingle(data.date, AppConstants.DATE_DAY_FORMAT),
            color = ColorBWBlack,
            fontWeight = FontWeight.W500,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
        )
        Row {
            AppText(
                text = data.startTime + " - " + data.endTime,
                color = ColorBWBlack,
                fontWeight = FontWeight.W500,
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

            Icon(
                painter = painterResource(id = R.drawable.ic_date),
                contentDescription = "",
                tint = MaterialTheme.appColors.material.primaryVariant
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

        }

    }
}