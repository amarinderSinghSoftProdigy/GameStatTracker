package com.allballapp.android.ui.features.home.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.allballapp.android.core.util.UiText
import com.allballapp.android.domain.repository.ITeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(val teamRepo: ITeamRepository) : ViewModel() {


    private val _homeUiState = mutableStateOf(HomeScreenState())
    val homeScreenState: State<HomeScreenState> = _homeUiState

    private val _homeScreenChannel = Channel<HomeScreenChannel>()
    val homeScreenChannel = _homeScreenChannel.receiveAsFlow()

    sealed class HomeScreenChannel {
        data class ShowToast(val message: UiText) : HomeScreenChannel()
    }

}