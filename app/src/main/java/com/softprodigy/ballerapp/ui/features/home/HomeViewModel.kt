package com.softprodigy.ballerapp.ui.features.home

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun setColor(color: Color) {
        _state.value = _state.value.copy(color = color)
    }

    fun setScreen(screen: Boolean) {
        _state.value = _state.value.copy(screen = screen)
    }
}