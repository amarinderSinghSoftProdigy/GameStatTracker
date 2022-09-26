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
fun OverviewScreen (

) {
    Box(
        modifier =  Modifier.fillMaxSize().background(Color.Black)
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

                GamePointController();

                /*ImageTextButton(
                    title = stringResource(id = R.string.app_name),
                    drawableResourceId = R.drawable.ic_facebook,
                    spacerBetween = R.dimen.size_12dp,
                ) {
                }*/

                /*AppText(
                    text = stringResource(id = R.string.no_players_in_team),
                    color = ColorBWBlack,
                    style = MaterialTheme.typography.h3
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                AppText(
                    text = stringResource(id = R.string.please_add_players),
                    color = ColorBWBlack,
                    style = MaterialTheme.typography.h5
                )*/
            }
        }


            /*AppText(
                text = stringResource(id = com.softprodigy.ballerapp.R.string.app_name),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.size_3dp)),
                textAlign = TextAlign.Start
            )*/


            /*Column(modifier = Modifier.align(Alignment.Center)) {

            }*/

    }
}