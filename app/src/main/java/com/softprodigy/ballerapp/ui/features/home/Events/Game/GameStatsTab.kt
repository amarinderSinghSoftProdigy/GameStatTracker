package com.softprodigy.ballerapp.ui.features.home.Events.Game

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
fun GameStatsTab() {
    Box(Modifier.fillMaxSize()) {
        AppText(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.coming_soon),
            fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
            color = ColorBWBlack,
            fontWeight = FontWeight.Bold
        )
    }
}