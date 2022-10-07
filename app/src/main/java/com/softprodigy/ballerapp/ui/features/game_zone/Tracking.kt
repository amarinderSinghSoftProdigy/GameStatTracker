package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun Tracking (
    isTrackingEmpty : Boolean,
    onAddRosterClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(color = colorResource(id = R.color.game_center_background_color))
            .background(Color.White.copy(alpha = 0.6f))
    ) {

        Column(
            Modifier
                .fillMaxHeight()
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_110dp)))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_46dp))
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_16dp),
                    )
                    //.border(dimensionResource(id = R.dimen.size_1dp), Color.Red)
                    .clip(RectangleShape)
                    .background(
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                        color = Color.Black.copy(alpha = if(isTrackingEmpty) 1.0f else 0.6f)),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                AppText(
                    text = stringResource(id = R.string.review_roster_and_add_starters),
                    color = colorResource(id = R.color.game_add_roster_button_text_color).copy(alpha = if(isTrackingEmpty) 1.0f else 0.6f),
                    fontFamily = rubikFamily,
                    fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_69dp)))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_86dp))
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
                        .fillMaxWidth()
                        .clip(RectangleShape)
                        .background(
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                            color = Color.Black.copy(alpha = if(isTrackingEmpty) 0.6f else 1.0f)),
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
                        teamLogo()
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        teamTitle(title = stringResource(id = R.string.my_team))
                    }

                    Row(
                        Modifier

                            .align(CenterVertically)
                            .clip(RectangleShape)
                            .background(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_45dp)),color= Color.Black.copy(alpha = if(isTrackingEmpty) 0.4f else 1.0f))
                           /*.background(
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                                color = Color.Black.copy(alpha = if(isTrackingEmpty) 0.6f else 1.0f))*/,
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        ImageButton(
                            icon = painterResource(id = R.drawable.ic_possesion_next_arrow_selected),
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_46dp))
                                .height(dimensionResource(id = R.dimen.size_46dp)),

                            onClick = { /*TODO*/ }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        ImageButton(
                            icon = painterResource(id = R.drawable.ic_possesion_next_arrow_selected),
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_46dp))
                                .height(dimensionResource(id = R.dimen.size_46dp)),
                            onClick = { /*TODO*/ }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                    Row(
                        Modifier
                            .fillMaxHeight()
                            .align(CenterVertically)
                            .weight(1f),
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.End,
                    ) {
                            ImageButton(
                                icon = painterResource(id = R.drawable.ic_possesion_next_arrow_selected),
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_46dp))
                                    .height(dimensionResource(id = R.dimen.size_46dp)),
                                onClick = { /*TODO*/ }
                            )
                        Row(
                            Modifier
                                .align(CenterVertically)
                                .weight(1f),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.End,
                        ) {
                            teamTitle(title = stringResource(id = R.string.other_team))
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                            teamLogo(logoColor = Color.Blue)
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                        }
                    }
                }
            }
        }
    }
}


