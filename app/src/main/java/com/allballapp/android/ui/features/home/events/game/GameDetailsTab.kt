package com.allballapp.android.ui.features.home.events.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.common.apiToUIDateFormat
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.Placeholder
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.ColorButtonRed
import com.allballapp.android.ui.theme.ColorMainPrimary
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.md_theme_light_primary

@Composable
fun GameDetailsTab(gameId: String, vm: EventViewModel, moveToGameRules: () -> Unit) {
    val state = vm.eventState.value
    val images = arrayListOf("", "", "", "", "", "", "", "", "")

    remember {
        vm.onEvent(EvEvents.RefreshGameDetailsScreen(gameId))
    }

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
                    TeamItem(
                        title = if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[0].name else "",
                        ColorMainPrimary,
                        if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[0].logo else ""
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.gameDetails.timeslot,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                            style = MaterialTheme.typography.h3
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        Text(

                            text = apiToUIDateFormat(state.gameDetails.date),
                            color = MaterialTheme.appColors.textField.label,
                            style = MaterialTheme.typography.h6
                        )

                    }
                    TeamItem(
                        title = if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[1].name else "",
                        ColorButtonRed,
                        if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[1].logo else ""

                    )

                }
                /*              Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
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

                              LocationBlock(
                                  Location(
                                      state.event.landmarkLocation,
                                      state.event.address.street,
                                      state.event.address.city,
                                      state.event.address.zip,
                                      latLong = if (state.event.address.lat == 0.0 && state.event.address.long == 0.0) {
                                          LatLng(0.0, 0.0)
                                      } else {
                                          LatLng(
                                              state.event.address.lat,
                                              state.event.address.long
                                          )
                                      }
                                  ),
                                  padding = 0.dp
                              )
                              Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                              CoilImage(
                                  src = R.drawable.ic_ball,
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
                          *//*LazyRow {
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))*//*
            AppDivider(color = MaterialTheme.appColors.material.primary)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            Text(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp)
                    ),
                text = stringResource(id = R.string.rsvp),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500
            )
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
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
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
        }*/
            }
        }
    }
}

@Composable
fun TeamItem(title: String, color: Color, teamImage: String) {
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
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + teamImage,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(dimensionResource(id = R.dimen.size_32dp)),
                onError = {
                    Placeholder(R.drawable.ic_team_placeholder)
                },
                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                isCrossFadeEnabled = false,
                contentScale = ContentScale.Crop
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
