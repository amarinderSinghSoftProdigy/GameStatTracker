package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun CoachFlowBackground(
    colorCode: String? = null,
    teamLogo: String? = null
) {
    val ballColor = colorResource(id = R.color.ball_color)
    Surface(color = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            Surface(
                shape = CircleShape,
                color = (if (colorCode.isNullOrEmpty()) ballColor else Color(
                    android.graphics.Color.parseColor(
                        "#$colorCode"
                    )
                )).copy(alpha = 0.05F),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .absoluteOffset(
                        x = dimensionResource(id = R.dimen.size_64dp),
                        y = -dimensionResource(id = R.dimen.size_45dp)
                    )
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (colorCode.isNullOrEmpty()) ballColor else Color(
                        android.graphics.Color.parseColor(
                            "#$colorCode"
                        )
                    ),
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.size_30dp),
                            end = dimensionResource(id = R.dimen.size_20dp),
                            start = dimensionResource(id = R.dimen.size_20dp),
                            top = dimensionResource(id = R.dimen.size_20dp)
                        )
                        .size(dimensionResource(id = R.dimen.size_200dp))
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_ball_lines),
                            contentDescription = "center ball Icon",
                            tint = colorResource(id = R.color.black),
                            modifier = Modifier.fillMaxSize()
                        )
                        teamLogo?.let {
                            Image(
                                painter = if(teamLogo.isNotEmpty()) rememberImagePainter(data = teamLogo) else painterResource(
                                    id = R.drawable.app_logo
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.size_65dp))
                                    .clip(CircleShape)
                                    .border(
                                        dimensionResource(id = R.dimen.size_3dp),
                                        MaterialTheme.colors.surface,
                                        CircleShape
                                    )
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserFlowBackground(
    modifier: Modifier = Modifier,
    padding: Dp? = dimensionResource(id = R.dimen.size_16dp),
    color: Color? = MaterialTheme.appColors.material.surface,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(all = padding ?: 0.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
        color = color ?: MaterialTheme.appColors.material.surface
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun BottomButtons(
    firstText: String = stringResource(id = R.string.back),
    secondText: String = stringResource(id = R.string.next),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    enableState: Boolean,
    showOnlyNext: Boolean = false,
    themed: Boolean = false,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!showOnlyNext) {
            AppButton(
                text = firstText,
                icon = null,
                onClick = onBackClick,
                border = ButtonDefaults.outlinedBorder,
                modifier = Modifier.weight(1f),
                themed = themed,
            )
        } else {
            AppSpacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
        AppSpacer(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_30dp)),
        )
        AppButton(
            text = secondText,
            onClick = onNextClick,
            icon = painterResource(id = R.drawable.ic_circle_next),
            enabled = enableState,
            modifier = Modifier.weight(1f),
            themed = themed,
        )
    }
}