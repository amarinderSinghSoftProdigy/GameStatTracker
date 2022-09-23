package com.softprodigy.ballerapp.ui.features.home.events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.validTeamName
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.theme.*

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EventRegistraionDetails(vm: EventViewModel) {
    val state = vm.eventState.value
    var firstName by rememberSaveable { mutableStateOf("George") }
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Heading(stringResource(id = R.string.events_info))
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
        Heading(stringResource(id = R.string.divisions))
        UserFlowBackground(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterItem(
                    stringResource(id = R.string.choose_division),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .background(color = Color.White)
                .padding(all = dimensionResource(id = R.dimen.size_16dp)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomCheckBox(
                    true
                ) { }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))

                AppText(
                    text = stringResource(id = R.string.i_agree)+" ",
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = md_theme_light_onSurface,
                    )
                AppText(
                    text = stringResource(id = R.string.terms_cond),
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                    color = ColorMainPrimary,
                    )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                 .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp)),
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    }
}

@Composable
fun Heading(title: String) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
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