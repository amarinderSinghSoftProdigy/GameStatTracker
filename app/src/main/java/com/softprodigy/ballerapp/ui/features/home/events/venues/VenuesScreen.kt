package com.softprodigy.ballerapp.ui.features.home.events.venues

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.division.VenuesData
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun VenuesScreen(vm: VenuesViewModel = hiltViewModel()) {

    val state = vm.venuesUiState.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(state.venuesData) { index, item ->
                    VenuesItem(item)
                }
            }
        }
    }
}

@Composable
fun VenuesItem(item: VenuesData) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(
                    id = R.dimen.size_16dp
                )
            )
            .height(dimensionResource(id = R.dimen.size_56dp))
            .background(shape = RectangleShape, color = MaterialTheme.colors.onPrimary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
            CoilImage(
                src = item.image,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .background(
                        color = MaterialTheme.colors.error,
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                    )
                    .clip(
                        RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                    ),
                isCrossFadeEnabled = false,
                onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                onError = { Placeholder(R.drawable.ic_user_profile_icon) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            AppText(
                text = item.title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            )
        }
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.size_12dp)
                    ),
                colorFilter = ColorFilter.tint(color = MaterialTheme.appColors.buttonColor.bckgroundDisabled)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }

    }
    Divider(color = MaterialTheme.appColors.material.primary)

}