package com.togitech.ccp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData

@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = Color.Transparent,
    unfocusedBorderColor: Color = Color.Transparent,
    cursorColor: Color = MaterialTheme.colors.primary,
    dialogAppBarColor: Color = MaterialTheme.colors.primary,
    dialogAppBarTextColor: Color = Color.White,
    error: Boolean,
    readOnly: Boolean = true,
    rowPadding: Modifier = modifier.padding(vertical = 0.dp, horizontal = 0.dp),
    content: @Composable () -> Unit,
    placeHolder: (@Composable () -> Unit)? = null,
    textStyle:TextStyle = TextStyle(textAlign = TextAlign.End)
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }
    val textFieldValue = textFieldValueState.copy(text = text)
    val keyboardController = LocalTextInputService.current
    Row(
        modifier = modifier
            .background(color = Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(0.3F), contentAlignment = Alignment.CenterStart) {
            content()
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Box(
                ) {
                    val dialog = TogiCodePicker()
                    dialog.TogiCodeDialog(
                        padding=0.dp,
                        pickedCountry = pickedCountry,
                        defaultSelectedCountry = defaultCountry,
                        dialogAppBarColor = dialogAppBarColor,
                        showCountryCode = showCountryCode,
                        showCountryFlag = showCountryFlag,
                        dialogAppBarTextColor = dialogAppBarTextColor
                    )
                }
                TextField(
                    modifier = Modifier.weight(1F),
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValueState = it
                        if (text != it.text) {
                            onValueChange(it.text)
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor,
                        cursorColor = cursorColor
                    ),
                    singleLine = true,
                    visualTransformation = MaskTransformation(),//PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                    placeholder =
                        placeHolder
                    ,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    textStyle = textStyle,
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hideSoftwareKeyboard() }),
                    readOnly = readOnly
                    /* leadingIcon = {

                },*/
                    /*trailingIcon = {
                    if (!error)
                        Icon(
                            imageVector = Icons.Filled.Warning, contentDescription = "Error",
                            tint = MaterialTheme.colors.error
                        )
                }*/
                )
            }
            if (!error)
                Text(
                    text = stringResource(id = R.string.invalid_number),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 0.8.dp)
                )
        }
    }
}

class MaskTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text.text)
    }
}

//const val mask = "(***) *** ****"

fun maskFilter(text: String): TransformedText {

    val trimmed = if (text.length >= 10) text.substring(0..9) else text
    var out = ""
    for (i in trimmed.indices) {
        if (i == 0) out += "("
        out += trimmed[i]
        if (i == 2) out += ") "
        if (i == 5) out += " "
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset < 1) return offset
            if (offset < 3) return offset + 1
            if (offset < 6) return offset + 3
            if (offset < 10) return offset + 4
            return 14
        }

        override fun transformedToOriginal(offset: Int): Int {
            /* if (offset <= 1) return offset
            if (offset <= 4) return offset - 1
            if (offset <= 9) return offset - 2*/
            //return 13
            return offset
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}
