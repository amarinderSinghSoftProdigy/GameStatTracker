package com.allballapp.android.ui.features.home.events.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.data.response.AverageStatsResponse
import com.allballapp.android.data.response.GameStatsResponse
import com.allballapp.android.ui.features.components.AppDivider
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.*

@Composable
fun GameStatsTab(vm: EventViewModel) {
    val state = vm.eventState.value

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = stringResource(id = R.string.game_result),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TeamItem(
                        title = if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[0].name else "",
                        ColorMainPrimary,
                        if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[0].logo else ""
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "87 : 76",
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                        Text(
                            text = "WIN",
                            color = GreenColor,
                            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                            fontWeight = FontWeight.Bold,
                        )


                    }
                    TeamItem(
                        title = if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[1].name else "",
                        ColorButtonRed,
                        if (state.gameDetails.teams.isNotEmpty()) state.gameDetails.teams[1].logo else ""

                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
            Stats()
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                Text(
                    text = stringResource(id = R.string.game_champions),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.appColors.material.primary,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimensionResource(id = R.dimen.size_20dp))
                                .height(dimensionResource(id = R.dimen.size_80dp)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Text(
                                text = stringResource(id = R.string.offensive),
                                color = ColorBWGrayLight,
                                style = MaterialTheme.typography.h6,
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_ball),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = dimensionResource(id = R.dimen.txt_size_16))
                                )
                                Text(
                                    text = "Ruben",
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.bonus_points) + " 7",
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.appColors.material.primary,
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_8dp)
                                )
                            )
                            .weight(1f)
                    )
                    {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = dimensionResource(id = R.dimen.size_20dp))
                                .height(dimensionResource(id = R.dimen.size_80dp)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        )
                        {
                            Text(
                                text = stringResource(id = R.string.defensive),
                                color = ColorBWGrayLight,
                                style = MaterialTheme.typography.h6,
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

                            Text(
                                text = stringResource(id = R.string.choose_playerr),
                                color = ColorMainPrimary,
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
            AppDivider()
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                Text(
                    text = stringResource(id = R.string.post_game_notes),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
            AppDivider()
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                Text(
                    text = "Risus enim egestas placerat adipiscing accumsan velit nam varius. Vulputate habitant vitae at laoreet. Arcu, vitae mi enim, aenean. Egestas cras venenatis dis augue felis.",
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_ball),
                        contentDescription = null,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_60dp))
                            .padding(end = dimensionResource(id = R.dimen.txt_size_16))
                    )
                    Text(
                        text = "Coach Sam",
                        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun Stats() {
    val headings =
        arrayListOf<String>("Player", "Pts", "Fouls", "FG", "3pt", "FT", "Rbnd", "Rbnd", "TO")
    val totals =
        arrayListOf<String>("Total", "32", "5", "3", "4", "1", "0", "3", "1")
    val data =
        arrayListOf<GameStatsResponse>(
            GameStatsResponse(
                "1",
                "77 Joe",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),
            GameStatsResponse(
                "1",
                "18 Gavin",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),
            GameStatsResponse(
                "1",
                "11 Alex",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),
            GameStatsResponse(
                "1",
                "11 Alex",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),
            GameStatsResponse(
                "1",
                "11 Alex",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),
            GameStatsResponse(
                "1",
                "11 Alex",
                "10",
                "1",
                "0",
                "0",
                "4",
                "0",
                "1",
                "1"
            ),

            )
    val average = arrayListOf<AverageStatsResponse>(
        AverageStatsResponse(
            "1",
            "Average",
            "",
            "",
            "30%",
            "30%",
            "30%",
            "",
            "",
            ""
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ColorBWGrayDark)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_5dp),
                    vertical = dimensionResource(id = R.dimen.size_10dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            headings.forEachIndexed { index, it ->
                Text(
                    text = it,
                    color = button_text_enable,
                    fontSize = dimensionResource(id = R.dimen.txt_size_10).value.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(if (index == 0) 1f else 0.6f)
                        .padding(horizontal = dimensionResource(id = R.dimen.size_5dp)),
                    textAlign = TextAlign.Center
                )
            }
        }
        Column() {
            data.forEachIndexed { index, col ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = if (index % 2 == 0) md_theme_light_primary else Color.White)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_5dp),
                            vertical = dimensionResource(id = R.dimen.size_10dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    col.iterator().forEachIndexed { i, v ->
                        Text(
                            text = v,
                            color = if (v == "0") ColorBWGrayLight else ColorBWBlack,
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.W400,
                            modifier = Modifier
                                .weight(if (i == 0) 1f else 0.6f)
                                .padding(horizontal = dimensionResource(id = R.dimen.size_5dp)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

        }
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
        AppDivider()
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_5dp),
                    vertical = dimensionResource(id = R.dimen.size_10dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            totals.forEachIndexed { index, it ->
                Text(
                    text = it,
                    color = ColorBWBlack,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .weight(if (index == 0) 1f else 0.6f)
                        .padding(horizontal = dimensionResource(id = R.dimen.size_5dp)),
                    textAlign = TextAlign.Center
                )
            }

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
        AppDivider()
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
        Column {
            average.forEachIndexed { index, col ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.size_5dp),
                            vertical = dimensionResource(id = R.dimen.size_10dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    col.iterator().forEachIndexed { i, v ->
                        Text(
                            text = v,
                            color = ColorBWBlack,
                            modifier = Modifier
                                .weight(if (i == 0) 1f else 0.6f)
                                .padding(horizontal = dimensionResource(id = R.dimen.size_5dp)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
        }
        AppDivider()

    }
}
