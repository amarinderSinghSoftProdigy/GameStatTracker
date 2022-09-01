package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.ColorBWBlack
import com.softprodigy.ballerapp.ui.theme.appColors
import com.softprodigy.ballerapp.ui.theme.spacing

@Composable
fun AppOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = androidx.compose.material.LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(MaterialTheme.spacing.small),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = if (value.isNotEmpty()) {
                trailingIcon
            } else null,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )
        if (isError) {
            androidx.compose.material.Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(0.95f)
            )
        }
    }
}

@Composable
fun AppOutlineDateField(
    value: String,
    data: String = "",
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable {
                onClick()
            },
        border = BorderStroke(
            0.5.dp,
            MaterialTheme.appColors.editField.borderUnFocused
        ),
        backgroundColor = MaterialTheme.appColors.material.background,
        shape = RoundedCornerShape(MaterialTheme.spacing.small),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 13.dp, end = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (data.isEmpty()) {
                AppText(
                    text = value,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.appColors.textField.label,
                    fontSize = dimensionResource(id = R.dimen.txt_size_12).value.sp
                )
            } else {
                AppText(
                    text = data,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    style = androidx.compose.material.LocalTextStyle.current
                )
            }
        }
    }
}

@Composable
fun AppOutlineGenderSelection(
    value: String,
    data: String = "",
    onClick: () -> Unit = {},
) {


}

@Composable
fun EditFields(
    data: String,
    onValueChange: (String) -> Unit,
    head: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = false,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_56dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AppText(
                text = head,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack,
                modifier = Modifier.padding(start = 16.dp)
            )
            TextField(
                value = data, onValueChange = onValueChange,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                ),
                textStyle = TextStyle(textAlign = TextAlign.End),
                singleLine = true,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                readOnly = enabled,
                trailingIcon = trailingIcon
            )
        }

        if (isError) {
            androidx.compose.material.Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}
