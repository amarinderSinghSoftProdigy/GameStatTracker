package com.softprodigy.ballerapp.ui.features.home.Events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
fun MyLeagueScreen(state: EventState, vm: EventViewModel) {
    if (state.leagues.size > 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.leagues) { leag ->
                        LeagueItem(leag,state)
                    }
                }
            }
        }

    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))
                Text(
                    color = MaterialTheme.appColors.textField.label,
                    text = stringResource(id = R.string.no_league),
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }
}

@Composable
fun LeagueItem(league: Leagues, state: EventState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface, shape = RoundedCornerShape(
                        topStart = dimensionResource(
                            id = R.dimen.size_8dp
                        ),
                        topEnd = dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(
                    dimensionResource(id = R.dimen.size_16dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ball),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_50dp))
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = league.title,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        Text(
                                text = league.desc,
                                color = Color.Black,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.W500,
                            )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        Text(
                                text = league.date,
                                color = MaterialTheme.appColors.textField.label,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.W500,
                            )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                            .background(league.color!!)
                            .padding(dimensionResource(id = R.dimen.size_6dp))
                    ) {
                        Text(
                            text =league.type,
                            color = Color.White,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            }
        }
    }
}