package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FilterScreen(vm: EventViewModel) {
    val state = vm.eventState.value
    var selectedGender by rememberSaveable { mutableStateOf("") }
    var selectedEvent by rememberSaveable { mutableStateOf("") }
    var selectedFormat by rememberSaveable { mutableStateOf("") }
    var selectedLevel by rememberSaveable { mutableStateOf("") }
    var genderList = arrayListOf<String>(
        stringResource(id = R.string.male),
        stringResource(id = R.string.female)
    )
    var eventType = arrayListOf<String>(
        stringResource(id = R.string.league),
        stringResource(id = R.string.clinic),
        stringResource(id = R.string.camp)
    )
    var format = arrayListOf<String>(
        stringResource(id = R.string.individual),
        stringResource(id = R.string.team)
    )
    var level = arrayListOf<String>(
        stringResource(id = R.string.elite),
        stringResource(id = R.string.competitive),
        stringResource(id = R.string.developmental),
        stringResource(id = R.string.learning)
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            fiterItem(stringResource(id = R.string.gender), genderList, setSelected = {
                selectedGender = it
            }, selectedGender)
            distanceItem()
            fiterItem(stringResource(id = R.string.event_type), eventType, setSelected = {
                selectedEvent = it
            }, selectedEvent)
            fiterItem(stringResource(id = R.string.format), format, setSelected = {
                selectedFormat = it
            }, selectedFormat)
            fiterItem(stringResource(id = R.string.level), level, setSelected = {
                selectedLevel = it
            }, selectedLevel)
            AppButton(
                enabled = true,
                icon = null,
                themed = true,
                onClick = { },
                text = stringResource(id = R.string.save),
                isForceEnableNeeded = true
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

        }
    }
}

@Composable
fun distanceItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.distance),
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.size_16dp))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White)
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.max_dis),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_sub),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_25dp))
                    .clip(CircleShape)
            )
            Text(
                text = "5mi",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.size_8dp))
            )
            Image(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_25dp))
                    .clip(CircleShape)
            )
        }
    }
}


@Composable
fun fiterItem(
    heading: String,
    list: ArrayList<String>,
    setSelected: (value: String) -> Unit,
    selectedValue: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = heading,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.size_16dp))
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White)
                .padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),

            ) {
            FlowRow(Modifier.fillMaxWidth()) {
                list.forEach() { value ->
                    Column() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = value,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = value == selectedValue,
                                onCheckedChange = { setSelected(value) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                                )
                            )
                        }
                        AppDivider()
                    }
                }
            }
        }
    }
}
