package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Standing
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.theme.rubikFamily


@Composable
fun GameSettingsScreen()  {
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
            gameSettingsNavigation()
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_1dp))
                .background(colorResource(id = R.color.game_settings_divider_color))
            )
            gameSettingViews()
        }
    }
}

@Composable
private fun gameSettingsNavigation() {
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
                text = stringResource(id = R.string.game_settings),
                color = colorResource(id = R.color.game_settings_title_color),
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.W500,
                modifier = Modifier.fillMaxWidth()
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
private fun gameSettingViews() {
    Row(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.size_24dp))
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_197dp))
            .fillMaxHeight()
            .padding(
                start = 0.dp,
                top = dimensionResource(id = R.dimen.size_32dp),
                end = 0.dp,
                bottom = 0.dp
            )) {
            settingSubTitle(placeHolder = stringResource(id = R.string.periods), value = "4 Quarters", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.period_length), value = "12 min", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.free_throws), value = "Standard", onValueChangeListener = { })

        }

       /* Spacer(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_65dp))
            .fillMaxHeight()
            .padding(horizontal = dimensionResource(id = R.dimen.size_32dp))
            .background(color =colorResource(id = R.color.game_settings_divider_color))
        )*/
        Spacer(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_1dp))
            .fillMaxHeight()
            //.padding(horizontal = dimensionResource(id = R.dimen.size_32dp))
            .background(color = colorResource(id = R.color.game_settings_divider_color))
        )

        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_197dp))
            .fillMaxHeight()
            .padding(
                start = 0.dp,
                top = dimensionResource(id = R.dimen.size_32dp),
                end = 0.dp,
                bottom = 0.dp
            )) {
            settingSubTitle(placeHolder = stringResource(id = R.string.timeouts), value = "Per Half", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.full_timeouts_per), value = "12", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.thirty_sec_timeouts_per), value = "7", onValueChangeListener = { })

        }
        /*Spacer(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_65dp))
            .fillMaxHeight()
            .padding(horizontal = dimensionResource(id = R.dimen.size_32dp))
            .background(color =colorResource(id = R.color.game_settings_divider_color))
        )*/
        Spacer(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_1dp))
            .fillMaxHeight()
            //.padding(horizontal = dimensionResource(id = R.dimen.size_32dp))
            .background(color = colorResource(id = R.color.game_settings_divider_color))
        )
        Column(modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_197dp))
            .fillMaxHeight()
            .padding(
                start = 0.dp,
                top = dimensionResource(id = R.dimen.size_32dp),
                end = 0.dp,
                bottom = 0.dp
            )) {
            settingSubTitle(placeHolder = stringResource(id = R.string.halftime_length), value = "4 Mins", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.shot_clock), value = "12 s", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.fouling_out), value = "5", onValueChangeListener = { })

        }
    }
}

@Composable
private fun settingSubTitle(placeHolder:String? = "", value: String? = "", onValueChangeListener: (String) -> Unit, ){
    AppText(
        text = placeHolder!!,
        color = colorResource(id = R.color.game_settings_title_color),
        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
        maxLines = 1,
        fontFamily = rubikFamily,
        fontWeight = FontWeight.W500,
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
    AppOutlineTextField(
        value = value!!,
        onValueChange = { onValueChangeListener },
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_44dp))
            .background(
                color = colorResource(id = R.color.game_settings_input_field_bg_color),
                RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
            ),

    )
}