package com.softprodigy.ballerapp.ui.features.confirm_phone

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.GlobalRequest
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmPhoneViewModel @Inject constructor(
    private var IUserRepository: IUserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _verifyPhoneChannel = Channel<VerifyPhoneChannel>()
    val uiEvent = _verifyPhoneChannel.receiveAsFlow()

    private val _verifyPhoneUiState = mutableStateOf(VerifyPhoneUIState())
    val verifyPhoneUiState: State<VerifyPhoneUIState> = _verifyPhoneUiState
    val verified = mutableStateOf(false)
    var profileData by mutableStateOf(GlobalRequest.SetupProfile())
        private set

    fun saveProfileData(request: GlobalRequest.SetupProfile) {
        profileData = request
    }


    fun onEvent(event: VerifyPhoneUIEvent) {
        when (event) {
            is VerifyPhoneUIEvent.Submit -> {
                verifyPhone(event.phoneNumber)
            }
            is VerifyPhoneUIEvent.Confirm -> {
                confirmPhone(event.phoneNumber, event.otp)
            }
        }
    }

    private fun verifyPhone(phone: String) {
        viewModelScope.launch {
            _verifyPhoneUiState.value = VerifyPhoneUIState(isDataLoading = true)

            val verifyResponseResponse =
                IUserRepository.verifyPhone(phone = phone)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _verifyPhoneUiState.value =
                                VerifyPhoneUIState(
                                    isDataLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            _verifyPhoneChannel.send(
                                VerifyPhoneChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage!!
                                    )
                                )
                            )

                        } else {
                            _verifyPhoneUiState.value = VerifyPhoneUIState(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isDataLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _verifyPhoneUiState.value = verifyPhoneUiState.value.copy(isDataLoading = false)
                    _verifyPhoneUiState.value =
                        VerifyPhoneUIState(
                            errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}",
                            isDataLoading = false
                        )
                    _verifyPhoneChannel.send(VerifyPhoneChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _verifyPhoneUiState.value =
                        VerifyPhoneUIState(
                            errorMessage = "${verifyResponseResponse.message}",
                            isDataLoading = false
                        )
                    _verifyPhoneChannel.send(
                        VerifyPhoneChannel.ShowToast(
                            UiText.DynamicString(
                                verifyResponseResponse.message
                            )
                        )
                    )
                }
            }
        }
    }

    private fun confirmPhone(phone: String, otp: String) {
        viewModelScope.launch {
            _verifyPhoneUiState.value = VerifyPhoneUIState(isDataLoading = true)

            val verifyResponseResponse =
                IUserRepository.confirmPhone(phone = phone, otp = otp)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _verifyPhoneUiState.value =
                                VerifyPhoneUIState(
                                    isDataLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            verified.value = response.status

                            _verifyPhoneChannel.send(
                                VerifyPhoneChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage!!
                                    )
                                )
                            )

                        } else {
                            _verifyPhoneUiState.value = VerifyPhoneUIState(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isDataLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _verifyPhoneUiState.value = verifyPhoneUiState.value.copy(isDataLoading = false)
                    _verifyPhoneUiState.value =
                        VerifyPhoneUIState(
                            errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}",
                            isDataLoading = false
                        )
                    _verifyPhoneChannel.send(VerifyPhoneChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _verifyPhoneUiState.value =
                        VerifyPhoneUIState(
                            errorMessage = "${verifyResponseResponse.message}",
                            isDataLoading = false
                        )
                    _verifyPhoneChannel.send(
                        VerifyPhoneChannel.ShowToast(
                            UiText.DynamicString(
                                verifyResponseResponse.message
                            )
                        )
                    )
                }
            }
        }
    }
}

sealed class VerifyPhoneChannel {
    data class ShowToast(val message: UiText) : VerifyPhoneChannel()
//    data class OnLoginSuccess(val loginResponse: UserInfo) : VerifyPhoneChannel()

}