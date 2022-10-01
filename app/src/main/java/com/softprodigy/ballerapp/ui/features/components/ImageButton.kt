package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(90.dp),
    border: BorderStroke? = null,
    colors: Color = Color.Transparent,// = ButtonDefaults.buttonColors(ColorBWBlack),

    icon: Painter? = null,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color =
        if (enabled) {
            colors
        } else {
            colors
        }
    ) {
        if (icon != null)
            Image(
                painter = icon!!,
                modifier = Modifier.size(dimensionResource(R.dimen.size_20dp)),
                contentDescription = null,
            )
    }
}






