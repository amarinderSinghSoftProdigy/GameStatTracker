package com.allballapp.android.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.LeadingIconAppButton
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors
import com.allballapp.android.ui.theme.rubikFamily
import com.allballapp.android.R
@Composable
fun EmptyScreen(
    singleText: Boolean,
    heading: String = "",
    description: String = "",
    icon: Int = 0,
    onClick: (() -> Unit)? = null
) {
    Box(
        Modifier.fillMaxSize()
            .background(color = MaterialTheme.appColors.material.surface)
    ) {
        if (singleText) {
            AppText(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.95f),
                text = heading.ifEmpty { stringResource(id = R.string.coming_soon) },
                fontSize = dimensionResource(id = R.dimen.txt_size_18).value.sp,
                color = MaterialTheme.appColors.textField.labelColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = rubikFamily
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
                    color = MaterialTheme.appColors.textField.labelColor,
                    text = heading.ifEmpty { stringResource(id = R.string.no_data_found) },
                    fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                AppText(
                    color = MaterialTheme.appColors.textField.labelColor,
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