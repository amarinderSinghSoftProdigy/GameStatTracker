package com.softprodigy.ballerapp.ui.features.profile.tabs


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.stringResourceByName
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DocumentTab(
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
            DocumentItem("birth_certificate", "https://picsum.photos/200")
            DocumentItem("grade_verification", "")
            DocumentItem("permission_slip", "")
            DocumentItem("aau_card", "")
            DocumentItem("waiver", "")
            DocumentItem("vaccine_card", "")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_80dp)))
        }

    }
}


@Composable
fun DocumentItem(stringId: String, imageUrl: String) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = dimensionResource(id = R.dimen.size_16dp),
                end = dimensionResource(id = R.dimen.size_16dp),
                top = dimensionResource(id = R.dimen.size_8dp)
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_16dp))
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.size_64dp))
                        .background(
                            color = MaterialTheme.appColors.material.primary,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                        )
                ) {
                    if (imageUrl.isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add_round),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable {
                                },
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(dimensionResource(id = R.dimen.size_64dp))
                                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(
                                    color = MaterialTheme.appColors.material.primary,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                ),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = stringResourceByName(stringId),
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }
            if (imageUrl.isNotEmpty()) {
                Box(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                            .align(Alignment.CenterEnd),
                    )
                }
            }
        }
    }
}


