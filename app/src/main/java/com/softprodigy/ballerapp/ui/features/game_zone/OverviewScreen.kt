package com.softprodigy.ballerapp.ui.features.game_zone

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AddNewPlayerDialog
import com.softprodigy.ballerapp.ui.features.components.GameSettingsDialog

@Composable
fun OverviewScreen (
    onSettingClick: (GameSettingsState) -> Unit,
    onPointClick: (GameSettingsState) -> Unit,
    onAddRosterClick: () -> Unit,
) {
    var isTracking:Boolean = true
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
                    .padding(
                        start = dimensionResource(id = R.dimen.size_20dp),
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    ),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GamePointController(
                    onSettingClick = onSettingClick,
                    onPointClick = onPointClick
                )
                Spacer(modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_1dp))
                    .fillMaxWidth()
                    .background(
                        colorResource(id = R.color.game_grid_border_color)
                    ))
            }
            Column(
                Modifier
                    .weight(1f)
                    //.background(colorResource(id = R.color.game_center_background_color)),
            ) {
                TeamNavigationController(
                    isTracking = isTracking,
                    onAddRosterClick = onAddRosterClick
                )
            }
            Column(
                Modifier
                    .width(dimensionResource(id = R.dimen.size_180dp)),
                    //.padding(start = 0.dp, top = 0.dp, end = dimensionResource(id = R.dimen.size_20dp), bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameSettingsController()
            }
        }
        /*AddNewPlayerDialog(
            onDismiss = {   },
            onSaveClick = {   },
            playerName = "Satish",
            jerseyNumber = "02"
        )*/
        //GameSettingsDialog {}
    }
}
