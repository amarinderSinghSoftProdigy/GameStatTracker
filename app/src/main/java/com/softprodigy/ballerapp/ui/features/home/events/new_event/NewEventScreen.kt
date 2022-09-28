package com.softprodigy.ballerapp.ui.features.home.events.new_event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.get24HoursTimeWithAMPM
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewEventScreen(vm: NewEventViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(pageCount = 3) // Add the count for number of pages
        Column(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    color = Color.White
                )

        ) {
            AppText(
                text = stringResource(id = R.string.event_type),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                EventTabs(pagerState = pagerState, onSelectionChange = {
                    vm.onEvent(NewEvEvent.OnEventTypeChange(it))

                })
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            PracticeScreen(vm)
        }

        ButtonWithLeadingIcon(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(
                    dimensionResource(
                        id = R.dimen.size_140dp
                    )
                ),
            text = stringResource(id = R.string.save),
            onClick = {

            },
            iconAllowed = false
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventTabs(pagerState: PagerState,onSelectionChange:(String)->Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val width = screenWidth.minus(dimensionResource(id = R.dimen.size_16dp).times(4))

    val list = listOf(EventTabItems.Practice, /*EventTabItems.Game,*/ EventTabItems.Activity)
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .width(width)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))),
        indicator = {}
    ) {

        list.forEachIndexed { index, text ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_32dp))
                    .width(dimensionResource(id = R.dimen.size_100dp))
                    .background(
                        if (selected)
                            MaterialTheme.appColors.material.primaryVariant
                        else
                            MaterialTheme.appColors.material.primary
                    ),
                selected = selected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = text.name,
                        color = if (selected) Color.White else MaterialTheme.appColors.textField.label
                    )
                }
            )
        }
        onSelectionChange.invoke(list[pagerState.currentPage].stringId)

    }
}

enum class EventTabItems(val stringId: String) {
    Practice(stringId = "practice"),
    Game(stringId = "game"),
    Activity(stringId = "activity")
}

@SuppressLint("SimpleDateFormat")
@Composable
fun PracticeScreen(vm: NewEventViewModel) {

    val state = vm.state.value

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
            vm.onEvent(NewEvEvent.OnDateChanged("$month_name $mDayOfMonth, $mYear"))
        }, mYear, mMonth, mDay
    )

    val mArrivalPickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            vm.onEvent(NewEvEvent.OnArrivalTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))
        }, mHour, mMinute, false
    )

    val mStartTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->

            vm.onEvent(NewEvEvent.OnStartTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))

        }, mHour, mMinute, false
    )

    val mEndTimePickerDialog = TimePickerDialog(
        context,
        { _, mHour: Int, mMinute: Int ->
            vm.onEvent(NewEvEvent.OnEndTimeChanged(get24HoursTimeWithAMPM("$mHour:$mMinute")))
        }, mHour, mMinute, false
    )

    Box {
        Column {
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                title = stringResource(R.string.event_name),
                label = stringResource(id = R.string.enter_event_name),
                selectedValue = state.eventName,
                isEditableField = true,
                onSelectedValueChange = {
                    vm.onEvent(NewEvEvent.OnEventNameChange(it))

                }
            ) {
                mDatePickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                title = stringResource(R.string.date),
                icon = painterResource(id = R.drawable.ic_calender),
                label = stringResource(id = R.string.select_date),
                selectedValue = state.selectedDate,
                onSelectedValueChange = {

                }
            ) {
                mDatePickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.arrival_time),
                label = stringResource(id = R.string.select_arrival_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedArrivalTime,
                onSelectedValueChange = {

                }
            ) {
                mArrivalPickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.start_time),
                label = stringResource(id = R.string.select_start_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedStartTime,
                onSelectedValueChange = {

                }
            ) {
                mStartTimePickerDialog.show()
            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.end_time),
                label = stringResource(id = R.string.select_end_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedEndTime,
                onSelectedValueChange = {

                }
            ) {
                mEndTimePickerDialog.show()

            }

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.location),
                label = stringResource(R.string.select_location),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                selectedValue = state.selectedLocation,
                onSelectedValueChange = {

                }
            ) {}

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.address),
                label = stringResource(R.string.send_address),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                selectedValue = state.selectedAddress,
                onSelectedValueChange = {

                }
            ) {}

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                stringResource(R.string.send_push_notification),
                onlyIcon = true,
                onSelectedValueChange = {

                }
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
    isEditableField:Boolean=false,
    onSelectedValueChange:(String)->Unit,
    OnClick: () -> Unit,

) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        backgroundColor = Color.Transparent
    )

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
                    if(!isEditableField){
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

                    }
                    else{
                        Box(Modifier.fillMaxSize()) {


                            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                                TextField(
                                    value = selectedValue, onValueChange = onSelectedValueChange,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        backgroundColor = Color.Transparent,
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    ),
                                    textStyle = TextStyle(
                                        textAlign = TextAlign.End,
                                        color = ColorBWBlack
                                    ),
                                    singleLine = true,
                                )

                            }
                            AppText(
                                text = if(selectedValue.isEmpty()) label else "",
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.appColors.textField.label,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(
                                        end = dimensionResource(
                                            id = R.dimen.size_24dp
                                        )
                                    )
                            )
                        }
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