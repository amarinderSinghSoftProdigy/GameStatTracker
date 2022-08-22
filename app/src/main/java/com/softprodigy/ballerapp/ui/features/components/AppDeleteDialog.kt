package com.softprodigy.ballerapp.ui.features.components

import android.content.res.Configuration
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.text_field_label

@Composable
fun <T> DeleteDialog(
    item: T,
    message: String,
    title: @Composable (() -> Unit)? = null,
    onDelete: (T) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = title,
        text = { Text(text = message) },
        confirmButton = {
            AppButton(
                text = stringResource(R.string.dialog_button_confirm),
                onClick = { onDelete(item) }
            )
        },
        dismissButton = {
            AppButton(
                text = stringResource(R.string.dialog_button_cancel),
                onClick = onDismiss
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview("default", "round")
@Preview("dark theme", "round", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", "round", fontScale = 2f)
@Composable
private fun ButtonPreview() {
    BallerAppTheme() {
        DeleteDialog(
            item = "Delete Item",
            title = {
                AppText(text = "Delete title")
            },
            message = "Do you really want to kill me?",
            onDelete = {

            }) {
        }
    }
}