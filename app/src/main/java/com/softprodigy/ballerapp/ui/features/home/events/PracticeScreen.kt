package com.softprodigy.ballerapp.ui.features.home.events

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun PracticeScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.date),
                "May 23, 2022",
                painterResource(id = R.drawable.ic_calender)
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.arrival_time),
                "5:45 PM",
                painterResource(id = R.drawable.ic_date)
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.start_time),
                "6:00 PM",
                painterResource(id = R.drawable.ic_date)
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.end_time),
                "7:00 PM",
                painterResource(id = R.drawable.ic_date)
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.location),
                stringResource(R.string.select_location),
                painterResource(id = R.drawable.ic_next),
                choose = true
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.address),
                stringResource(R.string.send_address),
                painterResource(id = R.drawable.ic_next),
                choose = true
            )
            Divider(color = MaterialTheme.appColors.material.primary)
            PracticeItem(
                stringResource(R.string.send_push_notification),
                onlyIcon = true
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
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


    }
}

@Composable
fun PracticeItem(
    title: String,
    subtitle: String = "",
    icon: Painter? = null,
    choose: Boolean = false,
    onlyIcon: Boolean = false
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
                if (!choose && !onlyIcon) {
                    AppText(
                        text = subtitle,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                    if (icon != null) {
                        Image(
                            painter = icon, contentDescription = "", modifier = Modifier.size(
                                dimensionResource(id = R.dimen.size_14dp)
                            ),
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.material.primaryVariant)
                        )
                    }
                } else if (choose && !onlyIcon) {
                    AppText(
                        text = subtitle,
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.appColors.textField.label,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                    if (icon != null) {
                        Image(
                            painter = icon, contentDescription = "", modifier = Modifier.size(
                                dimensionResource(id = R.dimen.size_8dp)
                            ),
                            colorFilter = ColorFilter.tint(MaterialTheme.appColors.textField.label)
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