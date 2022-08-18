package com.softprodigy.ballerapp.ui.features.sign_up

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller_app.core.util.UiText
import com.softprodigy.ballerapp.common.*
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.data.response.SignUpResponse
import com.softprodigy.ballerapp.ui.features.welcome.SocialLoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private var signUpRepository: SignUpRepository,
    private var socialLoginRepo: SocialLoginRepo,
) :
    ViewModel() {
    private val _signUpChannel = Channel<SignUpChannel>()
    val uiEvent = _signUpChannel.receiveAsFlow()

    private val _signUpUiState = mutableStateOf(SignUpUIState())
    val signUpUIState: State<SignUpUIState> = _signUpUiState

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _passwordVisibility = mutableStateOf(false)
    val passwordVisibility: State<Boolean> = _passwordVisibility

    private val _termsAccepted = mutableStateOf(false)
    val termsAccepted: State<Boolean> = _termsAccepted


    var signupResponse: SignUpResponse? = null
        private set

    fun onEvent(event: SignUpUIEvent) {
        when (event) {

            is SignUpUIEvent.NameChange -> {
                _name.value = event.name
            }

            is SignUpUIEvent.EmailChange -> {
                _email.value = event.email
            }
            is SignUpUIEvent.PasswordChange -> {
                _password.value = event.password

            }
            is SignUpUIEvent.PasswordToggleChange -> {
                _passwordVisibility.value = event.showPassword
            }
            is SignUpUIEvent.ConfirmTermsChange -> {
                _termsAccepted.value = event.acceptTerms
            }
            is SignUpUIEvent.OnGoogleClick -> {
                viewModelScope.launch {
                    _signUpUiState.value = SignUpUIState(isLoading = true)
                    when (val response = socialLoginRepo.loginWithGoogle(event.googleUser)) {
                        is ResultWrapper.Success -> {
                            _signUpChannel.send(
                                SignUpChannel.OnLoginSuccess(
                                    response.value
                                )
                            )
                        }
                        is ResultWrapper.GenericError -> {
                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
                                    UiText.DynamicString("${response.code} ${response.message}")
                                )
                            )
                        }
                        is ResultWrapper.NetworkError -> {
                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
                                    UiText.DynamicString("${response.message}")
                                )
                            )
                        }
                    }
                    _signUpUiState.value = SignUpUIState(isLoading = false)

                }
            }
            is SignUpUIEvent.Submit -> {
                signUp(event.name, event.email, event.password)
            }
            }
        }


    private fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpUiState.value = SignUpUIState(isLoading = true)

            val signUpResponse =
                signUpRepository.signUpWithDetails(
                    firstName = name.split(" ").component1(),
                    lastName = name.split(" ").component2(),
                    email = email,
                    password = password
                )
            when (signUpResponse) {
                is ResultWrapper.NetworkError -> {
                    _signUpUiState.value =
                        SignUpUIState(
                            user = null,
                            errorMessage = signUpResponse.message,
                            isLoading = false
                        )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString(signUpResponse.message)))
                }
                is ResultWrapper.GenericError -> {
                    _signUpUiState.value =
                        SignUpUIState(
                            user = null,
                            errorMessage = "${signUpResponse.code} ${signUpResponse.message}",
                            isLoading = false
                        )
                    _signUpChannel.send(SignUpChannel.ShowToast(UiText.DynamicString("${signUpResponse.code} ${signUpResponse.message}")))
                }
                is ResultWrapper.Success -> {

                    signUpResponse.value.let { response ->
                        if (response.status == 200) {
                            _signUpUiState.value =
                                SignUpUIState(user = response.userInfo)
                            _signUpChannel.send(SignUpChannel.OnSignUpSuccess(response))
                        } else {
                            _signUpChannel.send(
                                SignUpChannel.ShowToast(
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
sealed class SignUpChannel {
    data class ShowToast(val message: UiText) : SignUpChannel()
    data class OnSignUpSuccess(val signUpResponse: SignUpResponse) : SignUpChannel()
    data class OnLoginSuccess(val loginResponse: LoginResponse) : SignUpChannel()

}