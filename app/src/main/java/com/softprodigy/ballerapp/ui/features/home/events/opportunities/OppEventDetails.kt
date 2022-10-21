package com.softprodigy.ballerapp.ui.features.home.events.opportunities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.BasePay
import com.softprodigy.ballerapp.ui.features.home.events.DaysOfPlay
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.features.venue.Location
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.CommonUtils

@Composable
fun OppEventDetails(vm: EventViewModel, moveToRegistration: () -> Unit) {
    val context = LocalContext.current
    val state = vm.eventState.value
    remember {
        vm.onEvent(EvEvents.GetOpportunityDetail)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        CoilImage(
            src = BuildConfig.IMAGE_SERVER + state.opportunitiesDetail.logo,
            modifier = Modifier
                .background(color = ColorBWGrayLight)
                .height(dimensionResource(id = R.dimen.size_200dp))
                .fillMaxWidth(),
            onError = {
                PlaceholderRect(R.drawable.ic_team_placeholder)
            },
            onLoading = { PlaceholderRect(R.drawable.ic_team_placeholder) },
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
                text = state.opportunitiesDetail.eventGeneralInfo,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            DetailsItem(
                stringResource(id = (R.string.start_date)),
                stringResource(id = (R.string.end_Date)),
                CommonUtils.formatDateSingle(state.opportunitiesDetail.startDate),
                CommonUtils.formatDateSingle(state.opportunitiesDetail.endDate)
            )
            DetailsItem(
                stringResource(id = (R.string.event_type)),
                stringResource(id = (R.string.participation)),
                state.opportunitiesDetail.eventType,
                CommonUtils.getParticipation(
                    state.opportunitiesDetail.participation,
                    context = context
                )
            )
            DetailsItem(
                stringResource(id = (R.string.regsitration_deadliine)),
                stringResource(id = (R.string.price)),
                CommonUtils.formatDateSingle(state.opportunitiesDetail.registrationDeadline),
                "$ " + state.opportunitiesDetail.standardPrice
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
            itemsIndexed(state.opportunitiesDetail.skillLevel) { index, item ->
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
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            LocationBlock(
                Location(
                    state.opportunitiesDetail.address,
                    state.opportunitiesDetail.city,
                    state.opportunitiesDetail.state,
                    "",
                    //latLong = LatLng(state.opportunitiesDetail.)
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            /* CoilImage(
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
                     PlaceholderRect(R.drawable.ic_team_placeholder)
                 },
                 onLoading = { PlaceholderRect(R.drawable.ic_team_placeholder) },
                 isCrossFadeEnabled = false,
                 contentScale = ContentScale.Crop
             )
             Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))*/
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
                text = state.opportunitiesDetail.eventShortDescription,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider()
        if (UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
                Text(
                    text = stringResource(id = R.string.staff_details),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = state.opportunitiesDetail.staffDetails,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            }
            AppDivider()
        }


        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            if (UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
                Text(
                    text = stringResource(id = R.string.question_contact),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            } else {
                Text(
                    text = stringResource(id = R.string.contact_With_questions),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = state.opportunitiesDetail.userId.name.capitalize(),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                text = state.opportunitiesDetail.userId.email,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Text(
                text = state.opportunitiesDetail.userId.phone,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }

        AppDivider()

        if (UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
                Text(
                    text = stringResource(id = R.string.pay),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = stringResource(id = R.string.referees),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                RefereesLevels(state.opportunitiesDetail.basePay)
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                Row(
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_12dp))
                ) {
                    Text(
                        text =  stringResource(id = R.string.stat_managers),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "-",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        modifier = Modifier.weight(0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Row(
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_12dp))
                ) {
                    Text(
                        text =  stringResource(id = R.string.clock_managers),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = "-",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        modifier = Modifier.weight(0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            }

            AppDivider()
        }

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
            DaysPlay(state.opportunitiesDetail.potentialDaysOfPlay)
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
            if (state.opportunitiesDetail.registration)
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
fun DaysPlay(potentialDaysOfPlay: List<DaysOfPlay>) {
    potentialDaysOfPlay.forEachIndexed { index, item ->
        Row(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_12dp))
        ) {
            Text(
                text = item.day.capitalize(),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = item.earliestStartTime + " - " + item.latestStartTime,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                modifier = Modifier.weight(0.6f)
            )
        }
    }
}

@Composable
fun RefereesLevels(basePay: ArrayList<BasePay>) {
    basePay.forEachIndexed { index, item ->

        Row(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.size_12dp), horizontal = dimensionResource(
                id = R.dimen.size_12dp
            ))
        ) {
            Text(
                text = item.level.capitalize(),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(0.4f)
            )
            Text(
                text = "$" + item.cost + "/hr",
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