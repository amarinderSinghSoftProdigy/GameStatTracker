package com.softprodigy.ballerapp.ui.features.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    val userRepository: IUserRepository
) : ViewModel() {
    var state = mutableStateOf(ProfileState())
        private set

    private val _channel = Channel<ProfileChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getUserDetails()
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnParentDialogChange -> {
                state.value = state.value.copy(showParentDialog = event.showDialog)
            }
            is ProfileEvent.OnBirthdayChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(birthdate = event.birthday))

            }
            is ProfileEvent.OnClassChange -> {
                state.value = state.value.copy(
                    user = state.value.user.copy(
                        userDetails = state.value.user.userDetails.copy(classOf = event.classOf)
                    )
                )

            }
            is ProfileEvent.OnEmailChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(email = event.email))
            }
            is ProfileEvent.OnFirstNameChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(firstName = event.firstName))
            }
            is ProfileEvent.OnJerseyChange -> {
                state.value.user.teamDetails[event.index] =
                    state.value.user.teamDetails[event.index].copy(jersey = event.jerseyNumber)
            }
            is ProfileEvent.OnLastNameChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(lastName = event.lastName))
            }
            is ProfileEvent.OnLeaveTeamCLick -> {

            }
            is ProfileEvent.OnPhoneChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(phone = event.phone))
            }
            is ProfileEvent.OnPositionChange -> {
                state.value.user.teamDetails[event.index] =
                    state.value.user.teamDetails[event.index].copy(position = event.position)
            }
            is ProfileEvent.OnRoleChange -> {
                state.value.user.teamDetails[event.index] =
                    state.value.user.teamDetails[event.index].copy(role = event.role)
            }
            is ProfileEvent.OnActivePlayerChange -> {}
            is ProfileEvent.OnAllTimeFavChange -> {}
            is ProfileEvent.OnCollegeTeamChange -> {}
            is ProfileEvent.OnGenderChange -> {}
            is ProfileEvent.OnNbaTeamChange -> {}
            is ProfileEvent.OnPrefJerseyNoChange -> {
                /* if (state.value.user.userDetails.jerseyPerferences.isNotEmpty()) {
                     state.value.user.userDetails.jerseyPerferences[0] =
                         state.value.user.userDetails.jerseyPerferences[0].copy(
                             jerseyNumberPerferences = event.prefJerseyNumber
                         )
                     Log.i("ViewModel", "jerseyPerferences:${event.prefJerseyNumber} ")
                 }*/
//                state.value.user.teamDetails[event.index] =
//                    state.value.user.teamDetails[event.index].copy(jersey = event.jerseyNumber)

                state.value.jerseyPerferences[0] =
                    state.value.jerseyPerferences[0].copy(jerseyNumberPerferences = event.prefJerseyNumber)
                Log.i("ViewModel", "jerseyPerferences:${event.prefJerseyNumber.forEach { number-> println("number--$number") }} ")
            }
            is ProfileEvent.OnShirtChange -> TODO()
            is ProfileEvent.OnWaistChange -> TODO()
        }
    }

    private suspend fun getUserDetails() {
        state.value = state.value.copy(isLoading = true)
        val userResponse = userRepository.getFullUserFullDetails()
        state.value = state.value.copy(isLoading = false)

        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status) {
                        state.value =
                            state.value.copy(
                                user = response.data
                            )
                    } else {
                        _channel.send(
                            ProfileChannel.ShowToast(
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
}

sealed class ProfileChannel {
    data class ShowToast(val message: UiText) : ProfileChannel()
}
