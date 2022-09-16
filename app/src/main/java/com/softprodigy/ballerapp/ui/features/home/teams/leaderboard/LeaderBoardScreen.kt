package com.softprodigy.ballerapp.ui.features.home.teams.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryOrange
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LeaderBoardScreen(vm: LeaderBoardViewModel = hiltViewModel()) {
    val state = vm.leaderUiState.value
    val onLeaderSelectionChange = { leader: Player ->
        vm.onEvent(LeaderBoardUIEvent.OnLeaderSelected(leader))
    }
    Box {
        if (state.isLoading) {
            CommonProgressBar()
        } else {
            val pagerState = rememberPagerState(
                pageCount = state.leaderBoard.size,
                initialOffScreenLimit = 1,
            )
            Column(Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                LeaderTopTabs(pagerState, state.leaderBoard)
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    itemsIndexed(state.leaders) { index, item ->
                        val srNo = index + 1
                        LeaderListItem(
                            srNo = srNo,
                            leader = item,
                            selected = state.selectedLeader == item,
                            key = state.leaderBoard[pagerState.currentPage].key
                        ) {
                            onLeaderSelectionChange.invoke(item)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp)))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LeaderTopTabs(pagerState: PagerState, tabData: List<TeamLeaderBoard>) {
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
                            pagerState.animateScrollToPage(index)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LeaderListItem(
    modifier: Modifier = Modifier,
    srNo: Int,
    leader: Player,
    selected: Boolean,
    key: String,
    onClick: (Player) -> Unit,
) {
    val jsonPlayer = JSONObject(leader.toString())
    var points: String = ""
    try {
        points = jsonPlayer[key].toString()
    } catch (e: Exception) {
        points = ""
    }
    Row(
        modifier
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
                .clickable { onClick(leader) },
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
                    text = srNo.toString(),
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
                /*AsyncImage(
                    model = leader.profileImage,
                    contentDescription = "",
                    modifier =
                    Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape)
                        .background(
                            color = ColorBWGrayStatus,
                            shape = CircleShape
                        )
                        .border(
                            width = dimensionResource(id = R.dimen.size_2dp),
                            color = if (srNo <= 3) ColorPrimaryOrange else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.FillBounds


                )*/

                CoilImage(
                    src = leader.profileImage.toString(),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape)
                        .border(
                            width = dimensionResource(id = R.dimen.size_2dp),
                            color = if (srNo <= 3) ColorPrimaryOrange else Color.Transparent,
                            shape = CircleShape
                        ),
                    onError = {
                        Placeholder(R.drawable.ic_user_profile_icon)
                    },
                    onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                    isCrossFadeEnabled = false
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = leader.name,
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
                if (srNo == 1) {
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
                        MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

            }

        }
    }
}

enum class LeaderBoardTabItems(val stringId: String, val key: String) {
    RECORD("points_label", "points"),
    WIN("rebounds_label", "rebounds"),
    GB("_3s_label", "3s"),
    HOME("fts_label", "fts"),
    AWAY("fg_percent_label", "fg"),
    PF("assistance_label", "assistance"),
}