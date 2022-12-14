package com.softprodigy.ballerapp.ui.features.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun stringResourceByName(name: String): String {
    return if (name.isNotEmpty()) LocalContext.current.runCatching {
        resources.getIdentifier(name, "string", packageName)
    }.getOrNull()?.let { stringResource(id = it) } ?: name else ""
}

fun fromHex(color: String? = "0177C1") = Color(android.graphics.Color.parseColor("#" + color))

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
    topBarData: TopBarData,
    userRole: String,
    backClick: () -> Unit = {},
    iconClick: (() -> Unit)? = null,
    labelClick: (() -> Unit)? = null,
) {
    if (topBarData.topBar == TopBar.EMPTY) {
        return
    }

    if (topBarData.topBar.back) {
        Box(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {
                backClick()
            }) {
            Icon(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "", tint = Color.White
            )
        }
    } else if (topBarData.topBar == TopBar.MY_EVENT) {
        Box(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {

            }) {
            Icon(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                painter = painterResource(id = R.drawable.ic_dots),
                contentDescription = "", tint = Color.White
            )
        }
    }

    Row(
        modifier = Modifier
            .align(Alignment.Center)
            .background(Color.Transparent)
            .clickable(enabled = topBarData.topBar == TopBar.TEAMS) {
                if (topBarData.topBar == TopBar.TEAMS) {
                    if (labelClick != null) {
                        labelClick()
                    }
                }
            }
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val label = if (topBarData.topBar.stringId.isEmpty()) {
            if (!topBarData.label.isNullOrEmpty())
                topBarData.label
            else
                stringResource(id = R.string.app_name)
        } else {
            stringResourceByName(name = topBarData.topBar.stringId)
        }
        Text(
            textAlign = TextAlign.Center,
            text = label,
            style = MaterialTheme.typography.h3,
            color = Color.White
        )
        if (topBarData.topBar == TopBar.TEAMS) {
            AppSpacer(Modifier.size(dimensionResource(id = R.dimen.size_5dp)))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "",
                tint = Color.White
            )
        }
    }

    var icon: Painter? = null
    //Add the checks where we want to display the icon on the right corner
    when (topBarData.topBar) {
        TopBar.TEAMS -> {
            if (userRole.equals(UserType.COACH.key, ignoreCase = true))
                icon = painterResource(id = R.drawable.ic_settings)
        }
        TopBar.MY_EVENT -> {
            icon = painterResource(id = R.drawable.ic_add_circle)
        }
        TopBar.EVENT_OPPORTUNITIES -> {
            icon = painterResource(id = R.drawable.ic_filter)
        }
        else -> {}
    }
    icon?.let {
        Icon(
            painter = icon,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    if (iconClick != null) {
                        iconClick()
                    }
                }
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            tint = Color.White
        )
    }

    if (TopBar.MANAGE_TEAM == topBarData.topBar) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.save),
            style = MaterialTheme.typography.h5,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    if (iconClick != null) {
                        iconClick()
                    }
                }
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        )
    }

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
    colors: ButtonColor = MaterialTheme.appColors.buttonColor,// = ButtonDefaults.buttonColors(ColorBWBlack),
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp),
        horizontal = dimensionResource(id = R.dimen.size_16dp),
    ),
    text: String? = null,
    onlyBorder: Boolean = true,
) {
    val contentColor =
        if (onlyBorder) colors.textDisabled else if (enabled) colors.textEnabled else colors.textDisabled

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = if (onlyBorder)
            Color.Transparent
        else if (enabled)
            AppConstants.SELECTED_COLOR
        else
            colors.bckgroundDisabled,
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
                    ButtonView(text = text ?: "", color = contentColor)
                }
            }
        }
    }
}

@Composable
fun CommonProgressBar() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = MaterialTheme.appColors.material.primaryVariant
        )
    }
}

data class TopBarData(
    val label: String? = "",
    val topBar: TopBar,
)

enum class TopBar(val stringId: String, val back: Boolean) {
    PROFILE(stringId = "profile_label", back = false),
    MY_EVENT(stringId = "events_label", back = false),
    EVENT_LEAGUES(stringId = "events_label", back = false),
    TEAMS(stringId = "teams_label", back = false),
    EVENT_OPPORTUNITIES(stringId = "events_label", back = false),
    MANAGE_TEAM(stringId = "", back = true),
    SINGLE_LABEL_BACK(stringId = "", back = true),
    SINGLE_LABEL(stringId = "", back = false),
    EMPTY(stringId = "", back = false),
}
