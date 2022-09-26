package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
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
                var settingsList = stringArrayResource(id = R.array.game_settings);
                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(
                    dimensionResource(id = R.dimen.size_90dp)) ) {
                    itemsIndexed(settingsList) { index, item ->
                        Row(Modifier.height(dimensionResource(id = R.dimen.size_45dp))) {
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
                                    title = item,//stringResource(id = R.string.app_name),
                                    drawableResourceId = R.drawable.ic_facebook,
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
                        }
                    }
                }

                // Game point grid view
                var pointsList = stringArrayResource(id = R.array.game_points);
                LazyVerticalGrid(columns = GridCells.Fixed(2), ) {
                    itemsIndexed(pointsList) { index, item ->
                        Row(Modifier.height(dimensionResource(id = R.dimen.size_80dp))) {
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
                                    title = item,//stringResource(id = R.string.app_name),
                                    drawableResourceId = R.drawable.ic_facebook,
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
                        }
                    }
                }
            }
        }
    }
}