package com.softprodigy.ballerapp.ui.features.home

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.Route
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.PagerIndicator
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeScreen(
    name: String?,
    gotToProfile: () -> Unit
) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
    val context = LocalContext.current

    Box {
        Box(
            Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = CircleShape,
                color = Color(
                    android.graphics.Color.parseColor(
                        "#0177C1"

                    )
                ).copy(alpha = 0.05F),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .absoluteOffset(
                        x = dimensionResource(id = R.dimen.size_64dp),
                        y = -dimensionResource(id = R.dimen.size_45dp)
                    )
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(
                        android.graphics.Color.parseColor(
                            "#0177C1"
                        )
                    ),
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.size_30dp),
                            end = dimensionResource(id = R.dimen.size_20dp),
                            start = dimensionResource(id = R.dimen.size_20dp),
                            top = dimensionResource(id = R.dimen.size_20dp)
                        )
                        .size(dimensionResource(id = R.dimen.size_200dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ball_green),
                            contentDescription = "center ball Icon",
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier.fillMaxSize()
                        )
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_48dp))
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .clickable {
                                    Toast
                                        .makeText(context, "You", Toast.LENGTH_SHORT)
                                        .show()
                                    gotToProfile()
                                },
                        )
                    }
                }
            }
        }
        Column(
            Modifier
                .padding(top = dimensionResource(id = R.dimen.size_100dp))
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
//            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_25dp)))
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
                            .clip(CircleShape)
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