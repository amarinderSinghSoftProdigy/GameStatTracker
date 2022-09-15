package com.softprodigy.ballerapp.ui.features.sign_up

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUri
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.SignUpData
import com.softprodigy.ballerapp.data.response.UserInfo
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private var IUserRepository: IUserRepository,
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStore: DataStoreManager,

    application: Application
) : AndroidViewModel(application) {

    private val _signUpChannel = Channel<SignUpChannel>()
    val signUpChannel = _signUpChannel.receiveAsFlow()

    private val _signUpUiState = mutableStateOf(SignUpUIState())
    val signUpUiState: State<SignUpUIState> = _signUpUiState

    fun onEvent(event: SignUpUIEvent) {
        when (event) {

            is SignUpUIEvent.OnFirstNameChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            firstName = event.firstName
                        )
                    )
            }

            is SignUpUIEvent.OnLastNameChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            lastName = event.lastName
                        )
                    )
            }

            is SignUpUIEvent.OnEmailChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            email = event.email
                        )
                    )
            }

            is SignUpUIEvent.OnPhoneNumberChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            phone = event.phoneNumber
                        )
                    )
            }

            is SignUpUIEvent.OnImageSelected -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            profileImageUri = event.profileImageUri
                        )
                    )
            }

            is SignUpUIEvent.OnImageUploadSuccess -> {
              /*  if (_signUpUiState.value.signUpData.token == null) {
                    viewModelScope.launch {
                        signUp()
                    }
                } else {
                    viewModelScope.launch {
                        updateUserProfile()
                    }
                }*/
                viewModelScope.launch {
                    updateUserProfile()
                }
            }

            is SignUpUIEvent.OnScreenNext -> {
                viewModelScope.launch {
                    if (_signUpUiState.value.isSocialUser) /*Means we have token to call upload profile pic*/
                        imageUpload()
                    else{
                        /*Means we don't have token to call upload profile pic*/
                        signUp()
                    }
                }
            }

            is SignUpUIEvent.OnSignUpDataSelected -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(signUpData = event.signUpData, registered = false)

                viewModelScope.launch {
                    _signUpChannel.send(SignUpChannel.OnSignUpSelected)
                }
            }

            is SignUpUIEvent.OnVerifyNumber -> {
                verifyPhone()
            }

            is SignUpUIEvent.OnConfirmNumber -> {
                confirmPhone(event.phoneNumber, event.otp)
            }
            is SignUpUIEvent.OnFacebookClick -> {
                login(
                    LoginRequest(
                        email = event.socialUser.email,
                        loginType = ApiConstants.FACEBOOK,
                        facebookId = event.socialUser.id
                    )
                )

            }
            is SignUpUIEvent.OnGoogleClick -> {
                login(
                    LoginRequest(
                        email = event.socialUser.email,
                        loginType = ApiConstants.GOOGLE,
                        googleId = event.socialUser.id
                    )
                )
            }
            is SignUpUIEvent.OnTwitterClick -> {
                login(
                    LoginRequest(
                        email = event.socialUser.email,
                        loginType = ApiConstants.TWITTER,
                        twitterId = event.socialUser.id
                    )
                )
            }
            is SignUpUIEvent.OnGenderChange -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            gender = event.gender
                        )
                    )
            }
            is SignUpUIEvent.OnAddressChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            address = event.address
                        )
                    )
            }
            is SignUpUIEvent.OnBirthdayChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            birthdate = event.birthday
                        )
                    )
            }

            is SignUpUIEvent.OnRoleChanged -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = _signUpUiState.value.signUpData.copy(
                            role = event.role
                        )
                    )
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val loginResponse =
                IUserRepository.userLogin(loginRequest)

            when (loginResponse) {
                is ResultWrapper.Success -> {
                    loginResponse.value.let { response ->
                        if (response.status) {
                            setToken(
                                response.data.token,
                                response.data.user.role,
                                response.data.user.email
                            )
                            setLoginDataToState(response.data)
                            _signUpChannel.send(SignUpChannel.OnLoginSuccess(response.data))
                            _signUpUiState.value = _signUpUiState.value.copy(
                                isSocialUser = true
                            )
                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage,
                                isLoading = false
                            )

                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        errorMessage = "${loginResponse.message}",
                        isLoading = false
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${loginResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        errorMessage = loginResponse.message,
                        isLoading = false
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString(loginResponse.message)))
                }

            }
        }
    }

    private fun setLoginDataToState(userInfo: UserInfo) {
        _signUpUiState.value =
            _signUpUiState.value.copy(
                isLoading = false,
                signUpData = _signUpUiState.value.signUpData.copy(
                    token = userInfo.token,
                    id = userInfo.user.id,
                    email = userInfo.user.email,
                    role = userInfo.user.role
                )
            )
    }

    private suspend fun imageUpload() {
        _signUpUiState.value =
            _signUpUiState.value.copy(isLoading = true)

        val uri = Uri.parse(signUpUiState.value.signUpData.profileImageUri)

        val file = getFileFromUri(getApplication<Application>().applicationContext, uri)

        file?.let {
            val size = Integer.parseInt((it.length() / 1024).toString())
            Timber.i("Filesize after compressiod--> $size")
        }
        val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
            type = AppConstants.PROFILE_IMAGE,
            file
        )
        _signUpUiState.value =
            _signUpUiState.value.copy(isLoading = false)

        when (uploadLogoResponse) {
            is ResultWrapper.GenericError -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(isLoading = false)
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(isLoading = false)
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
                            _signUpUiState.value.copy(
                                isLoading = false,
                                signUpData = _signUpUiState.value.signUpData.copy(
                                    profileImage = uploadLogoResponse.value.data.data
                                )
                            )
                        _signUpChannel.send(
                            SignUpChannel.OnProfileImageUpload
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
        _signUpUiState.value =
            _signUpUiState.value.copy(isLoading = true)
        val signUpData = signUpUiState.value.signUpData
        val signUpDataRequest = SignUpData(
            firstName = signUpData.firstName,
            lastName = signUpData.lastName,
            email = signUpData.email,
//            profileImage = signUpData.profileImage,
            phone = signUpUiState.value.phoneCode + signUpData.phone,
            address = signUpData.address,
            phoneVerified = signUpData.phoneVerified,
            gender = signUpData.gender,
            birthdate = signUpData.birthdate,
            role = signUpData.role?.lowercase(),
            password = signUpData.password,
            repeatPassword = signUpData.repeatPassword,
        )

        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val signUpResponse =
                IUserRepository.signUp(signUpDataRequest)
            _signUpUiState.value =
                _signUpUiState.value.copy(isLoading = false)
            when (signUpResponse) {

                is ResultWrapper.Success -> {

                    signUpResponse.value.let { response ->
                        if (response.status) {
                            setToken(
                                response.data.token,
                                response.data.user.role,
                                response.data.user.email
                            )
                            _signUpUiState.value = _signUpUiState.value.copy(
                                registered = true,
                                isLoading = false,
                                errorMessage = null,
                                successMessage = response.statusMessage
                            )
                          /*  _signUpChannel.send(
                                SignUpChannel.OnSignUpSuccess(
                                    UiText.DynamicString(
                                        signUpResponse.value.statusMessage
                                    )
                                )

                            )*/
                            imageUpload()

                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage,
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(isLoading = false)
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
                            errorMessage = "${signUpResponse.message}",
                            isLoading = false
                        )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${signUpResponse.message}")))
                }
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        _signUpUiState.value.copy(
                            errorMessage = signUpResponse.message,
                            isLoading = false
                        )
                    _signUpChannel.send(
                        SignUpChannel.ShowToast(
                            UiText.DynamicString(
                                signUpResponse.message
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun updateUserProfile() {
        val updateUserRequestData = signUpUiState.value.signUpData
        val signUpDataRequest = SignUpData(
            firstName = updateUserRequestData.firstName,
            lastName = updateUserRequestData.lastName,
            email = null, /*null data does not considered in request by rettrofit*/
            profileImage = updateUserRequestData.profileImage,
            phone = signUpUiState.value.phoneCode + updateUserRequestData.phone,
            address = updateUserRequestData.address,
            phoneVerified = updateUserRequestData.phoneVerified,
            gender = updateUserRequestData.gender,
            birthdate = updateUserRequestData.birthdate,
            role = updateUserRequestData.role?.lowercase(),
//            password = updateUserRequestData.password,
//            repeatPassword = updateUserRequestData.repeatPassword
        )
        when (val updateProfileResp = IUserRepository.updateUserProfile(signUpDataRequest)) {
            is ResultWrapper.GenericError -> {
                _signUpUiState.value = _signUpUiState.value.copy(
                    isLoading = false,
                    errorMessage = "${updateProfileResp.message}"
                )
                _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${updateProfileResp.message}")))

            }
            is ResultWrapper.NetworkError -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        errorMessage = updateProfileResp.message,
                        isLoading = false
                    )
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            updateProfileResp.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                updateProfileResp.value.let { response ->
                    if (response.status) {
                        setToken(
//                            token = signUpUiState.value.signUpData.token ?: "",
                            token = response.data.token,
                            role = signUpUiState.value.signUpData.role ?: "",
                            email = signUpUiState.value.signUpData.email ?: "",
                        )
                        _signUpUiState.value = _signUpUiState.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            successMessage = response.statusMessage
                        )
                        _signUpChannel.send(
                            SignUpChannel.OnProfileUpdateSuccess(
                                UiText.DynamicString(
                                    updateProfileResp.value.statusMessage
                                )
                            )
                        )

                    } else {
                        _signUpUiState.value = _signUpUiState.value.copy(
                            errorMessage = response.statusMessage,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun verifyPhone() {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val verifyResponseResponse =
                IUserRepository.verifyPhone(phone = signUpUiState.value.phoneCode + _signUpUiState.value.signUpData.phone)

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {

                            _signUpUiState.value = _signUpUiState.value.copy(
                                isLoading = false,
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
                                errorMessage = response.statusMessage,
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        isLoading = false,
                        errorMessage = "${verifyResponseResponse.message}"
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.message}")))

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

    private fun confirmPhone(phone: String, otp: String) {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val verifyResponseResponse =
                IUserRepository.confirmPhone(
                    phone = signUpUiState.value.phoneCode + phone,
                    otp = otp
                )

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->

                        if (response.status) {
                            _signUpUiState.value =
                                _signUpUiState.value.copy(
                                    isLoading = false,
                                    errorMessage = null,
                                    successMessage = response.statusMessage,
                                    signUpData = _signUpUiState.value.signUpData.copy(
                                        phoneVerified = response.status
                                    )
                                )
                            _signUpChannel.send(
                                SignUpChannel.OnSuccess(
                                    UiText.DynamicString(
                                        response.statusMessage
                                    )
                                )
                            )

                        } else {
                            _signUpUiState.value = _signUpUiState.value.copy(
                                errorMessage = response.statusMessage,
                                isLoading = false
                            )
                        }
                    }
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value = _signUpUiState.value.copy(
                        isLoading = false,
                        errorMessage = "${verifyResponseResponse.message}",
                    )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${verifyResponseResponse.message}")))
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

    private fun setToken(token: String, role: String, email: String) {
        viewModelScope.launch {
            if (token.isNotEmpty()){
                dataStore.saveToken(token)
                UserStorage.token=token
            }
            if (role.isNotEmpty())
                dataStore.setRole(role)
            if (email.isNotEmpty())
                dataStore.setEmail(email)
        }
    }
}

sealed class SignUpChannel {
    data class ShowToast(val message: UiText) : SignUpChannel()
    object OnProfileImageUpload : SignUpChannel()
    data class OnProfileUpdateSuccess(val message: UiText) : SignUpChannel()
    data class OnSuccess(val message: UiText) : SignUpChannel()
    object OnOTPScreen : SignUpChannel()
    object OnSignUpSelected : SignUpChannel()
    data class OnLoginSuccess(val loginResponse: UserInfo) : SignUpChannel()

}