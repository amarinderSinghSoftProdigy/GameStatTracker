package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.softprodigy.ballerapp.R

@Composable
fun CoachFlowBackground(
    modifier: Modifier = Modifier,
    outerIcon: Painter,
    innerIcon: Painter? = null,
    centerIcon: Painter? = null
) {

    ConstraintLayout(modifier = modifier) {
        val outerIconRef = createRef()
        val innerIconRef = createRef()
        val centerIconRef = createRef()
        Icon(
            painter = outerIcon,
            contentDescription = "Outer ball Icon",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_236dp))
                .constrainAs(outerIconRef) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )
        if (innerIcon != null) {
            Icon(
                painter = innerIcon,
                contentDescription = "inner ball Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_200dp))
                    .constrainAs(innerIconRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }
        if (centerIcon != null) {
            Icon(
                painter = centerIcon,
                contentDescription = "center ball Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_44dp))
                    .constrainAs(centerIconRef) {
                        top.linkTo(innerIconRef.top)
                        end.linkTo(innerIconRef.end)
                        bottom.linkTo(innerIconRef.bottom)
                        start.linkTo(innerIconRef.start)
                    }
            )
        }
    }
}