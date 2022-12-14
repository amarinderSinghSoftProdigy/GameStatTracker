package com.softprodigy.ballerapp.ui.features.home

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.HomeItemResponse
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.TopBarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    application: Application,
    val userRepo: IUserRepository
) :
    AndroidViewModel(application) {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _homeChannel = Channel<HomeChannel>()
    val homeChannel = _homeChannel.receiveAsFlow()

    init {

        getHomeList()
        viewModelScope.launch {
            getUserInfo()
        }
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

    fun setBottomNav(color: BottomNavKey) {
        _state.value = _state.value.copy(bottomBar = color)
    }
    fun setDialog(show: Boolean) {
        _state.value = _state.value.copy(showDialog = show)
    }

    fun setLogoutDialog(show: Boolean) {
        _state.value = _state.value.copy(showLogout = show)
    }
    fun setTopBar(topBar: TopBarData) {
        _state.value = _state.value.copy(topBar = topBar, appBar = true)
    }
    fun setScreen(screen: Boolean) {
        _state.value = _state.value.copy(screen = screen)
    }

    fun setAppBar(screen: Boolean) {
        _state.value = _state.value.copy(appBar = screen)
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
        }
    }

    private suspend fun getUserInfo() {
        _state.value = _state.value.copy(isDataLoading = true)
        val userResponse = userRepo.getUserProfile()
        _state.value = _state.value.copy(isDataLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _homeChannel.send(
                    HomeChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _homeChannel.send(
                    HomeChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status) {
                        _state.value =
                            _state.value.copy(
                                user = response.data
                            )
                    } else {
                        _homeChannel.send(
                            HomeChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }

    }
}

sealed class HomeChannel {
    data class ShowToast(val message: UiText) : HomeChannel()
}
