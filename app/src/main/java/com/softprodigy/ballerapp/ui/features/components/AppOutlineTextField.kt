package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val customTextSelectionColors = TextSelectionColors(
        handleColor = if (!isError) MaterialTheme.appColors.buttonColor.bckgroundEnabled else MaterialTheme.colors.error,
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
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
        }
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(0.95f),

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
            .height(dimensionResource(id = R.dimen.size_50dp))
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
                .padding(
                    start = dimensionResource(id = R.dimen.size_12dp),
                    end = dimensionResource(id = R.dimen.size_12dp)
                ),
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

            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_24dp))
            )

        }
    }
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
    textStyle: TextStyle = TextStyle(textAlign = TextAlign.End, color = ColorBWBlack),
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AppText(
                text = head,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
            )
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                TextField(
                    value = data, onValueChange = onValueChange,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                    ),
                    textStyle = textStyle,
                    singleLine = true,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    readOnly = enabled,
                    trailingIcon = trailingIcon,
                    placeholder = placeholder
                )
            }
        }

        if (isError) {
            Text(
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


@Composable
fun EditProfileFields(
    data: String,
    onValueChange: (String) -> Unit,
    head: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String = "",
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    fontWeight: FontWeight = FontWeight.W400,
    height: Dp = dimensionResource(id = R.dimen.size_56dp),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AppText(
                text = head,
                style = MaterialTheme.typography.h6,
                color = ColorBWBlack,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16dp))
            )
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                TextField(
                    value = data, onValueChange = onValueChange,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.appColors.buttonColor.bckgroundEnabled,
                        disabledBorderColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.End,
                        color = ColorBWBlack,
                        fontWeight = fontWeight
                    ),
                    singleLine = true,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    readOnly = readOnly,
                    trailingIcon = trailingIcon,
                    placeholder = placeholder,
                    enabled = enabled,
                    visualTransformation = visualTransformation
                )
            }
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

