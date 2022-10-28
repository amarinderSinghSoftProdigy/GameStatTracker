package com.allballapp.android.ui.features.venue

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.ui.features.components.AppOutlineTextField
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily
import com.allballapp.android.R
@Composable
fun VenueListScreen(vm: VenueSearchVIewModel = hiltViewModel(), onVenueClick: (String) -> Unit) {
    val state = vm.state.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = R.dimen.size_10dp),
                horizontal = dimensionResource(id = R.dimen.size_16dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppOutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.searchVenue,
            onValueChange = {
                if (it.length <= 30)
                    vm.onEvent(VenueEvent.OnSearchVenueChange(it))
            },
            placeholder = {
                AppText(
                    text = stringResource(id = R.string.search_venue_by_name),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                focusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                textColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            ),
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                if (state.isLoading)
                    CircularProgressIndicator(color = ColorBWBlack)
            }
            items(state.venues) { venue ->
                VenueList(venue = venue, onVenueClick = {
                    onVenueClick.invoke(venue.venueName ?: "")
                })
            }
        }

    }
}

@Composable
fun VenueList(venue: VenueDetails, onVenueClick: () -> Unit) {
    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onVenueClick.invoke() }
            .padding(dimensionResource(id = R.dimen.size_8dp)))
        {
            Image(
                painter = painterResource(id = R.drawable.ic_ball),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_40dp))
                    .clip(CircleShape),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
            Column(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
            ) {
                AppText(
                    text = venue.venueName.ifEmpty { "" },
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = rubikFamily,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = if (!venue.location.isNullOrEmpty()) venue.location else getAddress(venue.venueAddress),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = rubikFamily,
                    color = MaterialTheme.appColors.textField.label
                )

            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
    }
}

fun getAddress(venueAddress: VenueAddress?): String {
    if (venueAddress != null) {
        var result = venueAddress.address.ifEmpty { " " }
        result += if (result.isNotEmpty()) ", " else ""
        result += if (venueAddress.city.isNotEmpty()) venueAddress.city + ", " else ""
        result += if (venueAddress.state.isNotEmpty()) venueAddress.state + ", " else ""
        result += if (venueAddress.zipCode.isNotEmpty()) venueAddress.zipCode else ""
        return result
    } else return ""
}