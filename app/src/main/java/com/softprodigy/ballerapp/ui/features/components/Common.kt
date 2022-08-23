package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R

@Composable
fun stringResourceByName(name: String): String {
    return if (name.isNotEmpty()) LocalContext.current.runCatching {
        resources.getIdentifier(name, "string", packageName)
    }.getOrNull()?.let { stringResource(id = it) } ?: name else ""
}


@Composable
fun TabBar(
    color: Color = Color.White,
    isNewDesign: Boolean = false,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_56dp))
            .background(if (!isNewDesign) color else Color.Yellow),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}