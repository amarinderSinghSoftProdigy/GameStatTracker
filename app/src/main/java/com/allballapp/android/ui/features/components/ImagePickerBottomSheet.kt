package com.allballapp.android.ui.features.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Photo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.allballapp.android.R

@Composable
fun ImagePickerBottomSheet(
    title: String,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_25dp))
        ) {
            AppText(
                text = title,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_close_color_picker),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(dimensionResource(id = R.dimen.size_16dp))
                    .clickable {
                        onDismiss.invoke()
                    }
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onCameraClick.invoke()
                        onDismiss.invoke()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Camera,
                    contentDescription = "camera",
                    modifier = Modifier
                        .size(50.dp)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                androidx.compose.material.Text(text = stringResource(id = R.string.camera))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onGalleryClick.invoke()
                        onDismiss.invoke()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Photo, contentDescription = "Caller",
                    modifier = Modifier
                        .size(50.dp)
                )
                Spacer(
                    modifier =
                    Modifier.height(dimensionResource(id = R.dimen.size_12dp))
                )
                androidx.compose.material.Text(text = stringResource(id = R.string.gallery))

            }

        }
    }
}
