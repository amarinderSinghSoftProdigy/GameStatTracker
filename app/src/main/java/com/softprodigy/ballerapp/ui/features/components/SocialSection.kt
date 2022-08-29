package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.ui.theme.spacing
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight


@Composable
fun SocialLoginSection(
    modifier: Modifier = Modifier,
    headerText: String = stringResource(id = R.string.or_sign_in_with),
    onGoogleClick: () -> Unit,
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
                icon = painterResource(id = R.drawable.ic_google),
            )
            { onGoogleClick() }

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


