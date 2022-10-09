package com.softprodigy.ballerapp.ui.features.profile.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowColumn
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.apiToUIDateFormat2
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.profile.ProfileEvent
import com.softprodigy.ballerapp.ui.features.profile.ProfileViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun RefereeProfileScreen(vm: ProfileViewModel) {

    val state = vm.state.value
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    remember {
        vm.onEvent(ProfileEvent.GetReferee)
    }

    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(color = Color.White),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.size_16dp))
                        .background(color = Color.White)
                ) {
                    CoilImage(
                        src = BuildConfig.IMAGE_SERVER + state.user.profileImage,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                        onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                    AppText(
                        text = "${state.user.firstName} ${state.user.lastName}",
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                    Row(
                        modifier = Modifier
                    ) {
                        DetailItem(stringResource(id = R.string.email), state.user.email)
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        DetailItem(stringResource(id = R.string.number), state.user.phone)
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                    AppText(
                        text = stringResource(id = R.string.mailing_address),
                        style = MaterialTheme.typography.h4,
                        color = ColorBWGrayLight,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    AppText(
                        text = state.user.address,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(color = Color.White),
            ) {

                Column(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_10dp))) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                    Row {

                        AppText(
                            text = stringResource(id = R.string.total_games),
                            style = MaterialTheme.typography.h4,
                            color = ColorBWGrayLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1F),
                            fontWeight = FontWeight.W500
                        )

                        AppText(
                            text = stringResource(id = R.string.total_hoop_games),
                            style = MaterialTheme.typography.h4,
                            color = ColorBWGrayLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(2F),
                            fontWeight = FontWeight.W500
                        )

                        AppText(
                            text = stringResource(id = R.string.rating),
                            style = MaterialTheme.typography.h4,
                            color = ColorBWGrayLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1F),
                            fontWeight = FontWeight.W500
                        )

                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Row {
                        AppText(
                            text = state.user.userDetails.totalGames,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1F),
                            fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp
                        )

                        AppText(
                            text = state.user.userDetails.totalHoopsGames,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(2F),
                            fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp
                        )

                        AppText(
                            text = state.user.userDetails.rating,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1F),
                            fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp

                        )
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                }

            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            TeamList(state.user.teamDetails)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            ProfileItem(
                stringResource(id = R.string.team_age_preference),
                state.user.userDetails.teamAgePerference
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            ProfileItem(
                stringResource(id = R.string.prefered_partner),
                state.user.userDetails.perferredPartner, ""
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            ProfileItem(
                stringResource(id = R.string.refereeing_experience),
                state.user.userDetails.refereeningExperience
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {

                Column {
                    AppText(
                        text = stringResource(id = R.string.about_exp),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

                    AppText(
                        text = state.user.userDetails.aboutExperience,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        }
    }
}