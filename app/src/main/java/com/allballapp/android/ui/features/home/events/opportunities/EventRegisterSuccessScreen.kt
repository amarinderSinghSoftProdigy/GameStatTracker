package com.allballapp.android.ui.features.home.events.opportunities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.allballapp.android.R
import com.allballapp.android.ui.features.components.AppText
import com.allballapp.android.ui.features.components.LeadingIconAppButton
import com.allballapp.android.ui.theme.GreenColor
import com.allballapp.android.ui.theme.appColors

@Composable
fun EventRegisterSuccessScreen(
    heading: String = stringResource(id = R.string.registered),
    description: String = stringResource(id = R.string.wait_for_response),
    onClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_160dp))
                    .background(shape = CircleShape, color = GreenColor)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(dimensionResource(id = R.dimen.size_50dp)),
                    tint = Color.White,
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
            AppText(
                color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                text = heading.ifEmpty { stringResource(id = R.string.no_data_found) },
                fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
            AppText(
                color = MaterialTheme.appColors.textField.label,
                text = description.ifEmpty { stringResource(id = R.string.try_something_else) },
                fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            LeadingIconAppButton(
                iconBorder = false,
                icon = painterResource(id = R.drawable.ic_left_arrow),
                text = stringResource(id = R.string.back_to_events),
                onClick = {
                    onClick()
                },
            )
        }

    }
}