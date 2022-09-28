package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageTextButton
import com.softprodigy.ballerapp.ui.theme.Shapes

@Composable
fun GameSettingsController (

) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            periodSelection()
            gameSettings()
        }
    }
}

@Composable
inline fun periodSelection() {
    var periods = stringArrayResource(id = R.array.game_periods)
    Column(Modifier.height(dimensionResource(id = R.dimen.size_45dp)),
    verticalArrangement = Arrangement.Center) {
        LazyRow {
            itemsIndexed(periods) { index, period ->
                periodListItem(index, period)
            }
        }
    }
}

@Composable
fun periodListItem(index: Int, periodItem: String = "", selectedIndex: Int = 1) {

    Box(
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_24dp))
            .height(dimensionResource(id = R.dimen.size_24dp))
            .clip(CircleShape)
            .background(color = if (index == selectedIndex) Color.Blue else Color.Black)
            .padding(dimensionResource(id = R.dimen.size_4dp)),
    ) {
        AppText(
            text = periodItem,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Composable
inline fun gameSettings() {

        Column(
            Modifier.fillMaxSize(),
            //.padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Game setting grid view
            var pointList = ArrayList<GameSettingsState>();
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.asst),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.blk),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.off_reb),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.def_reb),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.stl),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.to),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.foul),
                    R.drawable.ic_add_button
                )
            )
            pointList.add(
                GameSettingsState(
                    stringResource(id = R.string.sub),
                    R.drawable.ic_add_button
                )
            )
            LazyVerticalGrid(columns = GridCells.Fixed(2),) {
                itemsIndexed(pointList) { index, point ->

                    Column(
                        Modifier
                            .height(dimensionResource(id = R.dimen.size_80dp))
                            .weight(1f)
                            .border(
                                dimensionResource(id = R.dimen.size_1dp),
                                colorResource(id = R.color.gray_border)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        ImageTextButton(
                            title = point.title!!,
                            drawableResourceId = point.icon!!,
                            spacerBetween = R.dimen.size_5dp,
                        ) {}
                    }
                    /*Row(Modifier.height(dimensionResource(id = R.dimen.size_80dp))) {
                    Column() {
                        Divider(
                            color = colorResource(id = R.color.gray_border),
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(dimensionResource(id = R.dimen.size_1dp))
                        )
                    }
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ImageTextButton(
                            title = point.title!!,
                            drawableResourceId = point.icon!!,
                            spacerBetween = R.dimen.size_5dp,
                        ) {}
                        Divider(color = colorResource(id = R.color.gray_border), thickness = dimensionResource(id = R.dimen.size_1dp))
                    }
                    if ((index+1) % 2 == 0 ) {
                        Column() {
                            Divider(
                                color = colorResource(id = R.color.gray_border),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(dimensionResource(id = R.dimen.size_1dp))
                            )
                        }
                    }
                }*/
                }
            }
        }

}