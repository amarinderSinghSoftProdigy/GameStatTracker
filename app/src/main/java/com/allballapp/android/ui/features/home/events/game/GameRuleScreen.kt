package com.allballapp.android.ui.features.home.events.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.AppDivider
import com.allballapp.android.ui.features.home.events.EventViewModel
import com.allballapp.android.ui.theme.ColorBWGrayLight
import com.allballapp.android.ui.theme.appColors

@Composable
fun GameRuleScreen(vm: EventViewModel) {
    val state = vm.eventState.value
    var images = arrayListOf<String>("", "", "", "", "")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                .verticalScroll(rememberScrollState()),
        ) {

            GameRuleItem(
                stringResource(id = R.string.periods),
                stringResource(id = R.string.period_length),
                stringResource(id = R.string.free_throws),
                stringResource(id = R.string.variances),
                "4 Quarters", "12 min", "Standard",
                "Eleifend enim sit posuere placerat sit quis."
            )
            GameRuleItem(
                stringResource(id = R.string.timeout),
                stringResource(id = R.string.full_timeout),
                stringResource(id = R.string.timeout_30),
                stringResource(id = R.string.clock_stoppage),
                "Per Half", "7", "7",
                "Ut augue sapien porta leo adipiscing bibendum dui."
            )

            GameRuleItem(
                stringResource(id = R.string.halftime),
                stringResource(id = R.string.shot_clock),
                stringResource(id = R.string.fouling_out),
                stringResource(id = R.string.variances),
                "15 min", "24s", "7",
                "Ut augue sapien porta leo adipiscing bibendum dui."
            )
            OtherItem(
                stringResource(id = R.string.full_court),
                "Eleifend enim sit posuere placerat sit quis.",
            )
            OtherItem(
                stringResource(id = R.string.technical_fouls),
                "Eleifend enim sit posuere placerat sit quis.",
            )
            OtherItem(
                stringResource(id = R.string.coach_rules),
                "Eleifend enim sit posuere placerat sit quis.",
            )
            OtherItem(
                stringResource(id = R.string.forfeit),
                "Eleifend enim sit posuere placerat sit quis.",
            )
            OtherItem(
                stringResource(id = R.string.other_rules),
                "Eleifend enim sit posuere placerat sit quis.",
            )

        }
    }
}

@Composable
fun GameRuleItem(
    key1: String,
    key2: String,
    key3: String,
    key4: String,
    value1: String,
    value2: String,
    value3: String,
    value4: String
) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Row() {
        Text(
            text = key1,
            color = ColorBWGrayLight,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = key2,
            color = ColorBWGrayLight,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = key3,
            color = ColorBWGrayLight,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Row() {
        Text(
            text = value1,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value2,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f),
            )
        Text(
            text = value3,
            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    Text(
        text = key4,
        color = ColorBWGrayLight,
        style = MaterialTheme.typography.h6,
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Text(
        text = value4,
        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        style = MaterialTheme.typography.h5
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
    AppDivider()
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
}

@Composable
fun OtherItem(key: String, value: String) {
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

    Text(
        text = key,
        color = ColorBWGrayLight,
        style = MaterialTheme.typography.h6,
        )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
    Text(
        text = value,
        color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        style = MaterialTheme.typography.h5
    )
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
}
