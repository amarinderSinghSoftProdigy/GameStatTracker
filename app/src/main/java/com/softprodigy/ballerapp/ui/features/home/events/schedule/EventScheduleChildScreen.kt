package com.softprodigy.ballerapp.ui.features.home.events.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun EventScheduleChildScreen(
    vm: EventScheduleViewModel = hiltViewModel(),
    moveToOpenDetails: (String) -> Unit,
) {
    val state = vm.eventScheduleState.value
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.appColors.material.primary)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        LazyColumn(Modifier.fillMaxWidth()) {
            items(state.leagueSchedules) { item: LeagueScheduleModel ->
                item.matches[0].time = "Friday, Sep 1, 5:00 PM"
                EventScheduleSubItem(item.matches[0], 0) {
                    moveToOpenDetails(item.matches[0].divisions)
                }
            }
        }
    }

}