package com.softprodigy.ballerapp.ui.features.home.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.Options
import com.softprodigy.ballerapp.ui.features.components.PagerIndicator
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.features.home.HomeViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    name: String?,
    vm: HomeViewModel,
    logoClick: () -> Unit,
    onInvitationCLick: () -> Unit,
    gotToProfile: () -> Unit
) {
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
    val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
    val homeState = vm.state.value
    val homeScreenState = homeScreenViewModel.homeScreenState.value
    val coroutineScope = rememberCoroutineScope()

    remember {
        coroutineScope.launch {
            homeScreenViewModel.getHomePageDetails()
        }
    }

    CoachFlowBackground(
        colorCode = color.value.ifEmpty { "0177C1" },
        teamLogo = BuildConfig.IMAGE_SERVER + homeState.user.profileImage
        , click = {
        when (it) {
            Options.PROFILE -> {
                gotToProfile()
            }
            Options.LOGOUT -> {
                logoClick()
            }
        }
    }) {
        Box {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    start = dimensionResource(id = R.dimen.size_16dp)
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))
            AppText(
                text = stringResource(id = R.string.hey_label).replace("name",      homeState.user.firstName
                ),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
            AppText(
                text = stringResource(id = R.string.welcome_back),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.W700,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.appColors.material.primaryVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            UserFlowBackground(
                padding = 0.dp,
                color = Color.White
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
                color = Color.White
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        /*.clickable {
                           onInvitationCLick.invoke()
                       }*/
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_briefcase),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.material.primaryVariant,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = stringResource(id = R.string.opportunities_to_work),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Text(
                        text = homeScreenState.homePageCoachModel.opportunityToWork.toString(),
                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            UserFlowBackground(
                padding = 0.dp,
                color = Color.White
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onInvitationCLick.invoke()
                        }
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_invite),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.material.primaryVariant,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_14dp))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = stringResource(id = R.string.pending_invitations),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Text(
                        text = homeScreenState.homePageCoachModel.pendingInvitations.toString(),
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
                EventItem(
                    "my_events",
                    "events_label",
                    homeScreenState.homePageCoachModel.myEvents.toString()
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                EventInviteItem("invite_members")
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            UserFlowBackground(
                padding = 0.dp,
                color = Color.White
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "",
                            tint = MaterialTheme.appColors.material.primaryVariant,
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                        Text(
                            text = stringResource(id = R.string.opportunities_to_play),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = homeScreenState.homePageCoachModel.opportunityToPlay.toString(),
                        fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            Row {
                EventItem(
                    "my_leagues",
                    "leagues",
                    homeScreenState.homePageCoachModel.myLeagues.toString(),
                    ,
                    R.drawable.ic_leagues
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                EventItem(
                    "all_leagues",
                    "leagues",
                    homeScreenState.homePageCoachModel.allLeagues.toString(),
                    R.drawable.ic_leagues
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    }
    if (homeScreenState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.appColors.material.primaryVariant
        )
    }
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
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = Color.White

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
                    tint = MaterialTheme.appColors.material.primaryVariant,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))

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

@Composable
fun RowScope.EventInviteItem(
    headingId: String,
    painter: Int = R.drawable.ic_invite
) {
    UserFlowBackground(
        padding = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .height(dimensionResource(id = R.dimen.size_160dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(painter),
                    contentDescription = null,
                    tint = MaterialTheme.appColors.material.primaryVariant,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_18dp))
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    text = stringResourceByName(headingId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                ButtonWithLeadingIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.invite),
                    onClick = { },
                    painter = painterResource(id = R.drawable.ic_add_button),
                    isTransParent = false,
                    iconSize = dimensionResource(id = R.dimen.size_10dp)
                )
            }
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