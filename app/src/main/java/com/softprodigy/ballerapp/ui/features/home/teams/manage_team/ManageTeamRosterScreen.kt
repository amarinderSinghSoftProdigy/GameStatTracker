package com.softprodigy.ballerapp.ui.features.home.teams.manage_team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowRow
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AddPlayerDialog
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun ManageTeamRoster(vm: ManageTeamRstrViewModel = hiltViewModel()) {
    val state = vm.manageTeamRstrState.value
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                )

        ) {
            state.teamData.forEach {
                MangeTeamDataHeaderItem(title = it.position, players = it.players)
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.appColors.material.surface.copy(alpha = .97f))
                .height(dimensionResource(id = R.dimen.size_72dp))
        ) {
            ButtonWithLeadingIcon(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.add_player),
                onClick = { vm.onEvent(ManageTeamRstrUIEvent.OnDialogClick(true)) },
                painter = painterResource(id = R.drawable.ic_add_button),
            )
        }
    }
    if (state.showDialog) {
        AddPlayerDialog(
            searchKey = state.search,
            onSelectionChange = { vm.onEvent(ManageTeamRstrUIEvent.OnPlayerClick(player = it)) },
            matchedPlayers = state.matchedPlayers,
            selectedPlayers = state.selectedPlayers,
            onSearchKeyChange = {
                vm.onEvent(ManageTeamRstrUIEvent.OnSearch(it))
            },
            onDismiss = { vm.onEvent(ManageTeamRstrUIEvent.OnDialogClick(false)) },
            onConfirmClick = {}
        )
    }

}

@Composable
fun MangeTeamDataHeaderItem(title: String, players: MutableList<TeamUser>) {
    Column {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
        Text(
            text = title,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontWeight = FontWeight.W600,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
        FlowRow {
            players.forEach {
                TeamUserListItem(teamUser = it)
            }
        }
    }
}

@Composable
fun TeamUserListItem(
    teamUser: TeamUser,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.size_8dp))
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
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
            AsyncImage(
                model = BuildConfig.IMAGE_SERVER + teamUser.profileImage,
                contentDescription = "",
                modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.size_32dp))
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds,

                )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                text = teamUser.name,
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
                text = if (teamUser.role.equals("Coach", ignoreCase = true)) {
                    teamUser.role
                } else {
                    teamUser.points
                },
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                color = MaterialTheme.appColors.material.primaryVariant
            )
            if (!teamUser.role.equals("Coach", ignoreCase = true)) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                Text(
                    text = teamUser.role,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = MaterialTheme.appColors.textField.label
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))
            Icon(
                painter = painterResource(id = R.drawable.ic_drag),
                contentDescription = "",
                modifier =
                Modifier
                    .size(dimensionResource(id = R.dimen.size_12dp)),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

        }

    }
}