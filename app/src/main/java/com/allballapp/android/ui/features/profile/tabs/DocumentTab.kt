package com.allballapp.android.ui.features.profile.tabs

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.allballapp.android.R
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.UserDocType
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.profile.ProfileEvent
import com.allballapp.android.ui.features.profile.ProfileViewModel
import com.allballapp.android.ui.theme.ColorBWBlack
import com.allballapp.android.ui.theme.appColors


@Composable
fun DocumentTab(vm: ProfileViewModel) {
    val context = LocalContext.current
    val state = vm.state.value
    val dataStoreManager = DataStoreManager(LocalContext.current)
    val role = dataStoreManager.getRole.collectAsState(initial = "")

    remember {
        if (role.value != UserType.REFEREE.key)
            vm.onEvent(ProfileEvent.GetDocumentTypes(state.selectedTeamId))
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null)
                vm.onEvent(ProfileEvent.OnImageSelected(state.selectedDocKey, uri.toString()))
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                }
                itemsIndexed(state.userDocTypes) { index, item ->
                    DocumentItem(item, {
                        if (hasFileManagerPermission(context)) {
                            launcher.launch("*/*")
                            vm.onEvent(ProfileEvent.SetUploadKey(it))
                        } else {
                            requestFileManagerPermission(context, context as Activity)
                        }
                    }, {
                        vm.onEvent(ProfileEvent.SetDeleteDocument(item))
                        vm.onEvent(ProfileEvent.ImageUploadedDialog(true))
                    }) {
                        vm.onEvent(ProfileEvent.SetDeleteDocument(item))
                        vm.onEvent(ProfileEvent.ShowDeleteDialog(true))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                }
            }
        }

        if (state.showImage) {
            state.deleteDocument?.let {
                ShowImageUploaded(
                    onDismiss = {
                        vm.onEvent(ProfileEvent.ImageUploadedDialog(false))
                    },
                    it.url
                )
            }
        }

        if (state.showDeleteDialog) {
            DeleteDialog(
                item = state.deleteDocument,
                message = stringResource(id = R.string.alert_delete_document),
                onDismiss = {
                    vm.onEvent(ProfileEvent.ShowDeleteDialog(false))
                },
                onDelete = {
                    vm.onEvent(ProfileEvent.DeleteDocument(state.deleteDocument))
                    vm.onEvent(ProfileEvent.ShowDeleteDialog(false))
                }
            )
        }

        /* if (state.isLoading) {
             CommonProgressBar()
         }*/
    }
}


@Composable
fun DocumentItem(
    item: UserDocType,
    onImageClick: (String) -> Unit,
    onImageViewClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
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
                    if (item.url.isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add_round),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clickable {
                                    //uriHandler.openUri("https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg")
                                    onImageClick(item.key)
                                },
                        )
                    } else {
                        CoilImage(
                            src = com.allballapp.android.BuildConfig.IMAGE_SERVER + item.url,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(dimensionResource(id = R.dimen.size_64dp))
                                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(
                                    color = MaterialTheme.appColors.material.primary,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                )
                                .clickable {
                                    val extension: String =
                                        item.url.substring(item.url.lastIndexOf("."))

                                    if (extension.equals(
                                            ".png",
                                            ignoreCase = true
                                        ) || extension.equals(
                                            ".jpg",
                                            ignoreCase = true
                                        ) || extension.equals(".jpeg", ignoreCase = true)
                                    ) {
                                        onImageViewClick()
                                    } else {
                                        try {
                                            uriHandler.openUri(com.allballapp.android.BuildConfig.IMAGE_SERVER + item.url)
                                        } catch (e: Exception) {
                                            Toast
                                                .makeText(context, e.message, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                },
                            onLoading = {
                                Box {
                                    Image(
                                        painter = painterResource(R.drawable.ic_file),
                                        contentDescription = null,
                                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                    )
                                }
                            },
                            onError = {
                                Box {
                                    Image(
                                        painter = painterResource(R.drawable.ic_file),
                                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                        contentDescription = null,
                                    )
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_20dp)))
                AppText(
                    text = item.name,
                    style = MaterialTheme.typography.h6,
                    color = ColorBWBlack
                )
            }
            if (item.url.isNotEmpty()) {
                Box(modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        onDeleteClick()
                    }) {
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




