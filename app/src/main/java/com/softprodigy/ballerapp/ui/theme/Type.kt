package com.softprodigy.ballerapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R

val rubikFamily = FontFamily(

    Font(R.font.rubik_bold_700, weight = FontWeight.W500),
    Font(R.font.rubik_medium_500, weight = FontWeight.Medium),
    Font(R.font.rubik_regular_400, weight = FontWeight.Normal),
    Font(R.font.rubik_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.rubik_black_900),

    )

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontFamily = rubikFamily

    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        fontFamily = rubikFamily

    ),
    h3 = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        fontFamily = rubikFamily

    ),
    h5 = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        fontFamily = rubikFamily
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        fontFamily = rubikFamily
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        fontFamily = rubikFamily
    ),

    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
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
    )

)
