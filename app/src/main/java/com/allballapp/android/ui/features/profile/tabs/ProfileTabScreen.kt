package com.allballapp.android.ui.features.profile.tabs

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.allballapp.android.BuildConfig
import com.allballapp.android.common.apiToUIDateFormat2
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.TeamDetails
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.profile.ProfileChannel
import com.allballapp.android.ui.features.profile.ProfileEvent
import com.allballapp.android.ui.features.profile.ProfileViewModel
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.ColorBWGrayLight
import com.allballapp.android.ui.theme.ColorMainPrimary
import com.allballapp.android.ui.theme.appColors
import com.google.accompanist.flowlayout.FlowRow
import com.allballapp.android.R

@Composable
fun ProfileTabScreen(vm: ProfileViewModel, id: String) {
    /* val showParentDialog = remember {
         mutableStateOf(false)
     }*/
    val state = vm.state.value
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp / 2
    val width = screenWidth.minus(dimensionResource(id = R.dimen.size_16dp).times(2))
    val dataStoreManager = DataStoreManager(LocalContext.current)
//    val role = dataStoreManager.getRole.collectAsState(initial = "")

    remember {
        vm.onEvent(ProfileEvent.ClearUserData)
        vm.onEvent(ProfileEvent.GetProfile(id))
    }

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
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp)
                ),
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
                    CoilImage(
                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + state.user.profileImage,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                        onError = { Placeholder(R.drawable.ic_user_profile_icon) },
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
                    AppText(
                        text = "${state.user.firstName} ${state.user.lastName}",
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        fontSize = dimensionResource(id = R.dimen.txt_size_20).value.sp
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

                    Row(
                        modifier = Modifier
                    ) {
                        DetailItem(stringResource(id = R.string.email), state.user.email)
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        DetailItem(stringResource(id = R.string.number), state.user.phone)
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

            if (state.user.parentDetails.isNotEmpty())
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                        .background(color = Color.White),
                ) {
                    Column(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16dp)),
                    ) {
                        AppText(
                            text = stringResource(id = R.string.players),
                            style = MaterialTheme.typography.h5,
                            color = ColorBWBlack,
                            fontWeight = FontWeight.W500
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        FlowRow {
                            state.user.parentDetails.forEachIndexed { index, item ->

                                if (item != null) {
                                    ParentItem(
                                        width,
                                        item.parentType,
                                        "${item.firstName} ${item.lastName}",
                                        item.profileImage
                                    ) {
                                        vm.onEvent(
                                            ProfileEvent.OnParentDialogChange(true)
                                        )
                                        vm.onEvent(
                                            ProfileEvent.OnParentClick(item)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            if (state.user.birthdate != null)
                ProfileItem(
                    stringResource(id = R.string.birthday),
                    apiToUIDateFormat2(state.user.birthdate)
                )
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
                        style = MaterialTheme.typography.h5,
                        color = ColorBWBlack,
                        fontWeight = FontWeight.W500
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
                        style = MaterialTheme.typography.h5,
                        color = ColorBWBlack,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                    PreferenceItem(
                        stringResource(id = R.string.favorite_college_team),
                        if (state.user.userDetails.funFacts.isNotEmpty()) {
                            state.user.userDetails.funFacts[0].favCollegeTeam
                        } else "",
                        stringResource(id = R.string.favorite_proff_team),
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

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
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
            },
            parentDetails = state.selectedParentDetails
        )
    }
}

@Composable
fun ProfileItem(type: String, value: String, imageUrl: String? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(
                text = type,
                style = MaterialTheme.typography.h5,
                color = ColorBWBlack,
                fontWeight = FontWeight.W500
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AppText(
                    text = value,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )

                if (imageUrl != null) {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                    CoilImage(
                        src = com.allballapp.android.BuildConfig.IMAGE_SERVER + imageUrl,
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_24dp))
                            .clip(CircleShape),
                        isCrossFadeEnabled = false,
                        onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                        onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                    )
                }
            }
        }
    }
}

@Composable
fun ParentItem(
    width: Dp,
    relation: String,
    value: String,
    imageUrl: String,
    click: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = Color.Transparent)
            .width(width)
            .padding(
                bottom = dimensionResource(id = R.dimen.size_12dp),
                end = dimensionResource(id = R.dimen.size_2dp)
            )
            .clickable {
                click()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + imageUrl,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_44dp))
                .clip(CircleShape),
            isCrossFadeEnabled = false,
            onLoading = { PlaceholderRect(R.drawable.ic_user_profile_icon) },
            onError = { PlaceholderRect(R.drawable.ic_user_profile_icon) }
        )
        Column(
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_10dp)),
        ) {
            AppText(
                text = value,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack
            )
            if(relation.isNotEmpty()){
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))
                AppText(
                    text = relation,
                    style = MaterialTheme.typography.h6,
                    color = ColorBWGrayLight,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Composable
fun RowScope.DetailItem(heading: String, value: String) {
    Column(
        modifier = Modifier
            .padding(all = dimensionResource(id = R.dimen.size_8dp))
            .weight(1F),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = heading,
            style = MaterialTheme.typography.h4,
            color = ColorBWGrayLight,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
        AppText(
            text = value,
            textAlign = TextAlign.Center,
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

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AppText(
                text = stringResource(id = R.string.teams),
                style = MaterialTheme.typography.h5,
                color = ColorBWBlack,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_14dp)))
            teams.forEach { team ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CoilImage(
                            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + team.teamId.logo,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_44dp))
                                .clip(CircleShape),
                            isCrossFadeEnabled = false,
                            onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                            onError = { Placeholder(R.drawable.ic_team_placeholder) }
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                        Column {
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
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
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
                    modifier = Modifier.height(dimensionResource(id = R.dimen.size_35dp))
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_5dp)))

                AppText(
                    text = firstValue,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWBlack,
                    )

            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = secondKey,
                    style = MaterialTheme.typography.h5,
                    color = ColorBWGrayLight,
                    modifier = Modifier.height(dimensionResource(id = R.dimen.size_35dp))
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

