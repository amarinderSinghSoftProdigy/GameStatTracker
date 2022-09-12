package com.softprodigy.ballerapp.ui.features.profile.tabs


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileTab(
) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.appColors.material.primary,)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_10dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                    AppText(
                        text = "George Will",
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                    )
                    Row(
                        modifier = Modifier

                    ) {
                        DetailItem("email", "joe@gmail.com")
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        DetailItem("number", "9888834352")
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.parents),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ParentItem(stringResource(id = R.string.mother), "Alena Culhane", "")
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        ParentItem(stringResource(id = R.string.father), "Brandon Culhan", "")
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.mother), "May 15, 1999")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.classof), "2025")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            ProfileItem(stringResource(id = R.string.positons), "PG,SG")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            TeamList()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.jersey_pref),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.jersey_number),
                        "23, 16, 18",
                        stringResource(id = R.string.gender),
                        "Male"
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.shirt_size),
                        "Adult, L",
                        stringResource(id = R.string.waist_size),
                        "32"
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = Color.White)
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                Column() {
                    AppText(
                        text = stringResource(id = R.string.fun_facts),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.favorite_college_team),
                        "Favorite College Team",
                        stringResource(id = R.string.favorite_nba_team),
                        "Team Name"
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.favorite_active_player),
                        "Player Name",
                        stringResource(id = R.string.favoritea_all_time_tlayer),
                        "Player Name"
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
        }
    }
}

@Composable
fun ProfileItem(type: String, value: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppText(
                text = type,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )

            AppText(
                text = value,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
        }
    }
}

@Composable
fun RowScope.ParentItem(relation: String, value: String, imageUrl: String) {

    Row(
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_demo),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_40dp))
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
        ) {
            AppText(
                text = relation,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )
            AppText(
                text = value,
                style = MaterialTheme.typography.h5,
                color = ColorBWGrayLight
            )
        }

    }
}

@Composable
fun RowScope.DetailItem(stringId: String, value: String) {
    Column(
        modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = stringResourceByName(stringId),
            style = MaterialTheme.typography.h5,
            color =ColorBWGrayLight
        )
        AppText(
            text = value,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
        )
    }
}

@Composable
fun TeamList() {
    var teams: Array<Team> = arrayOf(
        Team(name = "Springfield Bucks", status = "Player"),
        Team(name = "Springfield Sprouts", status = "Program Manger")
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            AppText(
                text = stringResource(id = R.string.teams_label),
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack,
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            teams.forEach { team ->
                Column(
                ) {
                    Row() {
                        Image(
                            painter = painterResource(id = R.drawable.user_demo),
                            contentDescription = "",
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_40dp))
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                        Column(
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
                        ) {
                            AppText(
                                text = team.name,
                                style = MaterialTheme.typography.h5,
                                color = ColorBWBlack
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                            AppText(
                                text = team.status,
                                style = MaterialTheme.typography.h6,
                                color = ColorMainPrimary
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                }
            }
        }
//
    }
}

@Composable
private fun PreferenceItem(
    firstKey: String,
    firstValue: String,
    secondKey: String,
    secondValue: String
) {
    Column(
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = firstKey,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWGrayLight,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = firstValue,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWBlack
                )

            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = secondKey,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWGrayLight
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = secondValue,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWBlack
                )

            }

        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
    }
}

