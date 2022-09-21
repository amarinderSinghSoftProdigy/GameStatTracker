package com.softprodigy.ballerapp.ui.features.user_type.team_setup.updated

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.validTeamName
import com.softprodigy.ballerapp.ui.features.components.AppDivider
import com.softprodigy.ballerapp.ui.features.components.AppOutlineTextField
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.CoilImage
import com.softprodigy.ballerapp.ui.features.components.Placeholder
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
fun TeamSetupScreenUpdated(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    vm: SetupTeamViewModelUpdated
) {

    val maxTeamChar = 30
    var currentColorType: ColorType? by remember {
        mutableStateOf(null)
    }
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val state = vm.teamSetupUiState.value

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null)
                vm.onEvent(TeamSetupUIEventUpdated.OnImageSelected(uri.toString()))
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
                    selectionUpdated.value = colorEnvelope.hexCode
                    when (currentColorType) {
                        ColorType.PRIMARY -> {
                            AppConstants.SELECTED_COLOR = colorEnvelope.color
                            vm.onEvent(TeamSetupUIEventUpdated.OnColorSelected(colorEnvelope.hexCode))
                        }
                        ColorType.SECONDARY -> {
                            vm.onEvent(TeamSetupUIEventUpdated.OnSecColorSelected(colorEnvelope.hexCode))

                        }
                        ColorType.TERTIARY -> {
                            vm.onEvent(TeamSetupUIEventUpdated.OnTerColorSelected(colorEnvelope.hexCode))
                        }
                    }
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
             UserFlowBackground(color = MaterialTheme.appColors.buttonColor.textEnabled) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp))
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_name),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamName,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamSetupUIEventUpdated.OnTeamNameChange(it))
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.your_team_name),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            isError = !validTeamName(state.teamName) && state.teamName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_jerseys),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnJerseys,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamSetupUIEventUpdated.OnTeamNameJerseyChange(it))
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.your_team_name_on_jersey),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            isError = !validTeamName(state.teamNameOnJerseys) && state.teamNameOnJerseys.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_tournament),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnTournaments,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(
                                        TeamSetupUIEventUpdated.OnTeamNameTournamentsChange(
                                            it
                                        )
                                    )
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.your_team_name_on_tournaments),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            isError = !validTeamName(state.teamNameOnTournaments) && state.teamNameOnTournaments.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )
                    }

                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppText(
                            text = stringResource(id = R.string.team_logo),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

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
                            .height(dimensionResource(id = R.dimen.size_200dp))
                            .background(

                                color = if (state.teamImageUri == null) ColorPrimaryTransparent
                                else MaterialTheme.appColors.material.surface,
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
                        if (state.teamImageUri == null) {
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
                        }
                        state.teamImageUri?.let {
                            CoilImage(
                                src = Uri.parse(it),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(dimensionResource(id = R.dimen.size_160dp))
                                    .clip(CircleShape)
                                    .align(Alignment.Center)
                                    .background(
                                        color = MaterialTheme.appColors.material.onSurface,
                                        CircleShape
                                    ),
                                isCrossFadeEnabled = false,
                                onLoading = { Placeholder(R.drawable.ic_team_placeholder) },
                                onError = { Placeholder(R.drawable.ic_team_placeholder) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                    AppDivider()
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.primary_team_color),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.PRIMARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))

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
                                    text = if (state.teamColorPrimary.isNotEmpty()) {
                                        "#" + state.teamColorPrimary
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorPrimary.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(android.graphics.Color.parseColor("#" + state.teamColorPrimary))
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }
                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.secondary_team_color),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.SECONDARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))
                                    .border(
                                        /*BorderStroke(
                                            dimensionResource(id = R.dimen.size_1dp),
                                            ColorBWGrayBorder
                                        )*/
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
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
                                    text = if (state.teamColorSec.isNotEmpty()) {
                                        "#" + state.teamColorSec
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled


                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorSec.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(android.graphics.Color.parseColor("#" + state.teamColorSec))
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }
                    AppDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        AppText(
                            text = stringResource(id = R.string.tertiary_team_color),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    currentColorType = ColorType.TERTIARY
                                    modalBottomSheetState.show()
                                }
                            },
                        ) {

                            Box(
                                modifier = Modifier
                                    .border(
                                        /* BorderStroke(
                                             dimensionResource(id = R.dimen.size_1dp),
                                             ColorBWGrayBorder
                                         )*/
                                        width = dimensionResource(id = R.dimen.size_1dp),
                                        color = MaterialTheme.appColors.editField.borderUnFocused,
                                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))

                                    )
                                    .width(dimensionResource(id = R.dimen.size_100dp))
                                    .height(dimensionResource(id = R.dimen.size_32dp))

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
                                    text = if (state.teamColorThird.isNotEmpty()) {
                                        "#" + state.teamColorThird
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorThird.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.bckgroundDisabled
                                } else {
                                    Color(android.graphics.Color.parseColor("#" + state.teamColorThird))
                                },
                                shape = RoundedCornerShape(
                                    dimensionResource(id = R.dimen.size_4dp)
                                )
                            ) {}
                        }

                    }

                }

                AppText(
                    text = stringResource(id = R.string.home_court),
                    fontSize = dimensionResource(id = R.dimen.txt_size_14).value.sp,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_16dp))
                )

                UserFlowBackground() {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(id = R.dimen.size_16dp))
                    ) {

//                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.name_of_venue),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.venueName,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamSetupUIEventUpdated.OnVenueChange(it))
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.enter_venue_name),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            isError = !validTeamName(state.venueName) && state.venueName.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_venue_name)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.address),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.buttonColor.bckgroundEnabled
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.address,
                            onValueChange = {
//                                if (it.length <= maxTeamChar)
                                vm.onEvent(TeamSetupUIEventUpdated.OnAddressChange(it))
                            },
                            placeholder = {
                                AppText(
                                    text = stringResource(id = R.string.enter_address),
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled

                            ),
                            isError = (state.address.isNotEmpty() && state.address.length <= 4),
                            errorMessage = stringResource(id = R.string.address_error),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
                val enable =
                    validTeamName(state.teamName)
                            && state.teamImageUri != null
                            && state.teamColorPrimary.isNotEmpty()
                            && state.teamColorSec.isNotEmpty()
                            && state.teamColorThird.isNotEmpty()
                            && state.teamName.isNotEmpty()
                            && state.teamNameOnJerseys.isNotEmpty()
                            && state.teamNameOnTournaments.isNotEmpty()
                            && validTeamName(state.venueName)
                            && state.address.isNotEmpty()
                            && state.address.length >= 4
                BottomButtons(
                    firstText = stringResource(id = R.string.back),
                    secondText = stringResource(id = R.string.next),
                    onBackClick = onBackClick,
                    onNextClick = {
                        onNextClick.invoke()
                    },
                    enableState = enable,
                    showOnlyNext = enable,
                    themed = false,//Just set to true to show selected color as background
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

                if (enable) {
                    BackHandler {}
                }
            } //column
        }
    }
}

@Composable
fun ColorPickerBottomSheet(
    controller: ColorPickerController,
    colorEnvelope: (ColorEnvelope) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.size_40dp))
    ) {
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

enum class ColorType {
    PRIMARY, SECONDARY, TERTIARY
}

@Composable
fun UpdateColor() {
    MaterialTheme.appColors.material.copy(primaryVariant = AppConstants.SELECTED_COLOR)
}
