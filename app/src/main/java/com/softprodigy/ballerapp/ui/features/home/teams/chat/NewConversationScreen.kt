package com.softprodigy.ballerapp.ui.features.home.teams.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Coach
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayStatus
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun NewConversationScreen(vm: TeamViewModel) {
    val state = vm.teamUiState.value

    var selectedPlayers = remember {
        mutableStateListOf<String>()
    }
    var selectedCoach = remember {
        mutableStateListOf<String>()
    }

    Box(Modifier.fillMaxSize()) {
        if (state.coaches.isNotEmpty() || state.players.isNotEmpty()) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16dp),
                        end = dimensionResource(id = R.dimen.size_16dp),
                    )


            ) {
                if (state.coaches.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.appColors.material.surface,
                                shape = RoundedCornerShape(
                                    topStart = dimensionResource(
                                        id = R.dimen.size_8dp
                                    ),
                                    topEnd = dimensionResource(
                                        id = R.dimen.size_8dp
                                    )
                                )
                            )
                    ) {


                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                        Text(
                            text = stringResource(id = R.string.coaches),
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontWeight = FontWeight.W600,
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                        LazyColumn() {
                            items(state.coaches) { coach ->
                                TeamUserListCheckbox(
                                    isCoach = true,
                                    coachUser = coach,
                                    selectedTeamMembers = selectedCoach
                                ) {
                                    selectedCoach = it
                                    Log.i("selectedCoach", "NewConversationScreen: $it")

                                }
                            }
                        }
                    }

                }

                if (state.players.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.appColors.material.surface,
                            )
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                            Text(
                                text = stringResource(id = R.string.players),
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))

                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                        }
                        LazyColumn() {
                            items(state.players) { player ->
                                TeamUserListCheckbox(
                                    isCoach = false,
                                    teamUser = player,
                                    selectedTeamMembers = selectedPlayers
                                ) {
                                    selectedPlayers = it
                                    Log.i("selectedPlayers", "NewConversationScreen: $it")
                                }
                            }
                        }

                    }
                }
            }

        }
        IconButton(
            modifier = Modifier
                .offset((-20).dp, (-20).dp)
                .size(dimensionResource(id = R.dimen.size_44dp))
                .background(

                    color = if (selectedPlayers.isNotEmpty() || selectedCoach.isNotEmpty())
                        MaterialTheme.appColors.material.primaryVariant
                    else MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                    RoundedCornerShape(50)
                )
                .align(Alignment.BottomEnd),
            enabled = selectedPlayers.isNotEmpty() || selectedCoach.isNotEmpty(),
            onClick = {}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_next),
                "",
                modifier = Modifier.width(
                    dimensionResource(id = R.dimen.size_14dp)
                )
            )
        }
        if (state.isLoading) {
            CommonProgressBar()
        }

    }


}

@Composable
fun TeamUserListCheckbox(
    modifier: Modifier = Modifier,
    teamUser: Player? = null,
    coachUser: Coach? = null,
    isCoach: Boolean = false,
    selectedTeamMembers: SnapshotStateList<String>,
    onSelectedMemberChange: (SnapshotStateList<String>) -> Unit
) {

    val statusTeam = remember {
        if (teamUser != null)
            mutableStateOf(selectedTeamMembers.contains(teamUser._id))
        else {
            mutableStateOf(selectedTeamMembers.contains(coachUser?._id))
        }
    }

    Column {
        AppDivider()
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                )
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(color = MaterialTheme.appColors.material.surface),
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .align(Alignment.CenterStart)
                    .padding(vertical = dimensionResource(id = R.dimen.size_8dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                val url = "" + if (isCoach) coachUser?.profileImage else teamUser?.profileImage
                CoilImage(
                    src = if (url.contains("http")) url else BuildConfig.IMAGE_SERVER + url,
                    modifier =
                    Modifier
                        .background(
                            color = ColorBWGrayStatus,
                            shape = CircleShape
                        )
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape),
                    isCrossFadeEnabled = false,
                    onLoading = { Placeholder(R.drawable.ic_user_profile_icon) },
                    onError = { Placeholder(R.drawable.ic_user_profile_icon) }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = if (isCoach) "${coachUser?.firstName} ${coachUser?.lastName}"
                        ?: "" else "${teamUser?.firstName} ${teamUser?.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isCoach) coachUser?.coachPosition ?: "" else teamUser?.jerseyNumber
                        ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = MaterialTheme.appColors.material.primaryVariant
                )
                if (!isCoach) {
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = teamUser?.position ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        color = MaterialTheme.appColors.textField.label.copy(alpha = 1f)
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))
                if (coachUser != null) {
                    CustomCheckBox(statusTeam.value) {
                        statusTeam.value = !statusTeam.value
                        if (selectedTeamMembers.contains(coachUser._id)) {
                            selectedTeamMembers.remove(coachUser._id)
                        } else {
                            coachUser._id?.let { selectedTeamMembers.add(it) }
                        }
                        onSelectedMemberChange.invoke(selectedTeamMembers)
                    }
                } else if (teamUser != null) {
                    CustomCheckBox(statusTeam.value) {
                        statusTeam.value = !statusTeam.value
                        if (selectedTeamMembers.contains(teamUser._id)) {
                            selectedTeamMembers.remove(teamUser._id)
                        } else {
                            teamUser._id.let { selectedTeamMembers.add(it) }
                        }
                        onSelectedMemberChange.invoke(selectedTeamMembers)
                    }
                }



                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

            }

        }
    }
}


