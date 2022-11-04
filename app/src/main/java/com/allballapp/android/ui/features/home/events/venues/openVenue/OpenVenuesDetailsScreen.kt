package com.allballapp.android.ui.features.home.events.venues.openVenue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.flowlayout.FlowRow
import com.allballapp.android.BuildConfig
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.features.home.events.schedule.Space
import com.allballapp.android.ui.features.venue.Location
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.R
@Composable
fun OpenVenuesDetailsScreen(venueId: String, eventViewModel: EventViewModel) {
    val state = eventViewModel.eventState.value.venueDetails

    remember {
        eventViewModel.onEvent(EvEvents.RefreshVenueDetailsById(venueId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    top = dimensionResource(id = R.dimen.size_16dp)
                )
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
                AppText(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.size_16dp), top = dimensionResource(
                            id = R.dimen.size_16dp
                        )
                    ),
                    text = stringResource(id = R.string.venue_details),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
                LocationBlock(
                    Location(
                        state.venueAddress.address,
                        state.venueAddress.city,
                        state.venueAddress.state,
                        state.venueAddress.zipCode,
                    )
                )
            }
            Space(dp = dimensionResource(id = R.dimen.size_8dp))

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
                AppText(
                    modifier = Modifier.padding(
                        start = dimensionResource(id = R.dimen.size_16dp), top = dimensionResource(
                            id = R.dimen.size_16dp
                        )
                    ),
                    text = stringResource(id = R.string.venue_photos),
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_14dp))
                ) {
                    state.courtId.forEachIndexed { index, item ->
                        FlowRow {
                            item.exteriorPhotos.forEach { ImageItem(it) }
                            item.courtFloorPhotos.forEach { ImageItem(it) }
                            item.gymPhotos.forEach { ImageItem(it) }
                        }
                    }
                }
            }
            Space(dp = dimensionResource(id = R.dimen.size_14dp))
        }
    }
    if (eventViewModel.eventState.value.isLoading) {
        CommonProgressBar()
    }
}

@Composable
fun ImageItem(url: String) {
    Column {
        CoilImage(
            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + url,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(
                    color = Color.White,
                    RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                )
                .height(dimensionResource(id = R.dimen.size_200dp))
                .fillMaxWidth(),
            onError = {
                PlaceholderRect(R.drawable.ic_team_placeholder)
            },
            onLoading = { PlaceholderRect(R.drawable.ic_team_placeholder) },
            isCrossFadeEnabled = false,
            contentScale = ContentScale.Crop
        )
        Space(dp = dimensionResource(id = R.dimen.size_12dp))
    }
}