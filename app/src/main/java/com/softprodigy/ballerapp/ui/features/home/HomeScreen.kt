package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeScreen(name: String?) {
    Box(Modifier.fillMaxSize()) {

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