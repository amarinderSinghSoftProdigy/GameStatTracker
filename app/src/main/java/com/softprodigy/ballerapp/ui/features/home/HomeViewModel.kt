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
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.HomeItemResponse
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.TopBarData
import com.softprodigy.ballerapp.ui.features.components.UserType
import com.softprodigy.ballerapp.ui.features.home.home_screen.HomeScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val userRepo: IUserRepository, application: Application,
) : AndroidViewModel(application) {
    var searchJob: Job? = null

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _homeChannel = Channel<HomeChannel>()
    val homeChannel = _homeChannel.receiveAsFlow()

    init {
        viewModelScope.launch {

            if (!UserStorage.role.equals(UserType.REFEREE.key, ignoreCase = true)) {

                _state.value = _state.value.copy(
                    players = arrayListOf(
                        Player(_id = "1", name = "Neeraj", profileImage = null),
                        Player(_id = "2", name = "Kausha;", profileImage = null),
                        Player(_id = "3", name = "Amrinder", profileImage = null),
                        Player(_id = "4", name = "Harsh", profileImage = null),
                    ),
                    selectedPlayer = Player(_id = "1", name = "Neeraj", profileImage = null)
                )
                getHomeList()
                getUserInfo()

            }

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
        _state.value = _state.value.copy(bottomBar = color, showDialog = false)
    }

    fun setDialog(show: Boolean) {
        _state.value = _state.value.copy(showDialog = show)
    }

    fun setLogoutDialog(show: Boolean) {
        _state.value = _state.value.copy(showLogout = show)
    }

    fun setSwapProfile(show: Boolean) {
        _state.value = _state.value.copy(showSwapProfile = show)
    }

    fun setAddProfile(show: Boolean) {
        _state.value = _state.value.copy(showAddProfile = show)
    }

    fun setTopBar(topBar: TopBarData) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.value = _state.value.copy(topBar = topBar, showTopAppBar = true)
        }
    }

    fun setScreen(screen: Boolean) {
        _state.value = _state.value.copy(screen = screen)
    }

    fun setTopAppBar(screen: Boolean) {
        _state.value = _state.value.copy(showTopAppBar = screen)
    }

    fun showBottomAppBar(showBottomAppBar: Boolean) {
        _state.value = _state.value.copy(showBottomAppBar = showBottomAppBar)
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
            dataStoreManager.setColor("")
            dataStoreManager.setTeamName("")
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnSwapClick -> {
                viewModelScope.launch {
                    getSwapProfiles()
                }
            }
            is HomeScreenEvent.OnSwapUpdate -> {
                viewModelScope.launch {
                    updateProfileToken(event.userId)
                }
            }
        }
    }

    private suspend fun getUserInfo(showToast: Boolean = false) {
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
                        UserStorage.userId = response.data._Id
                        UserStorage.role = response.data.role
                        setRole(response.data.role, response.data.email)
                        if (showToast) {
                            _homeChannel.send(
                                HomeChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )
                        }
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

    private suspend fun getSwapProfiles() {
        _state.value = _state.value.copy(isDataLoading = true)
        val userResponse = userRepo.getSwapProfiles()
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
                                swapUsers = response.data
                            )
                        _homeChannel.send(
                            HomeChannel.OnSwapListSuccess
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

    private suspend fun updateProfileToken(userId: String) {
        _state.value = _state.value.copy(isDataLoading = true)
        val userResponse = userRepo.updateProfileToken(userId)
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
                        setToken(response.data)
                        getUserInfo(true)
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

    private fun setToken(token: String) {
        viewModelScope.launch {
            UserStorage.token = token
            dataStoreManager.saveToken(token)
        }
    }

    private fun setRole(role: String, email: String) {
        viewModelScope.launch {
            dataStoreManager.setRole(role)
            dataStoreManager.setEmail(email)
        }
    }
}

sealed class HomeChannel {
    data class ShowToast(val message: UiText) : HomeChannel()
    object OnSwapListSuccess : HomeChannel()
}
