package com.softprodigy.ballerapp.ui.features.home.manage_team.teams

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.argbToHexString
import com.softprodigy.ballerapp.common.validTeamName
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIEvent
import com.softprodigy.ballerapp.ui.features.home.teams.TeamViewModel
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.ColorPickerBottomSheet
import com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated.UpdateColor
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageTeamScreen(vm: TeamViewModel) {

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val state = vm.teamUiState.value

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null)
                vm.onEvent(TeamUIEvent.OnImageSelected(uri.toString()))
        }
    val controller = rememberColorPickerController()
    val selectionUpdated = remember {
        mutableStateOf("")
    }
    if (selectionUpdated.value.isNotEmpty()) {
        UpdateColor()
    }
    ModalBottomSheetLayout(
        sheetContent = {
            ColorPickerBottomSheet(controller, colorEnvelope = { colorEnvelope ->
                if (!colorEnvelope.hexCode.contentEquals(AppConstants.PICKER_DEFAULT_COLOR)) {
                    AppConstants.SELECTED_COLOR = colorEnvelope.color
                    selectionUpdated.value = colorEnvelope.hexCode
                    vm.onEvent(TeamUIEvent.OnColorSelected(colorEnvelope.hexCode))
                }
            }, onDismiss = {
                scope.launch {
                    modalBottomSheetState.hide()
                }
            })
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.size_16dp),
            topEnd = dimensionResource(id = R.dimen.size_16dp)
        ),
        sheetBackgroundColor = colorResource(id = R.color.white)
    ) {

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {

                UserFlowBackground {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_name),
                            style = MaterialTheme.typography.h6,
                            color = ColorBWBlack
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_8dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamName,
                            onValueChange = {
                                vm.onEvent(TeamUIEvent.OnTeamNameChange(it))
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.your_team_name),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = ColorBWGrayBorder,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                            ),
                            isError = !validTeamName(state.teamName) && state.teamName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )
                    }

                    Divider(thickness = dimensionResource(id = R.dimen.divider))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_logo),
                            style = MaterialTheme.typography.h6
                        )
                        if (state.teamImageUri != null) {
                            Text(
                                text = stringResource(id = R.string.change),
                                color = ColorBWGrayLight,
                                modifier = Modifier.clickable {
                                    if (state.teamImageUri != null) {
                                        scope.launch {
                                            modalBottomSheetState.hide()
                                        }
                                        launcher.launch("image/*")
                                    }
                                },
                                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp
                            )
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensionResource(id = R.dimen.size_16dp),
                                end = dimensionResource(id = R.dimen.size_16dp)
                            )
                            .height(dimensionResource(id = R.dimen.size_180dp))
                            .background(
                                color = ColorPrimaryTransparent,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .clickable {
                                if (state.teamImageUri == null) {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
                                }
                            }

                    ) {
                        Row(modifier = Modifier.align(Alignment.Center)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_upload),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))
                            AppText(
                                text = stringResource(id = R.string.upload_files),
                                style = MaterialTheme.typography.h6,
                                color = ColorMainPrimary
                            )
                        }


                        if (!state.teamImageUri.isNullOrEmpty() && state.teamImageUri.contains("http")) {
                            AsyncImage(
                                model = state.teamImageUri,
                                contentDescription = "",
                                modifier =
                                Modifier
                                    .size(dimensionResource(id = R.dimen.size_160dp))
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Image(
                                painter = if (state.teamImageUri == null) painterResource(id = R.drawable.ball) else rememberImagePainter(
                                    data = Uri.parse(BuildConfig.IMAGE_SERVER + state.teamImageUri)
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(dimensionResource(id = R.dimen.size_160dp))
                                    .clip(CircleShape)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                    Divider(thickness = dimensionResource(id = R.dimen.divider))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_color),
                            style = MaterialTheme.typography.h6
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        BorderStroke(
                                            dimensionResource(id = R.dimen.size_1dp),
                                            ColorBWGrayBorder
                                        ), shape = RoundedCornerShape(
                                            dimensionResource(id = R.dimen.size_6dp)
                                        )
                                    )
                                    .background(
                                        color = Color.Transparent, shape = RoundedCornerShape(
                                            dimensionResource(id = R.dimen.size_16dp)
                                        )
                                    )
                                    .padding(dimensionResource(id = R.dimen.size_1dp))
                            ) {
                                AppText(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(
                                            horizontal = dimensionResource(
                                                id = R.dimen.size_16dp
                                            )
                                        ),
                                    textAlign = TextAlign.Center,
                                    text = if (state.teamColor.isNotEmpty()) {
                                        "#" + state.teamColor
                                    } else {
                                        MaterialTheme.appColors.material.primaryVariant.toArgb()
                                            .argbToHexString()
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColor.isEmpty()) {
                                    MaterialTheme.appColors.material.primaryVariant
                                } else {
                                    if (state.teamColor.substring(0, 1) == "#") {
                                        Color(android.graphics.Color.parseColor(state.teamColor))

                                    } else {
                                        Color(android.graphics.Color.parseColor("#" + state.teamColor))
                                    }


                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                )
            }
        }
    }
}