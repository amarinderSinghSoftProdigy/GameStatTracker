package com.softprodigy.ballerapp.ui.features.login

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.ResultWrapper.GenericError
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val IUserRepository: IUserRepository,
    application: Application
) :
    AndroidViewModel(application) {
    private val _loginChannel = Channel<LoginChannel>()
    val loginChannel = _loginChannel.receiveAsFlow()

    private val _loginUiState = mutableStateOf(LoginUIState())
    val loginUiState: State<LoginUIState> = _loginUiState

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.Submit -> {
                login(
                    LoginRequest(
                        email = event.email,
                        password = event.password,
                        loginType = ApiConstants.EMAIL
                    )
                )
            }
            is LoginUIEvent.OnFacebookClick -> {
                login(
                    LoginRequest(
                        email = event.socialUser.email,
                        loginType = ApiConstants.FACEBOOK,
                        facebookId = event.socialUser.id
                    )
                )
            }
            is LoginUIEvent.OnGoogleClick -> {
                login(
                    LoginRequest(
                        email = event.socialUser.email,
                        loginType = ApiConstants.GOOGLE,
                        googleId = event.socialUser.id
                    )
                )
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginUiState.value = LoginUIState(isDataLoading = true)

            val loginResponse =
                IUserRepository.userLogin(loginRequest)

            when (loginResponse) {
                is ResultWrapper.Success -> {
                    loginResponse.value.let { response ->
                        if (response.status) {
                            _loginUiState.value =
                                LoginUIState(
                                    user = response.data,
                                    isDataLoading = false,
                                    errorMessage = null
                                )
                            _loginChannel.send(LoginChannel.OnLoginSuccess(response.data))
                        } else {
                            _loginUiState.value = LoginUIState(
                                user = null,
                                errorMessage = response.statusMessage,
                                isDataLoading = false
                            )
                        }
                }
                }
                is GenericError -> {
                    _loginUiState.value = loginUiState.value.copy(isDataLoading = false)
                    _loginUiState.value =
                        LoginUIState(
                            user=null,
                            errorMessage = "${loginResponse.code} ${loginResponse.message}",
                            isDataLoading = false
                        )
                    _loginChannel.send(LoginChannel.ShowToast(UiText.DynamicString("${loginResponse.code} ${loginResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _loginUiState.value =
                        LoginUIState(
                            user = null,
                            errorMessage = loginResponse.message,
                            isDataLoading = false
                        )
                    _loginChannel.send(LoginChannel.ShowToast(UiText.DynamicString(loginResponse.message)))
                }

            }
        }
    }
}

sealed class LoginChannel {
    data class ShowToast(val message: UiText) : LoginChannel()
    data class OnLoginSuccess(val loginResponse: UserInfo) : LoginChannel()
}