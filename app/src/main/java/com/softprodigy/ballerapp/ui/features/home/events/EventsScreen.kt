package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.components.DeclineEventDialog
import com.softprodigy.ballerapp.ui.features.components.SwitchTeamDialog
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorButtonGreen
import com.softprodigy.ballerapp.ui.theme.ColorButtonRed
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun EventsScreen(
    vm: EventViewModel = hiltViewModel(),
    moveToDetail: () -> Unit,
    moveToPracticeDetail: (String) -> Unit, moveToGameDetail: (String) -> Unit,
    moveToLeague: () -> Unit,
    moveToOppDetails: () -> Unit,
    setIndex: (index:Int) -> Unit
) {
    val state = vm.eventState.value
    Box(Modifier.fillMaxSize()) {
        TabLayout(moveToDetail, state, vm, moveToPracticeDetail, moveToGameDetail, moveToLeague,moveToOppDetails,setIndex)
    }
}

// on below line we are creating a
// composable function for our tab layout
@ExperimentalPagerApi
@Composable
fun TabLayout(
    moveToDetail: () -> Unit,
    state: EventState,
    vm: EventViewModel,
    moveToPracticeDetail: (String) -> Unit,
    moveToGameDetail: (String) -> Unit,
    moveToLeague: () -> Unit,
    moveToOppDetails: () -> Unit,
    setIndex: (index:Int) -> Unit
) {
    // on below line we are creating variable for pager state.
    val pagerState = rememberPagerState(pageCount = 3) // Add the count for number of pages
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Tabs(pagerState = pagerState,setIndex)
        TabsContent(
            pagerState = pagerState,
            state,
            vm,
            moveToDetail,
            moveToPracticeDetail,
            moveToGameDetail,
            moveToLeague,
            moveToOppDetails
        )
    }
}

// on below line we are
// creating a function for tabs
@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState,setIndex: (index:Int) -> Unit) {
    val list = listOf(
        TabItems.Events,
        TabItems.Leagues,
        TabItems.Opportunity,
    )
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = dimensionResource(id = R.dimen.size_2dp),
                color = MaterialTheme.appColors.material.primaryVariant
            )
        }
    ) {
        list.forEachIndexed { index, item ->
            Tab(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = null,
                            tint = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5dp)))
                        Text(
                            stringResourceByName(name = item.stringId),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = if (pagerState.currentPage == index) MaterialTheme.appColors.material.primaryVariant else MaterialTheme.appColors.textField.label
                        )
                    }
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        setIndex(index)
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

// on below line we are creating a tab content method
// in which we will be displaying the individual page of our tab .
@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    state: EventState,
    vm: EventViewModel,
    moveToDetail: () -> Unit,
    moveToPracticeDetail: (String) -> Unit, moveToGameDetail: (String) -> Unit,
    moveToLeague: () -> Unit,
    moveToOppDetails: () -> Unit
) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> {
                MyEvents(state, vm, moveToPracticeDetail, moveToGameDetail)
            }
            1 -> {
                MyLeagueScreen(state, vm, moveToDetail, moveToLeague)
            }
            2 -> {
                OpportunitieScreen(state, vm, moveToOppDetails)
            }
        }
    }
}


// on below line we are creating a Tab Content
// Screen for displaying a simple text message.
@Composable
fun BoxScope.MyEvents(
    state: EventState,
    vm: EventViewModel,
    moveToPracticeDetail: (String) -> Unit,
    moveToGameDetail: (String) -> Unit
) {
    if (state.currentEvents.size > 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                var text = buildAnnotatedString {
                    append(stringResource(id = R.string.upcoming_Events))
                    val startIndex = length
                    append(" ( ")
                    append("" + state.currentEvents.size)
                    append(" )")
                    addStyle(
                        SpanStyle(color = MaterialTheme.appColors.textField.label),
                        startIndex,
                        length,
                    )
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.h2,
                    color = ColorBWBlack,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                FlowRow(Modifier.fillMaxWidth()) {
                    state.currentEvents.forEach {
                        EventItem(events = it, onAcceptCLick = {
                            vm.onEvent(EvEvents.OnGoingCLick(it))
                        }, onDeclineCLick = {
                            vm.onEvent(EvEvents.OnDeclineCLick(it))
                        }, moveToPracticeDetail = moveToPracticeDetail,
                            moveToGameDetail = moveToGameDetail,
                            isPast = false
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_18dp)))
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.past_event))
                    val startIndex = length
                    append(" ( ")
                    append("" + state.pastEvents.size)
                    append(" )")
                    addStyle(
                        SpanStyle(color = MaterialTheme.appColors.textField.label),
                        startIndex,
                        length,
                    )
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.h2,
                    color = ColorBWBlack,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_6dp)))
                Box {
                    FlowRow(Modifier.fillMaxWidth()) {
                        state.pastEvents.forEach {
                            EventItem(events = it, onAcceptCLick = {
                            }, onDeclineCLick = {
                                vm.onEvent(EvEvents.OnDeclineCLick(it))
                            }, moveToPracticeDetail = moveToPracticeDetail,
                                moveToGameDetail = moveToGameDetail,
                                isPast = true
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
            }

            if (state.showGoingDialog) {
                val teams: ArrayList<Team> = arrayListOf(
                    Team(name = "Springfield Bucks"),
                    Team(name = "Springfield Sprouts"),
                )
                SwitchTeamDialog(
                    teams = teams,
                    title = stringResource(id = R.string.switch_teams),
                    onDismiss = {
                        vm.onEvent(EvEvents.onCancel(false))
                    },
                    onConfirmClick = {
                    }
                )
            }
            if (state.showDeclineDialog) {
                DeclineEventDialog(
                    title = stringResource(id = R.string.decline_head),
                    onDismiss = {
                        vm.onEvent(EvEvents.onCancelDeclineDialog(false))
                    },
                    onConfirmClick = {

                    },
                    onReasonChange = {

                    },
                    reason = ""
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_events_large),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.no_upcoming_events),
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            Text(
                color = MaterialTheme.appColors.textField.label,
                text = stringResource(id = R.string.add_events_to_use_app),
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            /* LeadingIconAppButton(
             icon = painterResource(id = R.drawable.ic_add_player),
             text = stringResource(id = R.string.add_events),
             onClick = {},
         )*/
        }
    }
}

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    events: Events,
    onAcceptCLick: (Events) -> Unit,
    onDeclineCLick: (Events) -> Unit,
    moveToPracticeDetail: (String) -> Unit,
    moveToGameDetail: (String) -> Unit,
    isPast: Boolean
) {
    Box(
        modifier = modifier
            .clickable {
                if (events.type!!.type == EventType.PRACTICE.type)
                    moveToPracticeDetail(events.title)
                else if (events.type.type == EventType.GAME.type) {
                    moveToGameDetail(events.title)
                }
            }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .alpha(if (isPast) 0.5F else 1F)
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.appColors.material.surface,
                        shape = RoundedCornerShape(
                            topStart = dimensionResource(id = R.dimen.size_8dp),
                            topEnd = dimensionResource(id = R.dimen.size_8dp),
                            bottomEnd = if (isPast) dimensionResource(id = R.dimen.size_8dp) else 0.dp,
                            bottomStart = if (isPast) dimensionResource(id = R.dimen.size_8dp) else 0.dp,
                        )
                    )
                    .padding(
                        dimensionResource(id = R.dimen.size_16dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = events.title,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(color = events.type!!.color)
                                .padding(dimensionResource(id = R.dimen.size_4dp))
                        ) {
                            Text(
                                text = events.type.type,
                                color = Color.White,
                                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = events.venue,
                            color = MaterialTheme.appColors.textField.label,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                        )
                        Text(
                            text = events.date,
                            color = MaterialTheme.appColors.textField.label,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                        )
                    }

                }
            }
            if (events.status.equals(EventStatus.PENDING.status, ignoreCase = true)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.appColors.buttonColor.bckgroundDisabled,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .clickable {
                                onDeclineCLick.invoke(events)
                            }
                            .padding(dimensionResource(id = R.dimen.size_14dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross_2),
                            contentDescription = "",
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        Text(
                            text = stringResource(id = R.string.decline),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,

                            )
                    }

                    Row(
                        Modifier
                            .weight(1f)
                            .clickable {
                                onAcceptCLick.invoke(events)
                            }
                            .padding(dimensionResource(id = R.dimen.size_14dp)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = "",
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                            tint = MaterialTheme.appColors.material.primaryVariant
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                        Text(
                            text = stringResource(id = R.string.accept),
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            fontWeight = FontWeight.W500,
                        )
                    }

                }
            } else if (events.status.equals(EventStatus.ACCEPT.status, ignoreCase = true)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = ColorButtonGreen,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        )
                        .clickable {

                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                        tint = MaterialTheme.appColors.buttonColor.textEnabled,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = stringResource(id = R.string.going),
                        color = MaterialTheme.appColors.buttonColor.textEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }
            } else if (events.status.equals(EventStatus.REJECT.status, ignoreCase = true)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color = ColorButtonRed,
                            shape = RoundedCornerShape(
                                bottomStart = dimensionResource(
                                    id = R.dimen.size_8dp
                                ),
                                bottomEnd = dimensionResource(id = R.dimen.size_8dp)
                            )
                        )
                        .clickable {

                        }
                        .padding(dimensionResource(id = R.dimen.size_14dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross_2),
                        contentDescription = "",
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_6dp)),
                        tint = MaterialTheme.appColors.buttonColor.textEnabled,
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))
                    Text(
                        text = stringResource(id = R.string.not_going),
                        color = MaterialTheme.appColors.buttonColor.textEnabled,
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                        fontWeight = FontWeight.W500,
                    )
                }
            }

        }
        /* if (isPast)
             Box(
                 modifier = Modifier
                     .matchParentSize()
                     .background(color = BackgroundColor)
             )*/
    }

}

enum class TabItems(val icon: Int, val stringId: String) {
    Events(R.drawable.ic_events, stringId = "my_events"),
    Leagues(R.drawable.ic_leagues, stringId = "my_leagues"),
    Opportunity(R.drawable.ic_star, stringId = "opportunities")
}