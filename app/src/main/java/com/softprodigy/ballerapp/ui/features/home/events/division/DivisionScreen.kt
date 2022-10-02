package com.softprodigy.ballerapp.ui.features.home.events.division

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.DivisionResponse
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.rememberPagerState
import com.softprodigy.ballerapp.ui.features.home.events.*
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionScreen(
    moveToOpenDivisions: (String) -> Unit, eventViewModel: EventViewModel
) {
    val vmState = eventViewModel.eventState.value
    val divisionTab = listOf("All", "Male", "Female")

    remember {
        eventViewModel.onEvent(EvEvents.GetDivision)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
        ) {

            val pagerState = rememberPagerState(
                pageCount = divisionTab.size,
                initialOffScreenLimit = 1,
            )

            DivisionTopTabs(pagerState, divisionTab)
            Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
            TabsContent(moveToOpenDivisions, pagerState, eventViewModel, vmState)

        }

        if (vmState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.appColors.material.primaryVariant
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionTopTabs(pagerState: PagerState, tabData: List<String>) {
    val coroutineScope = rememberCoroutineScope()
    LazyRow(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(tabData) { index, item ->
            Row {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                AppTab(title = tabData[index],
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

@ExperimentalPagerApi
@Composable
fun TabsContent(
    moveToOpenDivisions: (String) -> Unit,
    pagerState: PagerState,
    vm: EventViewModel,
    vmState: EventState
) {

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        when (page) {
            0 -> {
                vm.onEvent(EvEvents.GetGender(""))
                DivisionData(moveToOpenDivisions, vm, vmState)
            }
            1 -> {
                vm.onEvent(EvEvents.GetGender("Male"))
                DivisionData(moveToOpenDivisions, vm, vmState)
            }
            2 -> {
                vm.onEvent(EvEvents.GetGender("Female"))
                DivisionData(moveToOpenDivisions, vm, vmState)
            }
        }

    }
}

@Composable
fun DivisionData(moveToOpenDivisions: (String) -> Unit, vm: EventViewModel, vmState: EventState) {


    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                color = MaterialTheme.colors.onPrimary
            )
    ) {
        itemsIndexed(vmState.divisions) { index, item ->
            DivisionItems(item) {
                moveToOpenDivisions(item.divisionName)
            }
        }
    }

}

@Composable
fun DivisionItems(
    item: DivisionResponse, onDivisionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .height(dimensionResource(id = R.dimen.size_56dp))
            .clickable { onDivisionClick() },
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
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_bottom_event),
                contentDescription = "",
                modifier = Modifier
                    .size(
                        height = dimensionResource(id = R.dimen.size_12dp),
                        width = dimensionResource(id = R.dimen.size_12dp)
                    )
                    .then(
                        Modifier.rotate(270f)
                    ),
                tint = MaterialTheme.appColors.buttonColor.textDisabled
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }

    }
    Divider(color = MaterialTheme.appColors.material.primary)

}