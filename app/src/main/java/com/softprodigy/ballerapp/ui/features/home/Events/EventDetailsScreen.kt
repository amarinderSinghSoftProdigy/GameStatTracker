package com.softprodigy.ballerapp.ui.features.home.Events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.invitation.InvitationEvent
import com.softprodigy.ballerapp.ui.features.home.invitation.InvitationItem
import com.softprodigy.ballerapp.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventDetailsScreen(vm: EventViewModel) {
    val state = vm.eventState.value
    var images = arrayListOf<String>("", "", "", "", "")
    Box(modifier = Modifier.fillMaxSize()) {
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
            Row() {
                Text(
                    text = stringResource(id = R.string.date),
                    color = ColorBWGrayLight,
                    fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp,
                    modifier = Modifier.weight(1f)

                )
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
                    text = "Friday, May 27",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    modifier = Modifier.weight(1f)
                )
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
                modifier = Modifier.background(color=md_theme_light_outline).height(dimensionResource(id = R.dimen.size_100dp)).fillMaxWidth()
            ){
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
                                   .clip(RoundedCornerShape(dimensionResource(R.dimen.size_10dp)))                                   .size(100.dp)
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
                    Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
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
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).padding(end=dimensionResource(id = R.dimen.txt_size_16))
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

            Text(
                text = stringResource(id = R.string.post_practive_head),
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
            Row( verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp).padding(end=dimensionResource(id = R.dimen.txt_size_16))
                )
                Text(
                    text = "Coach Sam",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

