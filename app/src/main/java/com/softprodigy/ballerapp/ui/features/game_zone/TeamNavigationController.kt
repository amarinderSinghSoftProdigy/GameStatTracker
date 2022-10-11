package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.ImageButton
import com.softprodigy.ballerapp.ui.features.components.PlaceholderRect
import com.softprodigy.ballerapp.ui.theme.rubikFamily

@Composable
fun TeamNavigationController (

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()) {
            teamHandler()
            //PointList()
            Tracking(
                isTrackingEmpty = true,
                onAddRosterClick = {}
            )
        }
    }
}

@Composable
inline fun teamHandler() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.game_period_background_color))
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
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
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
                ImageButton(
                    icon = painterResource(id = R.drawable.ic_possesion_next_arrow_selected),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.size_32dp))
                        .height(dimensionResource(id = R.dimen.size_32dp)),

                    onClick = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                teamScore("50")
                teamScore(
                    ":",
                    dimensionResource(id = R.dimen.size_8dp),
                    dimensionResource(id = R.dimen.size_8dp)
                )
                teamScore("100")
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                ImageButton(
                    icon = painterResource(id = R.drawable.ic_possesion_next_arrow_selected),
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.size_32dp))
                        .height(dimensionResource(id = R.dimen.size_32dp)),
                    onClick = { /*TODO*/ }
                )
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
                teamTitle(
                    "Other Team",
                    startPadding = dimensionResource(id = R.dimen.size_8dp)
                );
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_8dp)))
                teamLogo(logoColor = Color.Blue);
            }
        }
    }
}

@Composable
inline fun teamLogo(logoSource :String = "", logoColor: Color = Color.Red, alpha: Float = 1f) {
    if (logoSource != "") {
        CoilImage(
            src = "", //state.localLogo ?: (BuildConfig.IMAGE_SERVER + state.logo),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                .background(
                    color = Color.Transparent.copy(alpha = alpha),
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
inline fun teamTitle(title: String = "", startPadding: Dp = 0.dp, endPadding: Dp = 0.dp, alpha: Float? = 1f) {
    AppText(
        text = title,
        color = colorResource(id = R.color.game_grid_item_text_color).copy(alpha = alpha!!),
        fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(start = startPadding, top = 0.dp, end = endPadding, bottom = 0.dp),
        fontFamily = rubikFamily,
        fontWeight = FontWeight.W500,
    )
}

@Composable
inline fun teamScore(score: String = "", startPadding: Dp = 0.dp, endPadding: Dp = 0.dp) {
    AppText(
        text = score,
        color = colorResource(id = R.color.game_grid_item_text_color),
        fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
        style = MaterialTheme.typography.h4,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(startPadding,  0.dp, endPadding,  0.dp),
        fontFamily = rubikFamily,
        fontWeight = FontWeight.SemiBold,
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
}