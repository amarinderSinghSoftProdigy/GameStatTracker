package com.softprodigy.ballerapp.ui.features.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight


@Composable
fun SocialLoginSection(
    modifier: Modifier = Modifier,
    headerText: String = stringResource(id = R.string.or_sign_in_with),
    onAppleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                text = headerText,
                style = MaterialTheme.typography.h4,
                color = ColorBWGrayLight
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SocialLoginButton(
                icon = painterResource(id = R.drawable.ic_apple),
            )
            { onAppleClick() }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            SocialLoginButton(
                icon = painterResource(id = R.drawable.ic_facebook)
            ) { onFacebookClick() }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            SocialLoginButton(
                icon = painterResource(id = R.drawable.ic_twitter)
            ) { onTwitterClick() }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        }
    }
}


