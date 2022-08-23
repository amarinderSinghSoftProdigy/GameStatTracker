package com.softprodigy.ballerapp.ui.features.sign_up

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.isValidEmail
import com.softprodigy.ballerapp.common.validName
import com.softprodigy.ballerapp.common.validPhoneNumber
import com.softprodigy.ballerapp.ui.features.components.AppText
import com.softprodigy.ballerapp.ui.features.components.EditFields
import com.softprodigy.ballerapp.ui.features.components.BottomButtons
import com.softprodigy.ballerapp.ui.features.components.CoachFlowBackground
import com.softprodigy.ballerapp.ui.features.components.UserFlowBackground
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.ColorPrimaryTransparent
import com.softprodigy.ballerapp.ui.theme.appColors

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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_40dp)))

        UserFlowBackground(color = MaterialTheme.appColors.material.onSecondary) {

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

                if (imageUri == null) {
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_12dp)))
                    Text(
                        text = stringResource(id = R.string.valid_image),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

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
                    KeyboardOptions(keyboardType = KeyboardType.Email),
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
                    && email.value.isValidEmail(),
            firstText = stringResource(id = R.string.back),
            secondText = stringResource(id = R.string.next)
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_44dp)))

    }
}

