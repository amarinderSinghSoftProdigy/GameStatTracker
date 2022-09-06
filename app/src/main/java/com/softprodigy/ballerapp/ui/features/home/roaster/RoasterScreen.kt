package com.softprodigy.ballerapp.ui.features.home.roaster

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.roaster.PlayerDetail
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RoasterScreen(roasterViewModel: RoasterViewModel = hiltViewModel()) {

    val state = roasterViewModel.roasterUIState.value
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        roasterViewModel.roasterChannel.collect { uiEvent ->
            when (uiEvent) {
                is RoasterChannel.ShowToast -> {
                    Toast.makeText(
                        context,
                        uiEvent.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_20dp))
        ) {

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                state.coachList.forEach {
                    CoachListItem(
                        roaster = it,
                        modifier = Modifier
                            .padding(
                                bottom = dimensionResource(
                                    id = R.dimen.size_16dp
                                )
                            )
                            .weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_30dp)))
            CompositionLocalProvider(
                LocalOverScrollConfiguration provides null
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    content = {

                        items(state.playerList) {
                            CoachListItem(
                                roaster = it, modifier = Modifier.padding(
                                    bottom = dimensionResource(
                                        id = R.dimen.size_16dp
                                    )
                                )
                            )
                        }
                    })
            }
        }
    }
}

@Composable
fun CoachListItem(roaster: PlayerDetail, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = if (roaster.profileImage == "") painterResource(id = R.drawable.ball) else rememberAsyncImagePainter(
                model = roaster.profileImage
            ),
            contentDescription = "",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_80dp))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

        AppText(
            text = roaster.firstName,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
            style = MaterialTheme.typography.h6
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (roaster.role != "player") {
                AppText(
                    text = roaster.role,
                    color = ColorMainPrimary,
                    style = MaterialTheme.typography.h6
                )
            } else {
                AppText(
                    text = roaster.position,
                    color = ColorMainPrimary,
                    style = MaterialTheme.typography.h6
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

                AppText(
                    text = roaster.jerseyNumber,
                    color = MaterialTheme.appColors.textField.label,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}
