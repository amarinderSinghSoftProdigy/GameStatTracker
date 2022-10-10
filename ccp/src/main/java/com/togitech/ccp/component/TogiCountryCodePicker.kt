package com.togitech.ccp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation

@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showCountryCode: Boolean = true,
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
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }
    val textFieldValue = textFieldValueState.copy(text = text)
    val keyboardController = LocalTextInputService.current
    Row(
        modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(0.3F), contentAlignment = Alignment.CenterStart) {
            content()
        }
        Column(
            modifier = rowPadding.weight(0.7F),
            horizontalAlignment = Alignment.End
        ) {
            Row {
                Box(
                    modifier = Modifier.weight(0.9F),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    val dialog = TogiCodePicker()
                    dialog.TogiCodeDialog(
                        pickedCountry = pickedCountry,
                        defaultSelectedCountry = defaultCountry,
                        dialogAppBarColor = dialogAppBarColor,
                        showCountryCode = showCountryCode,
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
                    visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                    placeholder = {
                        Box(contentAlignment = Alignment.CenterEnd) {
                           /* Text(
                                textAlign = TextAlign.End,
                                text = stringResource(id = getNumberHint(defaultCountry.countryCode))
                            )*/
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    textStyle = TextStyle(textAlign = TextAlign.End),
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