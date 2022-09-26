package com.softprodigy.ballerapp.ui.features.profile.tabs


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.apiToUIDateFormat2
import com.softprodigy.ballerapp.data.response.TeamDetails
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.profile.ProfileChannel
import com.softprodigy.ballerapp.ui.features.profile.ProfileEvent
import com.softprodigy.ballerapp.ui.features.profile.ProfileViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun ProfileTabScreen(vm: ProfileViewModel) {
    /* val showParentDialog = remember {
         mutableStateOf(false)
     }*/
    val state = vm.state.value
    val context = LocalContext.current


    LaunchedEffect(key1 = Unit) {
        vm.channel.collect { uiEvent ->
            when (uiEvent) {
                is ProfileChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
    Box(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CommonProgressBar()
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
                /*Image(
                    painter = painterResource(id = R.drawable.user_demo),
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_200dp))
                        .clip(CircleShape)
                )*/
                CoilImage(
                    src = BuildConfig.IMAGE_SERVER + state.user.profileImage,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_200dp))
                        .clip(CircleShape),
                    isCrossFadeEnabled = false,
                    onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                    onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = "${state.user.firstName} ${state.user.lastName}",
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack,
                    fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                )
                Row(
                    modifier = Modifier
                ) {
                    DetailItem("email", state.user.email)
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                    DetailItem("number", state.user.phone)
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
                        "-",
                        ""
                    ) { /*showParentDialog.value = true */
                        vm.onEvent(ProfileEvent.OnParentDialogChange(true))
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                    ParentItem(
                        stringResource(id = R.string.father),
                        "-",
                        ""
                    ) {
                        vm.onEvent(
                            ProfileEvent.OnParentDialogChange(true)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(stringResource(id = R.string.birthday), apiToUIDateFormat2(state.user.birthdate))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(stringResource(id = R.string.classof), state.user.userDetails.classOf)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        ProfileItem(
            stringResource(id = R.string.positons),
            state.user.userDetails.positionPlayed.joinToString { position -> position })
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        TeamList(state.user.teamDetails)
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
                    if (state.user.userDetails.jerseyPerferences.isNotEmpty())
                        state.user.userDetails.jerseyPerferences[0].jerseyNumberPerferences.joinToString { number -> number }
                    else "",
                    stringResource(id = R.string.gender),
                    state.user.gender
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                PreferenceItem(
                    stringResource(id = R.string.shirt_size),
                    if (state.user.userDetails.jerseyPerferences.isNotEmpty())
                        state.user.userDetails.jerseyPerferences[0].shirtSize
                    else "-",
                    stringResource(id = R.string.waist_size),
                    if (state.user.userDetails.jerseyPerferences.isNotEmpty())
                        state.user.userDetails.jerseyPerferences[0].waistSize
                    else "-"
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
                    if (state.user.userDetails.funFacts.isNotEmpty()) {
                        state.user.userDetails.funFacts[0].favCollegeTeam
                    } else "",
                    stringResource(id = R.string.favorite_nba_team),
                    if (state.user.userDetails.funFacts.isNotEmpty()) {
                        state.user.userDetails.funFacts[0].favProfessionalTeam
                    } else "",
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                PreferenceItem(
                    stringResource(id = R.string.favorite_active_player),
                    if (state.user.userDetails.funFacts.isNotEmpty()) {
                        state.user.userDetails.funFacts[0].favActivePlayer
                    } else "",
                    stringResource(id = R.string.favoritea_all_time_tlayer),
                    if (state.user.userDetails.funFacts.isNotEmpty()) {
                        state.user.userDetails.funFacts[0].favAllTimePlayer
                    } else "",
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
    }
    }

    if (state.showParentDialog) {
        ShowParentDialog(
            onDismiss = {
                vm.onEvent(
                    ProfileEvent.OnParentDialogChange(false)
                )
            },
            onConfirmClick = {
                vm.onEvent(
                    ProfileEvent.OnParentDialogChange(false)
                )
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
fun TeamList(teams: SnapshotStateList<TeamDetails>) {
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
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + team.teamId.logo,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_40dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                            onError = { Placeholder(R.drawable.ic_team_placeholder) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                        Column(
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
                        ) {
                            AppText(
                                text = team.teamId.name,
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

