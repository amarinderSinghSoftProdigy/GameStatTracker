package com.softprodigy.ballerapp.ui.features.home.events.venues.openVenue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
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
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.features.home.events.schedule.Space
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun OpenVenuesDetailsScreen() {

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

                    AppText(
                        text = stringResource(id = R.string.location),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.buttonColor.textDisabled
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AppText(
                            text = "Springvillie HS Gym A",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )

                        TransparentButtonButton(
                            text = stringResource(id = R.string.navigate),
                            onClick = {},
                            icon = painterResource(id = R.drawable.ic_nav),
                            enabled = false,
                            themed = false,
                        )

                    }
                    AppText(
                        text = "8502 Preston Rd. Inglewood, Maine",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.appColors.buttonColor.textDisabled
                    )

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
            }
            Space(dp = dimensionResource(id = R.dimen.size_14dp))

        }

    }
}
