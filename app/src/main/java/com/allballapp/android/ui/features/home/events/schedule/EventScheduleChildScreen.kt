package com.allballapp.android.ui.features.home.events.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.allballapp.android.R
import com.allballapp.android.ui.features.home.events.EventDetail
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.appColors

@Composable
fun EventScheduleChildScreen(
    vm: EventViewModel = hiltViewModel(),
    moveToOpenDetails: (String) -> Unit,
) {
    val state = vm.eventState.value
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.appColors.material.primary)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        LazyColumn(Modifier.fillMaxWidth()) {
            items(state.scheduleResponse) { item ->
                if (item.matches.isNotEmpty()) {
                    EventScheduleSubItem(item.matches[0], EventDetail(), 0) {
                        // moveToOpenDetails(item.matches[0].divisions)
                    }
                }
            }
        }
    }
}