package com.softprodigy.ballerapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R

val rubikFamily = FontFamily(
    fonts = listOf(
        Font(R.font.rubik_regular_400, weight = FontWeight.W400),
        Font(R.font.rubik_semi_bold_600, weight = FontWeight.W600),
        Font(R.font.rubik_medium_500, weight = FontWeight.W500),
        Font(R.font.rubik_light_300, weight = FontWeight.W300),
        Font(R.font.rubik_extra_bold_800, weight = FontWeight.W800),
        Font(R.font.rubik_bold_700, weight = FontWeight.W700),
        Font(R.font.rubik_black_900, weight = FontWeight.W900),

        )
)

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = rubikFamily,
    h3 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        color = ColorBWBlack
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        color = ColorBWBlack

    ),
    h6 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        color = ColorBWBlack
    ),
/*    body2 = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    ),*/
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),

    )
