package com.allballapp.android.ui.features.sign_up

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.getFileFromUri
import com.allballapp.android.core.util.UiText
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.AuthorizeRequest
import com.allballapp.android.data.request.SignUpData
import com.allballapp.android.data.request.SignUpPhoneData
import com.allballapp.android.data.response.AddProfileRequest
import com.allballapp.android.data.response.SwapUser
import com.allballapp.android.data.response.UserInfo
import com.allballapp.android.domain.repository.IImageUploadRepo
import com.allballapp.android.domain.repository.IUserRepository
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
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
            is SignUpUIEvent.OnSwapUpdate -> {
                viewModelScope.launch {
                    updateProfileToken(event.user)
                }
            }
            /* is SignUpUIEvent.OnAddProfileClicked -> {
                 viewModelScope.launch {
                     if (!_signUpUiState.value.status)
                         imageUpload()
                 }
             }*/
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
                viewModelScope.launch {
                    if (event.fromNewProfile) {
                        addProfile()
                    } else {
                        updateUserProfile()
                    }
                }
            }

            is SignUpUIEvent.OnScreenNext -> {
                viewModelScope.launch {
                    if (_signUpUiState.value.registered) /*Means we have token to call upload profile pic*/
                        imageUpload()
                    else {
                        /*Means we don't have token to call upload profile pic*/
                        signUpWithPhone()
                    }
                }
            }
            is SignUpUIEvent.OnAddProfile -> {
                viewModelScope.launch {
                    imageUpload()
                }
            }

            is SignUpUIEvent.OnSignUpDataSelected -> {
                /*_signUpUiState.value =
                    _signUpUiState.value.copy(signUpData = event.signUpData, registered = false)

                viewModelScope.launch {
                    _signUpChannel.send(SignUpChannel.OnSignUpSelected)
                }*/
            }
            is SignUpUIEvent.SetRegister -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(registered = true)
            }

            is SignUpUIEvent.OnVerifyNumber -> {
                verifyPhone()
            }

            is SignUpUIEvent.OnConfirmNumber -> {
                confirmPhone(event.phoneNumber, event.otp)
            }
            /*is SignUpUIEvent.OnFacebookClick -> {
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
            }*/
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

            is SignUpUIEvent.OnCountryCode -> {
                _signUpUiState.value = _signUpUiState.value.copy(phoneCode = event.countryCode)
            }

            is SignUpUIEvent.AuthorizeUser -> {
                viewModelScope.launch {
                    authorizeUser(event.phone, event.parentPhone)
                }
            }
            is SignUpUIEvent.ClearPhoneField -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        signUpData = SignUpData()
                    )
            }
            is SignUpUIEvent.OnTermsAndConditionChanged -> {
                _signUpUiState.value = _signUpUiState.value.copy(
                    signUpData = _signUpUiState.value.signUpData.copy(termsAndCondition = event.termsAndCondition)
                )
            }
        }
    }

    private suspend fun updateProfileToken(userId: SwapUser) {
        _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)
        val userResponse = IUserRepository.updateInitialProfileToken(userId._Id)
        _signUpUiState.value = _signUpUiState.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        setToken(response.data, userId.role, "")
                        _signUpChannel.send(
                            SignUpChannel.OnProfileUpdateSuccess(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                    } else {
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

    /*private fun login(loginRequest: LoginRequest) {
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
    }*/

    /*private fun setLoginDataToState(userInfo: UserInfo) {
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
    }*/

    private suspend fun imageUpload() {
        if (_signUpUiState.value.registered && _signUpUiState.value.signUpData.profileImageUri == null) {
            _signUpChannel.send(
                SignUpChannel.OnProfileImageUpload
            )
            return
        }
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
                    if (response.status && response.data != null) {
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

    private suspend fun signUpWithPhone() {
        _signUpUiState.value =
            _signUpUiState.value.copy(isLoading = true)
        val signUpData = signUpUiState.value.signUpData
        val signUpDataRequest = SignUpPhoneData(
            firstName = signUpData.firstName,
            lastName = signUpData.lastName,
            email = signUpData.email,
            phone = signUpUiState.value.phoneCode + signUpData.phone,
        )

        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val signUpResponse =
                IUserRepository.signUpPhone(signUpDataRequest)

            _signUpUiState.value =
                _signUpUiState.value.copy(isLoading = false)
            when (signUpResponse) {

                is ResultWrapper.Success -> {
                    signUpResponse.value.let { response ->
                        if (response.status) {
                            setToken(
                                response.data.token,
                                response.data.user.role,
                                signUpData.email ?: ""
                            )
                            _signUpUiState.value = _signUpUiState.value.copy(
                                registered = true,
                                isLoading = false,
                                errorMessage = null,
                                successMessage = response.statusMessage
                            )
                            if (_signUpUiState.value.signUpData.profileImageUri.isNullOrEmpty()) {
                                _signUpChannel.send(
                                    SignUpChannel.OnProfileUpdateSuccess(
                                        UiText.DynamicString(
                                            response.statusMessage
                                        ),
                                    )
                                )
                            } else {
                                imageUpload()
                            }
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

    /*  private suspend fun signUp() {
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
                              *//*  _signUpChannel.send(
                                  SignUpChannel.OnSignUpSuccess(
                                      UiText.DynamicString(
                                          signUpResponse.value.statusMessage
                                      )
                                  )

                              )*//*
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
    }*/

    private suspend fun addProfile() {
        _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)
        val request = AddProfileRequest(
            firstName = _signUpUiState.value.signUpData.firstName,
            lastName = _signUpUiState.value.signUpData.lastName,
            profileImage = _signUpUiState.value.signUpData.profileImage,
//            city = "",
//            state = "",
//            zip = "",
//            address = _signUpUiState.value.signUpData.address,
//            gender = _signUpUiState.value.signUpData.gender,
//            birthdate = _signUpUiState.value.signUpData.birthdate,
//            role = _signUpUiState.value.signUpData.role
        )
        when (val response = IUserRepository.addProfile(request)) {
            is ResultWrapper.Success -> {
                response.value.let { resp ->
                    if (resp.status) {
                        _signUpUiState.value = _signUpUiState.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            successMessage = resp.statusMessage,
                        )
                        _signUpChannel.send(
                            SignUpChannel.OnProfileUpdateSuccess(
                                UiText.DynamicString(
                                    resp.statusMessage
                                )
                            )
                        )
                        /*Register newly created user to cometchat*/
                        registerProfileToCometChat(
                            "${resp.data.user.firstName} ${resp.data.user.lastName}",
                            resp.data.user.id
                        )
                    } else {
                        _signUpUiState.value = _signUpUiState.value.copy(
                            errorMessage = resp.statusMessage,
                            isLoading = false
                        )
                    }
                }
            }
            is ResultWrapper.GenericError -> {
                _signUpUiState.value = _signUpUiState.value.copy(
                    isLoading = false,
                    errorMessage = "${response.message}"
                )
                _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${response.message}")))
            }
            is ResultWrapper.NetworkError -> {
                _signUpUiState.value =
                    _signUpUiState.value.copy(
                        errorMessage = response.message,
                        isLoading = false
                    )
                _signUpChannel.send(
                    SignUpChannel.ShowToast(
                        UiText.DynamicString(
                            response.message
                        )
                    )
                )
            }
        }
    }

    private suspend fun updateUserProfile() {
        val updateUserRequestData = signUpUiState.value.signUpData
        val signUpDataRequest = SignUpPhoneData(
            phone = null,
            firstName = updateUserRequestData.firstName,
            lastName = updateUserRequestData.lastName,
            email = if ((updateUserRequestData.email
                    ?: "").isEmpty()
            ) null else updateUserRequestData.email,
            profileImage = updateUserRequestData.profileImage,
            /* phone = signUpUiState.value.phoneCode + updateUserRequestData.phone,*/
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
                    if (response.status && response.data != null) {
                        setToken(
//                            token = signUpUiState.value.signUpData.token ?: "",
                            token = "",
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

                        /*Register newly updated user to cometchat*/
                        registerProfileToCometChat(
                            "${response.data.updateUser.firstName} ${response.data.updateUser.lastName}",
                            response.data.updateUser.id
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

    private fun authorizeUser(phone: String, parent: String) {
        viewModelScope.launch {
            _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

            val verifyResponseResponse =
                IUserRepository.authorizeGuardian(AuthorizeRequest(phone, parent))

            when (verifyResponseResponse) {

                is ResultWrapper.Success -> {
                    verifyResponseResponse.value.let { response ->
                        _signUpUiState.value = _signUpUiState.value.copy(isLoading = false)
                        _signUpChannel.send(
                            SignUpChannel.OnAuthorizeSuccess(
                                UiText.DynamicString(response.statusMessage)
                            )
                        )
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
                                    profileList = response.data.profiles,
                                    signUpData = _signUpUiState.value.signUpData.copy(
                                        phoneVerified = response.status,
                                    )
                                )
                            if (response.data.isAuthorised || response.data.profiles.isNotEmpty()) {
                                _signUpChannel.send(
                                    SignUpChannel.OnSuccess(
                                        UiText.DynamicString(
                                            response.statusMessage,
                                        ),
                                        count = response.data.profiles.size,
                                        profileIdIfSingle = if (response.data.profiles.size == 1) response.data.profiles[0] else null
                                    )
                                )
                            } else {
                                _signUpChannel.send(
                                    SignUpChannel.OnAuthorize
                                )
                            }
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
            if (token.isNotEmpty()) {
                dataStore.saveToken(token)
                UserStorage.token = token
            }
            if (role.isNotEmpty())
                dataStore.setRole(role)
            if (email.isNotEmpty())
                dataStore.setEmail(email)
        }
    }

    /*Register newly updated user to cometchat*/
    private fun registerProfileToCometChat(name: String, uid: String) {
        if(AppConstants.ENABLE_CHAT){
        val authKey = com.allballapp.android.BuildConfig.COMET_CHAT_AUTH_KEY
        val user = User()
        user.uid = uid
        user.name = name

        CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("createUser", user.toString())
            }

            override fun onError(e: CometChatException) {
                Log.e("createUser", "${e.message}")
                e.printStackTrace()
            }
        })
    }
    }
}

sealed class SignUpChannel {
    data class ShowToast(val message: UiText) : SignUpChannel()
    object OnProfileImageUpload : SignUpChannel()
    data class OnProfileUpdateSuccess(val message: UiText) : SignUpChannel()
    data class OnSuccess(val message: UiText, val count: Int, val profileIdIfSingle: SwapUser?) :
        SignUpChannel()

    object OnAuthorize : SignUpChannel()
    data class OnAuthorizeSuccess(val message: UiText) : SignUpChannel()

    object OnOTPScreen : SignUpChannel()
    object OnSignUpSelected : SignUpChannel()
    data class OnLoginSuccess(val loginResponse: UserInfo) : SignUpChannel()

}
