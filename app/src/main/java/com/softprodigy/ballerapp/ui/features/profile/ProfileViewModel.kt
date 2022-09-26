package com.softprodigy.ballerapp.ui.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.*
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
            is ProfileEvent.OnActivePlayerChange -> {
                state.value =
                    state.value.copy(favActivePlayer = event.activePlayer)
            }
            is ProfileEvent.OnAllTimeFavChange -> {
                state.value =
                    state.value.copy(favAllTimePlayer = event.allTimeFav)
            }
            is ProfileEvent.OnCollegeTeamChange -> {
                state.value =
                    state.value.copy(favCollegeTeam = event.collegeTeam)
            }
            is ProfileEvent.OnGenderChange -> {
                state.value =
                    state.value.copy(user = state.value.user.copy(gender = event.gender))
            }
            is ProfileEvent.OnNbaTeamChange -> {
                state.value =
                    state.value.copy(favProfessionalTeam = event.nbaTeam)
            }
            is ProfileEvent.OnPrefJerseyNoChange -> {
                state.value =
                    state.value.copy(jerseyNumerPerferences = event.prefJerseyNumber)
            }
            is ProfileEvent.OnShirtChange -> {
                state.value =
                    state.value.copy(shirtSize = event.shirt)
            }
            is ProfileEvent.OnWaistChange -> {
                state.value =
                    state.value.copy(waistSize = event.waist)
            }
            ProfileEvent.OnSaveUserDetailsClick -> {
                viewModelScope.launch {
                    updateUserFullDetails()
                }
            }
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

    private suspend fun updateUserFullDetails() {
        val teamDetailsReq = state.value.user.teamDetails.map {
            TeamDetailsReq(
                teamId = it.teamId.Id,
                role = it.role,
                position = it.position,
                jersey = it.jersey
            )
        }
        val userDetailReq = UserDetailsReq(
            positionPlayed = state.value.user.userDetails.positionPlayed,
            jerseyPerferences = arrayListOf(
                JerseyPerferencesReq(
                    jerseyNumberPerferences = state.value.jerseyNumerPerferences.split(
                        ","
                    ).toList()
                )
            ),
            funFacts = arrayListOf(
                FunFactsReq(
                    favCollegeTeam = state.value.favCollegeTeam,
                    favProfessionalTeam = state.value.favProfessionalTeam,
                    favAllTimePlayer = state.value.favAllTimePlayer,
                    favActivePlayer = state.value.favActivePlayer
                )
            ),
            classOf = state.value.user.userDetails.classOf,
            birthCertificate = state.value.user.userDetails.birthCertificate,
            gradeVerfication = state.value.user.userDetails.gradeVerfication,
            permissionSlip = state.value.user.userDetails.permissionSlip,
            auuCard = state.value.user.userDetails.auuCard,
            waiver = state.value.user.userDetails.waiver,
            vaccineCard = state.value.user.userDetails.vaccineCard,
        )


        val request = UpdateUserDetailsReq(
            firstName = state.value.user.firstName,
            lastName = state.value.user.lastName,
            birthdate = state.value.user.birthdate,
            phone = state.value.user.phone,
            gender = state.value.user.gender,
            grade = state.value.user.userDetails.grade,
            teamDetailsReq = teamDetailsReq,
            userDetailsReq = userDetailReq
        )

        state.value = state.value.copy(isLoading = true)
        val userResponse = userRepository.updateUserFullDetails(request)
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
                        _channel.send(
                            ProfileChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
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
