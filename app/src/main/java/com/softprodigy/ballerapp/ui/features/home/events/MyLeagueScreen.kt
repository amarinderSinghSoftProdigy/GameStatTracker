package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.MyLeagueResponse
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.PlaceholderRect
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.CommonUtils

@Composable
fun MyLeagueScreen(
    state: EventState,
    vm: EventViewModel,
    moveToDetail: () -> Unit,
) {
    remember {
        vm.onEvent(EvEvents.GetMyLeagues)
    }
    if (state.isLoading) {
        CommonProgressBar()
    } else if (state.myLeaguesList.isNotEmpty()) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp))
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.myLeaguesList) { item ->
                        LeagueItem(item) {
                            vm.onEvent(EvEvents.GetLeagueId(item._id))
                            moveToDetail()
                        }
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
fun LeagueItem(league: MyLeagueResponse, OnNextClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                OnNextClick()
            }
            .padding(bottom = dimensionResource(id = R.dimen.size_12dp))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColors.material.surface,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                )
                .padding(
                    dimensionResource(id = R.dimen.size_12dp)
                ),
        ) {
            CoilImage(
                src = BuildConfig.IMAGE_SERVER + league.eventDetail.logo,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.appColors.material.surface,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
                    .size(dimensionResource(id = R.dimen.size_64dp))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))),
                isCrossFadeEnabled = false,
                onLoading = { PlaceholderRect(R.drawable.ic_team_placeholder) },
                onError = { PlaceholderRect(R.drawable.ic_team_placeholder) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = league.eventDetail.name,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = league.eventDetail.zip + " " + league.eventDetail.city + ", " + league.eventDetail.state,
                    color = Color.Black,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    fontWeight = FontWeight.W400,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = CommonUtils.formatDate(
                            league.eventDetail.startDate,
                            league.eventDetail.startDate
                        ),
                        color = MaterialTheme.appColors.textField.label,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(all = dimensionResource(id = R.dimen.size_12dp))
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(MaterialTheme.appColors.material.primaryVariant)
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_12dp),
                        vertical = dimensionResource(
                            id = R.dimen.size_6dp
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = league.eventDetail.eventType,
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}