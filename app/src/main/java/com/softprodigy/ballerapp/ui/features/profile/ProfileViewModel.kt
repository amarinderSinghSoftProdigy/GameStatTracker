package com.softprodigy.ballerapp.ui.features.profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUriWithoutCompress
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.FunFactsReq
import com.softprodigy.ballerapp.data.request.JerseyPerferencesReq
import com.softprodigy.ballerapp.data.request.TeamDetailsReq
import com.softprodigy.ballerapp.data.request.UpdateUserDetailsReq
import com.softprodigy.ballerapp.data.request.UserDetailsReq
import com.softprodigy.ballerapp.data.response.*
import com.softprodigy.ballerapp.data.request.*
import com.softprodigy.ballerapp.data.response.User
import com.softprodigy.ballerapp.data.response.UserDocType
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.ui.features.components.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    private val imageUploadRepo: IImageUploadRepo,
    val userRepository: IUserRepository,
    application: Application
) : AndroidViewModel(application) {


    private val _state = mutableStateOf(ProfileState())
    var state: State<ProfileState> = _state
        private set

    private val _channel = Channel<ProfileChannel>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.getRole.collect {
                if (it == UserType.REFEREE.key) {
                    getRefereeProfileData()
                    getPayData()
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnParentDialogChange -> {
                _state.value = _state.value.copy(showParentDialog = event.showDialog)
            }
            is ProfileEvent.OnParentClick -> {
                _state.value =
                    _state.value.copy(selectedParentDetails = event.selectedParentDetails)
            }
            is ProfileEvent.OnBirthdayChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(birthdate = event.birthday))

            }
            is ProfileEvent.OnClassChange -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(classOf = event.classOf)
                    )
                )

            }
            is ProfileEvent.OnEmailChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(email = event.email))
            }
            is ProfileEvent.OnFirstNameChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(firstName = event.firstName))
            }
            is ProfileEvent.OnJerseyChange -> {
                _state.value.user.teamDetails[event.index] =
                    _state.value.user.teamDetails[event.index].copy(jersey = event.jerseyNumber)
            }
            is ProfileEvent.OnLastNameChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(lastName = event.lastName))
            }
            is ProfileEvent.OnLeaveTeamCLick -> {
                _state.value =
                    _state.value.copy(
                        showRemoveFromTeamDialog = true,
                        selectedTeamId = event.teamId,
                        selectedTeamIndex = event.index
                    )
            }
            is ProfileEvent.OnPhoneChange -> {
                _state.value = _state.value.copy(user = _state.value.user.copy(phone = event.phone))
            }
            is ProfileEvent.OnAddressChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(address = event.address))

            }
            is ProfileEvent.OnExperienceChange -> {
                _state.value.user.userDetails =
                    _state.value.user.userDetails.copy(aboutExperience = event.exp)

            }

            is ProfileEvent.OnPositionChange -> {
                _state.value.user.teamDetails[event.index] =
                    _state.value.user.teamDetails[event.index].copy(position = event.position)
            }
            is ProfileEvent.OnRoleChange -> {
                _state.value.user.teamDetails[event.index] =
                    _state.value.user.teamDetails[event.index].copy(role = event.role)
            }
            is ProfileEvent.OnActivePlayerChange -> {
                _state.value =
                    _state.value.copy(favActivePlayer = event.activePlayer)
            }
            is ProfileEvent.OnAllTimeFavChange -> {
                _state.value =
                    _state.value.copy(favAllTimePlayer = event.allTimeFav)
            }
            is ProfileEvent.OnCollegeTeamChange -> {
                _state.value =
                    _state.value.copy(favCollegeTeam = event.collegeTeam)
            }
            is ProfileEvent.OnGenderChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(gender = event.gender))
            }
            is ProfileEvent.OnNbaTeamChange -> {
                _state.value =
                    _state.value.copy(favProfessionalTeam = event.nbaTeam)
            }
            is ProfileEvent.OnPrefJerseyNoChange -> {
                _state.value =
                    _state.value.copy(jerseyNumerPerferences = event.prefJerseyNumber)
            }
            is ProfileEvent.OnShirtChange -> {
                _state.value =
                    _state.value.copy(shirtSize = event.shirt)
            }
            is ProfileEvent.OnWaistChange -> {
                _state.value =
                    _state.value.copy(waistSize = event.waist)
            }
            ProfileEvent.OnSaveUserDetailsClick -> {
                viewModelScope.launch {
                    updateUserFullDetails()
                }
            }
            is ProfileEvent.OnPositionPlayedChanges -> {
                _state.value.positionPlayed[event.index] =
                    _state.value.positionPlayed[event.index].copy(isChecked = event.isChecked)
            }
            is ProfileEvent.OnLeaveConfirmClick -> {
                _state.value =
                    _state.value.copy(showRemoveFromTeamDialog = false)
                viewModelScope.launch {
                    removeFromTeam()
                }
            }
            is ProfileEvent.GetDocumentTypes -> {
                _state.value =
                    _state.value.copy(showRemoveFromTeamDialog = false)
                viewModelScope.launch {
                    getDocumentTypes()
                }
            }
            is ProfileEvent.ShowDeleteDialog -> {
                _state.value =
                    _state.value.copy(showDeleteDialog = event.show)
            }
            is ProfileEvent.SetDeleteDocument -> {
                _state.value =
                    _state.value.copy(deleteDocument = event.docType)
            }
            is ProfileEvent.DeleteDocument -> {
                viewModelScope.launch {
                    deleteDocument(event.docType)
                }
            }
            is ProfileEvent.UpdateUserDoc -> {
                viewModelScope.launch {
                    updateUserDoc(event.docType, event.url)
                }
            }
            is ProfileEvent.OnImageSelected -> {
                _state.value =
                    _state.value.copy(imageUri = event.teamImageUri)
                viewModelScope.launch {
                    imageUpload(event.docType)
                }
            }
            is ProfileEvent.SetUploadKey -> {
                _state.value =
                    _state.value.copy(selectedDocKey = event.docType)
            }
            is ProfileEvent.OnLeaveDialogClick -> {
                _state.value = _state.value.copy(showRemoveFromTeamDialog = event.showDialog)
            }
            is ProfileEvent.GetReferee -> {
                getRefereeProfileData()
            }
            ProfileEvent.GetProfile -> {
                viewModelScope.launch { getUserDetails() }
            }
        }
    }

    private suspend fun updateUserDoc(userDocType: String, url: String) {
        Timber.i("selectedTeamId--${_state.value.selectedTeamId}")
        val leaveTeamResponse = userRepository.updateUserDoc(userDocType, url)
        _state.value = _state.value.copy(isLoading = false)

        when (leaveTeamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${leaveTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status) {
                        getDocumentTypes()
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

    private suspend fun deleteDocument(userDocType: UserDocType?) {
        if (userDocType == null) {
            return
        }
        Timber.i("selectedTeamId--${_state.value.selectedTeamId}")
        _state.value = _state.value.copy(isLoading = true)
        val leaveTeamResponse = userRepository.deleteUserDoc(userDocType.key)
        _state.value = _state.value.copy(isLoading = false)

        when (leaveTeamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${leaveTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status) {
                        getDocumentTypes()
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

    private suspend fun getDocumentTypes() {
        Timber.i("selectedTeamId--${_state.value.selectedTeamId}")
        _state.value = _state.value.copy(isLoading = true)
        val leaveTeamResponse = userRepository.getDocTypes(_state.value.selectedTeamId)
        _state.value = _state.value.copy(isLoading = false)

        when (leaveTeamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${leaveTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status) {
                        _state.value = _state.value.copy(
                            userDocTypes = response.data
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

    private suspend fun removeFromTeam() {
        Timber.i("selectedTeamId--${_state.value.selectedTeamId}")
        _state.value = _state.value.copy(isLoading = true)
        val leaveTeamResponse = userRepository.leaveTeam(_state.value.selectedTeamId)
        _state.value = _state.value.copy(isLoading = false)

        when (leaveTeamResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${leaveTeamResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status) {
                        _state.value.selectedTeamIndex?.let {
                            _state.value.user.teamDetails.toMutableList().removeAt(index = it)
                        }

                        _channel.send(
                            ProfileChannel.ShowToast(
                                UiText.DynamicString(
                                    response.statusMessage
                                )
                            )
                        )
                        getUserDetails()

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

    private suspend fun getUserDetails() {
        _state.value = _state.value.copy(isLoading = true)
        val userResponse = userRepository.getFullUserFullDetails()
        _state.value = _state.value.copy(isLoading = false)

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
                        saveResponseToState(response.data)
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

        _state.value = _state.value.copy(isLoading = true)

        val request = generateUpdateRequest()

        val userResponse = userRepository.updateUserFullDetails(request)

        _state.value = _state.value.copy(isLoading = false)


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
                            ProfileChannel.OnUpdateSuccess(
                                response.statusMessage
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

    private suspend fun imageUpload(userDocType: String) {
        _state.value = _state.value.copy(isLoading = true)
        val uri = Uri.parse(_state.value.imageUri)

        val file =
            getFileFromUriWithoutCompress(getApplication<Application>().applicationContext, uri)

        file?.let {
            val size = Integer.parseInt((it.length() / 1024).toString())
            Timber.i("Filesize after compressiod--> $size")
        }
        val userResponse = imageUploadRepo.uploadSingleFile(
            mime = file?.extension ?: "image",
            type = userDocType,
            file
        )
        when (userResponse) {
            is ResultWrapper.GenericError -> {
                _state.value = _state.value.copy(isLoading = false)

                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${userResponse.message}"
                        )
                    )
                )

            }
            is ResultWrapper.NetworkError -> {
                _state.value = _state.value.copy(isLoading = false)

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
                        _state.value = _state.value.copy(isLoading = false)
                        onEvent(
                            ProfileEvent.UpdateUserDoc(
                                docType = userDocType,
                                response.data.data
                            )
                        )
                    } else {
                        _state.value = _state.value.copy(isLoading = false)
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

    private fun generateUpdateRequest(): UpdateUserDetailsReq {
        val teamDetailsReq = _state.value.user.teamDetails.map {
            TeamDetailsReq(
                teamId = it.teamId.Id,
                role = it.role,
                position = it.position,
                jersey = it.jersey
            )
        }

        val checkedData = state.value.positionPlayed.toList().filter {
            it.isChecked
        }
        val userDetailReq = UserDetailsReq(
            /*positionPlayed = _state.value.positionPlayed.toList().map {
                if (it.isChecked)
                    it.label
                else ""*/
            positionPlayed = checkedData.toList().map {
                it.label
            },
            jerseyPerferences = arrayListOf(
                JerseyPerferencesReq(
                    jerseyNumberPerferences = _state.value.jerseyNumerPerferences.split(
                        ","
                    ).toList(),
                    shirtSize = _state.value.shirtSize, waistSize = _state.value.waistSize,

                    )
            ),
            funFacts = arrayListOf(
                FunFactsReq(
                    favCollegeTeam = _state.value.favCollegeTeam,
                    favProfessionalTeam = _state.value.favProfessionalTeam,
                    favAllTimePlayer = _state.value.favAllTimePlayer,
                    favActivePlayer = _state.value.favActivePlayer
                )
            ),
            classOf = _state.value.user.userDetails.classOf,
            birthCertificate = _state.value.user.userDetails.birthCertificate,
            gradeVerfication = _state.value.user.userDetails.gradeVerfication,
            permissionSlip = _state.value.user.userDetails.permissionSlip,
            auuCard = _state.value.user.userDetails.auuCard,
            waiver = _state.value.user.userDetails.waiver,
            vaccineCard = _state.value.user.userDetails.vaccineCard,

            )


        return UpdateUserDetailsReq(
            firstName = _state.value.user.firstName,
            lastName = _state.value.user.lastName,
            birthdate = _state.value.user.birthdate ?: "",
            phone = _state.value.user.phone,
            gender = _state.value.user.gender,
            grade = _state.value.user.userDetails.grade,
//            teamDetailsReq = teamDetailsReq,
            userDetailsReq = userDetailReq
        )
    }

    private fun saveResponseToState(user: User) {
        _state.value =
            _state.value.copy(
                user = user,
            )
        if (user.userDetails.jerseyPerferences.isNotEmpty()) {
            _state.value = _state.value.copy(
                jerseyNumerPerferences = user.userDetails.jerseyPerferences[0]
                    .jerseyNumberPerferences.joinToString { jerseyPerferences ->
                        jerseyPerferences
                    },
                shirtSize = user.userDetails.jerseyPerferences[0].shirtSize,
                waistSize = user.userDetails.jerseyPerferences[0].waistSize
            )
        }
        if (user.userDetails.funFacts.isNotEmpty()) {
            _state.value = _state.value.copy(
                favCollegeTeam = user.userDetails.funFacts[0].favCollegeTeam,
                favActivePlayer = user.userDetails.funFacts[0].favActivePlayer,
                favAllTimePlayer = user.userDetails.funFacts[0].favAllTimePlayer,
                favProfessionalTeam = user.userDetails.funFacts[0].favProfessionalTeam
            )
        }
        if (user.userDetails.positionPlayed.isNotEmpty()) {

            user.userDetails.positionPlayed.forEachIndexed { index1, positionSTring ->
                for (i in _state.value.positionPlayed.indices) {
                    if (positionSTring.equals(
                            _state.value.positionPlayed[i].label,
                            ignoreCase = true
                        )
                    ) {
                        _state.value.positionPlayed[i] =
                            _state.value.positionPlayed[i].copy(isChecked = true)
                    }
                }

            }
        }
    }

    private fun getRefereeProfileData() {

        val user = User(
            firstName = "George",
            lastName = "Will",
            email = "joe.m@example.com",
            phone = "+91 8219163443",
            address = "6127 Evergreen Rd, Dearborn Heights Michigan(MI), 48127",
            teamDetails = mutableStateListOf(
                TeamDetails(
                    teamId = TeamId("1", "Springfield Bucks", logo = ""),
                    role = "Referee"
                ),
                TeamDetails(
                    teamId = TeamId("1", "Springfield Sprouts", logo = ""),
                    role = "Program Manager"
                )
            ),
            userDetails = UserDetails(
                teamAgePerference = "10 - 16",
                perferredPartner = "Lincoln Gouse",
                refereeningExperience = "6 years",
                aboutExperience = "Mauris, mauris, ut sed tortor nullam tristique habitant viverra. Sagittis tincidunt pellentesque pellentesque sem ornare fermentum amet, dictum mi.",
                totalGames = "35",
                totalHoopsGames = "24",
                rating = "4.5"
            )
        )

        _state.value = _state.value.copy(user = user)

    }

    private fun getPayData() {

        val payData =
            arrayListOf(
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00"),
                PayResponse("Lorem Ipsum Dolor Sit Amet", "May 25, 2022", "\$120.00")
            )

        _state.value = _state.value.copy(payData = payData)
    }

}

sealed class ProfileChannel {
    data class ShowToast(val message: UiText) : ProfileChannel()
    data class OnUpdateSuccess(val message: String) : ProfileChannel()
}
