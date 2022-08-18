package com.softprodigy.deliveryapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.deliveryapp.R

val rubikFamily = FontFamily(
    fonts = listOf(
        Font(R.font.rubik_regular),
        Font(R.font.rubik_bold, weight = FontWeight.Bold),
        Font(R.font.rubik_regular, weight = FontWeight.Medium),
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = rubikFamily,
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
//        color = heading1Color
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
//        color = heading2Color

    ),
    h3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
//        color = heading2Color

    ),
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
//        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
//        letterSpacing = 0.25.sp
    )
/*    body1 = TextStyle(
        fontFamily = latoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),*/

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */

)
