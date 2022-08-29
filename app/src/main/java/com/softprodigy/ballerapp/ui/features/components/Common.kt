package com.softprodigy.ballerapp.ui.features.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
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
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_65dp))
            .background(if (!isNewDesign) color else Color.Yellow),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}


@Composable
fun BoxScope.CommonTabView(
    canMoveBack: Boolean = true,
    user: BottomNavKey,
    label: String = "",
    icon: Painter,
    onLabelClick: () -> Unit,
    iconClick: () -> Unit
) {

    if (canMoveBack) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            modifier = Modifier
                .clickable { }
                .align(Alignment.CenterStart),
            tint = Color.White
        )
    }

    Row(
        modifier = Modifier
            .align(Alignment.Center)
            .background(Color.Transparent)
            .clickable {
                if (user == BottomNavKey.TEAMS)
                    onLabelClick()
            }
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = label,
            style = MaterialTheme.typography.h3,
            color = Color.White
        )
        if (user == BottomNavKey.TEAMS) {
            AppSpacer(Modifier.size(dimensionResource(id = R.dimen.size_5dp)))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "",
                tint = Color.White
            )
        }
    }
    Icon(
        painter = icon,
        contentDescription = "",
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .clickable { iconClick() }
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        tint = Color.White
    )

}

@Composable
fun PagerIndicator(size: Int, currentPage: Int) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }
    }

}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = dimensionResource(id = R.dimen.size_10dp))

    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.size_1dp))
            .height(dimensionResource(id = R.dimen.size_10dp))
            .width(width = width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.Black else Color.Gray)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun rememberPagerState(
    @androidx.annotation.IntRange(from = 0) pageCount: Int,
    @androidx.annotation.IntRange(from = 0) initialPage: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) initialPageOffset: Float = 0f,
    @androidx.annotation.IntRange(from = 1) initialOffScreenLimit: Int = 1,
    infiniteLoop: Boolean = false
): PagerState = rememberSaveable(saver = PagerState.Saver) {

    PagerState(
        pageCount = pageCount,
        currentPage = initialPage,
        currentPageOffset = initialPageOffset,
        offscreenLimit = initialOffScreenLimit,
        infiniteLoop = infiniteLoop
    )

}