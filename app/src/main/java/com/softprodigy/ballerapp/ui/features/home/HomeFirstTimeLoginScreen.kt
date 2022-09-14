package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.HomeItemResponse
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ButtonWithLeadingIcon
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun HomeFirstTimeLoginScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreateTeamClick: () -> Unit
) {

    val state = viewModel.state.value
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val color = dataStoreManager.getColor.collectAsState(initial = "0177C1")
    Box {
        CoachFlowBackground(colorCode = color.value.ifEmpty { "0177C1" }, teamLogo = "")
        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_50dp)))
            AppText(
                text = stringResource(id = R.string.hey_label).replace("name", "George"),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W500,
                color = ColorBWBlack
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

            AppText(
                text = stringResource(id = R.string.welcome_to_total_hoop),
                fontWeight = FontWeight.W600,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.appColors.material.primaryVariant
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                        ),

                    ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_ball),
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_48dp))
                            )

                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            AppText(
                                text = stringResource(id = R.string.team_total_hoop),
                                style = MaterialTheme.typography.h3,
                                fontWeight = FontWeight.W700,
                                color = ColorBWBlack
                            )

                        }

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = ColorGreyLighter
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                LazyColumn {
                    items(state.homeItemList) {
                        HomeScreenItem(it)
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))

                ButtonWithLeadingIcon(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.create_new_team),
                    onClick = onCreateTeamClick,
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    isTransParent = false,
                    iconSize = dimensionResource(id = R.dimen.size_20dp)
                )
            }
        }
    }
}

@Composable
fun HomeScreenItem(data: HomeItemResponse) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
            ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_72dp))
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    top = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_24dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = data.image!!),
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_16dp)),
                    colorFilter = ColorFilter.tint(MaterialTheme.appColors.material.primaryVariant)
                )

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))

                AppText(
                    text = data.item,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.W700,
                    color = ColorBWBlack
                )
            }

            AppText(
                text = data.total,
                fontWeight = FontWeight.W300,
                fontSize = dimensionResource(id = R.dimen.txt_size_36).value.sp,
                color = ColorBWBlack,
                style = MaterialTheme.typography.h1
            )
        }

    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
}