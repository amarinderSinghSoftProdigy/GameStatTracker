package com.softprodigy.ballerapp.ui.features.home.manage_team.leaderboard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.utils.dragDrop.ReorderableItem
import com.softprodigy.ballerapp.ui.utils.dragDrop.detectReorderAfterLongPress
import com.softprodigy.ballerapp.ui.utils.dragDrop.rememberReorderableLazyListState
import com.softprodigy.ballerapp.ui.utils.dragDrop.reorderable

@Composable
fun ManageTeamLeaderBoard(vm: ManageLeaderBoardViewModel = hiltViewModel()) {

    val state = vm.manageLeaderBoardUiState.value

    val leaderBoardList = remember {
        mutableStateOf(state.leaderBoardList)
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

                AppText(
                    text = stringResource(id = R.string.select_all),
                    style = MaterialTheme.typography.h4,
                    fontSize = dimensionResource(id = R.dimen.txt_size_10).value.sp,
                    color = ColorBWGrayLight,
                    modifier = Modifier.clickable {
                        vm.updateSelection("All")
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            val recordState =
                rememberReorderableLazyListState(
                    onMove = vm::moveItem,
                    canDragOver = vm::isDragEnabled
                )
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
                items(state.leaderBoardList, { item -> item.name }) { item ->
                    ReorderableItem(reorderableState = recordState, key = item.name) { dragging ->
                        val elevation =
                            animateDpAsState(if (dragging) dimensionResource(id = R.dimen.size_10dp) else 0.dp)
                        LeaderBoardItem(
                            modifier = Modifier
                                .shadow(elevation.value),
                            item.name,
                            selected = state.selected.contains(item.name)
                        ) {
                            vm.updateSelection(item.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderBoardItem(
    modifier: Modifier = Modifier,
    points: String,
    selected: Boolean,
    onSelectionChange: () -> Unit,
) {

    Row(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp)
            )
            .height(dimensionResource(id = R.dimen.size_48dp))
            .clickable {

            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)))
                    .size(
                        dimensionResource(id = R.dimen.size_16dp)
                    )
                    .background(color = if (selected) MaterialTheme.appColors.material.primaryVariant else Color.White)
                    .border(
                        width = if (!selected) dimensionResource(id = R.dimen.size_1dp) else 0.dp,
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_4dp)),
                        color = if (!selected) MaterialTheme.appColors.buttonColor.bckgroundDisabled else Color.Transparent
                    )
                    .clickable { onSelectionChange() }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

            AppText(
                text = points.capitalize(),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.h6
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_nav),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_12dp))
        )
    }

    Divider(color = MaterialTheme.colors.primary)
}
