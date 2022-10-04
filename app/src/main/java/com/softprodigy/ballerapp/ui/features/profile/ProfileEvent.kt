package com.softprodigy.ballerapp.ui.features.profile

import com.softprodigy.ballerapp.data.response.ParentDetails
import com.softprodigy.ballerapp.data.response.UserDocType

sealed class ProfileEvent {
    data class OnParentDialogChange(val showDialog: Boolean) : ProfileEvent()
    data class OnParentClick(val selectedParentDetails: ParentDetails) : ProfileEvent()

    data class OnFirstNameChange(val firstName: String) : ProfileEvent()
    data class OnLastNameChange(val lastName: String) : ProfileEvent()
    data class OnEmailChange(val email: String) : ProfileEvent()
    data class OnPhoneChange(val phone: String) : ProfileEvent()
    data class OnBirthdayChange(val birthday: String) : ProfileEvent()
    data class OnClassChange(val classOf: String) : ProfileEvent()

    data class OnLeaveTeamCLick(val index: Int, val teamId: String) : ProfileEvent()
    data class OnRoleChange(val index: Int, val role: String) : ProfileEvent()
    data class OnPositionChange(val index: Int, val position: String) : ProfileEvent()
    data class OnJerseyChange(val index: Int, val jerseyNumber: String) : ProfileEvent()

    data class OnPrefJerseyNoChange(val prefJerseyNumber: String) : ProfileEvent()
    data class OnGenderChange(val gender: String) : ProfileEvent()
    data class OnWaistChange(val waist: String) : ProfileEvent()
    data class OnShirtChange(val shirt: String) : ProfileEvent()

    data class OnCollegeTeamChange(val collegeTeam: String) : ProfileEvent()
    data class OnNbaTeamChange(val nbaTeam: String) : ProfileEvent()
    data class OnActivePlayerChange(val activePlayer: String) : ProfileEvent()
    data class OnAllTimeFavChange(val allTimeFav: String) : ProfileEvent()

    object OnSaveUserDetailsClick : ProfileEvent()
    data class OnPositionPlayedChanges(val index: Int, val isChecked: Boolean) : ProfileEvent()
    data class OnLeaveDialogClick(val showDialog: Boolean) : ProfileEvent()
    data class OnLeaveConfirmClick(val teamId: String) : ProfileEvent()

    data class GetDocumentTypes(val teamId: String) : ProfileEvent()
     object GetProfile : ProfileEvent()
    data class ShowDeleteDialog(val show: Boolean) : ProfileEvent()
    data class SetDeleteDocument(val docType: UserDocType) : ProfileEvent()
    data class DeleteDocument(val docType: UserDocType?) : ProfileEvent()
    data class UpdateUserDoc(val docType: String,val url: String) : ProfileEvent()
    data class OnImageSelected(val docType: String,val teamImageUri: String) : ProfileEvent()
    data class SetUploadKey(val docType: String) : ProfileEvent()

}
