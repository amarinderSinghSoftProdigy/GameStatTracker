package com.allballapp.android.ui.features.home.events.venues

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.allballapp.android.R
import com.allballapp.android.data.response.VenuesId
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.components.PlaceholderRect
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
@Composable
fun VenuesScreen(moveToOpenVenues: (String, String) -> Unit, eventViewModel: EventViewModel) {
    val state = eventViewModel.eventState.value

    remember {
        eventViewModel.onEvent(
            EvEvents.GetVenues
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CommonProgressBar()
        } else if (state.venuesList.isEmpty()) {
            EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                        .background(
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                            color = MaterialTheme.colors.onPrimary
                        )

                ) {
                    itemsIndexed(state.venuesList) { index, item ->
                        VenuesItem(item) {
                            moveToOpenVenues(item.venueName, item._id)
                            eventViewModel.onEvent(EvEvents.ClearTeam)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VenuesItem(item: VenuesId, moveToOpenVenues: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_56dp))
            .clickable {
                moveToOpenVenues()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

        ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
            CoilImage(
                src = com.allballapp.android.BuildConfig.IMAGE_SERVER + item.logo,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .background(
                        color = MaterialTheme.appColors.material.primary,
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp))
                    )
                    .clip(
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_10dp))
                    ),
                isCrossFadeEnabled = false,
                onLoading = { PlaceholderRect(R.drawable.ic_events_placeholder) },
                onError = { PlaceholderRect(R.drawable.ic_events_placeholder) },
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            AppText(
                text = item.venueName,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            )
        }
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        height = dimensionResource(id = R.dimen.size_12dp),
                        width = dimensionResource(id = R.dimen.size_12dp)
                    )
                    .then(
                        Modifier.rotate(270f)
                    ),
                tint = ColorGreyLighter
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }

    }
    Divider(color = MaterialTheme.appColors.material.primary)

}