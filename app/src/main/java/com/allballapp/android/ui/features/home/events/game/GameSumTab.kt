package com.allballapp.android.ui.features.home.events.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.allballapp.android.data.response.GameSummaryResponse
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import kotlinx.coroutines.launch
import com.allballapp.android.R
@OptIn(ExperimentalPagerApi::class)
@Composable
fun GameSumTab() {

    val summary = arrayListOf<GameSummaryResponse>(
        GameSummaryResponse(
            "1",
            time = "1:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Cooper",
            desc = "scores 2 points",
            value = "4 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:56",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Sam",
            desc = "misses 3 point jumper",
            value = "2 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "0:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "George",
            desc = "defensive rebound",
            value = "2 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Cooper",
            desc = "scores 2 points",
            value = "0 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:35",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "James",
            desc = "scores 2 points",
            value = "4 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Cooper",
            desc = "scores 2 points",
            value = "2 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Cooper",
            desc = "scores 2 points",
            value = "4 : 0"
        ),
        GameSummaryResponse(
            "1",
            time = "1:59",
            playerDp = "profileImage/1662458857474-selected_image_4465069980681186921.jpg",
            playerName = "Cooper",
            desc = "scores 2 points",
            value = "4 : 0"
        ),
    )
//    stringResourceByName(SummaryTabItems.All.name)
    val categories = arrayListOf<String>(
        stringResource(id = R.string.all),
        stringResource(id = R.string.one_q),
        stringResource(id = R.string.two_q),
        stringResource(id = R.string.three_q),
        stringResource(id = R.string.four_q),
    )

    val pagerState = rememberPagerState(
        pageCount = categories.size,
        initialOffScreenLimit = 1,
    )
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
            SummaryTopTabs(pagerState, categories)
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                itemsIndexed(summary) { index, sum ->
                    SummaryTabItem(
                        index + 1,
                        summary = sum,
                        key = categories[pagerState.currentPage]
                    )

                }
            }
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp)))
        }
    }
}

enum class SummaryTabItems(val stringId: String, val key: String) {
    All("all", "all"),
    ONE_Q("one_q", "1Q"),
    TWO_Q("two_q", "2Q"),
    THREE_Q("three_q", "3Q"),
    FOUR_Q("four_q", "4Q"),
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SummaryTopTabs(pagerState: PagerState, tabData: List<String>) {
    val coroutineScope = rememberCoroutineScope()
    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        itemsIndexed(tabData) { index, item ->
            Row {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AppTab(
                    title = item,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SummaryTabItem(
    index: Int,
    summary: GameSummaryResponse,
    key: String,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_8dp),
                vertical = dimensionResource(id = R.dimen.size_2dp)
            )
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = MaterialTheme.appColors.material.surface),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        dimensionResource(id = R.dimen.size_12dp)
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = summary.time,
                    color = MaterialTheme.appColors.buttonColor.textDisabled,
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_30dp))
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                CoilImage(
                    src = com.allballapp.android.BuildConfig.IMAGE_SERVER + summary.playerDp,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.appColors.material.onSurface,
                            CircleShape
                        ),
                    onError = {
                        Placeholder(R.drawable.ic_team_placeholder)
                    },
                    onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                    isCrossFadeEnabled = false
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Row() {
                    Text(
                        text = summary.playerName,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        color =
                        ColorBWBlack
                    )
                    Text(
                        text = " " + summary.desc,
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        color =
                        MaterialTheme.appColors.buttonColor.backgroundEnabled
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }

            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Text(
                    text = summary.value,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.appColors.buttonColor.textDisabled
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
            }
        }
    }
}