package com.allballapp.android.ui.utils.anims

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

fun exitTransition(animeDuration: Int) = slideOutHorizontally(
    targetOffsetX = { -300 },
    animationSpec = tween(animeDuration)
) + fadeOut(animationSpec = tween(animeDuration))


fun slideOutHorizont(animeDuration: Int) = slideOutHorizontally(
    targetOffsetX = { animeDuration },
    animationSpec = tween(animeDuration)
) + fadeOut(animationSpec = tween(animeDuration))

fun slideInHorizont(animeDuration: Int) = slideInHorizontally(
    initialOffsetX = { animeDuration },
    animationSpec = tween(animeDuration)
) + fadeIn(animationSpec = tween(animeDuration))