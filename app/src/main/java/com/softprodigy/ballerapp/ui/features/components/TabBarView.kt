package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors


/**
 * composable for bottom navigation item
 */
@Composable
fun BottomNavigationBar(
    navKey: BottomNavKey,
    navController: NavController,
    height: Dp = dimensionResource(id = R.dimen.size_64dp),
    selectionColor: Color = MaterialTheme.appColors.material.primaryVariant,
    selectedValue: (BottomNavKey) -> Unit
) {
    val selected: MutableState<BottomNavKey> = remember { mutableStateOf(navKey) }
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
                ) {
                    Box(
                        modifier = Modifier
                            .height(height)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                selected.value = item.key
                                selectedValue(item.key)
                                navController.navigate(item.key.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Column {
                            Icon(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                painter = painterResource(id = item.icon),
                                contentDescription = null,
                                tint = if (selected.value == item.key) selectionColor else ColorGreyLighter
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center,
                                text = stringResourceByName(item.key.resId),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                color = if (selected.value == item.key) selectionColor else ColorGreyLighter,
                                fontWeight = FontWeight.W700
                            )
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
    REFEREE("referee_label", "referee"),
    PARENT("parent_label", "parent"),
    GAME_STAFF("game_staff_label", "gameStaff"),
    PROGRAM_STAFF("program_label", "programStaff"),
    FAN("fan_label", "fan")//Add items to add in the usertypes in the app.
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