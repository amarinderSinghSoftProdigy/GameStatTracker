package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.ui.theme.rubikFamily


@Composable
fun ImageTextButton(
    title: String,
    drawableResourceId: Int,
    titleFontSize: Int,
    fontWeight: FontWeight? = FontWeight.W500,
    fontFamily: FontFamily? = rubikFamily,
    color: Color? = Color.White,
    spacerBetween: Int? = 0,
    paddingTop: Dp? = 0.dp,
    paddingBottom: Dp? = 0.dp,
    letterSpacing: TextUnit? = TextUnit.Unspecified,
    //onClick: () -> Unit,
) {

    Box(
        Modifier
            .fillMaxWidth()
            /*.clickable {
                onClick.invoke()
            }*/,
        contentAlignment = Alignment.Center,
    ){
        Column(
            modifier = Modifier.align(Alignment.Center)
                .fillMaxWidth()
                .padding(
                    start = 0.dp,
                    top = paddingTop!!,
                    end = 0.dp,
                    bottom =  paddingBottom!!
                ),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = drawableResourceId),
                alignment = Alignment.Center,
                contentDescription = "",
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = spacerBetween!!)))

            AppText(
                text = title,
                color = color!!,
                fontSize = dimensionResource(id = titleFontSize).value.sp,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontFamily = fontFamily,
                fontWeight = fontWeight,
                letterSpacing = letterSpacing!!,
            )
        }
    }
}
