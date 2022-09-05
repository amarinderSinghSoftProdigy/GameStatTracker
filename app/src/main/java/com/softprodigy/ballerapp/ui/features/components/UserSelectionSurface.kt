package com.softprodigy.ballerapp.ui.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun UserSelectionSurface(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    isSelected: Boolean,
    contentPadding: PaddingValues = PaddingValues(
        vertical = dimensionResource(id = R.dimen.size_16dp)
    )
) {
    Box(modifier = modifier
        .clickable { onClick() }
        .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp)))
        .border(
            width = if (!isSelected) 1.dp else 0.dp,
            if (!isSelected) MaterialTheme.appColors.buttonColor.bckgroundDisabled else MaterialTheme.appColors.buttonColor.bckgroundEnabled,
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8dp))
        )
        .background(
            color = if (isSelected) {
                MaterialTheme.appColors.buttonColor.bckgroundEnabled
            } else {
                Color.Transparent
            }
        )
    ) {
        androidx.compose.material.Text(
            text = text,
            fontSize = dimensionResource(id = R.dimen.txt_size_16).value.sp,
            fontWeight = FontWeight.W600,
            color = if (isSelected) {
                MaterialTheme.appColors.buttonColor.textEnabled
            } else {
                MaterialTheme.appColors.buttonColor.textDisabled
            },
            modifier = Modifier
                .padding(contentPadding)
                .align(
                    Alignment.Center
                )
        )
    }
}