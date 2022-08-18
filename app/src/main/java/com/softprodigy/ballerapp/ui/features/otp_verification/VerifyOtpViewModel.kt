package com.softprodigy.ballerapp.ui.features.otp_verification

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiEvent
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.ForgotPasswordResponse
import com.softprodigy.ballerapp.data.response.VerifyOtpResponse
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private var repository: VerifyOtpRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _verifyOtpUiState = mutableStateOf(VerifyOtpUIState())
    val verifyOtpUiState: State<VerifyOtpUIState> = _verifyOtpUiState

    private val _resendPassUiState = mutableStateOf(ForgotPasswordUIState())
    val resendPassUiState: State<ForgotPasswordUIState> = _resendPassUiState

    var otpResponse: VerifyOtpResponse? = null
        private set

    var forgotResponse: ForgotPasswordResponse? = null
        private set

    fun onEvent(event: VerifyOtpUIEvent) {
        when (event) {

            is VerifyOtpUIEvent.Submit -> {
                confirm(event.otp, event.token)
            }
            is VerifyOtpUIEvent.ResendOtp -> {
                resendOtp(event.email)
            }
        }
    }

    private fun resendOtp(email: String) {

        viewModelScope.launch {

            _resendPassUiState.value = resendPassUiState.value.copy(isLoading = true)

            val resendPassResponse =
                repository.resendOtp(
                    email = email,
                    mobile = "",
                    otpType = "email"
                )
            when (resendPassResponse) {
                is ResultWrapper.NetworkError -> {

                    _resendPassUiState.value =
                        ForgotPasswordUIState(errorMessage = resendPassResponse.message)
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString(resendPassResponse.message)))
                }
                is ResultWrapper.GenericError -> {

                    _resendPassUiState.value = resendPassUiState.value.copy(isLoading = false)

                    _resendPassUiState.value =
                        ForgotPasswordUIState(errorMessage = "${resendPassResponse.code} ${resendPassResponse.message}")
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString("${resendPassResponse.code} ${resendPassResponse.message}")))

                }
                is ResultWrapper.Success -> {

                    resendPassResponse.value.let { response ->
                        if (response.status == 200) {
                            _resendPassUiState.value =
                                ForgotPasswordUIState(message = response.message)
                            forgotResponse = response
                            _uiEvent.send(UiEvent.GenerateOtp)
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

    private fun confirm(otp: String, token: String) {

        viewModelScope.launch {

            _verifyOtpUiState.value = verifyOtpUiState.value.copy(isLoading = true)

            val verifyOtpResponse =
                repository.verifyOtp(
                    token = token,
                    otp = otp,
                    otpType = "email"
                )

            when (verifyOtpResponse) {

                is ResultWrapper.NetworkError -> {
                    _verifyOtpUiState.value =
                        VerifyOtpUIState(errorMessage = verifyOtpResponse.message)
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString(verifyOtpResponse.message)))
                }

                is ResultWrapper.GenericError -> {
                    _verifyOtpUiState.value = verifyOtpUiState.value.copy(isLoading = false)
                    _verifyOtpUiState.value =
                        verifyOtpUiState.value.copy(errorMessage = verifyOtpResponse.message)
                    _uiEvent.send(UiEvent.ShowToast(UiText.DynamicString("${verifyOtpResponse.code} ${verifyOtpResponse.message}")))
                }

                is ResultWrapper.Success -> {
                    _verifyOtpUiState.value = verifyOtpUiState.value.copy(isLoading = false)
                    verifyOtpResponse.value.let { response ->
                        if (response.status == 200) {
                            _verifyOtpUiState.value =
                                VerifyOtpUIState(message = response.message)
                            otpResponse = response
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