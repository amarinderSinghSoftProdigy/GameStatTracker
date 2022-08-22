package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.AppSpacer
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.text_field_indicator

@Composable
fun CoachFlowBackground(
    colorCode: String? = null,
) {
    val ballColor = colorResource(id = R.color.ball_color)
    Surface() {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        .padding(all = dimensionResource(id = R.dimen.size_20dp))
                        .size(dimensionResource(id = R.dimen.size_200dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ball_green),
                        contentDescription = "center ball Icon",
                        tint = colorResource(id = R.color.black),
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_200dp))
                    )
                }
            }
        }
    }
}


@Composable
fun UserFlowBackground(
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
        color = MaterialTheme.appColors.material.surface
    ) {
        Column(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))) {
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
    showOnlyNext: Boolean = false
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                border = ButtonDefaults.outlinedBorder,
            )
        } else {
            AppSpacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1F))
        }
        AppSpacer(
            modifier = Modifier.width(dimensionResource(id = R.dimen.size_30dp)),
        )
        AppButton(
            text = secondText,
            onClick = onNextClick,
            icon = painterResource(id = R.drawable.ic_circle_next),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            enabled = enableState
        )
    }
}