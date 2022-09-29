package com.softprodigy.ballerapp.ui.features.home.events

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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun EventDetailsScreen(vm: EventViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White),
    ) {
        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            AppText(
                text = stringResource(id = R.string.events_info),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
            Row {
                AppText(
                    text = stringResource(id = R.string.date),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.8f)
                )
                AppText(
                    text = stringResource(id = R.string.arrival_time),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.5f)
                )
                AppText(
                    text = stringResource(id = R.string.duration),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                    modifier = Modifier.weight(1.8f)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            Row {
                Text(
                    text = "Friday, May 27",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.8f)
                )
                Text(
                    text = "5:45 PM",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.5f)

                )
                Text(
                    text = "6:00 PM - 7:00 PM",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1.8f)
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(2F)) {
                    AppText(
                        text = stringResource(id = R.string.location),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.textField.label,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Text(
                        text = "Springville HS Gym A",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Text(
                        text = "8502 Preston Rd. Inglewood, Maine",
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


            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        }
        /* LazyRow(
             modifier = Modifier
                 .fillMaxWidth()
         ) {
             item {
                 Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
             }
             itemsIndexed(images) { index, item ->
                 Image(
                     painter = rememberAsyncImagePainter("https://picsum.photos/200"),
                     contentDescription = null,
                     modifier = Modifier
                         .padding(end = dimensionResource(id = R.dimen.size_10dp))
                         .clip(RoundedCornerShape(dimensionResource(R.dimen.size_10dp)))
                         .size(dimensionResource(id = R.dimen.size_70dp))
                 )
             }
         }
 
         Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))*/

        AppDivider(color = MaterialTheme.appColors.material.primary)

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.rsvp),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
        }

        LazyRow {
            itemsIndexed(images) { index, item ->
                Column(
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
                    CoilImage(
                        src = "https://picsum.photos/200",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                color = Color.White, CircleShape
                            )
                            .size(dimensionResource(id = R.dimen.size_60dp)),
                        onError = {
                            Placeholder(R.drawable.ic_team_placeholder)
                        },
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        isCrossFadeEnabled = false,
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    Text(
                        text = "John",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W500
                    )

                    Text(
                        text = stringResource(id = R.string.going),
                        color = ColorButtonGreen,
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

        AppDivider(color = MaterialTheme.appColors.material.primary)

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    text = stringResource(id = R.string.jersey_color),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "N/A",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }

        AppDivider(color = MaterialTheme.appColors.material.primary)

        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))

            Text(
                text = stringResource(id = R.string.pre_practive_head),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

            Text(
                text = "Risus enim egestas placerat adipiscing accumsan velit nam varius. Vulputate habitant vitae at laoreet. Arcu, vitae mi enim, aenean. Egestas cras venenatis dis augue felis.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5

            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_60dp))
                            .padding(end = dimensionResource(id = R.dimen.txt_size_14))
                    )
                    AppText(
                        text = "Coach Sam",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h6
                    )
                }

                Row(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.size_32dp))
                        .background(
                            color = MaterialTheme.appColors.material.primaryVariant,
                            shape = RoundedCornerShape(
                                dimensionResource(id = R.dimen.size_8dp)
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tick),
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp))
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                    AppText(
                        text = stringResource(id = R.string.read_the_note),
                        color = Color.White,
                        style = MaterialTheme.typography.button
                    )
                    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
        }
        AppDivider(color = MaterialTheme.appColors.material.primary)
        Column(
            Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            Text(
                text = stringResource(id = R.string.post_practive_head),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))

            Text(
                text = "Risus enim egestas placerat adipiscing accumsan velit nam varius. Vulputate habitant vitae at laoreet. Arcu, vitae mi enim, aenean. Egestas cras venenatis dis augue felis.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_60dp))
                        .padding(end = dimensionResource(id = R.dimen.txt_size_14))
                )
                AppText(
                    text = "Coach Sam",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

