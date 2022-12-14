package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.theme.Buttons_background_color
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun Tracking (
    isTrackingEmpty : Boolean,
    onAddRosterClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
           .background(color = colorResource(id = R.color.game_setting_edit_bg_disable_color))

    ) {

        Column(
            Modifier
                .fillMaxHeight()
//                .background(colorResource(id = R.color.buttons_background_color))
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_70dp)))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_46dp))
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    //.border(dimensionResource(id = R.dimen.size_1dp), Color.Red)
                    .clip(RectangleShape)
                    .background(
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                        color = Color.Black.copy(alpha = if (isTrackingEmpty) 1.0f else 0.6f)
                    )
                    .clickable { onAddRosterClick.invoke() },

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                AppText(
                    text = stringResource(id = R.string.review_roster_and_add_starters),
                    color = colorResource(
                        id = R.color.game_add_roster_button_text_color).copy(alpha = if(isTrackingEmpty) 1.0f else 0.6f
                    ),
                    fontFamily = rubikFamily,
                    fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_69dp)))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_100dp))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp),
                    ),
                //.background(Color.Black.copy(if(isTrackingEmpty) 0.6f else 1.0f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppText(
                    text = stringResource(id = R.string.first_possession),
                    color = colorResource(id = R.color.game_add_roster_button_text_color).copy(if(isTrackingEmpty) 0.6f else 1.0f),
                    fontFamily = rubikFamily,
                    fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp,
                    fontWeight = FontWeight.W500,
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

                Row(
                    modifier = Modifier
                        .padding( vertical =  dimensionResource(id = R.dimen.size_10dp))
                        .fillMaxWidth()
                        .clip(RectangleShape)
                        .background(
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                            color = Color.Black.copy(alpha = if (isTrackingEmpty) 0.6f else 1.0f)
                        ),
                    verticalAlignment = CenterVertically,
                ) {
                    Row(
                        Modifier
                            .fillMaxHeight()
                            .align(CenterVertically)
                            .weight(1f),
                        verticalAlignment = CenterVertically,
                    ) {

                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                        teamLogo(alpha = if(isTrackingEmpty) 0.6f else 1.0f)
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        teamTitle(title = stringResource(id = R.string.my_team), alpha = if(isTrackingEmpty) 0.6f else 1.0f)
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .align(CenterVertically)
                                //.clip(RectangleShape)
                               ,
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            ImageButton(
                                icon = painterResource(id = R.drawable.ic_arrow_prev_selected),
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_56dp))
                                    .height(dimensionResource(id = R.dimen.size_56dp))
                                    .background(
                                        shape = RoundedCornerShape(
                                            topStart = dimensionResource(id = R.dimen.size_46dp) ,
                                            bottomStart = dimensionResource(id = R.dimen.size_46dp)),
                                        color = colorResource(id = R.color.game_setting_edit_bg_disable_color))
                                ,

                                onClick = { /*TODO*/ }
                            )
                        }
                    }

//                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_2dp)))

                    Row(
                        Modifier
                            .fillMaxHeight()
                            .align(CenterVertically)
                            .weight(1f),
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Row(
                            Modifier
                                .align(CenterVertically),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            ImageButton(
                                icon = painterResource(id = R.drawable.ic_next_arrow_selected),
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_56dp))
                                    .height(dimensionResource(id = R.dimen.size_56dp))
                                    .background(
                                        shape = RoundedCornerShape(
                                            topEnd = dimensionResource(id = R.dimen.size_46dp),
                                            bottomEnd = dimensionResource(id = R.dimen.size_46dp)
                                        ),
                                        color = colorResource(id = R.color.game_setting_edit_bg_disable_color)
                                    ),

                                onClick = { /*TODO*/ }
                            )
                        }
                        Row(
                            Modifier
                                .align(CenterVertically)
                                .weight(1f),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            teamTitle(title = stringResource(id = R.string.other_team), alpha = if(isTrackingEmpty) 0.6f else 1.0f)
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            teamLogo(logoColor = Color.Blue, alpha = if(isTrackingEmpty) 0.6f else 1.0f)
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                        }
                    }
                }
            }
        }
    }
}


