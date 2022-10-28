package  com.allballapp.android.core

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Dimensions(
    val default: Dp = 0.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceMediumL: Dp = 21.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceExtraLarge: Dp = 64.dp,
    val spaceBotLogo: Dp = 120.dp
)

data class TextDimensions(
    val default: TextUnit = 0.sp,
    val size4sp: TextUnit = 4.sp,
    val size8sp: TextUnit = 8.sp,
    val size14sp: TextUnit = 14.sp,
    val size15sp: TextUnit = 15.sp,
    val size16sp: TextUnit = 16.sp,
    val size18sp: TextUnit = 18.sp,
    val size20sp: TextUnit = 20.sp,
    val size22sp: TextUnit = 22.sp,
    val size24sp: TextUnit = 24.sp,
)

val LocalSpacing = compositionLocalOf { Dimensions() }
val LocalTextSize = compositionLocalOf { TextDimensions() }
