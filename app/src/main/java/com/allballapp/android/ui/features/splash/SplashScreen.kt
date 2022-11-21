package com.allballapp.android.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import com.allballapp.android.R
import com.allballapp.android.ui.theme.BallerAppMainTheme
import com.allballapp.android.ui.theme.BallerAppTheme
import com.allballapp.android.ui.theme.appColors

@Composable
fun SplashScreen(onNextClick: () -> Unit) {

    // Animation
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onNextClick()
    }

    BallerAppMainTheme {
        // Image
        Column(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.appColors.material.background),
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


}
