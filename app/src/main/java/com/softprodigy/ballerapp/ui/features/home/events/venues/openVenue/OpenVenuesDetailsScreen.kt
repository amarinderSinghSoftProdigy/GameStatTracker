package com.softprodigy.ballerapp.ui.features.home.events.venues.openVenue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.features.home.events.schedule.Space
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun OpenVenuesDetailsScreen(venueId: String, eventViewModel: EventViewModel) {
    val state = eventViewModel.eventState.value.venueDetails

    remember {
        eventViewModel.onEvent(EvEvents.RefreshVenueDetailsById(venueId))
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    top = dimensionResource(id = R.dimen.size_16dp)
                )
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        )
                    )
            )
            {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_14dp))
                ) {

                    AppText(
                        text = stringResource(id = R.string.venue_details),
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    )

                    Space(dp = dimensionResource(id = R.dimen.size_14dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(2F)) {
                            AppText(
                                text = stringResource(id = R.string.location),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.appColors.textField.label,
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            Text(
                                text = state.venueName,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                style = MaterialTheme.typography.h5,
                                fontSize = dimensionResource(id = R.dimen.size_16dp).value.sp
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                            Text(
                                text = state.venueAddress.address,
                                color = MaterialTheme.appColors.textField.label,
                                style = MaterialTheme.typography.h4
                            )
                        }
                        TransparentButtonButton(
                            modifier = Modifier.weight(1F),
                            text = stringResource(id = R.string.navigate),
                            onClick = {},
                            icon = painterResource(id = R.drawable.ic_nav),
                            enabled = false,
                        )
                    }


                    Space(dp = dimensionResource(id = R.dimen.size_12dp))

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
                }

            }
            Space(dp = dimensionResource(id = R.dimen.size_8dp))

            /*           Column(
                           modifier = Modifier
                               .fillMaxWidth()
                               .background(
                                   color = Color.White,
                                   shape = RoundedCornerShape(
                                       dimensionResource(id = R.dimen.size_8dp)
                                   )
                               )
                       )
                       {
                           Column(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(all = dimensionResource(id = R.dimen.size_14dp))
                           ) {
                               AppText(
                                   text = stringResource(id = R.string.venue_photos),
                                   style = MaterialTheme.typography.h5,
                                   fontWeight = FontWeight.W500,
                                   color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                               )
                               Space(dp = dimensionResource(id = R.dimen.size_14dp))
                               CoilImage(
                                   src = "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                                   modifier = Modifier
                                       .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                       .background(
                                           color = Color.White,
                                           RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                       )
                                       .height(dimensionResource(id = R.dimen.size_200dp))
                                       .fillMaxWidth(),
                                   onError = {
                                       Placeholder(R.drawable.ic_team_placeholder)
                                   },
                                   onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                   isCrossFadeEnabled = false,
                                   contentScale = ContentScale.Crop
                               )
                               Space(dp = dimensionResource(id = R.dimen.size_14dp))
                               CoilImage(
                                   src = "https://image.shutterstock.com/image-photo/word-demo-appearing-behind-torn-260nw-1782295403.jpg",
                                   modifier = Modifier
                                       .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                       .background(
                                           color = Color.White,
                                           RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                       )
                                       .height(dimensionResource(id = R.dimen.size_200dp))
                                       .fillMaxWidth(),
                                   onError = {
                                       Placeholder(R.drawable.ic_team_placeholder)
                                   },
                                   onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                   isCrossFadeEnabled = false,
                                   contentScale = ContentScale.Crop
                               )
                           }
                       }*/
            Space(dp = dimensionResource(id = R.dimen.size_14dp))

            if (eventViewModel.eventState.value.isLoading) {
                CommonProgressBar()
            }

        }

    }
}
