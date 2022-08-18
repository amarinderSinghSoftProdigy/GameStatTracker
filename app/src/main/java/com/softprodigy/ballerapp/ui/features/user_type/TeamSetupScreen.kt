package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.R


@Composable
fun TeamSetupScreen() {
    Box(Modifier.fillMaxSize()) {
        CoachFlowBackground(
            modifier = Modifier.fillMaxSize(),
            outerIcon = painterResource(id = R.drawable.ic_ball)
        )
        Column(Modifier.fillMaxSize(0.8f)) {
            AppText(
                text = stringResource(id = R.string.setup_your_team),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(40.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                AppText(
                    text = stringResource(id = R.string.team_name),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}