package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.theme.rubikFamily


@Composable
fun TimeoutsScreen()  {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.game_bg_color))
    ) {

        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            gameTimeoutsNavigation()
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_1dp))
                .background(colorResource(id = R.color.game_settings_divider_color))
            )
            gameTimeoutsViews()
            timeoutDivider(stringResource(id = R.string.first_half))
            timeslotView()
            timeoutDivider(stringResource(id = R.string.second_half))
            timeslotView()
        }
    }
}

@Composable
private fun gameTimeoutsNavigation() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(dimensionResource(id = R.dimen.size_46dp)),
    contentAlignment = Alignment.CenterEnd
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            AppText(
                text = stringResource(id = R.string.timeouts),
                color = colorResource(id = R.color.game_settings_title_color),
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .fillMaxWidth()

            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = dimensionResource(id = R.dimen.size_10dp))
                .align(Alignment.TopEnd),
            verticalArrangement = Arrangement.Center
        ) {
            ImageButton(
                icon = painterResource(id = R.drawable.ic_wrong_white),
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_24dp))
                    .height(dimensionResource(id = R.dimen.size_24dp)),
                onClick = { /*TODO*/ },
            )
        }
    }
}

@Composable
private fun gameTimeoutsViews() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_56dp))
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_24dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            Modifier
                //.width(dimensionResource(id = R.dimen.size_384dp))
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            teamLogo();
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
            teamTitle(title = "My Team name")

            Column(horizontalAlignment = Alignment.End, modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(id = R.dimen.size_24dp))
            ) {
                AppText(
                    text = "1",
                    textAlign = TextAlign.End,
                    fontSize = dimensionResource(id = R.dimen.size_14dp).value.sp,
                    fontFamily = rubikFamily,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_1dp))
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.game_settings_divider_color)))
        Row(
            Modifier
                .fillMaxHeight()
                //.width(dimensionResource(id = R.dimen.size_384dp))
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.size_24dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            teamLogo()
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
            teamTitle( title = "Other Team name")
            Column(modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.size_8dp))
                .fillMaxWidth(1f),
                horizontalAlignment = Alignment.End,
            ) {
                AppText(
                    text = "1",
                    textAlign = TextAlign.End,
                    fontSize = dimensionResource(id = R.dimen.size_14dp).value.sp,
                    fontFamily = rubikFamily,
                    fontWeight = FontWeight.W400,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun timeoutDivider(subTitle:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(dimensionResource(id = R.dimen.size_36dp)),
    verticalAlignment = Alignment.CenterVertically
        ) {
        
        Spacer(modifier = Modifier
            .weight(1f)
            .height(dimensionResource(id = R.dimen.size_1dp))
            .background(color = colorResource(id = R.color.game_settings_divider_color)))
        AppText(
            text = subTitle,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_12dp)),
            fontWeight = FontWeight.W500,
            fontFamily = rubikFamily,
            fontSize = dimensionResource(id = R.dimen.size_12dp).value.sp
        )
        Spacer(modifier = Modifier
            .weight(1f)
            .height(dimensionResource(id = R.dimen.size_1dp))
            .background(color = colorResource(id = R.color.game_settings_divider_color)))
    }
}

@Composable
private fun timeslotView() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_88dp))
            .padding(
                horizontal = dimensionResource(id = R.dimen.size_24dp),
            ),
        verticalAlignment = Alignment.CenterVertically,

    ){
        Row(modifier = Modifier
            .weight(1f)
        ) {
            var timeouts = stringArrayResource(id = R.array.game_timeouts)
            LazyRow(
                modifier = Modifier.padding(
                    end = dimensionResource(id = R.dimen.size_24dp),
                    top = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_16dp)
                )
            ) {
                itemsIndexed(timeouts) { index, timeItem ->
                    timeListItem(index, timeItem)
                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxHeight()
            .width(dimensionResource(id = R.dimen.size_1dp))
            .background(color = colorResource(id = R.color.game_settings_divider_color))
        )
        Row(modifier = Modifier
            .weight(1f)
        ) {
            var timeouts = stringArrayResource(id = R.array.game_timeouts)
            LazyRow(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.size_24dp),
                    top = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_16dp)
                )
            ) {
                itemsIndexed(timeouts) { index, timeItem ->
                    timeListItem(index, timeItem)
                }
            }
        }
    }
}

@Composable
private fun timeListItem(index: Int, timeItem: String = "", selected: Boolean = true) {
    Row() {
        if (index != 0)
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

        Row(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.size_57dp))
                .height(dimensionResource(id = R.dimen.size_32dp))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp)))
                .background(
                    color = if (selected) colorResource(id = R.color.game_timeouts_slot_selected_bg_color)
                    else colorResource(id = R.color.game_timeouts_slot_bg_color)
                ),
            verticalAlignment = Alignment . CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AppText(
                text = timeItem,
                textAlign = TextAlign.Center,
                color = if (selected) colorResource(id = R.color.game_timeouts_slot_selected_text_color)
                    else colorResource(id = R.color.game_timeouts_slot_text_color),
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.W500,
            )
            if (!selected) {
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                Icon(
                    painter = painterResource(id = R.drawable.ic_correct_white),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.size_12dp))
                        .height(dimensionResource(id = R.dimen.size_12dp)),
                    contentDescription = ""
                )
            }
        }
    }
}