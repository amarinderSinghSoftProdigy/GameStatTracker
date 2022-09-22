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
import com.softprodigy.ballerapp.ui.features.components.TransparentButtonButton
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.GreenColor
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.md_theme_light_outline
import com.softprodigy.ballerapp.ui.theme.md_theme_light_primary

@Composable
fun GameDetailsTab(vm: EventViewModel,moveToGameRules:()->Unit) {
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
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.events_info),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
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
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    Text(
                        text = "Fri, May 27",
                        color = ColorBWGrayLight,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                        fontWeight = FontWeight.Bold,
                    )


                }
                TeamItem("Other Team", ColorButtonRed)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Row() {
                Text(
                    text = stringResource(id = R.string.arrival_time),
                    color = ColorBWGrayLight,
                    fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.duration),
                    color = ColorBWGrayLight,
                    fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            Row() {
                Text(
                    text = "5:45 PM",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    modifier = Modifier.weight(1f)

                )
                Text(
                    text = "6:00 PM - 7:00 PM",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = stringResource(id = R.string.location),
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Springville HS Gym A",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    modifier = Modifier.weight(1f)
                )
                TransparentButtonButton(
                    text = stringResource(id = R.string.navigate),
                    onClick = {},
                    icon = painterResource(id = R.drawable.ic_nav),
                    enabled = false,
                    themed = false,
                )
            }
            Text(
                text = "8502 Preston Rd. Inglewood, Maine",
                color = ColorBWGrayLight,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,

                )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            Box(
                modifier = Modifier
                    .background(color = md_theme_light_outline)
                    .height(dimensionResource(id = R.dimen.size_100dp))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Google Map",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            LazyRow {
                itemsIndexed(images) { index, item ->
                    Image(
                        painter = rememberAsyncImagePainter("https://picsum.photos/200"),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.size_10dp))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.size_10dp)))
                            .size(100.dp)
                            .height(100.dp)
                            .width(100.dp)

                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            Text(
                text = stringResource(id = R.string.rsvp),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            LazyRow {
                itemsIndexed(images) { index, item ->
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            text = "Zair",
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = stringResource(id = R.string.going),
                            color = GreenColor,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.Bold,
                        )

                    }

                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.jersey_color),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "N/A",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Text(
                text = stringResource(id = R.string.pre_practive_head),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Text(
                text = "Risus enim egestas placerat adipiscing accumsan velit nam varius. Vulputate habitant vitae at laoreet. Arcu, vitae mi enim, aenean. Egestas cras venenatis dis augue felis.",
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = dimensionResource(id = R.dimen.txt_size_16))
                )
                Text(
                    text = "Coach Sam",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            AppDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
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
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)).clickable {
                        moveToGameRules()
                    },
                    painter = painterResource(id = R.drawable.ic_forward),
                    contentDescription = "", tint = ColorGreyLighter
                )
            }
        }
    }
}

@Composable
fun TeamItem(title: String, color: Color) {
    Box(
        modifier = Modifier.background(color = md_theme_light_primary)
    )
    {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_20dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .height(dimensionResource(id = R.dimen.size_60dp))
                    .width(dimensionResource(id = R.dimen.size_60dp))
                    .background(color = color)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Text(
                text = title,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}