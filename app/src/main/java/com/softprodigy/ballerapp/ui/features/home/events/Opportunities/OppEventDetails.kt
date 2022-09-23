package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.theme.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OppEventDetails(vm: EventViewModel, moveToRegistration: () -> Unit) {
    val state = vm.eventState.value
    var skills = arrayListOf<String>("Elite", "Competitive", "Developmental", "Learning")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Box(
                modifier = Modifier
                    .background(color = heading3Color)
                    .height(dimensionResource(id = R.dimen.size_120dp))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "League Banner",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.events_info),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "Risus enim egestas placerat adipiscing accumsan velit nam varius. Vulputate habitant vitae at laoreet. Arcu, vitae mi enim, aenean. Egestas cras venenatis dis augue felis.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            DetailsItem(
                stringResource(id = (R.string.start_date)),
                stringResource(id = (R.string.end_Date)),
                "Sep 1, 2022",
                "Dec 15, 2022"
            )
            DetailsItem(
                stringResource(id = (R.string.event_type)),
                stringResource(id = (R.string.participation)),
                "League",
                "All, Ages 10-16"
            )
            DetailsItem(
                stringResource(id = (R.string.regsitration_deadliine)),
                stringResource(id = (R.string.price)),
                "Sep 1, 2022",
                "\$1,250.00"
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = (R.string.skill_level)),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            LazyRow() {
                itemsIndexed(skills) { index, item ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.size_6dp))
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .background(MaterialTheme.appColors.material.primaryVariant)
                            .padding(dimensionResource(id = R.dimen.size_6dp))
                    ) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_6dp))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.skill_level),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.location),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Springville HS Gym A",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    modifier = Modifier.weight(1f)
                )
                TransparentButtonButton(
                    text = stringResource(id = R.string.navigate),
                    onClick = {},
                    icon = painterResource(id = R.drawable.ic_nav),
                    enabled = false,
                )
            }
            Text(
                text = "8502 Preston Rd. Inglewood, Maine",
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .background(color = heading3Color)
                    .height(dimensionResource(id = R.dimen.size_120dp))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Google Map",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.event_desc),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "Orci mattis purus egestas amet. Venenatis non eget euismod leo et vitae ac sit tempor. Sit parturient vestibulum turpis euismod enim elit. Rhoncus sit pharetra aliquam non. Pellentesque mi eget penatibus egestas enim, tincidunt amet nec. Imperdiet mauris et scelerisque faucibus nisl.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.contact_With_questions),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Text(
                text = "George Will",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "email@example.com",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "(704) 555-0127",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.days_play),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            DaysPlay()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.player_requirements),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            PlayerRequirements("Birth Certificate", "Grade Verification");
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            PlayerRequirements("Permission Slips", "Organization Waiver");
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            PlayerRequirements("AAU Card", null);
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppButton(
                enabled = true,
                icon = null,
                themed = true,
                onClick = { moveToRegistration() },
                text = stringResource(id = R.string.register),
                isForceEnableNeeded = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

        }
    }
}

@Composable
fun DetailsItem(key1: String, key2: String, value1: String, value2: String) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Row() {
        Text(
            text = key1,
            color = ColorBWGrayLight,
            fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = key2,
            color = ColorBWGrayLight,
            fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
    Row() {
        Text(
            text = value1,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            modifier = Modifier.weight(1f)

        )
        Text(
            text = value2,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
}

@Composable
fun DaysPlay() {
    var days = arrayListOf<String>(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )
    var values = arrayListOf<String>(
        "6:00 PM - 10:00 PM",
        "6:00 PM - 10:00 PM",
        "6:00 PM - 10:00 PM",
        "6:00 PM - 10:00 PM",
        "6:00 PM - 10:00 PM",
        "6:00 PM - 10:00 PM",
        "_"
    )
    days.forEachIndexed { index, it ->
        Row(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_8dp))
        ) {
            Text(
                text = it,
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = values[index],
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                modifier = Modifier.weight(0.6f)
            )
        }
    }
}

@Composable
fun PlayerRequirements(req1: String, req2: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = R.drawable.ic_checkbox),
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_20dp))
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                text = req1,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
        }
        if (req2 != null) {
            Row(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_checkbox),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_20dp))
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                Text(
                    text = req2,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
            }
        }
    }
}