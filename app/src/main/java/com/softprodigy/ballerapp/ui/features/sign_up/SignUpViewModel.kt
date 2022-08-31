package com.softprodigy.ballerapp.ui.features.sign_up

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.BuildConfig
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUri
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private var IUserRepository: IUserRepository,
    private val imageUploadRepo: IImageUploadRepo,
    application: Application
) : AndroidViewModel(application) {

    private val _signUpChannel = Channel<SignUpChannel>()
    val uiEvent = _signUpChannel.receiveAsFlow()

    private val _signUpUiState = mutableStateOf(SignUpUIState())
    val signUpUiState: State<SignUpUIState> = _signUpUiState

    val verified = mutableStateOf(false)


    fun onEvent(event: SignUpUIEvent) {
        when (event) {

            is SignUpUIEvent.OnFirstNameChanged -> {
                _signUpUiState.value = _signUpUiState.value.copy(firstName = event.firstName)
            }

            is SignUpUIEvent.OnLastNameChanged -> {
                _signUpUiState.value = _signUpUiState.value.copy(lastName = event.lastName)
            }

            is SignUpUIEvent.OnEmailChanged -> {
                _signUpUiState.value = _signUpUiState.value.copy(email = event.email)
            }

            is SignUpUIEvent.OnPhoneNumberChanged -> {
                _signUpUiState.value = _signUpUiState.value.copy(phoneNumber = event.phoneNumber)
            }

            is SignUpUIEvent.OnImageSelected -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(profileImageUri = event.profileImageUri)
            }

            is SignUpUIEvent.OnImageUploadSuccess -> {
                viewModelScope.launch { signUp() }
            }

            is SignUpUIEvent.OnScreenNext -> {
                viewModelScope.launch { uploadTeamLogo() }
            }

            is SignUpUIEvent.OnSignUpDataSelected -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(signUpData = event.signUpData)
            }

            is SignUpUIEvent.OnVerifyNumber -> {
                verifyPhone()
            }

            is SignUpUIEvent.OnConfirmNumber -> {
                confirmPhone(event.phoneNumber, event.otp)
            }

        }
    }

    private suspend fun uploadTeamLogo() {

        val uri = Uri.parse(signUpUiState.value.profileImageUri)

        val file = getFileFromUri(getApplication<Application>().applicationContext, uri)

        when (val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
            type = AppConstants.PROFILE_IMAGE,
            file
        )) {
            is ResultWrapper.GenericError -> {
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.code} ${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            uploadLogoResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                uploadLogoResponse.value.let { response ->
                    if (response.status) {
                        _signUpUiState.value =
                            _signUpUiState.value.copy(profileImageServerUrl = "${BuildConfig.IMAGE_SERVER}${uploadLogoResponse.value.data.data}")
                        _signUpChannel.send(
                            SignUpChannel.OnProfileUpload
                        )
                    } else {
                        _signUpUiState.value =
                            _signUpUiState.value.copy(isLoading = false)
                        _signUpChannel.send(
                            SignUpChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun signUp() {
        val signUpData = signUpUiState.value.signUpData
        val signUpDataRequest = SignUpData(
            firstName = signUpUiState.value.firstName,
            lastName = signUpUiState.value.lastName,
            email = signUpUiState.value.email,
            profileImage = signUpUiState.value.profileImageServerUrl,
            phone = "+" + signUpUiState.value.phoneNumber,
            address = signUpData.address,
            phoneVerified = signUpData.phoneVerified,
            gender = signUpData.gender,
            birthdate = signUpData.birthdate,
            role = signUpData.role?.toLowerCase(),
            password = signUpData.password,
            repeatPassword = signUpData.repeatPassword
        )

        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)


            val verifyResponseResponse =
                IUserRepository.signUp(signUpDataRequest)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                isLoading = false,
                                errorMessage = null,
                                successMessage = response.statusMessage
                            )

                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )
                            _signUpChannel.send(
                                SignUpChannel.OnNextScreen(
                                    UiText.DynamicString(
                                        verifyResponseResponse.value.statusMessage
                                    )
                                )
                            )

                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(isLoading = false)
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
                            errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}",
                            isLoading = false
                        )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
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

    private fun verifyPhone() {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val verifyResponseResponse =
                IUserRepository.verifyPhone(phone = "+" + _signUpUiState.value.phoneNumber)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _signUpUiState.value =
                                _signUpUiState.value.copy(
                                    isLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )
                            _signUpChannel.send(SignUpChannel.OnOTPScreen)

                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        isLoading = false,
                        errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}"
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))

                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
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

    private fun confirmPhone(phone: String, otp: String) {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)


            val verifyResponseResponse =
                IUserRepository.confirmPhone(phone = phone, otp = otp)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _signUpUiState.value =
                                _signUpUiState.value.copy(
                                    isLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage
                                )

                            verified.value = response.status

                            _signUpChannel.send(
                                SignUpChannel.OnSuccess(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )

                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage ?: "Something went wrong",
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        isLoading = false,
                        errorMessage = "${verifyResponseResponse.code} ${verifyResponseResponse.message}",
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.code} ${verifyResponseResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
                            errorMessage = verifyResponseResponse.message,
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
    object OnProfileUpload : SignUpChannel()
    data class OnNextScreen(val message: UiText) : SignUpChannel()
    data class OnSuccess(val message: UiText) : SignUpChannel()
    object OnOTPScreen : SignUpChannel()
}