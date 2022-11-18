package com.allballapp.android.ui.features.home

import androidx.compose.ui.graphics.Color
import com.allballapp.android.data.response.HomeItemResponse
import com.allballapp.android.data.response.SwapUser
import com.allballapp.android.data.response.User
import com.allballapp.android.data.response.homepage.HomePageCoachModel
import com.allballapp.android.ui.features.components.BottomNavKey
import com.allballapp.android.ui.features.components.TopBar
import com.allballapp.android.ui.features.components.TopBarData


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
    val phone: String = "",
    val swapUsers: List<SwapUser> = mutableListOf(),
    val homePageCoachModel: HomePageCoachModel = HomePageCoachModel(),
    val unReadMessageCount: Int = 0,
    val unreadUsersGroupsIds: List<String> = arrayListOf(),
    val showNoInternetDialog: Boolean = false,
    )
