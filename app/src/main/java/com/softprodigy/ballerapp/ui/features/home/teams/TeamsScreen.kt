package com.softprodigy.ballerapp.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.SelectTeamDialog
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.SetupTeamViewModel
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.TeamSetupUIEvent
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun TeamsScreen(
    name: String?,
    showDialog: Boolean,
    setupTeamViewModel: SetupTeamViewModel,
    dismissDialog: (Boolean) -> Unit,
    addPlayerClick: (Team) -> Unit
) {
    val vm: TeamViewModel = hiltViewModel()
    val context = LocalContext.current
    val state = vm.teamUiState.value
    val onTeamSelectionChange = { team: Team ->
        vm.onEvent(TeamUIEvent.OnTeamSelected(team))
        setupTeamViewModel.onEvent(
            TeamSetupUIEvent.OnColorSelected(
                team.colorCode.replace(
                    "#",
                    ""
                )
            )
        )
    }
    val onStandingSelectionChange = { standing: Standing ->
        vm.onEvent(TeamUIEvent.OnStandingSelected(standing))
     /*   setupTeamViewModel.onEvent(
            TeamSetupUIEvent.OnColorSelected(
                "0177C1"
                *//* standing.colorCode.replace(
                     "#",
                     ""
                 )*//*
            )
        )*/
    }
    val message = stringResource(id = R.string.no_team_selected)
    LaunchedEffect(key1 = Unit) {
        vm.teamChannel.collect { uiEvent ->
            when (uiEvent) {
                is TeamChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        if (showDialog) {
            SelectTeamDialog(
                teams = vm.teamUiState.value.teams,
                onDismiss = { dismissDialog.invoke(false) },
                onConfirmClick = { vm.onEvent(TeamUIEvent.OnConfirmTeamClick) },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam
            )
        }

/*        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_teams_large),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.no_players_in_team),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.add_players_to_use_app),
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))


            LeadingIconAppButton(
                icon = painterResource(id = R.drawable.ic_add_player),
                text = stringResource(id = R.string.add_players),
                onClick = {
                    if (state.selectedTeam == null) {
                        vm.onEvent(TeamUIEvent.ShowToast(message))
                    } else {
                        addPlayerClick(state.selectedTeam)
                    }
                },
            )
        }*/
        Column(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                /*   item {
                       state.standing.forEach {
                           StandingListItem(standing = it, selected = state.selectedStanding == it) { standing ->
                               onStandingSelectionChange.invoke(standing)
                           }
                       }
                   }*/
                itemsIndexed(state.standing) { index, standing ->
                    StandingListItem(
                        index + 1,
                        standing = standing,
                        selected = state.selectedStanding == standing
                    ) {
                        onStandingSelectionChange.invoke(standing)
                    }
//                    { standing ->
//                        onStandingSelectionChange.invoke(standing)
                    /*  state.standing.forEach {
                          StandingListItem(standing = it, selected = state.selectedStanding == it) { standing ->
                              onStandingSelectionChange.invoke(standing)
                          }
                      }*/

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StandingListItem(
    index: Int,
    standing: Standing,
    selected: Boolean,
    onClick: (Standing) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_12dp),
                vertical = dimensionResource(id = R.dimen.size_2dp)
            )
    ) {
//        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

        Surface(
            onClick = { onClick(standing) },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
            elevation = if (selected) dimensionResource(id = R.dimen.size_10dp) else 0.dp,
            color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.White,
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .padding(
                        PaddingValues(
                            dimensionResource(id = R.dimen.size_12dp),
                            dimensionResource(id = R.dimen.size_12dp)
                        )
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                AppText(
                    text = index.toString(),
//                    color = MaterialTheme.appColors.textField.label,
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.textDisabled
                    },
                    fontSize = dimensionResource(
                        id = R.dimen.txt_size_12
                    ).value.sp,
                    fontWeight = FontWeight.W400
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                AsyncImage(
                    model = BuildConfig.IMAGE_SERVER + standing.logo,
                    contentDescription = "",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = standing.name,
                    fontWeight = FontWeight.W500,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    modifier = Modifier.weight(1f),
                    color = if (selected) {
                        MaterialTheme.appColors.buttonColor.textEnabled
                    } else {
                        MaterialTheme.appColors.buttonColor.textDisabled
                    }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }
        }
    }
}