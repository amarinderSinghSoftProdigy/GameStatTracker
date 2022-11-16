package com.allballapp.android.ui.features.home.events

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.AppButton
import com.allballapp.android.ui.features.components.AppDivider
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.theme.appColors

@Composable
fun FilterScreen(vm: EventViewModel, onSuccess: () -> Unit) {
    val state = vm.eventState.value
    val context = LocalContext.current

    remember {
        vm.onEvent(EvEvents.GetFilters)
    }

    LaunchedEffect(key1 = true) {
        vm.eventChannel.collect { uiEvent ->
            when (uiEvent) {
                is EventChannel.ShowToast -> {
                   /* Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()*/
                }
                is EventChannel.OnSuccess -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                    onSuccess()
                }
            }
        }
    }

    if (state.filters.isEmpty()) {
        if (state.isLoading) {
            CommonProgressBar()
        } else {
            EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow {
                    state.filters.forEach { item ->
                        FilterItem(
                            heading = item.key,
                            list = item.value,
                            setSelected = {

                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                /*DistanceItem()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            */
                AppButton(
                    enabled = true,
                    icon = null,
                    themed = true,
                    onClick = {
                        vm.onEvent(EvEvents.UpdateFilters(FilterUpdateRequest(state.filterPreference.toMutableList())))
                    },
                    text = stringResource(id = R.string.save),
                    isForceEnableNeeded = true
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
        }
    }

}

@Composable
fun DistanceItem() {
    val distance = remember {
        mutableStateOf(0)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.distance),
            color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_16dp),
                    vertical = dimensionResource(id = R.dimen.size_12dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.max_dis),
                color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_sub),
                contentDescription = "",
                modifier = Modifier
                    .clickable { if (distance.value != 0) distance.value = distance.value - 1 }
                    .size(dimensionResource(id = R.dimen.size_25dp))
                    .clip(CircleShape)
            )
            Text(
                text = distance.value.toString() + "mi",
                color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.size_12dp))
            )
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "",
                modifier = Modifier
                    .clickable { distance.value = distance.value + 1 }
                    .size(dimensionResource(id = R.dimen.size_25dp))
                    .clip(CircleShape)
            )
        }
    }
}


@Composable
fun FilterItem(
    heading: String,
    list: ArrayList<FilterPreference>,
    setSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Text(
            text = heading.capitalize(),
            color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                    color = Color.White
                )
        ) {
            FlowRow(Modifier.fillMaxWidth()) {
                list.forEachIndexed { index, value ->
                    val status = remember {
                        mutableStateOf(value.status)
                    }
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.size_12dp),
                                    vertical = dimensionResource(id = R.dimen.size_16dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = value.name.capitalize(),
                                color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)),
                                checked = status.value,
                                onCheckedChange = {
                                    value.status = !value.status
                                    status.value = value.status
                                    setSelected(heading)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                                )
                            )
                        }
                        if (index != list.size - 1)
                            AppDivider()
                    }
                }
            }
        }
    }
}
