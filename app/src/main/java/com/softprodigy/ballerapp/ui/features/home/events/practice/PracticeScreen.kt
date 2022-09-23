package com.softprodigy.ballerapp.ui.features.home.events.practice

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.get24HoursTimeWithAMPM
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.appColors
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun PracticeScreen(vm: PracticeViewModel = hiltViewModel()) {

    val state = vm.practiceUiState.value

    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val month_date = SimpleDateFormat("MMM")

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mCalendar.set(Calendar.MONTH, mMonth)
            val month_name = month_date.format(mCalendar.time)
            vm.onEvent(PracticeUIEvent.OnDateChanged("$month_name $mDayOfMonth, $mYear"))
        }, mYear, mMonth, mDay
    )

    val mArrivalPickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            vm.onEvent(PracticeUIEvent.OnArrivalTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))
        }, mHour, mMinute, false
    )

    val mStartTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->

            vm.onEvent(PracticeUIEvent.OnStartTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))

        }, mHour, mMinute, false
    )

    val mEndTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            vm.onEvent(PracticeUIEvent.OnEndTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))
        }, mHour, mMinute, false
    )

    Box {
        Column {
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                title = stringResource(R.string.date),
                icon = painterResource(id = R.drawable.ic_calender),
                label = stringResource(id = R.string.select_date),
                selectedValue = state.selectedDate
            ) {
                mDatePickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.arrival_time),
                label = stringResource(id = R.string.select_arrival_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedArrivalTime
            ) {
                mArrivalPickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.start_time),
                label = stringResource(id = R.string.select_start_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedStartTime
            ) {
                mStartTimePickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.end_time),
                label = stringResource(id = R.string.select_end_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedEndTime
            ) {
                mEndTimePickerDialog.show()

            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.location),
                label = stringResource(R.string.select_location),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                selectedValue = state.selectedLocation
            ) {}

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.address),
                label = stringResource(R.string.send_address),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                selectedValue = state.selectedAddress
            ) {}

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                stringResource(R.string.send_push_notification),
                onlyIcon = true,
            ) {}

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        }
    }
}

@Composable
fun PracticeItem(
    title: String,
    selectedValue: String = "",
    label: String = "",
    icon: Painter? = null,
    onlyIcon: Boolean = false,
    color: Color = MaterialTheme.appColors.material.primaryVariant,
    OnClick: () -> Unit
) {

    var notification by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_56dp))
            .fillMaxWidth()
            .background(color = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp), end = dimensionResource(
                        id = R.dimen.size_14dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AppText(
                text = title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!onlyIcon) {

                    if (selectedValue.isEmpty()) {
                        AppText(
                            text = label,
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.appColors.textField.label,
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                    } else {
                        AppText(
                            text = selectedValue,
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                    }

                    if (icon != null) {
                        Image(
                            painter = icon, contentDescription = "", modifier = Modifier
                                .size(
                                    dimensionResource(id = R.dimen.size_14dp)
                                )
                                .clickable { OnClick() },
                            colorFilter = ColorFilter.tint(color = color)
                        )
                    }
                } else {
                    Switch(
                        checked = notification,
                        onCheckedChange = {
                            notification = it
                        }, colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                        )
                    )
                }
            }
        }
    }
}