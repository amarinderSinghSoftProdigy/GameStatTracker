package com.softprodigy.ballerapp.ui.features.home

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.HomeItemResponse
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

    init {

        getHomeList()

    }

    private fun getHomeList() {
        val homeList = ArrayList<HomeItemResponse>()
        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_home,
                item = "Opportunities To Play",
                total = "4"
            )
        )
        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_league,
                item = "All Leagues",
                total = "15"
            )
        )
        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_work,
                item = "Opportunities To Work",
                total = "2"
            )
        )

        _state.value = _state.value.copy(homeItemList = homeList)
    }

    fun setColor(color: Color) {
        _state.value = _state.value.copy(color = color)
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