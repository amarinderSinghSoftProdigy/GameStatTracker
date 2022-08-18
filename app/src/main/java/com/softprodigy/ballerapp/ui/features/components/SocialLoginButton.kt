package com.softprodigy.ballerapp.ui.features.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun SocialButton(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AppOutlinedButton(onClick = onClick, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                modifier = Modifier.size(24.dp),
                contentDescription = "Social Login ",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = text,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: Painter,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = icon,
            modifier = Modifier.size(24.dp),
            contentDescription = "Social Login ",
            tint = Color.Unspecified
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Preview("default", "rectangle")
@Preview("dark theme", "rectangle", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "rectangle", fontScale = 2f)
@Composable
private fun RectangleButtonPreview() {
    BallerAppTheme {
        SocialButton(text = "Google", icon = painterResource(id = R.drawable.ic_google)) {
        }
    }
}
