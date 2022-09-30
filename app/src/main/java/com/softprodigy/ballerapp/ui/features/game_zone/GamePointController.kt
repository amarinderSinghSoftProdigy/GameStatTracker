package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.ImageTextButton

@Composable
fun GamePointController (

) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                /*.padding(
                    horizontal = dimensionResource(id = R.dimen.size_12dp),
                    vertical = dimensionResource(id = R.dimen.size_2dp)
                )*/
        ) {
            Column(
                Modifier.
                        fillMaxSize(),
                    //.padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Game setting grid view
                var settingsList = ArrayList<GameSettingsState>();
                settingsList.add(GameSettingsState(stringResource(id = R.string.exit), R.drawable.ic_exit_back))
                settingsList.add(GameSettingsState(stringResource(id = R.string.settings), R.drawable.ic_game_settings))
                settingsList.add(GameSettingsState(stringResource(id = R.string.box_score), R.drawable.ic_box_score))
                settingsList.add(GameSettingsState(stringResource(id = R.string.timeouts), R.drawable.ic_timeouts))

                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(
                    dimensionResource(id = R.dimen.size_90dp)) ) {
                    itemsIndexed(settingsList) { index, setting ->

                        Column(
                            Modifier
                                .height(dimensionResource(id = R.dimen.size_46dp))
                                .weight(1f)
                                .border(dimensionResource(id = R.dimen.size_1dp), colorResource(id = R.color.gray_border)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            ImageTextButton(
                                title = setting.title!!,
                                drawableResourceId = setting.icon!!,
                                titleFontSize = R.dimen.txt_size_10,
                                spacerBetween = R.dimen.size_4dp,
                            ) {}
                        }

                        /*Row(Modifier.height(dimensionResource(id = R.dimen.size_45dp))) {
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
                                    title = setting.title!!,
                                    drawableResourceId = setting.icon!!,
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

                // Game point grid view
                var pointList = ArrayList<GameSettingsState>();
                pointList.add(GameSettingsState(stringResource(id = R.string.point_3), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(stringResource(id = R.string.point_3), R.drawable.ic_correct))
                pointList.add(GameSettingsState(stringResource(id = R.string.point_2), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(stringResource(id = R.string.point_2), R.drawable.ic_correct))
                pointList.add(GameSettingsState(stringResource(id = R.string.point_1), R.drawable.ic_wrong))
                pointList.add(GameSettingsState(stringResource(id = R.string.point_1), R.drawable.ic_correct))
                LazyVerticalGrid(columns = GridCells.Fixed(2), ) {
                    itemsIndexed(pointList) { index, point ->

                        Column(
                            Modifier
                                .height(dimensionResource(id = R.dimen.size_94dp))
                                .weight(1f)
                                .border(dimensionResource(id = R.dimen.size_1dp),colorResource(id = R.color.gray_border)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            ImageTextButton(
                                title = point.title!!,
                                drawableResourceId = point.icon!!,
                                titleFontSize = R.dimen.txt_size_12,
                                spacerBetween = R.dimen.size_12dp,
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
    }
}