package com.softprodigy.ballerapp.ui.features.create_new_password

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiEvent
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.ResetPasswordResponse

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private var resetPasswordRepository: ResetPasswordRepository) :
    ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _resetPassUiState = mutableStateOf(ResetPasswordUIState())
    val resetPassUiState: State<ResetPasswordUIState> = _resetPassUiState

    var resetPasswordResponse: ResetPasswordResponse? = null
        private set

    fun onEvent(event: ResetPasswordUIEvent) {
        when (event) {

            is ResetPasswordUIEvent.Submit -> {

                submit(event.token, event.password)

            }
        }
    }

    private fun submit(token: String, password: String) {
        viewModelScope.launch {

            _resetPassUiState.value = resetPassUiState.value.copy(isLoading = true)

            val resetPassResponse =
                resetPasswordRepository.resetPassword(
                    token = token, password = password
                )
            when (resetPassResponse) {
                is ResultWrapper.NetworkError -> {
                    _resetPassUiState.value =
                        ResetPasswordUIState(errorMessage = resetPassResponse.message)
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString(resetPassResponse.message)))

                }
                is ResultWrapper.GenericError -> {
                    _resetPassUiState.value = resetPassUiState.value.copy(isLoading = false)

                    _resetPassUiState.value =
                        ResetPasswordUIState(errorMessage = "${resetPassResponse.code} ${resetPassResponse.message}")
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString("${resetPassResponse.code} ${resetPassResponse.message}")))

                }
                is ResultWrapper.Success -> {

                    resetPassResponse.value.let { response ->
                        if (response.status == 200) {
                            _resetPassUiState.value =
                                ResetPasswordUIState(message = response.message)
                            resetPasswordResponse = response
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