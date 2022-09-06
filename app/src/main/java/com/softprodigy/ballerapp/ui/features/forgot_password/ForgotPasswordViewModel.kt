package com.softprodigy.ballerapp.ui.features.forgot_password

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private var IUserRepository: IUserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _forgotPasswordChannel = Channel<ForgotPasswordChannel>()
    val uiEvent = _forgotPasswordChannel.receiveAsFlow()

    private val _forgotPasswordUiState = mutableStateOf(ForgotPasswordUIState())
    val forgotPasswordUiState: State<ForgotPasswordUIState> = _forgotPasswordUiState

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {

            is ForgotPasswordEvent.OnForgotPassword -> {
                viewModelScope.launch { forgotPasswordPassword(event.email) }
            }
        }
    }

    private suspend fun forgotPasswordPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordUiState.value = ForgotPasswordUIState(isLoading = true)

            val forgotResponse =
                IUserRepository.forgotPassword(email)

            when (forgotResponse) {

                is ResultWrapper.Success -> {
                    forgotResponse.value.let { response ->

                        if (response.status) {
                            _forgotPasswordUiState.value =
                                ForgotPasswordUIState(
                                    isLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            _forgotPasswordChannel.send(
                                ForgotPasswordChannel.OnSuccess(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )

                        } else {
                            _forgotPasswordUiState.value = ForgotPasswordUIState(
                                errorMessage = response.statusMessage,
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _forgotPasswordUiState.value =
                        forgotPasswordUiState.value.copy(isLoading = false)
                    _forgotPasswordUiState.value =
                        ForgotPasswordUIState(
                            errorMessage = "${forgotResponse.message}",
                            isLoading = false
                        )
                    _forgotPasswordChannel.send(
                        ForgotPasswordChannel.ShowToast(
                            UiText.DynamicString(
                                "${forgotResponse.message}"
                            )
                        )
                    )
                }
                is ResultWrapper.NetworkError -> {
                    _forgotPasswordUiState.value =
                        ForgotPasswordUIState(
                            errorMessage = forgotResponse.message,
                            isLoading = false
                        )
                    _forgotPasswordChannel.send(
                        ForgotPasswordChannel.ShowToast(
                            UiText.DynamicString(
                                forgotResponse.message
                            )
                        )
                    )
                }
            }
        }
    }
}

sealed class ForgotPasswordChannel {
    data class ShowToast(val message: UiText) : ForgotPasswordChannel()
    data class OnSuccess(val message: UiText) : ForgotPasswordChannel()
}