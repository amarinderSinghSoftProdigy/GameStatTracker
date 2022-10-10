package com.softprodigy.ballerapp.ui.features.home

import androidx.compose.ui.graphics.Color
import com.softprodigy.ballerapp.data.response.HomeItemResponse
import com.softprodigy.ballerapp.data.response.SwapUser
import com.softprodigy.ballerapp.data.response.User
import com.softprodigy.ballerapp.ui.features.components.BottomNavKey
import com.softprodigy.ballerapp.ui.features.components.TopBar
import com.softprodigy.ballerapp.ui.features.components.TopBarData


data class HomeState(
    val color: Color? = null,
    val screen: Boolean = false,
    val appBar: Boolean = false,
    val isDataLoading: Boolean = false,
    val showTopAppBar: Boolean = false,
    val showBottomAppBar: Boolean = false,
    val errorMessage: String? = null,
    val userNameValid: Boolean = true,
    val userNameValidError: String? = null,
    val topBar: TopBarData = TopBarData("", TopBar.EMPTY),
    val showDialog: Boolean = false,
    val showLogout: Boolean = false,
    val showSwapProfile: Boolean = false,
    //val showAddProfile: Boolean = false,
    val bottomBar: BottomNavKey = BottomNavKey.HOME,
    val homeItemList: List<HomeItemResponse> = arrayListOf(),
    val user: User = User(),
    val swapUsers: List<SwapUser> = mutableListOf(),
)
