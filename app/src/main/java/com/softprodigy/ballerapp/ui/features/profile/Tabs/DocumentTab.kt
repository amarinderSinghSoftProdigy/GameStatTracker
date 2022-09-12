package com.softprodigy.ballerapp.ui.features.profile


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.Team
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.md_theme_light_primary

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DocumentTab(
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.appColors.material.primary)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column() {
            DcumentItem("birth_certificate", "http://testing.url.com")
            DcumentItem("grade_verification", "")
            DcumentItem("permission_slip", "")
            DcumentItem("aau_card", "")
            DcumentItem("waiver", "")
            DcumentItem("vaccine_card", "")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
        }

    }
}


@Composable
fun DcumentItem(stringId: String, imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp),
                top = dimensionResource(id = R.dimen.size_16dp)
            )
            .clip(RoundedCornerShape(5.dp))
            .background(color = Color.White)
            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(color = md_theme_light_primary)
                        .padding(
                            all = dimensionResource(
                                id = R.dimen.size_16dp
                            )
                        ),
                ) {
                    Image(
                        painter = if (imageUrl.isNotEmpty()) rememberAsyncImagePainter(imageUrl) else painterResource(
                            id = R.drawable.ic_add
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                            },
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = stringResourceByName(stringId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }
            if (imageUrl.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                        },
                )
            }
        }
    }
}


