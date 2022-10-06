package com.softprodigy.ballerapp.ui.features.home.events.opportunities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.home.events.EvEvents
import com.softprodigy.ballerapp.ui.features.home.events.EventViewModel
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.md_theme_light_onSurface

@Composable
fun EventRefereeRegistrationScreen(vm: EventViewModel, onNextScreen: () -> Unit) {
    val state = vm.eventState.value

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = dimensionResource(id = R.dimen.size_16dp))
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        ),
                        color = Color.White
                    )
            ) {
                RegisterItem(
                    stringResource(id = R.string.players),
                    if (state.registerRequest.players.isNotEmpty()) state.registerRequest.players.size.toString() else stringResource(
                        id = R.string.register_as
                    ),
                    updated = state.registerRequest.players.isNotEmpty()
                ) {

                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(
                            dimensionResource(id = R.dimen.size_8dp)
                        ),
                        color = Color.White
                    )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                    ) {
                        CustomCheckBox(
                            state.registerRequest.termsAndCondition
                        ) {
                            vm.onEvent(EvEvents.RegisterTerms(!state.registerRequest.termsAndCondition))
                        }
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
                            state.registerRequest.privacy
                        ) {
                            vm.onEvent(EvEvents.RegisterPrivacy(!state.registerRequest.privacy))
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                        AppText(
                            text = stringResource(id = R.string.privacy),
                            fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                            color = ColorMainPrimary,
                        )
                    }
                }
            }
        }
        AppButton(
            enabled = true,
            icon = null,
            themed = true,
            onClick = {
                onNextScreen()
            },
            text = stringResource(id = R.string.register),
            isForceEnableNeeded = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.size_16dp),
                    end = dimensionResource(id = R.dimen.size_16dp),
                    bottom = dimensionResource(id = R.dimen.size_16dp)
                )
                .align(Alignment.BottomCenter)
        )
    }
}
