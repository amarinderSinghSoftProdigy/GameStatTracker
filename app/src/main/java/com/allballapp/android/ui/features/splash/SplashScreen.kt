package com.allballapp.android.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.allballapp.android.R
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
            painter = painterResource(R.drawable.ic_all_ball_logo),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.size_95dp))
                .width(dimensionResource(id = R.dimen.size_160dp))
        )
    }
}
