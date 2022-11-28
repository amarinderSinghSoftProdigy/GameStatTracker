package com.allballapp.android.ui.features.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.ui.theme.ColorBWGrayLight
import com.allballapp.android.ui.theme.ColorGreyLighter
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily


/**
 * composable for bottom navigation item
 */
@Composable
fun BottomNavigationBar(
    navKey: BottomNavKey,
    badgeCount: Int = 0,
    navController: NavController,
    height: Dp = dimensionResource(id = R.dimen.size_64dp),
    selectionColor: Color = MaterialTheme.appColors.material.primaryVariant,
    selectedValue: (BottomNavKey) -> Unit,
) {
    //val selected: MutableState<BottomNavKey> = remember { mutableStateOf(navKey) }
    Surface(
        elevation = dimensionResource(id = R.dimen.size_12dp),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(height),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val mBottomNavItems: List<BottomNavigationItem> = listOf(
                BottomNavigationItem.Home,
                BottomNavigationItem.Events,
                BottomNavigationItem.Teams,
            )
            mBottomNavItems.forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(height)
                        .clickable {
                            //selected.value = item.key
                            if (navKey != item.key) {
                                selectedValue(item.key)
                                navController.navigate(item.key.route)
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .height(height)
                            .align(Alignment.CenterHorizontally)
                            .width(dimensionResource(id = R.dimen.size_50dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Icon(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                //tint = if (selected.value == item.key) selectionColor else ColorGreyLighter
                                tint = if (navKey == item.key) selectionColor else ColorGreyLighter
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center,
                                text = stringResourceByName(item.key.resId),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                //color = if (selected.value == item.key) selectionColor else ColorGreyLighter,
                                color = if (navKey == item.key) selectionColor else ColorGreyLighter,
                                fontWeight = FontWeight.W700
                            )
                        }
                        if (item.key == BottomNavKey.TEAMS && badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(y = (dimensionResource(id = R.dimen.size_5dp)))
                                    .background(
                                        shape = RoundedCornerShape(50),
                                        color = AppConstants.SELECTED_COLOR
                                    )
                                    .clip(CircleShape)
                                    .align(Alignment.TopEnd)

                                    .padding(
                                        horizontal = dimensionResource(id = R.dimen.size_6dp),
                                        vertical = dimensionResource(id = R.dimen.size_2dp)
                                    )
                            ) {
                                AppText(
                                    text = badgeCount.toString(),
                                    fontFamily = rubikFamily,
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.material.surface
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}


/**
 * bottom nav items , icons and their selected state
 */
sealed class BottomNavigationItem(
    var icon: Int,
    var key: BottomNavKey,
) {
    object Home :
        BottomNavigationItem(
            R.drawable.ic_home,
            key = BottomNavKey.HOME,
        )

    object Events :
        BottomNavigationItem(
            R.drawable.ic_events,
            key = BottomNavKey.EVENTS,
        )

    object Teams :
        BottomNavigationItem(
            R.drawable.ic_teams,
            key = BottomNavKey.TEAMS,
        )
}

enum class BottomNavKey(val resId: String, val route: String) {
    HOME("home_label", "homeScreen"),
    EVENTS("events_label", "eventsScreen"),
    TEAMS("teams_label", "teamsScreen")
    //Add items to add in the bottom navigation
}

enum class UserType(val stringId: String, val key: String) {
    PLAYER("player_label", "player"),
    COACH("coach_label", "coach"),
    REFEREE("referee_label", "gameStaff"),
    PARENT("parent_label", "guardian"),
    PROGRAM_MANAGER("program_manager", "programManager"),
    PROGRAM_STAFF("program_label", "assistantCoach"),
    //GAME_STAFF("game_staff_label", "gameStaff"),
    //FAN("fan_label", "fan")//Add items to add in the usertypes in the app.
    /*{ key: "Program Manager", value: "progranManager"},
  { key: "Coach", value: "coach"},
  { key: "Assistant Coach", value: "assistantCoach"},
  { key: "Player", value: "player"},
  { key: "Guardian", value: "guardian"}*/
}

enum class SizeList(val stringId: String, val key: String) {
    YouthXS("youth_xs", "YouthXS"),
    YouthS("youth_s", "YouthS"),
    YouthM("youth_m", "YouthM"),
    YouthL("youth_l", "YouthL"),
    YouthXL("youth_xl", "YouthXL"),
    AdultS("adult_s", "AdultS"),
    AdultM("adult_m", "AdultM"),
    AdultL("adult_l", "AdultL"),
    AdultXL("adult_xl", "AdultXL")
}

enum class EventType(val key: String) {
    GAME("game")
}

@Composable
fun ShimmerItem(padding: PaddingValues = PaddingValues(0.dp)) {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = ColorBWGrayLight.copy(alpha = 0.1f),
        targetValue = ColorBWGrayLight.copy(alpha = 0.4f),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .padding(padding)
            .background(color, RoundedCornerShape(50))
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_24dp))
    )
}