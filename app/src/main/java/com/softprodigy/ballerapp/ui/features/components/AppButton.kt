package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors

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
    singleButton: Boolean = false,
    isForceEnableNeeded: Boolean = false,
    themed: Boolean = false,
) {
    val contentColor = if (enabled) colors.textEnabled else colors.textDisabled

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = if (icon == null && !isForceEnableNeeded)
            Color.Transparent
        else if (enabled) {
            if (themed) {
                AppConstants.SELECTED_COLOR
            } else {
                colors.bckgroundEnabled
            }
        } else
            colors.bckgroundDisabled,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = if (enabled && icon != null) {
            dimensionResource(id = R.dimen.size_5dp)
        } else {
            0.dp
        }
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

                    } else if (isForceEnableNeeded && enabled) {
                        ButtonView(text = text ?: "", color = colors.textEnabled)
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

@Composable
fun ButtonWithLeadingIcon(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter,
    colors: ButtonColor = MaterialTheme.appColors.buttonColor,
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp),
        horizontal = dimensionResource(id = R.dimen.size_24dp),
    ),
    isTransParent: Boolean = false,
    iconSize: Dp = dimensionResource(id = R.dimen.size_10dp),
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .border(
                width = 1.dp,
                color =
                if (isTransParent)
                    MaterialTheme.appColors.editField.borderFocused else {
                    AppConstants.SELECTED_COLOR
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
            )
            .background(
                color =
                if (isTransParent) {
                    MaterialTheme.appColors.material.surface
                } else {
                    AppConstants.SELECTED_COLOR
                },
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_8dp)
                )
            )
            .padding(contentPadding),

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            tint = if (isTransParent) {
                AppConstants.SELECTED_COLOR
            } else {
                colors.textEnabled
            },
            modifier = Modifier.size(
                iconSize
            )

        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))

        AppText(
            text = text,
            color = if (isTransParent) {
                colors.textDisabled
            } else {
                colors.textEnabled
            },
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun ButtonWithLeadingIconGrayed(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter,
    colors: ButtonColor = MaterialTheme.appColors.buttonColor,
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp),
        horizontal = dimensionResource(id = R.dimen.size_24dp),
    ),
    iconSize: Dp = dimensionResource(id = R.dimen.size_20dp),
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .border(
                width = 1.dp,
                color = MaterialTheme.appColors.editField.borderFocused,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
            )
            .background(
                color = MaterialTheme.appColors.material.surface,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
            )
            .padding(contentPadding),

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.size(
                iconSize
            ),
            tint = ColorGreyLighter

        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))

        AppText(
            text = text,
            color = colors.textDisabled,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun InviteTeamMemberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter,
    colors: ButtonColor = MaterialTheme.appColors.buttonColor,
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_10dp),
        horizontal = dimensionResource(id = R.dimen.size_24dp),
    ),
    isTransParent: Boolean = false,
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .border(
                width = 1.dp,
                color =
                if (isTransParent)
                    Color.Transparent else {
                    AppConstants.SELECTED_COLOR
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
            )
            .background(
                color =
                if (isTransParent) {
                    AppConstants.SELECTED_COLOR.copy(alpha = 0.09f)
                } else {
                    AppConstants.SELECTED_COLOR
                },
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.size_8dp)
                )
            )
            .padding(contentPadding),

        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = "",
            tint = if (isTransParent) {
                AppConstants.SELECTED_COLOR
            } else {
                colors.textEnabled
            },
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.size_10dp)
            )

        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12dp)))

        AppText(
            text = text,
            color = if (isTransParent) {
                AppConstants.SELECTED_COLOR
            } else {
                colors.textEnabled
            },
            style = MaterialTheme.typography.button
        )
    }
}






