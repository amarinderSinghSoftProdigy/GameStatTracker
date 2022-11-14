package com.allballapp.android.ui.features.profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.getFileFromUri
import com.allballapp.android.common.getFileFromUriWithoutCompress
import com.allballapp.android.ui.utils.UiText
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.*
import com.allballapp.android.data.response.*
import com.allballapp.android.domain.repository.IImageUploadRepo
import com.allballapp.android.domain.repository.IUserRepository
import com.allballapp.android.ui.features.components.UserType
import com.allballapp.android.ui.utils.CommonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
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
    val waistSize = arrayListOf<String>()
    val size = arrayListOf(
        "YouthXS",
        "YouthS",
        "YouthM",
        "YouthL",
        "YouthXL",
        "AdultS",
        "AdultM",
        "AdultL",
        "AdultXL"
    )


    init {
        for (i in 30..50) {
            waistSize.add(i.toString())
        }
        viewModelScope.launch {
            dataStoreManager.getRole.collect {
                if (it == UserType.REFEREE.key) {
                    getPayData()
                }
            }
        }
    }


    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetStaffSchedule -> {
                viewModelScope.launch {
                    getStaffSchedule(UserStorage.userId)
                }
            }
            is ProfileEvent.UpdateScheduleStaff -> {
                viewModelScope.launch {
                    updateStaffSchedule(event.list)
                }
            }
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
                _state.value = _state.value.copy(user = _state.value.user.copy(email = event.email))
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
            is ProfileEvent.OnAAuNumChange -> {
                _state.value =
                    _state.value.copy(
                        user = _state.value.user.copy(
                            userDetails = _state.value.user.userDetails.copy(
                                auuCardNumber = event.cardNumber
                            )
                        )
                    )
            }
            is ProfileEvent.OnLeaveTeamCLick -> {
                _state.value = _state.value.copy(
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
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(aboutExperience = event.exp)
                    )
                )
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
                _state.value = _state.value.copy(favActivePlayer = event.activePlayer)
            }
            is ProfileEvent.OnAllTimeFavChange -> {
                _state.value = _state.value.copy(favAllTimePlayer = event.allTimeFav)
            }
            is ProfileEvent.OnCollegeTeamChange -> {
                _state.value = _state.value.copy(favCollegeTeam = event.collegeTeam)
            }
            is ProfileEvent.OnGenderChange -> {
                _state.value =
                    _state.value.copy(user = _state.value.user.copy(gender = event.gender))
            }

            is ProfileEvent.OnAgeRangeChanges -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(teamAgePerference = event.ageRange)
                    )
                )

            }

            is ProfileEvent.OnRefereeWithPartner -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(refereeWithPartner = event.refereeWithPartner)
                    )
                )
            }

            is ProfileEvent.OnNbaTeamChange -> {
                _state.value = _state.value.copy(favProfessionalTeam = event.nbaTeam)
            }
            is ProfileEvent.OnPrefJerseyNoChange -> {
                _state.value = _state.value.copy(jerseyNumerPerferences = event.prefJerseyNumber)
            }
            is ProfileEvent.OnShirtChange -> {
                _state.value = _state.value.copy(shirtSize = event.shirt)
            }
            is ProfileEvent.OnWaistChange -> {
                _state.value = _state.value.copy(waistSize = event.waist)
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
                _state.value = _state.value.copy(showRemoveFromTeamDialog = false)
                viewModelScope.launch {
                    removeFromTeam()
                }
            }
            is ProfileEvent.GetDocumentTypes -> {
                _state.value = _state.value.copy(showRemoveFromTeamDialog = false)
                viewModelScope.launch {
                    getDocumentTypes()
                }
            }
            is ProfileEvent.ShowDeleteDialog -> {
                _state.value = _state.value.copy(showDeleteDialog = event.show)
            }
            is ProfileEvent.SetDeleteDocument -> {
                _state.value = _state.value.copy(deleteDocument = event.docType)
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
                _state.value = _state.value.copy(imageUri = event.teamImageUri)
                viewModelScope.launch {
                    imageUpload(event.docType)
                }
            }
            is ProfileEvent.SetUploadKey -> {
                _state.value = _state.value.copy(selectedDocKey = event.docType)
            }
            is ProfileEvent.OnLeaveDialogClick -> {
                _state.value = _state.value.copy(showRemoveFromTeamDialog = event.showDialog)
            }
            is ProfileEvent.GetReferee -> {
            }
            is ProfileEvent.GetProfile -> {
                viewModelScope.launch { getUserDetails() }
            }
            is ProfileEvent.OnReferringExperience -> {
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(refereeningExperience = event.refereeningExperience)
                    )
                )
            }
            is ProfileEvent.OnGameStaffChanges -> {
                _state.value = _state.value.copy(searchGameStaff = event.searchGameStaff)
                viewModelScope.launch {
                    if (_state.value.searchGameStaff.length >= 3) getSearchGameStaff()
                }
            }

            is ProfileEvent.OnSelectedGameStaff -> {
                /*_state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        userDetails = _state.value.user.userDetails.copy(
                            preferredPartner = _state.value.user.userDetails.preferredPartner.copy(
                                id = event.getSearchStaff._id,
                                profileImage = event.getSearchStaff.profileImage,
                                name = event.getSearchStaff.name
                            )
                        )
                    )
                )*/
            }
            is ProfileEvent.OnProfileImageSelected -> {
                _state.value = _state.value.copy(selectedImage = event.selectedImage)
            }
            is ProfileEvent.ProfileUpload -> {
                viewModelScope.launch {
                    if (_state.value.selectedImage.isNotEmpty())
                        imageUpload()
                }
            }
        }
    }

    private suspend fun getStaffSchedule(userId: String, showToast: Boolean = false) {
        _state.value = _state.value.copy(isLoading = true)
        val leaveTeamResponse = userRepository.getStaffSchedule(userId)
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
                        if (response.data != null) {
                            _state.value = _state.value.copy(scheduleList = response.data)
                            if (showToast) {
                                _channel.send(
                                    ProfileChannel.ShowToast(
                                        UiText.DynamicString(
                                            response.statusMessage
                                        )
                                    )
                                )
                            }
                        }
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

    private suspend fun updateStaffSchedule(list: ArrayList<Date> = ArrayList()) {
        val schedule: ArrayList<ScheduleObject> = ArrayList()
        list.forEach {
            schedule.add(
                ScheduleObject(
                    UserStorage.userId, startTime = "12:00 AM", endTime = "12:00 AM",
                    date = CommonUtils.formatDateRequest(it), "description"
                )
            )
        }
        val request = StaffScheduleRequest(UserStorage.userId, schedule)

        _state.value = _state.value.copy(isLoading = true)
        val leaveTeamResponse = userRepository.updateStaffSchedule(request)
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
                        getStaffSchedule(UserStorage.userId, true)
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
                /*_channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )*/
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
               /* _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )*/
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
                /*_channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status && response.data != null) {
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
               /* _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            leaveTeamResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                leaveTeamResponse.value.let { response ->
                    if (response.status && response.data != null) {
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
             /*   _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
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

        val request = if (UserStorage.role.equals(
                UserType.REFEREE.key, ignoreCase = true
            )
        ) {
            generateRefereeUpdateRequest()
        } else {
            generateUpdateRequest()
        }


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
                /*_channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
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
            mime = file?.extension ?: "image", type = userDocType, file
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

               /* _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            userResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                userResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value = _state.value.copy(isLoading = false)
                        onEvent(
                            ProfileEvent.UpdateUserDoc(
                                docType = userDocType, response.data.data
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

    private fun generateRefereeUpdateRequest(): UpdateUserDetailsReq {

        val userDetails = UserDetailsReq(
            aboutExperience = state.value.user.userDetails.aboutExperience,
            teamAgePerference = state.value.user.userDetails.teamAgePerference,
            refereeningExperience = state.value.user.userDetails.refereeningExperience,
            refereeWithPartner = state.value.user.userDetails.refereeWithPartner,
            /*perferredPartner = _state.value.user.userDetails.preferredPartner.id,*/
        )

        return UpdateUserDetailsReq(
            firstName = _state.value.user.firstName,
            lastName = _state.value.user.lastName,
            phone = _state.value.user.phone,
            email = _state.value.user.email,
            address = _state.value.user.address,
            teamDetailsReq = arrayListOf(),
            userDetailsReq = userDetails,
            profileImage = _state.value.user.profileImage
        )

    }

    private fun generateUpdateRequest(): UpdateUserDetailsReq {
        val teamDetailsReq = _state.value.user.teamDetails.map {
            TeamDetailsReq(
                teamId = it.teamId.Id, role = it.role, position = it.position, jersey = it.jersey
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
            auuCardNumber = _state.value.user.userDetails.auuCardNumber,
            waiver = _state.value.user.userDetails.waiver,
            vaccineCard = _state.value.user.userDetails.vaccineCard,
        )

        return UpdateUserDetailsReq(
            profileImage = _state.value.user.profileImage,
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
        _state.value = _state.value.copy(
            user = user,
        )
        if (user.userDetails.jerseyPerferences.isNotEmpty()) {
            _state.value = _state.value.copy(
                jerseyNumerPerferences = user.userDetails.jerseyPerferences[0].jerseyNumberPerferences.joinToString { jerseyPerferences ->
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
                            _state.value.positionPlayed[i].label, ignoreCase = true
                        )
                    ) {
                        _state.value.positionPlayed[i] =
                            _state.value.positionPlayed[i].copy(isChecked = true)
                    }
                }

            }
        }
    }


    private suspend fun imageUpload() {
        _state.value =
            _state.value.copy(isLoading = true)

        val uri = Uri.parse(_state.value.selectedImage)

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
                _state.value =
                    _state.value.copy(isLoading = false)
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _state.value =
                    _state.value.copy(isLoading = false)
               /* _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            uploadLogoResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                uploadLogoResponse.value.let { response ->
                    if (response.status && response.data != null) {
                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                user = _state.value.user.copy(
                                    profileImage = uploadLogoResponse.value.data.data
                                )
                            )
                        _channel.send(
                            ProfileChannel.OnProfileImageUpload
                        )
                    } else {
                        _state.value =
                            _state.value.copy(isLoading = false)
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


    private fun getPayData() {

        val payData = arrayListOf(
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

    private suspend fun getSearchGameStaff() {
        _state.value = _state.value.copy(isLoading = true)
        val searchResponse = userRepository.getSearchGameStaff(_state.value.searchGameStaff)
        _state.value = _state.value.copy(isLoading = false)

        when (searchResponse) {
            is ResultWrapper.GenericError -> {
                _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            "${searchResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
               /* _channel.send(
                    ProfileChannel.ShowToast(
                        UiText.DynamicString(
                            searchResponse.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                searchResponse.value.let { response ->
                    if (response.data != null) {
                        _state.value = _state.value.copy(searchGameStaffList = response.data)
                    }
                }
            }
        }
    }
}

sealed class ProfileChannel {
    data class ShowToast(val message: UiText) : ProfileChannel()
    data class OnUpdateSuccess(val message: String) : ProfileChannel()
    object OnProfileImageUpload : ProfileChannel()
}
