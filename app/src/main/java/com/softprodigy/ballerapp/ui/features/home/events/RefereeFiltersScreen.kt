package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.EventType
import com.softprodigy.ballerapp.data.response.Format
import com.softprodigy.ballerapp.data.response.GenderList
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.DividerCommon
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun RefereeFiltersScreen(vm: EventViewModel, onSuccess: () -> Unit) {

    remember {
        vm.onEvent(EvEvents.GetRefereeFilters)
    }

    val state = vm.eventState.value
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppText(
                text = stringResource(id = R.string.gender),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                        color = Color.White
                    )
            ) {
                state.genderList.forEach {
                    RefereeGenderFilterItem(it.name, it.status, it) {
                        /*vm.onEvent(EvEvents.GenderSelected(it))*/
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            DistanceItem()

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            AppText(
                text = stringResource(id = R.string.event_Type),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier.fillMaxWidth(),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                textAlign = TextAlign.Start

            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            FlowRow(
                modifier = Modifier.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    color = Color.White
                )
            ) {
                state.eventType.forEach {
                    RefereeEventTypeFilterItem(it.name, it.status, it) {
                        /* vm.onEvent(EvEvents.EventType(it))*/
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            AppText(
                text = stringResource(id = R.string.format),
                style = MaterialTheme.typography.h2,
                color = ColorBWBlack,
                modifier = Modifier.fillMaxWidth(),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                textAlign = TextAlign.Start

            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            FlowRow(
                modifier = Modifier.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    color = Color.White
                )
            ) {
                state.formatList.forEach {
                    RefereeFormatFilterItem(it.name, it.status, it) {
                        /*vm.onEvent(EvEvents.Format(it))*/
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            AppButton(
                enabled = true,
                icon = null,
                themed = true,
                onClick = {
                    onSuccess()
                },
                text = stringResource(id = R.string.save),
                isForceEnableNeeded = true,

                )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        }

    }

}

@Composable
fun RefereeGenderFilterItem(
    name: String,
    selected: Boolean,
    genderList: GenderList,
    onSelection: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val status = remember {
            mutableStateOf(genderList.status)
        }

        AppText(
            text = name,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.W500,
        )

        Switch(
            modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
            checked = status.value,
            onCheckedChange = {
                /* onSelection()*/
                genderList.status = !genderList.status
                status.value = genderList.status

            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
            )
        )

    }
    DividerCommon()

}


@Composable
fun RefereeEventTypeFilterItem(
    name: String,
    selected: Boolean,
    eventType: EventType,
    onSelection: () -> Unit
) {
    val status = remember {
        mutableStateOf(eventType.status)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        AppText(
            text = name,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.W500,
        )

        Switch(
            modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
            checked = status.value,
            onCheckedChange = {
                /* onSelection()*/
                eventType.status = !eventType.status
                status.value = eventType.status
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
            )
        )

    }
    DividerCommon()

}


@Composable
fun RefereeFormatFilterItem(
    name: String,
    selected: Boolean,
    format: Format,
    onSelection: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val status = remember {
            mutableStateOf(format.status)
        }

        AppText(
            text = name,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.W500,
        )

        Switch(
            modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
            checked = status.value,
            onCheckedChange = {
                /* onSelection()*/
                format.status = !format.status
                status.value = format.status
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
            )
        )

    }
    DividerCommon()

}