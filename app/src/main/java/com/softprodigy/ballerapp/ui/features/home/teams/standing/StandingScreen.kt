package com.softprodigy.ballerapp.ui.features.home.teams.standing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StandingScreen(
    vm: StandingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = vm.standingUiState.value

    val standingTabData = listOf(
        StandingTabItems.RECORD,
        StandingTabItems.WIN,
        StandingTabItems.GB,
        StandingTabItems.HOME,
        StandingTabItems.AWAY,
        StandingTabItems.PF
    )
    val pagerState = rememberPagerState(
        pageCount = standingTabData.size,
        initialOffScreenLimit = 1,
    )

    val onStandingSelectionChange = { standing: Standing ->
        vm.onEvent(StandingUIEvent.OnStandingSelected(standing))
    }


    Column(Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
        StandingTopTabs(pagerState, standingTabData)
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            itemsIndexed(state.standing) { index, standing ->
                StandingListItem(
                    index + 1,
                    standing = standing,
                    selected = state.selectedStanding == standing
                ) {
                    onStandingSelectionChange.invoke(standing)
                }

            }
        }
    }
    if (state.isLoading) {
        CommonProgressBar()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StandingListItem(
    index: Int,
    roundBorderImage: Boolean = false,
    standing: Standing,
    selected: Boolean,
    onClick: (Standing) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_12dp),
                vertical = dimensionResource(id = R.dimen.size_2dp)
            )
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.White)
                .clickable { onClick(standing) },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        PaddingValues(
                            dimensionResource(id = R.dimen.size_12dp),
                            dimensionResource(id = R.dimen.size_12dp)
                        )
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                AppText(
                    text = index.toString(),
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.textDisabled
                    },
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AsyncImage(
                    model = BuildConfig.IMAGE_SERVER + standing.logo,
                    contentDescription = "",
                    modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds

                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = standing.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }

            Row(
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                if (index == 1) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hike_green),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = standing.standings,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

            }

        }
    }
}

enum class StandingTabItems(val stringId: String, val key: String) {
    RECORD("record_label", "record"),
    WIN("win_percent_label", "win"),
    GB("gb_label", "gb"),
    HOME("home_label", "home"),
    AWAY("away_label", "away"),
    PF("pf_label", "pf"),
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StandingTopTabs(pagerState: PagerState, tabData: List<StandingTabItems>) {
    val coroutineScope = rememberCoroutineScope()
    LazyRow {
        itemsIndexed(tabData) { index, item ->
            Row {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AppTab(
                    title = stringResourceByName(item.stringId),
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