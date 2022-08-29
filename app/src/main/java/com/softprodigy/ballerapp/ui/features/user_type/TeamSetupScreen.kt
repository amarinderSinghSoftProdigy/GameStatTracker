package com.softprodigy.ballerapp.ui.features.user_type

import android.graphics.Bitmap
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.validTeamName
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.request.GlobalRequest
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayBorder
import com.softprodigy.ballerapp.ui.theme.ColorBWGrayLight
import com.softprodigy.ballerapp.ui.theme.ColorMainPrimary
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeamSetupScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: UserTypeViewModel = hiltViewModel()
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val teamName = remember { mutableStateOf(viewModel.teamData.teamName) }
    val selectedColorCode = remember { mutableStateOf(viewModel.teamData.teamColor) }
    val editTextColorValue = remember { mutableStateOf(viewModel.teamData.teamColor) }

    var imageUri by remember(false) {
        mutableStateOf<Uri?>(viewModel.teamData.teamLogo)
    }
    val context = LocalContext.current
    val bitmap = remember(false) {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    val controller = rememberColorPickerController()
    ModalBottomSheetLayout(
        sheetContent = {
            ColorPickerBottomSheet(controller, colorEnvelope = { colorEnvelope ->
                AppConstants.SELECTED_COLOR = colorEnvelope.color
                editTextColorValue.value = colorEnvelope.hexCode
            }, onDismiss = {
                scope.launch {
                    modalBottomSheetState.hide()
                    selectedColorCode.value = editTextColorValue.value
                }
            })
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(
            topStart = dimensionResource(id = R.dimen.size_16dp),
            topEnd = dimensionResource(id = R.dimen.size_16dp)
        ),
        sheetBackgroundColor = colorResource(id = R.color.white),
    ) {

        Box(Modifier.fillMaxSize()) {
            CoachFlowBackground(
                colorCode = selectedColorCode.value.ifEmpty { null }
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
                AppText(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp)),
                    text = stringResource(id = R.string.setup_your_team),
                    style = MaterialTheme.typography.h3,
                    color = ColorBWBlack
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))
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
                            value = teamName.value,
                            onValueChange = {
                                teamName.value = it
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
                            isError = !validTeamName(teamName.value) && teamName.value.isNotEmpty(),
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
                        Text(
                            text = stringResource(id = R.string.change),
                            color = ColorBWGrayLight,
                            modifier = Modifier.clickable {
                                if (imageUri != null) {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
                                }
                            },
                            fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensionResource(id = R.dimen.size_16dp),
                                end = dimensionResource(id = R.dimen.size_16dp)
                            )
                            .height(dimensionResource(id = R.dimen.size_300dp))
                            .background(
                                color = ColorPrimaryTransparent,
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                            )
                            .clickable {
                                if (imageUri == null) {
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
                        imageUri?.let {
                            Image(
                                painter = rememberImagePainter(data = Uri.parse(it.toString())),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(dimensionResource(id = R.dimen.size_270dp))
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
                                    .width(dimensionResource(id = R.dimen.size_150dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        BorderStroke(
                                            dimensionResource(id = R.dimen.size_1dp),
                                            ColorBWGrayBorder
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
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = "#" + editTextColorValue.value,
                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (editTextColorValue.value.isEmpty()) {
                                    Color.White
                                } else {
                                    Color(android.graphics.Color.parseColor("#" + editTextColorValue.value))
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                BottomButtons(
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next),
                    onBackClick = onBackClick,
                    onNextClick = {
                        onNextClick.invoke()
                        UserStorage.teamColor = selectedColorCode.value
                        UserStorage.teamLogo = imageUri.toString()
                        val request = GlobalRequest.SetUpTeam(
                            teamName.value,
                            imageUri,
                            selectedColorCode.value
                        )
                        viewModel.saveTeamData(request)
                    },
                    enableState = validTeamName(teamName.value) && imageUri != null && selectedColorCode.value.isNotEmpty(),
                    showOnlyNext = false
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }
        }
    }
}

@Composable
fun ColorPickerBottomSheet(
    controller: ColorPickerController,
    colorEnvelope: (ColorEnvelope) -> Unit,
    onDismiss: () -> Unit
) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.size_25dp))
        ) {
            AppText(
                text = stringResource(id = R.string.colors),
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
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_300dp))
                .padding(dimensionResource(id = R.dimen.size_8dp)),
            controller = controller,
            onColorChanged = colorEnvelope
        )
    }
}
