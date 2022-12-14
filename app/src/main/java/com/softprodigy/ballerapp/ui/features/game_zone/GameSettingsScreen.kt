package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.GameSettingsDialog
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.rubikFamily



@Composable
fun GameSettingsScreen(
    onCloseSettings: () -> Unit
)  {
    var isPeriodSelected = remember { mutableStateOf(false) }
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
            GameSettingsNavigation(onCloseSettings)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_1dp))
                .background(colorResource(id = R.color.game_settings_divider_color))
            )
            GameSettingViews(onPeriodListener = { isPeriodSelected.value = !isPeriodSelected.value })
        }
        if(isPeriodSelected.value) {
            GameSettingsDialog(onDismiss = { isPeriodSelected.value = false })
        }
    }
}

@Composable
private fun GameSettingsNavigation(onCloseSettings: () -> Unit) {
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
                .align(Alignment.TopEnd)
                .clickable { onCloseSettings.invoke() },
            verticalArrangement = Arrangement.Center
        ) {
            ImageButton(
                icon = painterResource(id = R.drawable.ic_wrong_white),
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.size_24dp))
                    .height(dimensionResource(id = R.dimen.size_24dp)),
                onClick = { onCloseSettings.invoke() },
            )
        }
    }
}

@Composable
private fun GameSettingViews(onPeriodListener: () -> Unit) {
    Row(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.size_24dp))
            .verticalScroll(rememberScrollState())
            .padding(bottom = dimensionResource(id = R.dimen.size_24dp))
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
            )

        ) {
            settingPeriod(placeHolder = stringResource(id = R.string.periods), value = "4 Quarters","Error message",onValueChangeListener = { }, onPeriodListener = onPeriodListener )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.period_length), value = "12 min", "Error message", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.free_throws), value = "Standard", "Error message", onValueChangeListener = { })

        }
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
            )

        ) {
            settingSubTitle(placeHolder = stringResource(id = R.string.timeouts), value = "Per Half", "Error message", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.full_timeouts_per), value = "12", "Error message", onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.thirty_sec_timeouts_per), value = "7", "Error message", onValueChangeListener = { })

        }
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
            )

        ) {
            settingSubTitle(placeHolder = stringResource(id = R.string.halftime_length), value = "4 Mins", "Error message",onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.shot_clock), value = "12 s", "Error message",onValueChangeListener = { })
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_32dp)))
            settingSubTitle(placeHolder = stringResource(id = R.string.fouling_out), value = "5", "Error message", onValueChangeListener = { })

        }
    }
}

@Composable
private fun settingPeriod(placeHolder:String? = "", value: String? = "",errorMessage: String, onValueChangeListener: (String) -> Unit, onPeriodListener: () -> Unit, ){
//    AppText(
//        text = placeHolder!!,
//        color = colorResource(id = R.color.game_settings_title_color),
//        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
//        maxLines = 1,
//        fontFamily = rubikFamily,
//        fontWeight = FontWeight.W500,
//    )
//    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(dimensionResource(id = R.dimen.size_44dp))
//            .clip(RoundedCornerShape(size = dimensionResource(id = R.dimen.size_8dp)))
//            .background(color = colorResource(id = R.color.game_settings_input_field_bg_color))
//            .clickable { onPeriodListener.invoke() },
//        verticalArrangement = Arrangement.Center,
//    ) {
//        AppText(
//            text = placeHolder,
//            textAlign = TextAlign.Center,
//            fontWeight = FontWeight.W500,
//            fontSize = dimensionResource(id = R.dimen.size_14dp).value.sp,
//            color = Color.White,
//            fontFamily = rubikFamily,
//            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
//        )
//    }



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
            value = placeHolder,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onPeriodListener.invoke()
                }
            /*.height(dimensionResource(id = R.dimen.size_44dp))*/,
            onValueChange = onValueChangeListener,
            readOnly = true,
            enabled = false,
            placeholder = {},
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            errorMessage = errorMessage,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.game_settings_input_field_bg_color),
                unfocusedBorderColor = colorResource(id = R.color.game_settings_input_field_bg_color),
                backgroundColor = colorResource(id = R.color.game_settings_input_field_bg_color),
                textColor = Color.White,
                placeholderColor = MaterialTheme.appColors.textField.label,
                cursorColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.size_14dp).value.sp,
                fontFamily = rubikFamily,
                fontWeight = FontWeight.W500,
            ),
            isError = false,
        )
}

@Composable
private fun settingSubTitle(placeHolder:String? = "", value: String? = "", errorMessage: String, onValueChangeListener: (String) -> Unit){
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
        modifier = Modifier
            .fillMaxWidth()
            /*.height(dimensionResource(id = R.dimen.size_44dp))*/,
        onValueChange = onValueChangeListener,
        placeholder = {},
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences
        ),
        errorMessage = errorMessage,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.game_settings_input_field_bg_color),
            unfocusedBorderColor = colorResource(id = R.color.game_settings_input_field_bg_color),
            backgroundColor = colorResource(id = R.color.game_settings_input_field_bg_color),
            textColor = Color.White,
            placeholderColor = MaterialTheme.appColors.textField.label,
            cursorColor = Color.White
        ),
        textStyle = LocalTextStyle.current.copy(
            color = Color.White,
            fontSize = dimensionResource(id = R.dimen.size_14dp).value.sp,
            fontFamily = rubikFamily,
            fontWeight = FontWeight.W500,
        ),
        isError = (value.isNotEmpty() && value.isEmpty()),
    )
}