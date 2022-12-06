package com.allballapp.android.ui.features.home.events.division

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.allballapp.android.data.response.DivisionResponse
import com.allballapp.android.ui.features.components.AppTab
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.rememberPagerState
import com.allballapp.android.ui.features.home.EmptyScreen
import com.allballapp.android.ui.features.home.events.EvEvents
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch
import com.allballapp.android.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DivisionScreen(
    moveToOpenDivisions: (String, String) -> Unit, eventViewModel: EventViewModel
) {
    remember {
        eventViewModel.onEvent(EvEvents.GetDivision)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val vmState = eventViewModel.eventState.value
        if (vmState.divisions.isEmpty()) {

            EmptyScreen(singleText = true, stringResource(id = R.string.no_data_found))

        } else {
            val divisionTab = listOf("All", "Male", "Female")

            val pagerState = rememberPagerState(
                pageCount = divisionTab.size,
                initialOffScreenLimit = 1,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp))
            ) {

                DivisionTopTabs(pagerState, divisionTab)

                Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))

                DivisionData(
                    moveToOpenDivisions,
                    divisionTab[pagerState.currentPage],
                    eventViewModel,
                )

            }
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
                AppTab(
                    title = tabData[index],
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


@Composable
fun DivisionData(
    moveToOpenDivisions: (String, String) -> Unit,
    key: String,
    vm: EventViewModel,
) {

    val state = vm.eventState.value
    Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)))
    val divisionList = arrayListOf<DivisionResponse>()

    when (key) {
        "All" -> {
            state.divisions.forEach {
                divisionList.add(it)
            }
        }
        "Male" -> {
            divisionList.clear()
            state.divisions.forEach {
                if (it.gender == key) divisionList.add(it)
            }
        }
        else -> {
            divisionList.clear()
            state.divisions.forEach {
                if (it.gender == key) divisionList.add(it)
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                color = MaterialTheme.colors.onPrimary
            )
    ) {
        itemsIndexed(divisionList) { index, item ->

            DivisionItems(item) {
                vm.onEvent(EvEvents.RefreshData)
                moveToOpenDivisions(item.divisionName, item._id)
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
                        height = dimensionResource(id = R.dimen.size_14dp),
                        width = dimensionResource(id = R.dimen.size_14dp)
                    )
                    .then(
                        Modifier.rotate(270f)
                    ),
                tint = ColorGreyLighter
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }

    }
    Divider(color = MaterialTheme.appColors.material.primary)

}

