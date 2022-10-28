package com.allballapp.android.ui.features.home.events.new_event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.common.*
import com.allballapp.android.data.request.Address
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.theme.appColors
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewEventScreen(
    venue: String = "",
    vm: NewEventViewModel = hiltViewModel(),
    onVenueClick: () -> Unit,
    onEventCreationSuccess: () -> Unit,
) {
    val context = LocalContext.current
    val state = vm.state.value

    LaunchedEffect(key1 = Unit) {
        vm.channel.collect { uiEvent ->
            when (uiEvent) {
                is NewEventChannel.OnEventCreationSuccess -> {
                    Toast.makeText(
                        context,
                        uiEvent.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                    onEventCreationSuccess.invoke()
                }
                is NewEventChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
            PracticeScreen(
                venue,
                vm,
                onVenueClick = onVenueClick
            )
        }

        AppButton(
            text = stringResource(id = R.string.save),
            onClick = {
                vm.onEvent(NewEvEvent.OnSaveButtonClick)
            },
            modifier = Modifier
                .width(
                    dimensionResource(
                        id = R.dimen.size_140dp
                    )
                ),
            enabled = state.eventName.isNotEmpty()
                    && validEventName(state.eventName)
                    && state.selectedDate.isNotEmpty()
                    && state.selectedArrivalTime.isNotEmpty()
                    && state.selectedStartTime.isNotEmpty()
                    && state.selectedEndTime.isNotEmpty()
                    && state.selectedVenueName.isNotEmpty()
                    && state.selectedAddress.street.isNotEmpty() && state.pre_practice_prep.isNotEmpty(),

            singleButton = true,
            themed = true,
            isForceEnableNeeded = true
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
    }
    if (state.isLoading) {
        CommonProgressBar()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventTabs(pagerState: PagerState, onSelectionChange: (String) -> Unit) {
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
fun PracticeScreen(
    venue: String,
    vm: NewEventViewModel,
    onVenueClick: () -> Unit
) {

    val state = vm.state.value

    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateString: String = format.format(calendar.time)
            vm.onEvent(NewEvEvent.OnDateChanged(dateString))
        }, mYear, mMonth, mDay
    )
    mDatePickerDialog.datePicker.minDate = System.currentTimeMillis()


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
    LaunchedEffect(key1 = venue, block = {
        vm.onEvent(NewEvEvent.OnLocationVenueChange(venue))
    })

    val placePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { activityResult ->
            val addressReq = Address()

            if (activityResult.resultCode == Activity.RESULT_OK) {
                activityResult.data?.let {
                    val place = activityResult.data?.let { data ->
                        Autocomplete.getPlaceFromIntent(
                            data
                        )
                    }
                    val address = place?.address ?: ""
                    val latLng = place?.latLng
                    val lat = latLng?.latitude ?: 0.0
                    val long = latLng?.longitude ?: 0.0

                    addressReq.street = address
                    addressReq.lat = lat
                    addressReq.long = long

                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(lat, long, 1)
                        val stateName: String = addresses?.get(0)?.adminArea ?: ""
                        val cityName: String = addresses?.get(0)?.locality ?: ""
                        val countryName: String = addresses?.get(0)?.countryName ?: ""
                        val zip: String = addresses?.get(0)?.postalCode ?: ""
                        addressReq.state = stateName
                        addressReq.city = cityName
                        addressReq.country = countryName
                        addressReq.zip = zip
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            if (addressReq.street.isNotEmpty()) {
                vm.onEvent(NewEvEvent.OnAddressChanged(addressReq))
            }
            if (activityResult.resultCode == RESULT_ERROR) {
                activityResult.let {
                    val status = it.data?.let { it1 -> Autocomplete.getStatusFromIntent(it1) }
                    Log.i("statusMessage--", status?.statusMessage ?: "and ${status}")
                    Timber.i("RESULT_ERROR", status?.statusMessage ?: "and ${status}")
                }
            }

        }
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
                    if (it.length <= 30)
                        vm.onEvent(NewEvEvent.OnEventNameChange(it))

                },
                OnClick = {
                    mDatePickerDialog.show()
                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                title = stringResource(R.string.date),
                icon = painterResource(id = R.drawable.ic_calender),
                label = stringResource(id = R.string.select_date),
                selectedValue = apiToUIDateFormat2(state.selectedDate),
                onSelectedValueChange = {

                }, OnClick = {
                    mDatePickerDialog.show()
                },
                onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.arrival_time),
                label = stringResource(id = R.string.select_arrival_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedArrivalTime,
                onSelectedValueChange = {

                }, OnClick = {
                    mArrivalPickerDialog.show()
                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.start_time),
                label = stringResource(id = R.string.select_start_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedStartTime,
                onSelectedValueChange = {

                }, OnClick = {
                    mStartTimePickerDialog.show()
                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.end_time),
                label = stringResource(id = R.string.select_end_time),
                icon = painterResource(id = R.drawable.ic_date),
                selectedValue = state.selectedEndTime,
                onSelectedValueChange = {

                }, OnClick = {
                    mEndTimePickerDialog.show()

                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.location),
                label = stringResource(R.string.select_location),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
//                selectedValue = state.selectedLocation,
                selectedValue = venue,
                onSelectedValueChange = {
                    vm.onEvent(NewEvEvent.OnLocationVenueChange(venue))
                }, OnClick = {
                    onVenueClick.invoke()
                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                title = stringResource(R.string.address),
                label = stringResource(R.string.send_address),
                icon = painterResource(id = R.drawable.ic_next),
                color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                selectedValue = state.selectedAddress.street,
                onSelectedValueChange = {

                }, OnClick = {
                    if (!Places.isInitialized()) {
                        Places.initialize(context.applicationContext, com.allballapp.android.BuildConfig.MAPS_API_KEY)
                    }
                    val fields = listOf(
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG
                    )
                    placePicker.launch(
                        Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(context)
                    )
                }, onNotificationChange = {})

            Divider(color = MaterialTheme.appColors.material.primary)

            AppText(
                text = stringResource(id = R.string.pre_practive_head),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp)),
            )

            AppOutlineTextField(
                value = state.pre_practice_prep,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_16dp))
                    .height(dimensionResource(id = R.dimen.size_80dp)),
                onValueChange = {
                    vm.onEvent(NewEvEvent.OnPrePracticeChange(it))
                },
                placeholder = {

                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                isError = false,
                errorMessage = stringResource(id = R.string.email_error),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.appColors.editField.borderFocused,
                    unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                    backgroundColor = MaterialTheme.appColors.material.background,
                    textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    placeholderColor = MaterialTheme.appColors.textField.label,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                ),
                singleLine = false,
                maxLines = 6
            )

            Divider(color = MaterialTheme.appColors.material.primary)

            PracticeItem(
                stringResource(R.string.send_push_notification),
                onlyIcon = true,
                onSelectedValueChange = {

                }, OnClick = {
                },
                onNotificationChange = {
                    vm.onEvent(NewEvEvent.OnNotificationChange(it))
                })

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
    isEditableField: Boolean = false,
    onSelectedValueChange: (String) -> Unit,
    OnClick: () -> Unit,
    onNotificationChange: (Boolean) -> Unit,

    ) {
    val focusManager = LocalFocusManager.current

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
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(id = R.dimen.size_16dp))
                    .clickable { OnClick() }
            ) {
                if (!onlyIcon) {
                    if (!isEditableField) {
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
                                style = MaterialTheme.typography.h5,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontWeight = FontWeight.W500
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                        }

                    } else {
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
                                    textStyle = MaterialTheme.typography.overline,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Text,
                                        capitalization = KeyboardCapitalization.Sentences
                                    ),
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    isError = !validEventName(selectedValue) && selectedValue.isNotEmpty() || selectedValue.length > 30,
                                )

                            }
                            AppText(
                                text = if (selectedValue.isEmpty()) label else "",
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
                                ),
                            colorFilter = ColorFilter.tint(color = color)
                        )
                    }
                } else {
                    Switch(
                        checked = notification,
                        onCheckedChange = {
                            notification = it
                            onNotificationChange.invoke(it)
                        }, colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                        )
                    )

                }
            }
        }
    }
}