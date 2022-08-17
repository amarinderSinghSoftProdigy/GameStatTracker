package com.softprodigy.deliveryapp.ui.features.components

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
import com.softprodigy.deliveryapp.ui.theme.DeliveryProjectStructureDemoTheme
import com.softprodigy.deliveryapp.ui.theme.spacing
import com.softprodigy.deliveryapp.R

@Composable
fun SocialSection(
    modifier: Modifier = Modifier,
    headerText: String = stringResource(id = R.string.or_sign_up_with),
    footerText1: String = stringResource(id = R.string.already_have_an_account),
    footerText2: String = stringResource(id = R.string.login),
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onFooterClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppDivider(
                Modifier
                    .width(60.dp)
                    .height(1.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            AppText(
                text = headerText,
                style = MaterialTheme.typography.h2
            )
            Spacer(modifier = Modifier.width(5.dp))
            AppDivider(
                Modifier
                    .width(60.dp)
                    .height(1.dp)
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            SocialButton(
                text = stringResource(id = R.string.google),
                icon = painterResource(id = R.drawable.ic_google),
                modifier = Modifier
                    .weight(1.0f)
                    .padding(MaterialTheme.spacing.small), onClick = onGoogleClick
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            SocialButton(
                text = stringResource(id = R.string.facebook),
                icon = painterResource(id = R.drawable.ic_facbook),
                modifier = Modifier
                    .weight(1.0f)
                    .padding(MaterialTheme.spacing.small), onClick = onFacebookClick
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

        Text(
            buildAnnotatedString {
                append(footerText1)
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                    )
                ) {
                    append(" " + footerText2)
                }
            }, modifier = Modifier.clickable(onClick = onFooterClick), textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
    }
}

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
                style = MaterialTheme.typography.h2
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

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))

            SocialLoginButton(
                icon = painterResource(id = R.drawable.ic_facebook)
            ) { onFacebookClick() }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))

            SocialLoginButton(
                icon = painterResource(id = R.drawable.ic_twitter)
            ) { onTwitterClick() }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "rectangle", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    DeliveryProjectStructureDemoTheme() {
        Surface() {
            SocialSection(
                modifier = Modifier.fillMaxWidth(),
                onFooterClick = {},
                onFacebookClick = {},
                onGoogleClick = {})
        }
    }
}
