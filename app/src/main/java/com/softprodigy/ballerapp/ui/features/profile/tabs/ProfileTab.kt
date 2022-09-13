package com.softprodigy.ballerapp.ui.features.profile.tabs


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ShowParentDialog
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun ProfileTab() {
    val showParentDialog = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.size_16dp))
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

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White),
        ) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp))) {
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
                    ParentItem(
                        stringResource(id = R.string.mother),
                        "Alena Culhane",
                        ""
                    ) { showParentDialog.value = true }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                    ParentItem(
                        stringResource(id = R.string.father),
                        "Brandon Culhan",
                        ""
                    ) { showParentDialog.value = true }
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(stringResource(id = R.string.birthday), "May 15, 1999")
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(stringResource(id = R.string.classof), "2025")
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(stringResource(id = R.string.positons), "PG,SG")
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        TeamList()
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White)
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        ) {
            Column {
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
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = Color.White)
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        ) {
            Column {
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

    if (showParentDialog.value) {
        ShowParentDialog(onDismiss = { showParentDialog.value = false }, onConfirmClick = {
            showParentDialog.value = false
        })
    }
}

@Composable
fun ProfileItem(type: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
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
fun RowScope.ParentItem(
    relation: String,
    value: String,
    imageUrl: String,
    click: () -> Unit
) {
    Row(modifier = Modifier
        .weight(1F)
        .clickable {
            click()
        }) {
        Image(
            painter = painterResource(id = R.drawable.user_demo),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_44dp))
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
        modifier = Modifier
            .padding(all = dimensionResource(id = R.dimen.size_8dp))
            .weight(1F),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = stringResourceByName(stringId),
            style = MaterialTheme.typography.h4,
            color = ColorBWGrayLight
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
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
        Team(name = "Springfield Bucks", role = "Player"),
        Team(name = "Springfield Sprouts", role = "Program Manger")
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
                                text = team.role,
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

