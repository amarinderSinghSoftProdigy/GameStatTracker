package com.softprodigy.ballerapp.ui.features.sign_up

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.EditFields
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import timber.log.Timber
import java.io.File


@Composable
fun ProfileSetUpScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    Box(
        Modifier
            .fillMaxWidth()

    ) {
        CoachFlowBackground()
        SetUpProfile(onNext, onBack)
    }

}

@Composable
fun SetUpProfile(onNext: () -> Unit, onBack: () -> Unit) {

    var imageUri by remember(false) {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap = remember(false) {
        mutableStateOf<Bitmap?>(null)
    }

    val fName = remember {
        mutableStateOf("")
    }
    val lName = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val phoneNumber = remember {
        mutableStateOf("")
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_64dp)))
        AppText(
            text = stringResource(id = R.string.set_your_profile),
            style = MaterialTheme.typography.h3,
            color = ColorBWBlack,
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_20dp)))

        UserFlowBackground(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {

                Spacer(modifier = Modifier.height(48.dp))

                Box(
                    Modifier
                        .width(dimensionResource(id = R.dimen.size_200dp))
                        .height(dimensionResource(id = R.dimen.size_200dp))
                        .clip(shape = CircleShape)
                        .background(color = ColorPrimaryTransparent)
                        .clickable {
                            launcher.launch("image/*")
                        }
                        .align(Alignment.CenterHorizontally),

                    contentAlignment = Alignment.Center

                ) {
                    Row(modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .width(36.dp)
                                .height(32.dp)
                        )
                    }
                    imageUri?.let {
                        Image(
                            painter = rememberImagePainter(data = Uri.parse(it.toString())),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(dimensionResource(id = R.dimen.size_300dp))
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    }
                }
                val file = imageUri?.path?.let { File(it) }
//                val fileSizeInBytes = file!!.length()
//                val fileSizeInKB: Long = fileSizeInBytes / 1024
//                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                val fileSizeInMB = fileSizeInKB / 1024
//
//                val calString = fileSizeInMB.toString()
                Timber.d("SetUpProfilevalue: " + (file?.length()))

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_48dp)))

                Divider()

                EditFields(
                    fName,
                    stringResource(id = R.string.first_name),
                    isError = !validName(fName.value) && fName.value.length >= 2,
                    errorMessage = stringResource(id = R.string.valid_first_name)
                )

                Divider()

                EditFields(
                    lName,
                    stringResource(id = R.string.last_name),
                    isError = !validName(lName.value) && lName.value.length >= 2,
                    errorMessage = stringResource(id = R.string.valid_last_name)
                )
                Divider()

                EditFields(
                    email,
                    stringResource(id = R.string.email),
                    isError = (!email.value.isValidEmail() && email.value.length >= 6),
                    errorMessage = stringResource(id = R.string.email_error)
                )
                Divider()

                EditFields(
                    phoneNumber,
                    stringResource(id = R.string.phone_num),
                    KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = (!validPhoneNumber(phoneNumber.value) && phoneNumber.value.length >= 5),
                    errorMessage = stringResource(id = R.string.valid_phone_number)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

        BottomButtons(
            onBackClick = { onBack() },
            onNextClick = { onNext() },
            enableState = validName(fName.value)
                    && validName(lName.value)
                    && validPhoneNumber(phoneNumber.value)
                    && email.value.isValidEmail() && imageUri != null,
            firstText = stringResource(id = R.string.back),
            secondText = stringResource(id = R.string.next)
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

    }
}

