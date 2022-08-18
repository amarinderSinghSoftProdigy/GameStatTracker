package com.softprodigy.ballerapp.ui.features.user_type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.theme.*


@Composable
fun TeamSetupScreen() {
    var teamName by rememberSaveable { mutableStateOf("") }
    Box(Modifier.fillMaxSize()) {
        CoachFlowBackground(
            modifier = Modifier.fillMaxSize(),
            outerIcon = painterResource(id = R.drawable.ic_ball)
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_16dp)),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
            AppText(
                text = stringResource(id = R.string.setup_your_team),
                style = MaterialTheme.typography.h3,
                color = ColorBWBlack
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.size_8dp))
                ) {
                    AppText(
                        text = stringResource(id = R.string.team_name),
                        style = MaterialTheme.typography.h6,
                        color = ColorBWBlack
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    AppOutlineTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = teamName,
                        onValueChange = {
                            teamName = it
                        },
                        placeholder = { stringResource(id = R.string.your_team_name) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = ColorBWGrayBorder
                        )
                    )
                }

            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.size_16dp))
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        AppText(
                            text = stringResource(id = R.string.team_logo),
                            style = MaterialTheme.typography.h6
                        )
                        Text(text = stringResource(id = R.string.change), color = ColorBWGrayLight)
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.size_300dp))
                            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp)))
                            .background(color = ColorPrimaryTransparent)

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
                    }
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
        }
    }
}
