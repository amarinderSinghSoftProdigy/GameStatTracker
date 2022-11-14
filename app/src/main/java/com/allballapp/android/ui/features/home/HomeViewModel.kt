package com.allballapp.android.ui.features.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.BuildConfig
import com.allballapp.android.R
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.CometChatErrorCodes
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.response.HomeItemResponse
import com.allballapp.android.domain.repository.ITeamRepository
import com.allballapp.android.domain.repository.IUserRepository
import com.allballapp.android.ui.features.components.BottomNavKey
import com.allballapp.android.ui.features.components.TopBarData
import com.allballapp.android.ui.features.home.home_screen.HomeScreenEvent
import com.allballapp.android.ui.utils.UiText
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val userRepo: IUserRepository,
    application: Application,
    val teamRepo: ITeamRepository
) : AndroidViewModel(application) {
    var searchJob: Job? = null

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private val _homeChannel = Channel<HomeChannel>()
    val homeChannel = _homeChannel.receiveAsFlow()

    init {

        viewModelScope.launch {
            if (UserStorage.token.isNotEmpty()) {
                getUserInfo()
            }
        }
    }

    private fun getHomeList() {
        val homeList = ArrayList<HomeItemResponse>()
        val state = _state.value.homePageCoachModel

        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_home,
                item = R.string.opportunities_to_play,
                total = state.opportunityToPlay.toString()
            )
        )
        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_league,
                item = R.string.all_leagues,
                total = state.allLeagues.toString()
            )
        )
        homeList.add(
            HomeItemResponse(
                image = R.drawable.ic_work,
                item = R.string.opportunities_to_work,
                total = state.opportunityToWork.toString()
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

    /*fun setAddProfile(show: Boolean) {
        _state.value = _state.value.copy(showAddProfile = show)
    }*/

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
        if (_state.value.showBottomAppBar != showBottomAppBar)
            _state.value = _state.value.copy(showBottomAppBar = showBottomAppBar)
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStoreManager.saveToken("")
            dataStoreManager.setId("")
            dataStoreManager.setRole("")
            dataStoreManager.setEmail("")
            dataStoreManager.setColor("")
            dataStoreManager.setTeamName("")
            logoutFromCometChat()
            UserStorage.userId = ""
            UserStorage.teamId = ""
            UserStorage.teamName = ""
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnSwapClick -> {
                viewModelScope.launch {
                    getSwapProfiles(event.check)
                }
            }
            is HomeScreenEvent.HideSwap -> {
                setSwapProfile(event.check)
            }
            is HomeScreenEvent.OnSwapUpdate -> {
                viewModelScope.launch {
                    updateProfileToken(event.userId)
                }
            }
            is HomeScreenEvent.OnNetworkAvailable -> {
                _state.value = _state.value.copy(showNoInternetDialog = !event.isAvailable)

                if (event.isAvailable) {
                    viewModelScope.launch {
                        getUserInfo()
                    }
                }
            }
        }
    }

    private suspend fun getUserInfo(showToast: Boolean = false) {
        _state.value = _state.value.copy(isDataLoading = true)
        val userResponse = userRepo.getUserProfile()


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
                /* _homeChannel.send(
                     HomeChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
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

                        _homeChannel.send(
                            HomeChannel.OnUserIdUpdate
                        )
                        /*Register newly updated user to cometchat*/
                        registerProfileToCometChat(
                            "${response.data.firstName} ${response.data.lastName}",
                            response.data._Id
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
        _state.value = _state.value.copy(isDataLoading = false)
    }

    private suspend fun getSwapProfiles(check: Boolean) {
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
                /* _homeChannel.send(
                     HomeChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value =
                            _state.value.copy(
                                swapUsers = response.data,
                                phone = (response.data.filter { it.phone.isNotEmpty() })[0].phone
                            )
                        if (check)
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
                /* _homeChannel.send(
                     HomeChannel.ShowToast(
                         UiText.DynamicString(
                             userResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        setToken(response.data)
                        getUserInfo(false)
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

    suspend fun getHomePageDetails(teamId: String) {
        _state.value = _state.value.copy(isDataLoading = true)
        val homeResponse = teamRepo.getHomePageDetails(teamId)
        _state.value = _state.value.copy(isDataLoading = false)
        when (homeResponse) {
            is ResultWrapper.GenericError -> {
                _homeChannel.send(
                    HomeChannel.ShowToast(
                        UiText.DynamicString(
                            "${homeResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /* _homeChannel.send(
                     HomeChannel.ShowToast(
                         UiText.DynamicString(
                             homeResponse.message
                         )
                     )
                 )*/
            }
            is ResultWrapper.Success -> {
                homeResponse.value.let { baseResponse ->
                    if (baseResponse.status && baseResponse.data != null) {
                        _state.value =
                            _state.value.copy(homePageCoachModel = baseResponse.data)
                        getHomeList()
                        /* _homeChannel.send(
                             HomeChannel.OnUserIdUpdate
                         )*/
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

    fun getUnreadMessageCount() {

        if (AppConstants.ENABLE_CHAT) {
            CometChat.getUnreadMessageCount(object :
                CometChat.CallbackListener<HashMap<String?, HashMap<String?, Int?>?>?>() {

                override fun onError(e: CometChatException) {
                }

                override fun onSuccess(stringHashMapHashMap: HashMap<String?, HashMap<String?, Int?>?>?) {

                    val count = (stringHashMapHashMap?.get("user")?.keys?.size ?: 0).plus(
                        stringHashMapHashMap?.get("group")?.keys?.size ?: 0
                    )
                    _state.value =
                        _state.value.copy(unReadMessageCount = count)
                    Timber.i(
                        "CometChatUI onSuccess: unread count $count -- stringHashMapHashMap$stringHashMapHashMap"
                    )

                }
            })
        }
    }

    /*Register newly updated user to cometchat*/
    private fun registerProfileToCometChat(name: String, uid: String) {

        if (AppConstants.ENABLE_CHAT) {
            val authKey = com.allballapp.android.BuildConfig.COMET_CHAT_AUTH_KEY
            val user = User()
            user.uid = uid
            user.name = name

            CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
                override fun onSuccess(user: User) {
                    Timber.i("CometChat- createUser $user")
                    /*It means user registered successfully so let's login  */
                    loginToCometChat(uid)
                }

                override fun onError(e: CometChatException) {
                    Timber.e("CometChat- createUser ${e.message}")
                    if (e.code.equals(CometChatErrorCodes.ERR_UID_ALREADY_EXISTS)) {

                        /*It means user already registered so login to his profile */
                        loginToCometChat(uid)


                    }
                }
            })
        }
    }

    /*Login registered user */
    private fun loginToCometChat(uid: String) {
        if (AppConstants.ENABLE_CHAT) {
            CometChat.login(
                uid,
                BuildConfig.COMET_CHAT_AUTH_KEY,
                object : CometChat.CallbackListener<User?>() {
                    override fun onSuccess(user: User?) {
                        Timber.i(" CometChat- Login Successful : " + user.toString())
                        getUnreadMessageCount()
                        registerTokenForPN()
                    }

                    override fun onError(e: CometChatException) {
                        Timber.e("CometChat- Login failed with exception:  ${e.message} ${e.code}");
                    }
                })
        }
    }

    private fun registerTokenForPN() {
        if (AppConstants.ENABLE_CHAT) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "registerTokenForPN",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                Timber.i("registerTokenForPN-- Token--  $token")

                CometChat.registerTokenForPushNotification(
                    token,
                    object : CometChat.CallbackListener<String?>() {
                        override fun onError(e: CometChatException) {
                            Log.e("onErrorPN: ", e.message!!)
                        }

                        override fun onSuccess(p0: String?) {
                            Log.e("ononSuccessPN: ", p0.toString())
                        }
                    })
            })


        }
    }


}

private fun logoutFromCometChat() {
    if (AppConstants.ENABLE_CHAT) {
        CometChat.logout(object : CometChat.CallbackListener<String?>() {
            override fun onError(e: CometChatException) {
                Timber.e("Logout failed with exception: " + e.message)
            }

            override fun onSuccess(p0: String?) {
                Timber.i("Logout completed successfully")
            }
        })
    }
}


sealed class HomeChannel {
    data class ShowToast(val message: UiText) : HomeChannel()
    object OnSwapListSuccess : HomeChannel()
    object OnUserIdUpdate : HomeChannel()
}


