package com.softprodigy.ballerapp.ui.features.welcome

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val socialLoginRepo: SocialLoginRepo) :
    ViewModel() {
    private val _welcomeChannel = Channel<WelcomeChannel>()
    val welcomeChannel = _welcomeChannel.receiveAsFlow()

    private val _welcomeUiState = mutableStateOf(WelcomeUIState())
    val welcomeUiState: State<WelcomeUIState> = _welcomeUiState


    fun onEvent(event: WelcomeUIEvent) {
        when (event) {
            is WelcomeUIEvent.OnGoogleClick -> {
                viewModelScope.launch {
                    _welcomeUiState.value = WelcomeUIState(isDataLoading = true)
                    when (val response = socialLoginRepo.loginWithGoogle(event.googleUser)) {
                        is ResultWrapper.Success -> {
                            _welcomeChannel.send(WelcomeChannel.OnGoogleLoginSuccess(response.value))
                        }
                        is ResultWrapper.GenericError -> {
                            _welcomeChannel.send(WelcomeChannel.OnFailure("${response.code} ${response.message}"))
                        }
                        is ResultWrapper.NetworkError -> {
                            _welcomeChannel.send(WelcomeChannel.OnFailure(response.message))
                        }
                    }
                    _welcomeUiState.value = WelcomeUIState(isDataLoading = false)

                }
            }
        }
    }
}

sealed class WelcomeChannel {
    data class OnGoogleLoginSuccess(val loginResponse: UserInfo) : WelcomeChannel()
    data class OnFailure(val message: String) : WelcomeChannel()
}