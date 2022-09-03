package com.softprodigy.ballerapp.ui.features.home.roaster

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Roaster
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoasterScreen(roasterViewModel: RoasterViewModel = hiltViewModel()) {

    val state = roasterViewModel.roasterUIState.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_27dp))
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.roasterCoachesList.forEach { roaster ->
                    CoachListItem(roaster = roaster, modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))

            LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                itemsIndexed(state.roasterTeamList, itemContent = { index, roaster ->
                    CoachListItem(
                        roaster = roaster, modifier = Modifier.padding(
                            all = dimensionResource(
                                id = R.dimen.size_10dp
                            )
                        )
                    )
                })
            })
        }
    }
}

@Composable
fun CoachListItem(roaster: Roaster, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.user_demo),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_80dp))
                .clip(CircleShape),
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        Row {
            AppText(
                text = roaster.name.toString(),
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                style = MaterialTheme.typography.body1
            )

            if (!roaster.tag.isNullOrEmpty()) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

                AppText(
                    text = roaster.tag.toString(),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}