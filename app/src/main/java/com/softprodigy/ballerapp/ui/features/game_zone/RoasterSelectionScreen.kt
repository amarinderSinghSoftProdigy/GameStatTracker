package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.features.home.teams.roaster.CoachListItem


@Composable
fun RoasterSelectionScreen(
    onRoasterSelectionClose: () -> Unit
)  {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.game_bg_color))
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                ,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    //.background(Color.Transparent.copy(alpha = .75f)),

            ) {
                var profileImage = "https://images.unsplash.com/profile-1446404465118-3a53b909cc82?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=27a346c2362207494baa7b76f5d606e5"
                var playerList: ArrayList<Player> = ArrayList();
                playerList.add(Player(_id = "1", firstName = "Satish", position = "10", profileImage = profileImage))
                playerList.add(Player(_id = "2", firstName = "Satish", position = "10", profileImage = profileImage))
                playerList.add(Player(_id = "3", firstName = "Satish", position = "10", profileImage = profileImage))
                playerList.add(Player(_id = "4", firstName = "Satish", profileImage = profileImage))
                playerList.add(Player(_id = "5", firstName = "Satish", position = "10", profileImage = profileImage))
                playerList.add(Player(_id = "6", firstName = "Satish", profileImage = profileImage))
                playerList.add(Player(_id = "7", firstName = "Satish", position = "10", profileImage = profileImage))

                Column(modifier = Modifier.weight(1f)
                    .padding(
                        start = dimensionResource(id = R.dimen.size_32dp),
                        top = dimensionResource(id = R.dimen.size_24dp),
                        end = 0.dp,
                        bottom = 0.dp,
                    )) {
                    roasterGridView(players = playerList)
                }
                Spacer(modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_1dp))
                    .fillMaxHeight()
                    .background(
                        colorResource(id = R.color.game_roaster_divider_bg_color)
                    ))
                Column(modifier = Modifier.weight(1f)
                    .padding(
                        start = 0.dp,
                        top = dimensionResource(id = R.dimen.size_24dp),
                        end = dimensionResource(id = R.dimen.size_32dp),
                        bottom = 0.dp,
                    )) {
                    roasterGridView(players = playerList)
                }
            }
        }

        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_44dp))
            .height(dimensionResource(id = R.dimen.size_44dp))
            .align(Alignment.TopEnd)
            .padding(
                0.dp,
                top = dimensionResource(id = R.dimen.size_6dp),
                end = dimensionResource(id = R.dimen.size_6dp),
                0.dp,
            )
            .background(Color.Black, CircleShape),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageButton(
                icon = painterResource(id = R.drawable.ic_wrong_white),
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_24dp))
                    .height(dimensionResource(id = R.dimen.size_24dp)),
                onClick = { //onRoasterSelectionClose.invoke()

                    },
            )
        }
    }
}

@Composable
private fun roasterGridView(players: ArrayList<Player>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        content = {
            items(players) {
                CoachListItem(
                    isCoach = false,
                    isRoasterSelection = true,
                    player = it, modifier = Modifier.padding(
                        bottom = dimensionResource(
                            id = R.dimen.size_16dp
                        )
                    )
                )
            }
        }
    )
}
