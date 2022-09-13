package com.softprodigy.ballerapp.ui.features.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeScreen(name: String?, logoClick: () -> Unit, onInvitationCLick: () -> Unit) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")

    Box {
        CoachFlowBackground(colorCode = color.value.ifEmpty { "0177C1" }, teamLogo = "")
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))
            AppText(
                text = stringResource(id = R.string.hey_label).replace("name", "George"),
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                color = ColorBWBlack
            )
            AppText(
                text = stringResource(id = R.string.welcome_back),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                color = MaterialTheme.appColors.material.primaryVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            UserFlowBackground(
                padding = 0.dp,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { logoClick() }
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ball),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_48dp))
                                .clip(CircleShape),
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = stringResource(id = R.string.team_total_hoop),
                            style = MaterialTheme.typography.h3,
                            fontWeight = FontWeight.W700,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Icon(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = ColorGreyLighter
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            UserFlowBackground(
                padding = 0.dp,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onInvitationCLick.invoke()
                        }
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            ,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_briefcase),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = stringResource(id = R.string.opportunities_to_work),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f)
                        )

                    }
                    Text(
                        text = "2",
                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            /*UserFlowBackground(
                padding = 0.dp,
            ) {
                MessageComponent()
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))*/

            Row {
                EventItem("my_events", "events_label", "2")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                EventItem("my_events", "events_label", "4")
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            UserFlowBackground(
                padding = 0.dp,
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = stringResource(id = R.string.opportunities_to_play),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f)
                        )

                    }
                    Text(
                        text = "2",
                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Row {
                EventItem("my_leagues", "leagues", "2", R.drawable.ic_leagues)
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                EventItem("all_leagues", "leagues", "4", R.drawable.ic_leagues)
            }

        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    }
}

@Composable
fun RowScope.EventItem(
    headingId: String,
    stringId: String,
    value: String,
    painter: Int = R.drawable.ic_events
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
    ) {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(painter),
                    contentDescription = null,
                    tint = MaterialTheme.appColors.material.primaryVariant
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    text = stringResourceByName(headingId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }
            AppText(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                text = value,
                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                color = ColorBWBlack
            )
            AppText(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_5dp)),
                text = stringResourceByName(stringId),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.textField.label
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MessageComponent() {
    val items = ArrayList<String>()
    items.add("")
    items.add("")
    items.add("")
    items.add("")

    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffScreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_48dp))
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = "Team name",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.weight(1f)
                )

            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        PagerIndicator(size = items.size, currentPage = pagerState.currentPage)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    }
}