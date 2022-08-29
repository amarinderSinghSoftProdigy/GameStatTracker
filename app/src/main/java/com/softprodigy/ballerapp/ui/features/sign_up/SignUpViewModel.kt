package com.softprodigy.ballerapp.ui.features.sign_up

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.ui.features.confirm_phone.VerifyPhoneChannel
import com.softprodigy.ballerapp.ui.features.confirm_phone.VerifyPhoneUIEvent
import com.softprodigy.ballerapp.ui.features.confirm_phone.VerifyPhoneUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private var IUserRepository: IUserRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _signUpChannel = Channel<SignUpChannel>()
    val uiEvent = _signUpChannel.receiveAsFlow()

    private val _signUpUiState = mutableStateOf(SignUpUIState())
    val signUpUiState: State<SignUpUIState> = _signUpUiState

    fun onEvent(event: SignUpUIEvent) {
        when (event) {
            is SignUpUIEvent.Submit -> {
                signUp(event.signUpData)
            }

        }
    }

    private fun signUp(signUpData: SignUpData) {


        viewModelScope.launch {
            _signUpUiState.value = SignUpUIState(isLoading = true)

            val verifyResponseResponse =
                IUserRepository.signUp(signUpData)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _signUpUiState.value =
                                SignUpUIState(
                                    isLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage!!
                                    )
                                )
                            )

                        } else {
                            _signUpUiState.value = SignUpUIState(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = signUpUiState.value.copy(isLoading = false)
                    _signUpUiState.value =
                        SignUpUIState(
                            errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}",
                            isLoading = false
                        )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        SignUpUIState(
                            errorMessage = "${verifyResponseResponse.message}",
                            isLoading = false
                        )
                    _signUpChannel.send(
                        SignUpChannel.ShowToast(
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

sealed class SignUpChannel {
    data class ShowToast(val message: UiText) : SignUpChannel()
//    data class OnLoginSuccess(val loginResponse: UserInfo) : VerifyPhoneChannel()

}