package com.softprodigy.ballerapp.ui.features.home.manage_team

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryOrange
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun ManageTeamScreen() {

    Box(Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp)),
                text = stringResource(id = R.string.setup_your_team),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
            UserFlowBackground {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp))
                ) {
                    AppText(
                        text = stringResource(id = R.string.team_name),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    AppOutlineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = stringResource(id = R.string.team_name),
                        onValueChange = {
                            //  vm.onEvent(TeamSetupUIEvent.OnTeamNameChange(it))
                        },
                        placeholder = {
                            AppText(
                                text = stringResource(id = R.string.your_team_name),
                                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = ColorBWGrayBorder,
                            cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                        ),
                        // isError = !validTeamName(state.teamName) && state.teamName.isNotEmpty(),
                        errorMessage = stringResource(id = R.string.valid_team_name),
                        enabled = false
                    )
                }

                Divider(thickness = dimensionResource(id = R.dimen.divider))


                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppText(
                        text = stringResource(id = R.string.team_logo),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = stringResource(id = R.string.change),
                        color = ColorBWGrayLight,
                        modifier = Modifier.clickable {
                        },
                        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.size_16dp),
                            end = dimensionResource(id = R.dimen.size_16dp)
                        )
                        .height(dimensionResource(id = R.dimen.size_180dp))
                        .background(
                            color = ColorPrimaryTransparent,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                        )
                        .clickable {
                        }

                ) {
                    Row(modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_upload),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.upload_files),
                            style = MaterialTheme.typography.h6,
                            color = ColorMainPrimary
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ball),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(dimensionResource(id = R.dimen.size_160dp))
                            .clip(CircleShape)
                            .align(Alignment.Center)
                    )

                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))


                Divider(thickness = dimensionResource(id = R.dimen.divider))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    AppText(
                        text = stringResource(id = R.string.team_color),
                        style = MaterialTheme.typography.h6
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                        },
                    ) {

                        Box(
                            modifier = Modifier
                                .width(dimensionResource(id = R.dimen.size_150dp))
                                .height(dimensionResource(id = R.dimen.size_32dp))
                                .border(
                                    BorderStroke(
                                        dimensionResource(id = R.dimen.size_1dp),
                                        ColorBWGrayBorder
                                    ), RoundedCornerShape(
                                        dimensionResource(id = R.dimen.size_6dp)
                                    )
                                )
                                .background(
                                    color = Color.Transparent, shape = RoundedCornerShape(
                                        dimensionResource(id = R.dimen.size_16dp)
                                    )
                                )
                                .padding(dimensionResource(id = R.dimen.size_1dp))
                        ) {
                            AppText(
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center,
                                text = ColorMainPrimary.toArgb().argbToHexString()
                            )
                        }

                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                        Card(
                            modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                            backgroundColor = ColorMainPrimary,
                            shape = RoundedCornerShape(
                                dimensionResource(id = R.dimen.size_4dp)
                            )
                        ) {}
                    }
                }
            }
        }
    }
}