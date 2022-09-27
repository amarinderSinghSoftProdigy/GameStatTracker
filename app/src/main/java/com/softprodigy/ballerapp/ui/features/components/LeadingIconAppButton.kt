package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LeadingIconAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
    colors: Color = MaterialTheme.appColors.material.primaryVariant,// = ButtonDefaults.buttonColors(ColorBWBlack),
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp),
        horizontal = dimensionResource(id = R.dimen.size_24dp),
    ),
    text: String? = null,
    icon: Painter? = null,
    iconBorder: Boolean = true,
) {
    val contentColor = if (enabled) Color.White else ColorBWGrayLight

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = if (enabled)
            colors
        else
            Color.Transparent,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = dimensionResource(id = R.dimen.size_10dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier.padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (icon != null) {
                        Surface(
                            shape = CircleShape,
                            border = BorderStroke(
                                dimensionResource(id = R.dimen.size_3dp),
                                color = if (iconBorder) Color.White else Color.Transparent
                            ), color = Color.Transparent
                        ) {
                            Icon(
                                painter = icon,
                                modifier = Modifier
                                    .padding(all = dimensionResource(id = R.dimen.size_5dp))
                                    .size(dimensionResource(R.dimen.size_20dp)),
                                contentDescription = null,
                                tint = if (enabled) Color.White else contentColor.copy(
                                    alpha = 0.5f
                                )
                            )
                        }
                        ButtonView(text = text ?: "", color = contentColor)
                    } else if (enabled) {
                        ButtonView(text = text ?: "", color = contentColor)
                    } else {
                        ButtonView(text = text ?: "", color = contentColor)
                    }
                }
            }
        }
    }
}