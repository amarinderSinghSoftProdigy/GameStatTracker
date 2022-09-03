package com.softprodigy.ballerapp.ui.features.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun SocialLoginButton(
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
                color = MaterialTheme.colors.onPrimary
            )
            .clickable { onClick.invoke() }
            .size(dimensionResource(id = R.dimen.size_44dp))
    ) {
        Image(
            painter = icon,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.size_16dp))
                .width(dimensionResource(id = R.dimen.size_20dp)),
            contentDescription = "Social Login ",
            contentScale = ContentScale.Fit
//            tint = Color.Unspecified,
        )
    }
}


