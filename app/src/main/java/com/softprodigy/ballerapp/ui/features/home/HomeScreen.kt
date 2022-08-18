package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(name: String?) {
    Box(Modifier.fillMaxSize()) {
        if (name != null && name != "null")
            Text(
                text = "Welcome $name",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        else {
            Text(
                text = "HOME SCREEN -- User not logged in",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}