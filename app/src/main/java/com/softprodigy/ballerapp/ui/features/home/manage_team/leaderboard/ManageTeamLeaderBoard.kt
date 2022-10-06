package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.dragDrop.ReorderableItem
import com.softprodigy.ballerapp.ui.utils.dragDrop.detectReorderAfterLongPress
import com.softprodigy.ballerapp.ui.utils.dragDrop.rememberReorderableLazyListState
import com.softprodigy.ballerapp.ui.utils.dragDrop.reorderable

@Composable
fun ManageTeamLeaderBoard(vm: TeamViewModel) {

    val state = vm.teamUiState.value
    val leaderBoard = state.leaderBoard
    val recordState =
        rememberReorderableLazyListState(
            onMove = vm::moveItem,
            canDragOver = vm::isDragEnabled
        )
    val onLeaderSelectionChange = { leader: TeamLeaderBoard ->
        vm.onEvent(TeamUIEvent.OnItemSelected(leader.name))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.size_16dp),
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppText(
                    text = stringResource(id = R.string.leaderboards),
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.W500,
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )

                Row(
                    modifier = Modifier.clickable {
                        onLeaderSelectionChange.invoke(TeamLeaderBoard(name = if (!state.all) "All" else ""))
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
                            .size(
                                dimensionResource(id = R.dimen.size_16dp)
                            )
                            .background(color = if (state.all) MaterialTheme.appColors.material.primaryVariant else Color.White)
                            .border(
                                width = if (!state.all) dimensionResource(id = R.dimen.size_1dp) else 0.dp,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                                color = if (!state.all) MaterialTheme.appColors.buttonColor.bckgroundDisabled else Color.Transparent
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                        )
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                    AppText(
                        text = stringResource(id = R.string.select_all),
                        color = if (state.all) MaterialTheme.appColors.buttonColor.bckgroundEnabled else ColorGreyLighter,
                        style = if (state.all) MaterialTheme.typography.h6 else MaterialTheme.typography.h4,
                        fontSize = dimensionResource(id = R.dimen.txt_size_10).value.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            LazyColumn(
                state = recordState.listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                    )
                    .then(
                        Modifier
                            .reorderable(recordState)
                            .detectReorderAfterLongPress(recordState)
                    ),

                ) {
                itemsIndexed(leaderBoard, key = { index, item -> item.name }) { index, item ->
                    val status = item.status
                    ReorderableItem(
                        reorderableState = recordState,
                        key = item.name,
                    ) { dragging ->
                        val elevation =
                            animateDpAsState(if (dragging) dimensionResource(id = R.dimen.size_10dp) else 0.dp)
                        LeaderBoardItem(
                            dragging = dragging,
                            modifier = Modifier
                                .shadow(elevation.value),
                            item = item,
                            status = status,
                            all = state.all,
                        ) {
                            vm.onEvent(TeamUIEvent.OnItemSelected(it.name))
                        }
                    }
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
            )
        }
    }
}

@Composable
inline fun LeaderBoardItem(
    modifier: Modifier = Modifier,
    dragging: Boolean = false,
    item: TeamLeaderBoard,
    status: Boolean,
    all: Boolean,
    crossinline onSelectionChange: (TeamLeaderBoard) -> Unit,
) {
    val selection = remember {
        mutableStateOf(item.status)
    }

    Row(
        modifier = modifier
            .background(color = if (dragging) Color.White else Color.Transparent)
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            )
            .height(dimensionResource(id = R.dimen.size_48dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        selection.value = !selection.value
                        onSelectionChange(item)
                    }
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
                    .size(
                        dimensionResource(id = R.dimen.size_16dp)
                    )
                    .background(
                        color = if (all) {
                            MaterialTheme.appColors.material.primaryVariant
                        } else {
                            if (selection.value) {
                                MaterialTheme.appColors.material.primaryVariant
                            } else Color.White
                        }
                    )
                    .border(
                        width =
                        if (all) {
                            0.dp
                        } else {
                            if (selection.value) {
                                0.dp
                            } else dimensionResource(id = R.dimen.size_1dp)
                        },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                        color = if (selection.value) {
                            if (all)
                                Color.Transparent
                            else
                                MaterialTheme.appColors.buttonColor.bckgroundDisabled
                        } else MaterialTheme.appColors.buttonColor.bckgroundDisabled
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
            //Checkbox(checked = item.status, onCheckedChange = { onSelectionChange(item) })
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            AppText(
                text = item.name.capitalize(),
                color =
                if (all) {
                    MaterialTheme.appColors.buttonColor.bckgroundEnabled
                } else {
                    if (selection.value) {
                        MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    } else ColorGreyLighter
                },
                style = MaterialTheme.typography.h6
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_nav_icon),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_12dp))
        )
    }
    AppDivider(color = MaterialTheme.colors.primary)
}
