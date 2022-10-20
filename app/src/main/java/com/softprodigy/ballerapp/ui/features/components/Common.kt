package com.softprodigy.ballerapp.ui.features.components

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.ColorUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.features.home.events.schedule.Space
import com.softprodigy.ballerapp.ui.features.venue.Location
import com.softprodigy.ballerapp.ui.theme.ButtonColor
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.CommonUtils
import kotlinx.coroutines.delay

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
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .align(Alignment.Center)
            .background(Color.Transparent)
            .clickable(
                enabled = topBarData.topBar == TopBar.TEAMS,
                indication = null,
                interactionSource = interactionSource
            ) {
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

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (topBarData.logo != null) {
                CoilImage(
                    src = BuildConfig.IMAGE_SERVER + topBarData.logo,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_32dp))
                        .background(
                            color = MaterialTheme.appColors.material.primary,
                            CircleShape
                        )
                        .clip(
                            CircleShape
                        ),
                    isCrossFadeEnabled = false,
                    onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                    onError = { Placeholder(R.drawable.ic_team_placeholder) },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
            }

            Text(
                textAlign = TextAlign.Center,
                text = label,
                style = MaterialTheme.typography.h3,
                color = Color.White
            )
        }


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
                icon = painterResource(id = R.drawable.ic_top_add)
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = "",
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_24dp))
                    .clickable {
                        if (iconClick != null) {
                            iconClick()
                        }
                    },
                tint = Color.White
            )

            Spacer(
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_20dp))
            )
        }

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
    val width =
        if (isSelected) animateDpAsState(targetValue = dimensionResource(id = R.dimen.size_24dp)) else animateDpAsState(
            targetValue = dimensionResource(id = R.dimen.size_10dp)
        )


    Box(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.size_3dp))
            .height(dimensionResource(id = R.dimen.size_10dp))
            .width(width = width.value)
            .clip(CircleShape)
            .background(if (isSelected) Color.White else ColorBWBlack.copy(0.3f))
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
    val logo: String? = null
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
    TEAM_TAB(stringId = "", back = true),
    DOCUMENT(stringId = "", back = false)
}

fun getRoleList(): List<UserType> {
    return listOf(
        UserType.PLAYER,
        UserType.COACH,
        UserType.PARENT,
        //UserType.GAME_STAFF,
        UserType.PROGRAM_STAFF,
        //UserType.FAN,
        UserType.REFEREE
    )
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
    val context = LocalContext.current
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
                onClick = {
                    CommonUtils.openMaps(context, location.latLong)
                },
                icon = painterResource(id = R.drawable.ic_nav),
                enabled = true,
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
            val initialZoom = 6f
            val finalZoom = 20f
            val destinationLatLng = location.latLong//LatLng(destination.lat, destination.lng)

            if (location.latLong.latitude != 0.0 && location.latLong.longitude != 0.0) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(destinationLatLng, initialZoom)
                }
                LaunchedEffect(key1 = true) {
                    cameraPositionState.move(CameraUpdateFactory.zoomIn())
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(destinationLatLng, finalZoom, 0f, 0f)
                        ),
                        durationMs = 1000
                    )
                }
                GoogleMap(
                    uiSettings = MapUiSettings(
                        compassEnabled = false,
                        zoomControlsEnabled = false
                    ),
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                ) {
                    Marker(
                        state = MarkerState(position = location.latLong),
                        title = location.address,
                        snippet = location.city
                    )
                }
            } else {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(destinationLatLng, initialZoom)
                }
                GoogleMap(
                    uiSettings = MapUiSettings(compassEnabled = false, zoomControlsEnabled = false),
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                ) {
                    Marker(
                        state = MarkerState(position = location.latLong),
                        title = location.address,
                        snippet = location.city
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
    }
}

@Composable
fun Tooltip(
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    timeoutMillis: Long = TooltipTimeout,
    backgroundColor: Color = Color.Black,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded.value

    if (expandedStates.currentState || expandedStates.targetState) {
        if (expandedStates.isIdle) {
            LaunchedEffect(timeoutMillis, expanded) {
                delay(timeoutMillis)
                expanded.value = false
            }
        }

        Popup(
            onDismissRequest = { expanded.value = false },
            popupPositionProvider = DropdownMenuPositionProvider(offset, LocalDensity.current),
            properties = properties,
        ) {
            Box(
                // Add space for elevation shadow
                modifier = Modifier.padding(TooltipElevation),
            ) {
                TooltipContent(expandedStates, backgroundColor, modifier, content)
            }
        }
    }
}


/** @see androidx.compose.material.DropdownMenuContent */
@Composable
private fun TooltipContent(
    expandedStates: MutableTransitionState<Boolean>,
    backgroundColor: Color,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Tooltip open/close animation.
    val transition = updateTransition(expandedStates, "Tooltip")

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(durationMillis = InTransitionDuration)
            } else {
                // Expanded to dismissed.
                tween(durationMillis = OutTransitionDuration)
            }
        }
    ) { if (it) 1f else 0f }

    Card(
        backgroundColor = backgroundColor.copy(alpha = 0.75f),
        contentColor = MaterialTheme.colors.contentColorFor(backgroundColor)
            .takeOrElse { backgroundColor.onColor() },
        modifier = Modifier.alpha(alpha),
        elevation = TooltipElevation,
    ) {
        val p = TooltipPadding
        Column(
            modifier = modifier
                .padding(start = p, top = p * 0.5f, end = p, bottom = p * 0.7f)
                .width(IntrinsicSize.Max),
            content = content,
        )
    }
}

private val TooltipElevation = 16.dp
private val TooltipPadding = 10.dp

// Tooltip open/close animation duration.
private const val InTransitionDuration = 64
private const val OutTransitionDuration = 240

// Default timeout before tooltip close
private const val TooltipTimeout = 2_000L - OutTransitionDuration


// Color utils

/**
 * Calculates an 'on' color for this color.
 *
 * @return [Color.Black] or [Color.White], depending on [isLightColor].
 */
fun Color.onColor(): Color {
    return if (isLightColor()) Color.Black else Color.White
}

/**
 * Calculates if this color is considered light.
 *
 * @return true or false, depending on the higher contrast between [Color.Black] and [Color.White].
 *
 */
fun Color.isLightColor(): Boolean {
    val contrastForBlack = calculateContrastFor(foreground = Color.Black)
    val contrastForWhite = calculateContrastFor(foreground = Color.White)
    return contrastForBlack > contrastForWhite
}

fun Color.calculateContrastFor(foreground: Color): Double {
    return ColorUtils.calculateContrast(foreground.toArgb(), toArgb())
}

@Immutable
internal data class DropdownMenuPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPositionCalculated: (IntRect, IntRect) -> Unit = { _, _ -> }
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // The min margin above and below the menu, relative to the screen.
        val verticalMargin = with(density) { 48.dp.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        // Compute horizontal position.
        val toRight = anchorBounds.left + contentOffsetX
        val toLeft = anchorBounds.right - contentOffsetX - popupContentSize.width
        val toDisplayRight = windowSize.width - popupContentSize.width
        val toDisplayLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(
                toRight,
                toLeft,
                // If the anchor gets outside of the window on the left, we want to position
                // toDisplayLeft for proximity to the anchor. Otherwise, toDisplayRight.
                if (anchorBounds.left >= 0) toDisplayRight else toDisplayLeft
            )
        } else {
            sequenceOf(
                toLeft,
                toRight,
                // If the anchor gets outside of the window on the right, we want to position
                // toDisplayRight for proximity to the anchor. Otherwise, toDisplayLeft.
                if (anchorBounds.right <= windowSize.width) toDisplayLeft else toDisplayRight
            )
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: toLeft

        // Compute vertical position.
        val toBottom = maxOf(anchorBounds.bottom + contentOffsetY, verticalMargin)
        val toTop = anchorBounds.top - contentOffsetY - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height - verticalMargin
        val y = sequenceOf(toBottom, toTop, toCenter, toDisplayBottom).firstOrNull {
            it >= verticalMargin &&
                    it + popupContentSize.height <= windowSize.height - verticalMargin
        } ?: toTop

        onPositionCalculated(
            anchorBounds,
            IntRect(x, y, x + popupContentSize.width, y + popupContentSize.height)
        )
        return IntOffset(x, y)
    }
}

class MaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text.text)
    }
}

//const val mask = "(***) *** ****"

fun maskFilter(text: String): TransformedText {

    val trimmed = if (text.length >= 10) text.substring(0..9) else text
    var out = ""
    for (i in trimmed.indices) {
        if (i == 0) out += "("
        out += trimmed[i]
        if (i == 2) out += ") "
        if (i == 5) out += " "
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset < 1) return offset
            if (offset < 3) return offset + 1
            if (offset < 6) return offset + 3
            if (offset < 10) return offset + 4
            return 14
        }

        override fun transformedToOriginal(offset: Int): Int {
            /* if (offset <= 1) return offset
            if (offset <= 4) return offset - 1
            if (offset <= 9) return offset - 2*/
            //return 13
            return offset
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}
