package com.softprodigy.ballerapp.ui.features.login

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.ResultWrapper.GenericError
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.ui.features.welcome.SocialLoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var loginRepository: LoginRepository,
    private var socialLoginRepo: SocialLoginRepo,
    application: Application
) :
    AndroidViewModel(application) {
    private val _loginChannel = Channel<LoginChannel>()
    val uiEvent = _loginChannel.receiveAsFlow()

    private val _loginUiState = mutableStateOf(LoginUIState())
    val loginUiState: State<LoginUIState> = _loginUiState

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.Submit -> {
                login(event.email, event.password)
            }
            is LoginUIEvent.OnGoogleClick -> {
                viewModelScope.launch {
                    _loginUiState.value = LoginUIState(isDataLoading = true)
                    when (val response = socialLoginRepo.loginWithGoogle(event.googleUser)) {
                        is ResultWrapper.Success -> {
                            _loginChannel.send(LoginChannel.OnLoginSuccess(response.value))
                        }
                        is ResultWrapper.GenericError -> {
                            _loginChannel.send(LoginChannel.ShowToast(UiText.DynamicString("${response.code} ${response.message}")))
                        }
                        is ResultWrapper.NetworkError -> {
                            _loginChannel.send(LoginChannel.ShowToast(UiText.DynamicString("${response.message}")))
                        }
                    }
                    _loginUiState.value = LoginUIState(isDataLoading = false)

                }
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginUiState.value = LoginUIState(isDataLoading = true)

            val loginResponse =
                loginRepository.loginWithEmailAndPass(
                    email = email,
                    password = password
                )
            when (loginResponse) {
                is ResultWrapper.Success ->{
                    loginResponse.value.let { response ->

                        if (response.status == 200) {
                            _loginUiState.value =
                                LoginUIState(
                                    user = response.userInfo,
                                    isDataLoading = false,
                                    errorMessage = null
                                )
                            _loginChannel.send(LoginChannel.OnLoginSuccess(response))
                        } else {
                            _loginUiState.value = LoginUIState(
                                user=null,
                                errorMessage = response.message ?: "Something went wrong",
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
                            user=null,
                            errorMessage = "${loginResponse.message}",
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
    data class OnLoginSuccess(val loginResponse: LoginResponse) : LoginChannel()

}