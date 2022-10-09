package com.softprodigy.ballerapp.ui.features.components

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.features.home.events.schedule.Space
import com.softprodigy.ballerapp.ui.features.venue.Location
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
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
                contentDescription = "",
                tint = Color.White
            )
        }
    } else if (topBarData.topBar == TopBar.MY_EVENT) {
        Row(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {
                backClick()
            }) {

            Space(dp = dimensionResource(id = R.dimen.size_10dp))

            Icon(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp)),
                painter = painterResource(id = R.drawable.ic_dots),
                contentDescription = "",
                tint = Color.White
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
            if (!topBarData.label.isNullOrEmpty()) topBarData.label
            else stringResource(id = R.string.app_name)
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
            if (userRole.equals(UserType.COACH.key, ignoreCase = true)) icon =
                painterResource(id = R.drawable.ic_settings)
        }
        TopBar.PROFILE -> {
            icon = painterResource(id = R.drawable.ic_edit)
        }
        TopBar.MY_EVENT -> {
            if (userRole.equals(UserType.COACH.key, ignoreCase = true))
                icon = painterResource(id = R.drawable.ic_add_circle_filled)
        }
        TopBar.EVENT_OPPORTUNITIES -> {
            icon = painterResource(id = R.drawable.ic_filter)
        }
        TopBar.GAME_DETAILS -> {
            icon = painterResource(id = R.drawable.ic_dots)
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
        color = if (onlyBorder) Color.Transparent
        else if (enabled) AppConstants.SELECTED_COLOR
        else colors.bckgroundDisabled,
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

@Composable
fun DividerCommon() {
    Divider(thickness = dimensionResource(id = R.dimen.divider))
}

data class TopBarData(
    val label: String? = "",
    val topBar: TopBar,
)

enum class TopBar(val stringId: String, val back: Boolean) {
    PROFILE(stringId = "profile_label", back = true),
    EDIT_PROFILE(stringId = "edit_profile", back = true),
    MY_EVENT(stringId = "events_label", back = false),
    EVENT_DETAILS(stringId = "", back = true),
    FILTER_EVENT(stringId = "filter_events", back = true),
    GAME_DETAILS(stringId = "", back = true),
    EVENT_LEAGUES(stringId = "events_label", back = false),
    REGISTRATION_FORM(stringId = "registration_form", back = true),
    TEAMS(stringId = "teams_label", back = false),
    EVENT_OPPORTUNITIES(stringId = "events_label", back = false),
    MANAGE_TEAM(stringId = "", back = true),
    SINGLE_LABEL_BACK(stringId = "", back = true),
    SINGLE_LABEL(stringId = "", back = false),
    EMPTY(stringId = "", back = false),
    NEW_EVENT(stringId = "", back = true),
    MY_LEAGUE(stringId = "", back = true),
    GAME_RULES(stringId = "", back = true),
    CREATE_TEAM(stringId = "create_a_team", back = true),
    INVITE_TEAM_MEMBERS(stringId = "invite_team_member", back = true),
    OPEN_VENUE(stringId = "", back = true),
    DIVISION_TAB(stringId = "", back = true),
    TEAM_TAB(stringId = "", back = true)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> FoldableItem(
    expanded: Boolean,
    headerBackground: Color = Color.LightGray.copy(alpha = 0.2f),
    headerBorder: BorderStroke? = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
    headerMinHeight: Dp = 50.dp,
    header: @Composable RowScope.(Boolean) -> Unit,
    childItems: List<T>,
    itemHorizontalPadding: Dp = 8.dp,
    hasItemLeadingSpacing: Boolean = true,
    hasItemTrailingSpacing: Boolean = true,
    itemsBackground: Color = Color.White,
    itemSpacing: Dp = 12.dp,
    item: @Composable ColumnScope.(T, Int) -> Unit,
    elevation: Dp = 0.dp
) {
    val isExpanded = remember { mutableStateOf(expanded) }
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.background,
        border = headerBorder,
        elevation = elevation
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(headerBackground)
                    .fillMaxWidth()
//                    .heightIn(min = headerMinHeight)
//                    .padding(vertical = 8.dp)
                    .clickable { isExpanded.value = !isExpanded.value }) {
                header(isExpanded.value)
            }
            AnimatedVisibility(isExpanded.value) {
                Column(
                    modifier = Modifier
                        .background(itemsBackground)
                        .padding(horizontal = itemHorizontalPadding)
                ) {
                    if (hasItemLeadingSpacing) Space(itemSpacing)
                    childItems.forEachIndexed { index, value ->
                        item(value, index)
                        if (index < childItems.lastIndex || hasItemTrailingSpacing) Space(
                            itemSpacing
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCheckBox(selected: Boolean, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .clickable {
            onClick()
        }
        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
        .size(
            dimensionResource(id = R.dimen.size_16dp)
        )
        .background(
            color = if (selected) {
                MaterialTheme.appColors.material.primaryVariant
            } else Color.White
        )
        .border(
            width = if (selected) {
                0.dp
            } else dimensionResource(id = R.dimen.size_1dp),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
            color = if (selected) {
                Color.Transparent
            } else MaterialTheme.appColors.buttonColor.bckgroundDisabled
        )) {
        Icon(
            tint = if (!selected) {
                Color.Transparent
            } else Color.White,
            imageVector = Icons.Default.Check,
            contentDescription = null,
        )
    }
}

@Composable
fun CustomTeamCheckBox(id: String, selected: Boolean, onClick: (String) -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .clickable {
            onClick(id)
        }
        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
        .size(
            dimensionResource(id = R.dimen.size_16dp)
        )
        .background(
            color = if (selected) {
                MaterialTheme.appColors.material.primaryVariant
            } else Color.White
        )
        .border(
            width = if (selected) {
                0.dp
            } else dimensionResource(id = R.dimen.size_1dp),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
            color = if (selected) {
                Color.Transparent
            } else MaterialTheme.appColors.buttonColor.bckgroundDisabled
        )) {
        Icon(
            tint = if (!selected) {
                Color.Transparent
            } else Color.White,
            imageVector = Icons.Default.Check,
            contentDescription = null,
        )
    }
}

@Composable
fun CustomSwitch(
    isSelected: Boolean,
    onClick: () -> Unit,
    width: Dp = dimensionResource(id = R.dimen.size_50dp),
    height: Dp = dimensionResource(id = R.dimen.size_32dp),
    checkedTrackColor: Color = MaterialTheme.appColors.material.primaryVariant,
    uncheckedTrackColor: Color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
    gapBetweenThumbAndTrackEdge: Dp = dimensionResource(id = R.dimen.size_4dp),
    cornerSize: Dp = dimensionResource(id = R.dimen.size_16dp),
    iconInnerPadding: Dp = dimensionResource(id = R.dimen.size_4dp),
    thumbSize: Dp = dimensionResource(id = R.dimen.size_24dp),
) {

    // this is to disable the ripple effect
    val interactionSource = remember {
        MutableInteractionSource()
    }

    // for moving the thumb
    val alignment by animateAlignmentAsState(if (isSelected) 1f else -1f)

    // outer rectangle with border
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .background(
                color = if (isSelected) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        // this is to add padding at the each horizontal side
        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {

            /*// thumb with icon
            Icon(
                imageVector = if (switchOn) Icons.Filled.Done else Icons.Filled.Close,
                contentDescription = if (switchOn) "Enabled" else "Disabled",
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding),
                tint = Color.White
            )*/

            Box(
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (isSelected) Color.White else Color.White,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding),

                )
        }
    }
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) }
}


@Composable
fun LocationBlock(location: Location, padding: Dp = dimensionResource(id = R.dimen.size_16dp)) {
    Column(
        Modifier.padding(horizontal = padding)
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(2F)) {
                Text(
                    text = stringResource(id = R.string.location),
                    color = ColorBWGrayLight,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                Text(
                    text = location.address,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))
                Text(
                    text = location.city + ", " + location.state + ", " + location.zipCode,
                    color = ColorBWGrayLight,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                )
            }
            TransparentButtonButton(
                modifier = Modifier.weight(1F),
                text = stringResource(id = R.string.navigate),
                onClick = {},
                icon = painterResource(id = R.drawable.ic_nav),
                enabled = false,
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(
                    color = Color.White,
                    RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                )
                .height(dimensionResource(id = R.dimen.size_160dp))
                .fillMaxWidth()
        ) {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(location.latLong, 15F)
            }
            GoogleMap(
                uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false),
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = location.latLong),
                    title = location.address,
                    snippet = location.city
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
    }
}