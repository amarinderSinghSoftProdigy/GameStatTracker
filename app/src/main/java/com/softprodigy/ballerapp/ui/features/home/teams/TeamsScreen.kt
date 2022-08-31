package com.softprodigy.ballerapp.ui.features.home.teams

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.SelectTeamDialog

@Composable
fun TeamsScreen(name: String?, showDialog: Boolean, dismissDialog: (Boolean) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        val vm: TeamViewModel = hiltViewModel()
        val context = LocalContext.current
        val state = vm.teamUiState.value
        val onTeamSelectionChange = { team: Team ->
            vm.onEvent(TeamUIEvent.OnTeamSelected(team))
        }
        LaunchedEffect(key1 = Unit) {
            vm.teamChannel.collect { uiEvent ->
                when (uiEvent) {
                    is TeamChannel.ShowToast -> {
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

        if (showDialog) {
            SelectTeamDialog(
                teams = vm.teamUiState.value.teams,
                onDismiss = { dismissDialog.invoke(false) },
                onConfirmClick = { vm.onEvent(TeamUIEvent.OnConfirmTeamClick) },
                onSelectionChange = onTeamSelectionChange,
                selected = state.selectedTeam
            )
        }

        Column(
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
            Text(
                text = "No players in your team",
                fontSize = 16.sp,
            )
            Text(
                text = "Add new players to lorem ipsum dolor sit amet.",
                fontSize = 12.sp,
            )
        }
    }
}