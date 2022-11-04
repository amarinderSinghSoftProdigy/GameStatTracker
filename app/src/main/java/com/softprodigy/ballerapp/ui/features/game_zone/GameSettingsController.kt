package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageTextButton
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun GameSettingsController (
    isEditMode: Boolean,
    onPointsCategoryClick : (GameSettingsState) -> Unit
) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier
                .background(colorResource(id = R.color.game_grid_border_color))
                .width(dimensionResource(id = R.dimen.size_half_dp))
                .fillMaxHeight()
            )
            Column(modifier = Modifier.fillMaxHeight()) {
                PeriodSelection(isEditMode)
                Spacer(
                    modifier = Modifier
                        .background(colorResource(id = R.color.game_grid_border_color))
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_half_dp))
                )
                GameSettings(isEditMode , onPointsCategoryClick = onPointsCategoryClick)
            }
        }
    }
}

@Composable
inline fun PeriodSelection(isEditMode: Boolean) {
    var selectedPeriodIndex = remember { mutableStateOf(-1) }
    var periods = stringArrayResource(id = R.array.game_periods)
    Column(
        Modifier
            .height(dimensionResource(id = R.dimen.size_46dp))
            .background(if (isEditMode) colorResource(id = R.color.game_setting_edit_bg_enable_color) else  /*Color.Black.copy(alpha = .75f))*/colorResource(id = R.color.game_bg_color))
            .padding(horizontal = dimensionResource(id = R.dimen.size_8dp)),
        verticalArrangement = Arrangement.Center)
    {
        LazyRow {
            itemsIndexed(periods) { index, period ->
                PeriodListItem(index, period, selectedPeriodIndex)
            }
        }
    }
}

@Composable
fun PeriodListItem(index: Int, periodItem: String = "", selectedPeriodIndex: MutableState<Int>) {

    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.size_4dp))
            .background(Color.Transparent),
            contentAlignment = Alignment.Center)
        {

        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_24dp))
            .height(dimensionResource(id = R.dimen.size_24dp))
            .clip(CircleShape)
            .background(
                color = if (index == selectedPeriodIndex.value) colorResource(id = R.color.game_period_selection_background_color)
                else colorResource(id = R.color.game_period_background_color)
            )
            .clickable { selectedPeriodIndex.value = index },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = periodItem,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.game_grid_item_text_color),
                fontSize = dimensionResource(id = R.dimen.txt_size_11).value.sp,
                fontFamily = rubikFamily,
                fontWeight = if (index == selectedPeriodIndex.value) FontWeight.W700 else FontWeight.W400,
            )
        }
    }
}

@Composable
inline fun GameSettings(isEditMode: Boolean , crossinline onPointsCategoryClick : (GameSettingsState) -> Unit) {
    Row(modifier = Modifier
        .background(
            if (isEditMode) colorResource(id = R.color.game_setting_edit_bg_enable_color) else colorResource(
                id = R.color.game_period_background_color
            )
        )
        .fillMaxWidth()
        //.fillMaxHeight()
    ){
            // Game setting grid view
            val pointList = ArrayList<GameSettingsState>();
            pointList.add(
                GameSettingsState(
                    0,
                    stringResource(id = R.string.asst),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    1,
                    stringResource(id = R.string.blk),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    2,
                    stringResource(id = R.string.off_reb),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    3,
                    stringResource(id = R.string.def_reb),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    4,
                    stringResource(id = R.string.stl),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    5,
                    stringResource(id = R.string.to),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    6,
                    stringResource(id = R.string.foul),
                    R.drawable.ic_plus
                )
            )
            pointList.add(
                GameSettingsState(
                    7,
                    stringResource(id = R.string.sub),
                    R.drawable.ic_plus
                )
            )
        Spacer(
            modifier = Modifier
                .background(colorResource(id = R.color.game_grid_border_color))
                .fillMaxHeight()
                .width(dimensionResource(id = R.dimen.size_half_dp))
        )
        var expanded by remember { mutableStateOf(false) }
        val fouls = stringArrayResource(id = R.array.game_foul)
        val foul = stringResource(id = R.string.foul)
        val configuration = LocalConfiguration.current
        var screenHeight = configuration.screenHeightDp.dp
        screenHeight -= dimensionResource(id = R.dimen.size_46dp)
        screenHeight += dimensionResource(id = R.dimen.size_22dp)
        val rowHeight = screenHeight / 4

        LazyVerticalGrid(columns = GridCells.Fixed(2),) {
            itemsIndexed(pointList) { index, point ->
                Row(
                    Modifier
                        //.height(dimensionResource(id = R.dimen.size_82dp))
                        .height(rowHeight)
                        .width(dimensionResource(id = R.dimen.size_90dp))
                        .border(
                            dimensionResource(id = R.dimen.size_half_dp),
                            colorResource(id = R.color.game_grid_border_color)
                        )
                        .background(
                            color = if (expanded && point.title.equals(foul)) colorResource(id = R.color.game_setting_item_selected_bg_color) else Color.Transparent
                        )
                        .clickable {
                            if(point.id == 6)
                              expanded = point.title.equals(foul)
                            else { onPointsCategoryClick.invoke(point) }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageTextButton(
                        title = point.title!!,
                        drawableResourceId = point.icon!!,
                        titleFontSize = R.dimen.txt_size_12,
                        spacerBetween = R.dimen.size_12dp,
                        color = colorResource(id = R.color.game_grid_item_text_color),
                        fontFamily = rubikFamily,
                        fontWeight = FontWeight.W500,
                    )

                    // drop down menu
                    if (point.title.equals(foul)) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(colorResource(id = R.color.game_bg_color))
                                //.clip(RoundedCornerShape(size = dimensionResource(id = R.dimen.size_8dp))),
                        ) {
                            // adding items
                            fouls.forEachIndexed { itemIndex, itemValue ->
                                DropdownMenuItem(
                                    onClick = { expanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(dimensionResource(id = R.dimen.size_40dp)),
                                ) {
                                    Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(39.dp),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            AppText(
                                                text = itemValue,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center,
                                                color = colorResource(id = R.color.game_point_list_item_text_color),
                                                fontWeight = FontWeight.W400,
                                                fontFamily = rubikFamily,
                                                fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp,
                                            )
                                        }


                                        Spacer(
                                            modifier = Modifier
                                                .background(colorResource(id = R.color.game_grid_border_color))
                                                .fillMaxWidth()
                                                //.fillMaxHeight()
                                                .height(dimensionResource(id = R.dimen.size_1dp))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
        Spacer(
            modifier = Modifier
                .background(colorResource(id = R.color.game_grid_border_color))
                .fillMaxHeight()
                .width(dimensionResource(id = R.dimen.size_half_dp))
        )
    }
}