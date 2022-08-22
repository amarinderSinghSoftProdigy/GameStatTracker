package com.softprodigy.ballerapp.ui.features.user_type

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.features.components.*
import com.softprodigy.ballerapp.ui.theme.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeamSetupScreen(onBackClick: () -> Unit, onNextClick: () -> Unit) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val teamName = remember { mutableStateOf("") }
    val selectedColorCode = remember { mutableStateOf("") }
    val editTextColorValue = remember { mutableStateOf("") }
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    var imageUri by remember(false) {
        mutableStateOf<Uri?>(null)
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
                selectedColor.value = colorEnvelope.color
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
                    .padding(dimensionResource(id = R.dimen.size_16dp))
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
                AppText(
                    text = stringResource(id = R.string.setup_your_team),
                    style = MaterialTheme.typography.h3,
                    color = ColorBWBlack
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.size_8dp))
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
                            placeholder = { AppText(text = stringResource(id = R.string.your_team_name)) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = ColorBWGrayBorder
                            )
                        )
                    }

                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AppText(
                                text = stringResource(id = R.string.team_logo),
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                text = stringResource(id = R.string.change),
                                color = ColorBWGrayLight,
                                fontSize = dimensionResource(id = R.dimen.txt_size_13).value.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(48.dp))
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(id = R.dimen.size_300dp))
                                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp)))
                                .background(color = ColorPrimaryTransparent)
                                .clickable {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
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
                                if (Build.VERSION.SDK_INT < 28) {
                                    bitmap.value = MediaStore.Images
                                        .Media.getBitmap(context.contentResolver, it)

                                } else {
                                    val source = ImageDecoder
                                        .createSource(context.contentResolver, it)
                                    bitmap.value = ImageDecoder.decodeBitmap(source)
                                }

                                bitmap.value?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(250.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4dp)))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth()) {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(id = R.dimen.size_16dp)),
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
                                AppBasicTextField(
                                    modifier = Modifier
                                        .width(dimensionResource(id = R.dimen.size_150dp))
                                        .width(IntrinsicSize.Min)
                                        .height(
                                            dimensionResource(id = R.dimen.size_32dp)
                                        )
                                        .border(BorderStroke(1.dp, ColorBWGrayBorder))
                                        .padding(8.dp),
                                    enabled = false,
                                    value = editTextColorValue.value,
                                    onValueChange = {
                                        editTextColorValue.value = it
                                    } /*text = "this is"*/)

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

                }
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AppButton(
                        onClick = onBackClick,
                        modifier = Modifier.width(dimensionResource(id = R.dimen.size_156dp)),
                        colors = ButtonDefaults.outlinedButtonColors(),
                        border = ButtonDefaults.outlinedBorder,
                        elevation = null
                    ) {

                        AppText(
                            text = stringResource(id = R.string.back),
                            color = Color.Gray
                        )
                    }
                    AppButton(
                        onClick = onNextClick,
                        icon = painterResource(id = R.drawable.ic_circle_next),
                        modifier = Modifier.width(dimensionResource(id = R.dimen.size_156dp)),
                        enabled = teamName.value.isNotEmpty() && imageUri != null && selectedColorCode.value.isNotEmpty()
                    ) {

                        AppText(
                            text = stringResource(id = R.string.next),
                            color = if (teamName.value.isNotEmpty() && imageUri != null && selectedColorCode.value.isNotEmpty()) {
                                Color.White
                            } else {
                                Color.Gray
                            }
                        )
                    }

                }
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
    Column() {
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
