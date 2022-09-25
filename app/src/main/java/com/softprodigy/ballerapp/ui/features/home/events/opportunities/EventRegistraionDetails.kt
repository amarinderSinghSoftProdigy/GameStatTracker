package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppButton
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CustomCheckBox
import com.softprodigy.ballerapp.ui.features.components.DividerCommon
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorGreyLighter
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.md_theme_light_onSurface

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EventRegistraionDetails(vm: EventViewModel, onSuccess: () -> Unit) {
    val state = vm.eventState.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Heading(stringResource(id = R.string.team_info))
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.size_56dp))
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.team),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.user_demo),
                        contentDescription = "",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_32dp))
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10dp)))
                    Text(
                        text = "Springfield Bucks",
                        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                        fontWeight = FontWeight.W500,
                    )

                }
                DividerCommon()
                RegisterItem(stringResource(id = R.string.age_group), "10 - 12")
                DividerCommon()
                RegisterItem(
                    stringResource(id = R.string.players),
                    stringResource(id = R.string.all)
                )
                DividerCommon()
                RegisterItem(
                    stringResource(id = R.string.payment_options),
                    stringResource(id = R.string.venmo)
                )
                AppOutlineTextField(
                    value = "",
                    onValueChange = {
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    placeholder = {
                        AppText(
                            text = stringResource(id = R.string.venmo_hanle),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = ColorBWGrayBorder,
                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                    ),
                    errorMessage = stringResource(id = R.string.valid_team_name)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        }
        Heading(stringResource(id = R.string.division))
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterItem(
                    stringResource(id = R.string.division),
                    "Division 1"
                )
                DividerCommon()
                Row(
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.size_56dp))
                        .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = stringResource(id = R.string.send_push_notification),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack,
                        modifier = Modifier
                            .weight(1f)
                    )
                    Switch(
                        checked = true,
                        onCheckedChange = { },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.appColors.material.primaryVariant
                        )
                    )
                }

            }
        }
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                CustomCheckBox(
                    true
                ) { }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = stringResource(id = R.string.i_agree) + " ",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = md_theme_light_onSurface,
                )
                AppText(
                    text = stringResource(id = R.string.terms_cond),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = ColorMainPrimary,
                )
            }
            DividerCommon()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimensionResource(id = R.dimen.size_16dp)),
            ) {
                CustomCheckBox(
                    true
                ) { }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = stringResource(id = R.string.privacy),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = ColorMainPrimary,
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))

        AppButton(
            enabled = true,
            icon = null,
            themed = true,
            onClick = { onSuccess() },
            text = stringResource(id = R.string.procced),
            isForceEnableNeeded = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_24dp)))
    }
}

@Composable
fun Heading(title: String) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {
        Text(
            text = title,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun RegisterItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_56dp))
            .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = title,
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            modifier = Modifier
                .weight(1f)
        )
        AppText(
            text = value,
            style = MaterialTheme.typography.h6,
            color = ColorBWBlack,
            fontSize = dimensionResource(R.dimen.txt_size_14).value.sp,
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
        Icon(
            modifier = Modifier
                .clickable {
                },
            painter = painterResource(id = R.drawable.ic_forward),
            contentDescription = "", tint = ColorGreyLighter
        )
    }
}