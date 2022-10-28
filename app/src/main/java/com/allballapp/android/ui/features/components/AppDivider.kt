package com.allballapp.android.ui.features.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.allballapp.android.R

@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = dimensionResource(id = R.dimen.divider),
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

@Composable
fun AppSpacer(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier,
    )
}


private const val DividerAlpha = 0.12f

