package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack

@Composable
fun EmptyScreen() {
    Box(Modifier.fillMaxSize()) {
        AppText(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.coming_soon),
            fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
            color = ColorBWBlack,
            fontWeight = FontWeight.Bold
        )

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
    }
}