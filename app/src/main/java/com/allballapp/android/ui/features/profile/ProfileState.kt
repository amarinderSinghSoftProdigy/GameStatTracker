package com.allballapp.android.ui.features.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.allballapp.android.data.response.*

data class ProfileState(
    val isLoading: Boolean = false,
    val showParentDialog: Boolean = false,
    val selectedParentDetails: Parent = Parent(),
    val deleteDocument: UserDocType? = null,
    val showDeleteDialog: Boolean = false,
    val user: User = User(),
    val jerseyNumerPerferences: String = "",
    val shirtSize: String = "",
    val waistSize: String = "",
    val imageUri: String? = null, //local uri

    val favCollegeTeam: String = "",
    val favProfessionalTeam: String = "",
    val favActivePlayer: String = "",
    val favAllTimePlayer: String = "",
    val userDocTypes: List<UserDocType> = mutableListOf(),
    val userUploadedDocs: List<UserDocType> = mutableListOf(),
    val positionPlayed: SnapshotStateList<CheckBoxData> = mutableStateListOf(
        CheckBoxData("PG"),
        CheckBoxData("SG"),
        CheckBoxData("SF"),
        CheckBoxData("PF"),
        CheckBoxData("C")
    ),
    val showRemoveFromTeamDialog: Boolean = false,
    val selectedTeamId: String = "",
    val selectedDocKey: String = "",
    val selectedTeamIndex: Int? = null,
    val payData: ArrayList<PayResponse> = arrayListOf(),
    val scheduleList: List<ScheduleResponseObject> = mutableListOf(),
    val searchGameStaff: String = "",
    val searchGameStaffList: List<GetSearchStaff> = emptyList(),
    val selectedImage: String = "",
    val showImage: Boolean = false,
    val leaveGroupIds: ArrayList<String> = arrayListOf()
)
