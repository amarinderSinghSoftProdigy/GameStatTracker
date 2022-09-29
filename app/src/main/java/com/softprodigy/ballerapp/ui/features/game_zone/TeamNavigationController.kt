package com.softprodigy.ballerapp.ui.features.game_zone

import android.graphics.drawable.ShapeDrawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.Shapes

@Composable
fun TeamNavigationController (

) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_46dp))
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_12dp),
                    vertical = dimensionResource(id = R.dimen.size_7dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // My team
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    teamLogo();
                    teamTitle("My Team", endPadding = dimensionResource(id = R.dimen.size_8dp));
                }
            }

            // team handling
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    teamLogo();
                    teamScore("50", startPadding = dimensionResource(id = R.dimen.size_9dp))
                    teamScore(":", dimensionResource(id = R.dimen.size_8dp), dimensionResource(id = R.dimen.size_8dp))
                    teamScore("100", endPadding = dimensionResource(id = R.dimen.size_9dp))
                    teamLogo();
                }
            }

            // Other team
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    teamTitle("Other Team", startPadding = dimensionResource(id = R.dimen.size_8dp));
                    teamLogo(logoColor = Color.Blue);
                }
            }
        }
    }
}

@Composable
inline fun teamLogo(logoSource :String = "", logoColor: Color = Color.Red) {
    if (logoSource != "") {
        CoilImage(
            src = "", //state.localLogo ?: (BuildConfig.IMAGE_SERVER + state.logo),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                ),
            //.align(Alignment.Center),
            isCrossFadeEnabled = false,
            onLoading = { PlaceholderRect(R.drawable.ic_user_profile_icon) },
            onError = { PlaceholderRect(R.drawable.ic_user_profile_icon) }
        )
    } else {
        Box(
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.size_24dp))
                .height(dimensionResource(id = R.dimen.size_24dp))
                .clip(CircleShape)
                .background(logoColor)
        )
    }
}

@Composable
inline fun teamTitle(title: String = "", startPadding: Dp = 0.dp, endPadding: Dp = 0.dp) {
    AppText(
        text = title,
        //color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        color = Color.White,
        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(start = startPadding, top= 0.dp, end = endPadding, bottom = 0.dp)
    )
}

@Composable
inline fun teamScore(score: String = "", startPadding: Dp = 0.dp, endPadding: Dp = 0.dp) {
    AppText(
        text = score,
        //color = MaterialTheme.appColors.buttonColor.backgroundEnabled,
        color = Color.White,
        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(startPadding,  0.dp, endPadding,  0.dp)
    )
}

@Composable
inline fun navigationHandler(enabled: Boolean = false) {
    Surface(
        //onClick = onClick,
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.size_32dp))
            .background(Color.Yellow)
            .border(dimensionResource(id = R.dimen.divider), Color.Red)
            .clip(RoundedCornerShape(100)),
        //enabled = enabled,
        shape = CircleShape,

        contentColor = colorResource(id = R.color.grey).copy(alpha = 1f),
        border = BorderStroke(dimensionResource(id = R.dimen.size_1dp), Color.Red),
        elevation = if (enabled /*&& icon != null*/) {
            dimensionResource(id = R.dimen.size_5dp)
        } else {
            0.dp
        }
    ) {

    }
    /*Row(
        modifier = Modifier.width(dimensionResource(id = R.dimen.size_32dp))
            .background(Color.Yellow)
            .border(dimensionResource(id = R.dimen.divider), Color.Red).clip(RoundedCornerShape(100)),

        ) {


        AppButton(
            singleButton = true,
            enabled = true,
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_56dp)),
            text = stringResource(id = R.string.verify),
            icon = painterResource(id = R.drawable.ic_circle_next)
        )
    }*/
}