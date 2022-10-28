package com.allballapp.android.ui.features.profile.tabs

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.DialogButton
import com.allballapp.android.ui.features.profile.ProfileViewModel
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import java.util.*
import com.allballapp.android.R
@Composable
fun RefereeScheduleTab(vm: ProfileViewModel) {

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
                    { CalendarView(it) },
                    modifier = Modifier.fillMaxWidth(),
                    update = { views ->
                        /* views.date = scheduleViewModel.selectedCalender.value.timeInMillis*/
                        views.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                            val cal = Calendar.getInstance()
                            cal.set(year, month, dayOfMonth)
                            /* scheduleViewModel.onEvent(ScheduleEvent.DateSelected(cal))
                             onDateSelect()*/
                        }
                    }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_16dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DialogButton(
                        text = stringResource(R.string.dialog_button_cancel),
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = dimensionResource(id = R.dimen.size_10dp)),
                        border = ButtonDefaults.outlinedBorder,
                        onlyBorder = true,
                        enabled = false
                    )
                    DialogButton(
                        text = stringResource(R.string.update),
                        onClick = {

                        },
                        modifier = Modifier
                            .weight(1f),
                        border = ButtonDefaults.outlinedBorder,
                        enabled = true,
                        onlyBorder = false,
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp))
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    .background(
                        color = Color.White, shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        )
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppText(
                    text = "Thu, Aug 23",
                    color = ColorBWBlack,
                    fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))

                )

                Row {
                    AppText(
                        text = "4:00PM - 7:45 PM",
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_56dp))
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    .background(
                        color = Color.White, shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        )
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppText(
                    text = "Thu, Aug 23",
                    color = ColorBWBlack,
                    fontWeight = FontWeight.W500,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp))
                )

                Row {
                    AppText(
                        text = "4:00PM - 7:45 PM",
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
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        }
    }
}