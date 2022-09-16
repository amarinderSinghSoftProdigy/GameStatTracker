package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun NewEventScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            var selectedIndex by remember {
                mutableStateOf(0)
            }

            val list = listOf(EventTabItems.Practice, EventTabItems.Game, EventTabItems.Activity)

            TabRow(selectedTabIndex = selectedIndex,
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.size_4dp),
                        horizontal = dimensionResource(
                            id = R.dimen.size_8dp
                        )
                    )
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp)))
                    .padding(1.dp),
                indicator = { tabPositions: List<TabPosition> ->
                    Box {

                    }
                }
            ) {
                list.forEachIndexed { index, text ->
                    val selected = selectedIndex == index
                    Tab(
                        modifier = if (selected) Modifier
                            .background(
                                MaterialTheme.appColors.material.primaryVariant
                            )
                        else Modifier
                            .background(
                                MaterialTheme.appColors.material.primary
                            ),
                        selected = selected,
                        onClick = { selectedIndex = index },
                        text = { Text(text = text.name, color = Color(0xff6FAAEE)) }
                    )
                }
            }
        }
    }
}


enum class EventTabItems(val stringId: String) {
    Practice(stringId = "practice"),
    Game(stringId = "game"),
    Activity(stringId = "activity")
}