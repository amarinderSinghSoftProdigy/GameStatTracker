package com.softprodigy.ballerapp.ui.features.home.events.opportunities

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OppEventDetails(vm: EventViewModel, moveToRegistration: () -> Unit) {
    val state = vm.eventState.value
    var skills = arrayListOf<String>("Elite", "Competitive", "Developmental", "Learning")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        CoilImage(
            src = "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
            modifier = Modifier
                .background(color = Color.White)
                .height(dimensionResource(id = R.dimen.size_200dp))
                .fillMaxWidth(),
            onError = {
                Placeholder(R.drawable.ic_team_placeholder)
            },
            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
            isCrossFadeEnabled = false,
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)))
            Text(
                text = stringResource(id = R.string.events_info),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
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
                fontWeight = FontWeight.SemiBold,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        }
        LazyRow(Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))) {
            itemsIndexed(skills) { index, item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.size_6dp))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                        .background(MaterialTheme.appColors.material.primaryVariant)
                ) {
                    Text(
                        text = item,
                        color = Color.White,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_6dp))
                    )
                }
            }
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = stringResource(id = R.string.location),
                        color = ColorBWGrayLight,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Text(
                        text = "Springville HS Gym A",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
                    Text(
                        text = "8502 Preston Rd. Inglewood, Maine",
                        color = ColorBWGrayLight,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    )
                }
                TransparentButtonButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = stringResource(id = R.string.navigate),
                    onClick = {},
                    icon = painterResource(id = R.drawable.ic_nav),
                    enabled = false,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            CoilImage(
                src = R.drawable.rectangle,
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(
                        color = Color.White,
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
                    .height(dimensionResource(id = R.dimen.size_160dp))
                    .fillMaxWidth(),
                onError = {
                    Placeholder(R.drawable.ic_team_placeholder)
                },
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                isCrossFadeEnabled = false,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            Text(
                text = stringResource(id = R.string.event_desc),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "Orci mattis purus egestas amet. Venenatis non eget euismod leo et vitae ac sit tempor. Sit parturient vestibulum turpis euismod enim elit. Rhoncus sit pharetra aliquam non. Pellentesque mi eget penatibus egestas enim, tincidunt amet nec. Imperdiet mauris et scelerisque faucibus nisl.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            Text(
                text = stringResource(id = R.string.contact_With_questions),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = "George Will",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                text = "email@example.com",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                text = "(704) 555-0127",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            Text(
                text = stringResource(id = R.string.days_play),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            DaysPlay()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider()
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            /*Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))*/
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            AppButton(
                enabled = true,
                icon = null,
                themed = true,
                onClick = { moveToRegistration() },
                text = stringResource(id = R.string.register),
                isForceEnableNeeded = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
    }
}

@Composable
fun DetailsItem(key1: String, key2: String, value1: String, value2: String) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Row {
        Text(
            text = key1,
            color = ColorBWGrayLight,
            fontWeight = FontWeight.SemiBold,
            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = key2,
            fontWeight = FontWeight.SemiBold,
            color = ColorBWGrayLight,
            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
    Row {
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
    val days = arrayListOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )
    val values = arrayListOf(
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
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_12dp))
        ) {
            Text(
                text = it,
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = values[index],
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
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