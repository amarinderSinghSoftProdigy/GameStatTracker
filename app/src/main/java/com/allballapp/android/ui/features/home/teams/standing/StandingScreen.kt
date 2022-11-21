package com.allballapp.android.ui.features.home.teams.standing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.allballapp.android.R
import com.allballapp.android.data.response.Categories
import com.allballapp.android.data.response.Standing
import com.allballapp.android.ui.features.components.AppTab
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.CoilImage
import com.allballapp.android.ui.features.components.CommonProgressBar
import com.allballapp.android.ui.features.components.Placeholder
import com.allballapp.android.ui.features.components.rememberPagerState
import com.allballapp.android.ui.theme.appColors
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StandingScreen(
    vm: StandingViewModel = hiltViewModel()
) {
    val state = vm.standingUiState.value

    val onStandingSelectionChange = { standing: Standing ->
        vm.onEvent(StandingUIEvent.OnStandingSelected(standing))
    }

    Box(modifier = Modifier.background(color = MaterialTheme.appColors.material.primary)) {

        if (state.isLoading) {
            CommonProgressBar()
        } else {
            val pagerState = rememberPagerState(
                pageCount = state.categories.size,
                initialOffScreenLimit = 1,
            )
            Column(Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                StandingTopTabs(pagerState, state.categories)
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    itemsIndexed(state.standing) { index, standing ->
                        StandingListItem(
                            index + 1,
                            standing = standing,
                            selected = state.selectedStanding == standing,
                            key = state.categories[pagerState.currentPage].key
                        ) {
                            onStandingSelectionChange.invoke(standing)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp)))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StandingListItem(
    index: Int,
    roundBorderImage: Boolean = false,
    standing: Standing,
    selected: Boolean,
    key: String,
    onClick: (Standing) -> Unit
) {
    var points: String = ""
    if (key == "winPer") {
        points = standing.WinPer
    } else {
        val jsonPlayer = JSONObject(standing.toString())
        try {
            points = jsonPlayer[key].toString()
        } catch (e: Exception) {
            points = ""
        }
    }

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
                .background(color = if (selected) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.material.surface)
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
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp))
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                CoilImage(
                    src = com.allballapp.android.BuildConfig.IMAGE_SERVER + standing.logo,
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
                Text(
                    text = standing.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.backgroundEnabled
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
                    text = points,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.backgroundEnabled
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
fun StandingTopTabs(pagerState: PagerState, tabData: List<Categories>) {
    val coroutineScope = rememberCoroutineScope()
    LazyRow {
        itemsIndexed(tabData) { index, item ->
            Row {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AppTab(
                    title = item.name,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.pageCount != 0) pagerState.animateScrollToPage(index)
                        }
                    })
            }
        }
    }
}