package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
    colors: ButtonColor = MaterialTheme.appColors.buttonColor,// = ButtonDefaults.buttonColors(ColorBWBlack),
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp),
        horizontal = dimensionResource(id = R.dimen.size_24dp),
    ),
    text: String? = null,
    icon: Painter? = null,
    singleButton: Boolean = false
) {
    val contentColor = if (enabled) colors.textEnabled else colors.textDisabled

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = if (icon == null) Color.Transparent else if (enabled) colors.bckgroundEnabled else colors.bckgroundDisabled,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
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
                        if (singleButton) {
                            SingleButtonView(
                                text = text ?: "",
                                color = contentColor,
                                painter = icon,
                                iconColor = if (enabled) Color.White else contentColor
                            )
                        } else {
                            ButtonView(text = text ?: "", color = contentColor)
                            Icon(
                                painter = icon,
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.size_20dp)),
                                contentDescription = null,
                                tint = if (enabled) Color.White else contentColor.copy(
                                    alpha = 0.5f
                                )
                            )
                        }

                    } else {
                        ButtonView(text = text ?: "", color = colors.textDisabled)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.ButtonView(text: String, color: Color) {
    Box(
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.size_20dp),
                end = dimensionResource(id = R.dimen.size_20dp)
            )
            .align(Alignment.CenterVertically),
        contentAlignment = Alignment.Center,
    ) {
        AppText(
            textAlign = TextAlign.Center,
            text = text,
            color = color
        )
    }
}


@Composable
fun RowScope.SingleButtonView(text: String, color: Color, painter: Painter, iconColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .align(Alignment.CenterVertically),
        contentAlignment = Alignment.Center,
    ) {
        AppText(
            textAlign = TextAlign.Center,
            text = text,
            color = color
        )
        Icon(
            painter = painter,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(dimensionResource(R.dimen.size_20dp)),
            contentDescription = null,
            tint = iconColor
        )
    }

}
