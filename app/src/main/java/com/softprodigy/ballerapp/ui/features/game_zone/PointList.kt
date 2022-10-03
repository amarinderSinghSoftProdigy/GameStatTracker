package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.features.components.Placeholder
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun PointList (
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.game_center_background_color))
            //.background(Color.Red)
    ) {

        Column(
            Modifier
                .fillMaxHeight()
        ) {
            var items = stringArrayResource(id = R.array.game_periods);
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(items) { index, point ->
                    pointListItem(
                        index,
                        point = point,
                    )
                }
            }
        }
    }
}

@Composable
fun pointListItem(
    index: Int,
    point: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.size_46dp))
                .clickable { },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .padding(
                        PaddingValues(
                            dimensionResource(id = R.dimen.size_12dp),
                            dimensionResource(id = R.dimen.size_12dp)
                        )
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                AppText(
                    text = index.toString(),
                    color = colorResource(id = R.color.game_point_list_item_text_color),
                    fontSize = dimensionResource(id = R.dimen.txt_size_11).value.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = rubikFamily,
                    modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp))
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))

                CoilImage(
                    //src = BuildConfig.IMAGE_SERVER + "URL.........",
                    src = "https://images.unsplash.com/profile-1446404465118-3a53b909cc82?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=27a346c2362207494baa7b76f5d606e5",
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_24dp))
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.appColors.material.onSurface,
                            CircleShape
                        ),
                    onError = {
                        Placeholder(R.drawable.ic_team_placeholder)
                    },
                    onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                    isCrossFadeEnabled = false
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                Text(
                    text = "Player Name",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = colorResource(id = R.color.game_point_list_item_name_text_color),
                    fontWeight = FontWeight.W500,
                    fontFamily = rubikFamily,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
                Text(
                    text = "scores 2 points",
                    fontSize = dimensionResource(id = R.dimen.txt_size_11).value.sp,
                    color = colorResource(id = R.color.game_point_list_item_name_text_color),
                    fontWeight = FontWeight.W400,
                    fontFamily = rubikFamily,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_4dp)))
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                Text(
                    text = "10",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = colorResource(id = R.color.game_point_list_item_text_color),
                    fontWeight = FontWeight.W500,
                    fontFamily = rubikFamily,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = ":",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = colorResource(id = R.color.game_point_list_item_text_color),
                    fontWeight = FontWeight.W500,
                    fontFamily = rubikFamily,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                Text(
                    text = "0",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = colorResource(id = R.color.game_point_list_item_text_color),
                    fontWeight = FontWeight.W500,
                    fontFamily = rubikFamily,
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                ImageButton(
                    icon = painterResource(id = R.drawable.ic_menu),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.size_16dp))
                        .height(dimensionResource(id = R.dimen.size_16dp)),
                    onClick = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_6dp)))

            }

        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_1dp))
            .background(colorResource(id = R.color.game_center_list_item_divider_color)))
    }
}