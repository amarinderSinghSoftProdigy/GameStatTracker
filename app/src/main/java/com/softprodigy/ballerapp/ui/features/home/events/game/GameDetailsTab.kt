package com.softprodigy.ballerapp.ui.features.home.events.game

import androidx.compose.foundation.*
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.GreenColor
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.md_theme_light_outline
import com.softprodigy.ballerapp.ui.theme.md_theme_light_primary

@Composable
fun GameDetailsTab(vm: EventViewModel, moveToGameRules: () -> Unit) {
    val state = vm.eventState.value
    var images = arrayListOf<String>("", "", "", "", "")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    .fillMaxWidth()
            ) {


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = stringResource(id = R.string.events_info),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TeamItem("My Team", ColorMainPrimary)
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "6:00pm",
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                            style = MaterialTheme.typography.h3
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        Text(
                            text = "Fri, May 27",
                            color = MaterialTheme.appColors.textField.label,
                            style = MaterialTheme.typography.h6
                        )

                    }
                    TeamItem("Other Team", ColorButtonRed)
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Row {
                    AppText(
                        text = stringResource(id = R.string.arrival_time),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.textField.label,
                        modifier = Modifier.weight(1f)
                    )
                    AppText(
                        text = stringResource(id = R.string.duration),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.appColors.textField.label,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                Row {
                    AppText(
                        text = "5:45 PM",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f)

                    )
                    AppText(
                        text = "6:00 PM - 7:00 PM",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                AppText(
                    text = stringResource(id = R.string.location),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.textField.label,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Springville HS Gym A",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                    )
                    TransparentButtonButton(
                        text = stringResource(id = R.string.navigate),
                        onClick = {},
                        icon = painterResource(id = R.drawable.ic_nav),
                        enabled = false,
                    )
                }
                Text(
                    text = "8502 Preston Rd. Inglewood, Maine",
                    color = MaterialTheme.appColors.textField.label,
                    style = MaterialTheme.typography.h4
                )

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
                LazyRow {
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
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
            AppDivider(color = MaterialTheme.appColors.material.primary)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                Text(
                    text = stringResource(id = R.string.rsvp),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500
                )
                LazyRow {
                    itemsIndexed(images) { index, item ->
                        Column(
                            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
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
                                    .size(dimensionResource(id = R.dimen.size_50dp)),
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
            }
            AppDivider(color = MaterialTheme.appColors.material.primary)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
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
                        text = "Light",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h5,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            }
            AppDivider(color = MaterialTheme.appColors.material.primary)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_22dp)))

                Text(
                    text = stringResource(id = R.string.pre_game_head),
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
                    CoilImage(
                        src = "https://picsum.photos/200",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                color = Color.White, CircleShape
                            )
                            .size(dimensionResource(id = R.dimen.size_30dp)),
                        onError = {
                            Placeholder(R.drawable.ic_team_placeholder)
                        },
                        onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                        isCrossFadeEnabled = false,
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                    AppText(
                        text = "Coach Sam",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        style = MaterialTheme.typography.h6
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            }
            AppDivider(color = MaterialTheme.appColors.material.primary)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.games_rules),
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        modifier = Modifier
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                            .clickable {
                                moveToGameRules()
                            },
                        painter = painterResource(id = R.drawable.ic_forward),
                        contentDescription = "",
                        tint = ColorGreyLighter
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
            }
        }
    }
}

@Composable
fun TeamItem(title: String, color: Color) {
    Box(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_80dp))
            .width(dimensionResource(id = R.dimen.size_100dp))
            .background(
                color = md_theme_light_primary, shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_8dp)
                )
            )

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .background(color = color)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            AppText(
                text = title,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h6,

                )
        }
    }
}
