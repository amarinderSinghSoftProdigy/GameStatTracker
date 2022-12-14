package com.softprodigy.ballerapp.ui.features.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.softprodigy.ballerapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNextClick: () -> Unit) {

    // Animation
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onNextClick()
    }

    // Image
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.size_95dp))
                .width(dimensionResource(id = R.dimen.size_160dp))
        )
    }
}
