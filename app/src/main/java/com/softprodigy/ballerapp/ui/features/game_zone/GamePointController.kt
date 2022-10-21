package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.ImageTextButton
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun GamePointController (
    onSettingClick: (GameSettingsState) -> Unit,
    onPointClick: (GameSettingsState) -> Unit,
) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier
                .background(colorResource(id = R.color.game_grid_border_color))
                .width(dimensionResource(id = R.dimen.size_1dp))
                //.height(dimensionResource(id = R.dimen.size_368dp))

            )
            Column(
                Modifier.
                        fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Game setting grid view
                var settingsList = ArrayList<GameSettingsState>();
                settingsList.add(GameSettingsState(0, stringResource(id = R.string.exit), R.drawable.ic_exit_back))
                settingsList.add(GameSettingsState(1, stringResource(id = R.string.settings), R.drawable.ic_game_settings))
                settingsList.add(GameSettingsState(2, stringResource(id = R.string.box_score), R.drawable.ic_box_score))
                settingsList.add(GameSettingsState(3, stringResource(id = R.string.timeouts), R.drawable.ic_timeouts))


                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(
                    dimensionResource(id = R.dimen.size_90dp)) ) {
                    itemsIndexed(settingsList) { index, setting ->

                        Column(
                            Modifier
                                .height(dimensionResource(id = R.dimen.size_46dp))
                                .weight(1f)
                                .border(
                                    dimensionResource(id = R.dimen.size_half_dp),
                                    colorResource(id = R.color.game_grid_border_color)
                                )
                                .clickable { onSettingClick.invoke(setting) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            ImageTextButton(
                                title = setting.title!!,
                                drawableResourceId = setting.icon!!,
                                titleFontSize = R.dimen.txt_size_10,
                                spacerBetween = R.dimen.size_4dp,
                                color = colorResource(id = R.color.game_grid_item_text_color),
                                fontFamily = rubikFamily,
                                fontWeight = FontWeight.W500,
                                //onClick = { onSettingClick.invoke(setting) }
                            )
                        }
                    }
                }

                // Game point grid view
                var pointList = ArrayList<GameSettingsState>();
                pointList.add(GameSettingsState(0, stringResource(id = R.string.point_3), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(1, stringResource(id = R.string.point_3), R.drawable.ic_correct))
                pointList.add(GameSettingsState(2, stringResource(id = R.string.point_2), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(3, stringResource(id = R.string.point_2), R.drawable.ic_correct))
                pointList.add(GameSettingsState(4, stringResource(id = R.string.point_1), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(5, stringResource(id = R.string.point_1), R.drawable.ic_correct))
                Spacer(modifier = Modifier
                    .background(colorResource(id = R.color.game_grid_border_color))
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_1dp))
                )
                LazyVerticalGrid(columns = GridCells.Fixed(2), ) {
                    itemsIndexed(pointList) { index, point ->
                        Column(
                            Modifier
                                .height(dimensionResource(id = R.dimen.size_94dp))
                                .weight(1f)
                                .border(
                                    dimensionResource(id = R.dimen.size_half_dp),
                                    colorResource(id = R.color.game_grid_border_color)
                                )
                                .clickable { onPointClick(point) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,

                        ) {
                            ImageTextButton(
                                title = point.title!!,
                                drawableResourceId = point.icon!!,
                                titleFontSize = R.dimen.txt_size_12,
                                spacerBetween = R.dimen.size_12dp,
                                color = colorResource(id = R.color.game_grid_item_text_color),
                                fontWeight = FontWeight.W500,
                                //onClick = { onPointClick.invoke(point) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier
                    .background(colorResource(id = R.color.game_grid_border_color))
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_half_dp))
                )
            }
        }
    }
}