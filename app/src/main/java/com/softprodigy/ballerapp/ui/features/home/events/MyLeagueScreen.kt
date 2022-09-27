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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyLeagueScreen(
    state: EventState,
    vm: EventViewModel,
    moveToDetail: () -> Unit,
) {
    if (state.leagues.size > 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp))
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.leagues) { leag ->
                        LeagueItem(leag, state) {
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
fun LeagueItem(league: Leagues, state: EventState, OnNextClick: () -> Unit) {
    Column(
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
                    color = MaterialTheme.appColors.material.surface, shape = RoundedCornerShape(
                        topStart = dimensionResource(
                            id = R.dimen.size_8dp
                        ),
                        topEnd = dimensionResource(id = R.dimen.size_8dp)
                    )
                )
                .padding(
                    dimensionResource(id = R.dimen.size_12dp)
                ),
        ) {
            Image(
                painter = painterResource(id = R.drawable.rect_image),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.appColors.material.surface,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
                    .size(dimensionResource(id = R.dimen.size_64dp))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
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
                    fontWeight = FontWeight.W400,
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
                    .align(Alignment.Top)
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                    .background(league.color!!)
                    .padding(dimensionResource(id = R.dimen.size_6dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = league.type,
                    color = Color.White,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}