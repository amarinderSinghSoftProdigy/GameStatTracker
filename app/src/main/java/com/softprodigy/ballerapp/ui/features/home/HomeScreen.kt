package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.PagerIndicator
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeScreen(name: String?) {
    Box {
        CoachFlowBackground(/*colorCode = MaterialTheme.appColors.material.primaryVariant*/)
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
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            UserFlowBackground(
                padding = 0.dp,
            ) {
                MessageComponent()
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Row {
                EventItem("pending_rsvp_label", "2")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                EventItem("opportunities_label", "4")
            }

        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    }
}

@Composable
fun RowScope.EventItem(stringId: String, value: String) {
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
                    painter = painterResource(id = R.drawable.ic_events),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    text = stringResource(id = R.string.events_label),
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