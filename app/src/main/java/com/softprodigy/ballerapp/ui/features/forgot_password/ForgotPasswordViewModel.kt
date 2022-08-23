package com.softprodigy.ballerapp.ui.features.forgot_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiEvent
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.AppConstants

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.ForgotPasswordResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private var forgotPasswordRepository: ForgotPasswordRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _forgotPassUiState = mutableStateOf(ForgotPasswordUIState())
    val forgotPassUiState: State<ForgotPasswordUIState> = _forgotPassUiState

    var forgotResponse: ForgotPasswordResponse? = null
        private set

    fun onEvent(event: ForgotPasswordUIEvent) {
        when (event) {
            is ForgotPasswordUIEvent.Submit -> {
                submit(event.email)
            }
        }
    }

    private fun submit(email: String) {
        viewModelScope.launch {

            _forgotPassUiState.value = forgotPassUiState.value.copy(isLoading = true)

            val forgotPassResponse =
                forgotPasswordRepository.forgotPassword(
                    email = email,
                )
            when (forgotPassResponse) {
                is ResultWrapper.NetworkError -> {

                    _forgotPassUiState.value =
                        ForgotPasswordUIState(errorMessage = forgotPassResponse.message)
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString(forgotPassResponse.message)))
                }
                is ResultWrapper.GenericError -> {

                    _forgotPassUiState.value = forgotPassUiState.value.copy(isLoading = false)

                    _forgotPassUiState.value =
                        ForgotPasswordUIState(errorMessage = "${forgotPassResponse.code} ${forgotPassResponse.message}")
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString("${forgotPassResponse.code} ${forgotPassResponse.message}")))

                }
                is ResultWrapper.Success -> {

                    forgotPassResponse.value.let { response ->
                        if (response.status == 200) {
                            _forgotPassUiState.value =
                                ForgotPasswordUIState(message = response.message)
                            forgotResponse = response
                            _uiEvent.send(UiEvent.Success)
                        } else {
                            _uiEvent.send(
                                UiEvent.ShowToast(
                                    UiText.DynamicString(
                                        response.message ?: AppConstants.DEFAULT_ERROR_MESSAGE
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}