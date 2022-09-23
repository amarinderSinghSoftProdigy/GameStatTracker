package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.LeadingIconAppButton
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun EmptyScreen(
    singleText: Boolean,
    heading: String = "",
    description: String = "",
    icon: Int = 0,
    onClick: (() -> Unit)? = null
) {
    Box(Modifier.fillMaxSize()) {
        if (singleText) {
            AppText(
                modifier = Modifier.align(Alignment.Center),
                text = heading.ifEmpty { stringResource(id = R.string.coming_soon) },
                fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
                color = ColorBWBlack,
                fontWeight = FontWeight.Bold
            )
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    painter = painterResource(id = if (icon != 0) icon else R.drawable.ic_teams_large),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
                AppText(
                    color = MaterialTheme.appColors.textField.label,
                    text = heading.ifEmpty { stringResource(id = R.string.no_data_found) },
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    color = MaterialTheme.appColors.textField.label,
                    text = description.ifEmpty { stringResource(id = R.string.try_something_else) },
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                if (onClick != null)
                    LeadingIconAppButton(
                        icon = painterResource(id = R.drawable.ic_add_player),
                        text = stringResource(id = R.string.add_players),
                        onClick = {
                            onClick()
                        },
                    )
            }
        }
    }
}