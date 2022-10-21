package com.softprodigy.ballerapp.ui.features.profile.tabs


import android.net.Uri
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.UserDocType
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.DeleteDialog
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.features.profile.ProfileEvent
import com.softprodigy.ballerapp.ui.features.profile.ProfileViewModel
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors

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
                        vm.onEvent(ProfileEvent.SetUploadKey(it))
                        launcher.launch("*/*")
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
fun DocumentItem(item: UserDocType, onImageClick: (String) -> Unit, onDeleteClick: () -> Unit) {
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
                                    onImageClick(item.key)
                                },
                        )
                    } else {
                        CoilImage(
                            src = BuildConfig.IMAGE_SERVER + item.url,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(dimensionResource(id = R.dimen.size_64dp))
                                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(
                                    color = MaterialTheme.appColors.material.primary,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                )
                                .clickable {
                                    onImageClick(item.key)
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

