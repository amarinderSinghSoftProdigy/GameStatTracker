package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.ImageTextButton

@Composable
fun OverviewScreen () {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                Modifier
                    .width(dimensionResource(id = R.dimen.size_180dp))
                    .padding(horizontal = dimensionResource(id = R.dimen.size_20dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GamePointController()
            }
            Column(
                Modifier.fillMaxWidth(),
            ) {
                TeamNavigationController()
            }
        }
    }
}