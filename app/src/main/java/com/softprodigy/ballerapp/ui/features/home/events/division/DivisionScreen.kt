package com.softprodigy.ballerapp.ui.features.home.events.division

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.division.DivisionData
import com.softprodigy.ballerapp.data.response.division.DivisionTabs
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionScreen(vm: DivisionViewModel = hiltViewModel()) {

    val state = vm.divisionUiState.value
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(
                pageCount = state.divisionTab.size,
                initialOffScreenLimit = 1,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            DivisionTopTabs(pagerState, state.divisionTab)

            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(state.divisionData) { index, item ->
                    DivisionItems(item)
                }
            }
        }
    }
}

@Composable
fun DivisionItems(item: DivisionData) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(
                    id = R.dimen.size_16dp
                )
            )
            .background(shape = RectangleShape, color = MaterialTheme.colors.onPrimary)
            .height(dimensionResource(id = R.dimen.size_56dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        AppText(
            text = item.divisionName,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.W500,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
        )

        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null,
                modifier = Modifier
                    .size(
                        dimensionResource(id = R.dimen.size_12dp)
                    ),
                colorFilter = ColorFilter.tint(color = MaterialTheme.appColors.buttonColor.bckgroundDisabled)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }

    }
    Divider(color = MaterialTheme.appColors.material.primary)

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionTopTabs(pagerState: PagerState, tabData: List<DivisionTabs>) {
    val coroutineScope = rememberCoroutineScope()
    LazyRow(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(tabData) { index, item ->
            Row {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AppTab(
                    title = item.key,
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