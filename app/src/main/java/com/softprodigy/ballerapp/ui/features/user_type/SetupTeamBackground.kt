package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun CoachFlowBackground(
    colorCode: String? = null,
) {
    val ballColor = colorResource(id = R.color.ball_color)
    Surface {
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