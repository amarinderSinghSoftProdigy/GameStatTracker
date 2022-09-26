package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors


@Composable
fun ImageTextButton(
    title: String,
    drawableResourceId: Int,
    spacerBetween: Int? = 0,
    paddingTop: Dp? = 0.dp,
    paddingBottom: Dp? = 0.dp,
    onClick: () -> Unit,
) {

    Box(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
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

            AppText(
                text = title,
                //color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                color = Color.White,
                fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                style = MaterialTheme.typography.body1,

                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = spacerBetween!!)))
            Image(
                painter = painterResource(id = drawableResourceId),
                /*modifier = Modifier
                    .height(dimensionResource(id = R.dimen.size_16dp))
                    .width(dimensionResource(id = R.dimen.size_20dp))
                    ,*/
                alignment = Alignment.Center,
                contentDescription = "",
                //contentScale = ContentScale.Fit
                //tint = Color.Unspecified,
            )
        }
    }
}
