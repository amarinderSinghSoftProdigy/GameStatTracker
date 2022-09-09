package com.softprodigy.ballerapp.ui.features.home

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.ui.features.components.TopBarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    application: Application
) :
    AndroidViewModel(application) {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun setColor(color: Color) {
        _state.value = _state.value.copy(color = color)
    }

    fun setTopBar(topBar: TopBarData) {
        _state.value = _state.value.copy(topBar = topBar)
    }

    fun setDialog(show: Boolean) {
        _state.value = _state.value.copy(showDialog = show)
    }

    fun setLogoutDialog(show: Boolean) {
        _state.value = _state.value.copy(showLogout = show)
    }

    fun setScreen(screen: Boolean) {
        _state.value = _state.value.copy(screen = screen)
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
        }
    }
}