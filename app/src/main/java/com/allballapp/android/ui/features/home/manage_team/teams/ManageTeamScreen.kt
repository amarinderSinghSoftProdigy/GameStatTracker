package com.allballapp.android.ui.features.home.manage_team.teams

import android.app.Activity
import android.location.Geocoder
import android.net.Uri
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.argbToHexString
import com.allballapp.android.common.getCustomColorCode
import com.allballapp.android.common.validTeamName
import com.allballapp.android.data.request.Address
import com.allballapp.android.ui.features.components.*
import com.allballapp.android.ui.features.home.teams.TeamUIEvent
import com.allballapp.android.ui.features.home.teams.TeamViewModel
import com.allballapp.android.ui.features.user_type.team_setup.updated.ColorPickerBottomSheet
import com.allballapp.android.ui.features.user_type.team_setup.updated.ColorType
import com.allballapp.android.ui.features.user_type.team_setup.updated.UpdateColor
import com.allballapp.android.ui.theme.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageTeamScreen(
    vm: TeamViewModel, venue: String = "", onVenueClick: () -> Unit,
) {

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
    var currentColorType: ColorType? by remember {
        mutableStateOf(null)
    }
    val maxTeamChar = 30

    LaunchedEffect(key1 = venue, block = {
        if (venue.isNotEmpty()) {
            vm.onEvent(TeamUIEvent.OnVenueChange(venue))
        }
    })
    val context = LocalContext.current
    val placePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { activityResult ->
            val addressReq = Address()

            if (activityResult.resultCode == Activity.RESULT_OK) {
                activityResult.data?.let {
                    val place = activityResult.data?.let { data ->
                        Autocomplete.getPlaceFromIntent(
                            data
                        )
                    }
                    val address = place?.address ?: ""
                    val latLng = place?.latLng
                    val lat = latLng?.latitude ?: 0.0
                    val long = latLng?.longitude ?: 0.0

                    addressReq.street = address
                    addressReq.lat = lat
                    addressReq.long = long

                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(lat, long, 1)
                        val stateName: String = addresses?.get(0)?.adminArea ?: ""
                        val cityName: String = addresses?.get(0)?.locality ?: ""
                        val countryName: String = addresses?.get(0)?.countryName ?: ""
                        val zip: String = addresses?.get(0)?.postalCode ?: ""

                        addressReq.state = stateName
                        addressReq.city = cityName
                        addressReq.country = countryName
                        addressReq.zip = zip


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            if (addressReq.street.isNotEmpty()) {
                vm.onEvent(TeamUIEvent.OnAddressChanged(addressReq))
            }
            if (activityResult.resultCode == AutocompleteActivity.RESULT_ERROR) {
                activityResult.let {
                    val status = it.data?.let { it1 -> Autocomplete.getStatusFromIntent(it1) }
                    Timber.i("RESULT_ERROR ${status?.statusMessage}--$status")
                }
            }

        }
    )

    ModalBottomSheetLayout(
        sheetContent = {
            ColorPickerBottomSheet(controller, colorEnvelope = { colorEnvelope ->
                if (!colorEnvelope.hexCode.contentEquals(AppConstants.PICKER_DEFAULT_COLOR)) {
                    selectionUpdated.value = colorEnvelope.hexCode
                    when (currentColorType) {
                        ColorType.PRIMARY -> {
                            AppConstants.SELECTED_COLOR = colorEnvelope.color
                            selectionUpdated.value = colorEnvelope.hexCode
                            vm.onEvent(TeamUIEvent.OnColorSelected(colorEnvelope.hexCode))
                        }
                        ColorType.SECONDARY -> {
                            vm.onEvent(TeamUIEvent.OnSecColorSelected(colorEnvelope.hexCode))

                        }
                        ColorType.TERTIARY -> {
                            vm.onEvent(TeamUIEvent.OnTerColorSelected(colorEnvelope.hexCode))
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
        sheetBackgroundColor = MaterialTheme.appColors.material.background
    ) {

        Box(
            Modifier.fillMaxSize()
                .background(color = MaterialTheme.appColors.material.surface)
        ) {
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
                            color = MaterialTheme.appColors.textField.labelColor
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
                                cursorColor = MaterialTheme.appColors.textField.labelColor
                            ),
                            isError = !validTeamName(state.teamName) && state.teamName.isNotEmpty(),
                            textStyle = TextStyle(color = MaterialTheme.appColors.textField.labelColor),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )


                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_jerseys),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.textField.labelColor
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnJerseys,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(TeamUIEvent.OnTeamNameJerseyChange(it))
                            },
                            placeholder = {
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.textField.labelColor

                            ),
                            textStyle = TextStyle(color = MaterialTheme.appColors.textField.labelColor),
                            isError = !validTeamName(state.teamNameOnJerseys) && state.teamNameOnJerseys.isNotEmpty(),
                            errorMessage = stringResource(id = R.string.valid_team_name)
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.team_name_tournament),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.textField.labelColor
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))
                        AppOutlineTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.teamNameOnTournaments,
                            onValueChange = {
                                if (it.length <= maxTeamChar)
                                    vm.onEvent(
                                        TeamUIEvent.OnTeamNameTournamentsChange(
                                            it
                                        )
                                    )
                            },
                            placeholder = {

                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = MaterialTheme.appColors.editField.borderUnFocused,
                                cursorColor = MaterialTheme.appColors.buttonColor.backgroundEnabled

                            ),
                            textStyle = TextStyle(MaterialTheme.appColors.textField.labelColor),
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
                            style = MaterialTheme.typography.h6
                        )
                        if (state.logo != null) {
                            Text(
                                text = stringResource(id = R.string.change),
                                color = MaterialTheme.appColors.textField.label,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
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
                                if (state.logo == null) {
                                    scope.launch {
                                        modalBottomSheetState.hide()
                                    }
                                    launcher.launch("image/*")
                                }
                            }

                    ) {
                        if (state.logo == null) {
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


                        CoilImage(
                            src = state.localLogo
                                ?: (com.allballapp.android.BuildConfig.IMAGE_SERVER + state.logo),
                            modifier =
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
                                )
                                .align(Alignment.Center),
                            isCrossFadeEnabled = false,
                            onLoading = { PlaceholderRect(R.drawable.ic_user_profile_icon) },
                            onError = { PlaceholderRect(R.drawable.ic_user_profile_icon) }
                        )

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
                            color = MaterialTheme.appColors.textField.labelColor
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    /* modalBottomSheetState.animateTo(
                                         ModalBottomSheetValue.Expanded
                                     )*/
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
                                        .align(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                    text = if (state.teamColorPrimary.isNotEmpty()) {
                                        if (state.teamColorPrimary.substring(0, 1) == "#") {
                                            state.teamColorPrimary
                                        } else {
                                            "#" +
                                                    state.teamColorPrimary
                                        }
                                    } else {
                                        MaterialTheme.appColors.material.primaryVariant.toArgb()
                                            .argbToHexString()
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorPrimary.isEmpty()) {
                                    MaterialTheme.appColors.material.primaryVariant
                                } else if (state.teamColorPrimary.length < 6) {
                                    MaterialTheme.appColors.material.primaryVariant
                                } else {
                                    Timber.e("color " + state.teamColorPrimary)
                                    if (state.teamColorPrimary.startsWith("#")) {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorPrimary.removePrefix("#"))))
                                    } else {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorPrimary)))

                                    }
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
                            color = MaterialTheme.appColors.textField.labelColor
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
                                        if (state.teamColorSec.substring(0, 1) == "#") {
                                            state.teamColorSec
                                        } else {
                                            "#" + state.teamColorSec
                                        }
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.textField.labelColor


                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorSec.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.backgroundDisabled
                                } else {
//                                    Color(android.graphics.Color.parseColor("#" + state.teamColorSec))
                                    if (state.teamColorSec.startsWith("#")) {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorSec.removePrefix("#"))))

                                    } else {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorSec)))
                                    }
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
                            color = MaterialTheme.appColors.textField.labelColor
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
                                        if (state.teamColorThird.substring(0, 1) == "#") {
                                            state.teamColorThird
                                        } else {
                                            "#" + state.teamColorThird
                                        }
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.appColors.textField.labelColor


                                )
                            }
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_12dp)))

                            Card(
                                modifier = Modifier.size(dimensionResource(id = R.dimen.size_32dp)),
                                backgroundColor = if (state.teamColorThird.isEmpty()) {
                                    MaterialTheme.appColors.buttonColor.backgroundDisabled
                                } else {
//                                    Color(android.graphics.Color.parseColor("#" + state.teamColorThird))
                                    if (state.teamColorThird.startsWith("#")) {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorThird.removePrefix("#"))))

                                    } else {
                                        Color(android.graphics.Color.parseColor("#" + getCustomColorCode(state.teamColorThird)))
                                    }
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
                    color = MaterialTheme.appColors.textField.labelColor,
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
                            color = MaterialTheme.appColors.textField.labelColor
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable { onVenueClick.invoke() }
                                .border(
                                    width = dimensionResource(id = R.dimen.divider),
                                    color = MaterialTheme.appColors.editField.borderUnFocused,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                                )
                                .padding(dimensionResource(id = R.dimen.size_16dp))
                        )
                        {
                            if (state.venueName.isEmpty()) {
                                AppText(
                                    text = "",
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.textField.labelDark,
                                    fontWeight = FontWeight.W400,
                                    fontFamily = rubikFamily
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            } else {
                                AppText(
                                    text = state.venueName,
                                    color = MaterialTheme.appColors.textField.labelColor,
                                    fontWeight = FontWeight.W400,
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            }
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                        AppText(
                            text = stringResource(id = R.string.address),
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.appColors.textField.labelColor
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_10dp)))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!Places.isInitialized()) {
                                        Places.initialize(
                                            context.applicationContext,
                                            com.allballapp.android.BuildConfig.MAPS_API_KEY
                                        )
                                    }
                                    val fields = listOf(
                                        Place.Field.NAME,
                                        Place.Field.ADDRESS,
                                        Place.Field.LAT_LNG
                                    )
                                    placePicker.launch(
                                        Autocomplete
                                            .IntentBuilder(
                                                AutocompleteActivityMode.FULLSCREEN,
                                                fields
                                            )
                                            .build(context)
                                    )
                                }
                                .border(
                                    width = dimensionResource(id = R.dimen.divider),
                                    color = MaterialTheme.appColors.editField.borderUnFocused,
                                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6dp))
                                )
                                .padding(dimensionResource(id = R.dimen.size_16dp))
                        )
                        {
                            if (state.selectedAddress.street.isEmpty()) {
                                AppText(
                                    text = "",
                                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp,
                                    color = MaterialTheme.appColors.textField.labelDark,
                                    fontWeight = FontWeight.W400,
                                    fontFamily = rubikFamily
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            } else {
                                AppText(
                                    text = state.selectedAddress.street,
                                    color = MaterialTheme.appColors.textField.labelColor,
                                    fontWeight = FontWeight.W400,
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_14dp)))

                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            }

            if (state.isLoading) {
                CommonProgressBar()
            }
        }
    }
}