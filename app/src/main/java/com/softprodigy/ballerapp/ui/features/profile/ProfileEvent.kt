package com.softprodigy.ballerapp.ui.features.profile

import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class ProfileEvent() {
    data class OnParentDialogChange(val showDialog: Boolean) : ProfileEvent()

    data class OnFirstNameChange(val firstName: String) : ProfileEvent()
    data class OnLastNameChange(val lastName: String) : ProfileEvent()
    data class OnEmailChange(val email: String) : ProfileEvent()
    data class OnPhoneChange(val phone: String) : ProfileEvent()
    data class OnBirthdayChange(val birthday: String) : ProfileEvent()
    data class OnClassChange(val classOf: String) : ProfileEvent()

    data class OnLeaveTeamCLick(val index: Int) : ProfileEvent()
    data class OnRoleChange(val index: Int,val role: String) : ProfileEvent()
    data class OnPositionChange(val index: Int,val position: String) : ProfileEvent()
    data class OnJerseyChange(val index: Int,val jerseyNumber: String) : ProfileEvent()

    data class OnPrefJerseyNoChange(val prefJerseyNumber: SnapshotStateList<String>) : ProfileEvent()
    data class OnGenderChange(val gender: String) : ProfileEvent()
    data class OnWaistChange(val waist: String) : ProfileEvent()
    data class OnShirtChange(val shirt: String) : ProfileEvent()

    data class OnCollegeTeamChange(val collegeTeam: String) : ProfileEvent()
    data class OnNbaTeamChange(val nbaTeam: String) : ProfileEvent()
    data class OnActivePlayerChange(val activePlayer: String) : ProfileEvent()
    data class OnAllTimeFavChange(val allTimeFav: String) : ProfileEvent()
}
