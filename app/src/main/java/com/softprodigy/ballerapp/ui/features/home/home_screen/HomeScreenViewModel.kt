package com.softprodigy.ballerapp.ui.features.home.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val teamRepo: ITeamRepository) : ViewModel() {
    /*var homeScreenState = mutableStateOf(HomeScreenState())
        private set*/

    private val _homeUiState = mutableStateOf(HomeScreenState())
    val homeScreenState: State<HomeScreenState> = _homeUiState

    private val _homeScreenChannel = Channel<HomeScreenChannel>()
    val homeScreenChannel = _homeScreenChannel.receiveAsFlow()

    /*fun onEvent(event: HomeScreenEvent) {
        when(event){
            HomeScreenEvent.OnPendingInvitationClick -> {

            }
        }

    }
    init {
        viewModelScope.launch {
            getHomePageDetails()
        }
    }*/

    suspend fun getHomePageDetails() {
        when (val homeResponse = teamRepo.getHomePageDetails()) {
            is ResultWrapper.GenericError -> {
                _homeScreenChannel.send(
                    HomeScreenChannel.ShowToast(
                        UiText.DynamicString(
                            "${homeResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _homeScreenChannel.send(
                    HomeScreenChannel.ShowToast(
                        UiText.DynamicString(
                            homeResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                homeResponse.value.let { baseResponse ->
                    if (baseResponse.status) {
                        _homeUiState.value =
                            _homeUiState.value.copy(homePageCoachModel = baseResponse.data)
                    }
                }
            }
        }
    }

    sealed class HomeScreenChannel {
        data class ShowToast(val message: UiText) : HomeScreenChannel()
    }

}