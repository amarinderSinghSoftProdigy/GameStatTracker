package com.softprodigy.ballerapp.ui.features.game_zone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*

@Composable
fun TeamNavigationController (

) {
    Box(
        modifier =  Modifier.fillMaxSize()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_48dp))
                .padding(
                    horizontal = dimensionResource(id = R.dimen.size_12dp),
                    vertical = dimensionResource(id = R.dimen.size_6dp)
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
                    teamTitle("My Team");
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
                    teamTitle("My team");
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
                    teamTitle("Other Team");
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
inline fun teamTitle(title: String = "") {
    AppText(
        text = title,
        //color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        color = Color.White,
        fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
        style = MaterialTheme.typography.body1,
        maxLines = 1,
        textAlign = TextAlign.Center,

    )
}

@Composable
inline fun navigationHandler() {
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
}