package com.allballapp.android.ui.features.home.teams.chat

//import com.cometchat.pro.uikit.ui_settings.UIKitSettings
import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allballapp.android.R
import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.UserStorage
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.domain.repository.IChatRepository
import com.allballapp.android.domain.repository.IImageUploadRepo
import com.allballapp.android.domain.repository.ITeamRepository
import com.allballapp.android.ui.features.components.getCommonElementsCount
import com.allballapp.android.ui.utils.UiText
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.GroupMember
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_settings.UIKitSettings
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepo: IChatRepository,
    private val teamRepo: ITeamRepository,
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {
    var searchJob: Job? = null
    private val _chatChannel = Channel<ChatChannel>()
    val chatChannel = _chatChannel.receiveAsFlow()

    private val _chatUiState = mutableStateOf(ChatUIState())
    val chatUiState: State<ChatUIState> = _chatUiState


    fun onEvent(event: ChatUIEvent) {
        when (event) {


            /*New conversation event*/
            ChatUIEvent.OnInitiateNewConversation -> {
                startNewConversation()
            }
            is ChatUIEvent.OnGroupNameChange -> {
                _chatUiState.value = _chatUiState.value.copy(groupName = event.groupName)
            }
            is ChatUIEvent.OnSearchValueChange -> {
                _chatUiState.value = _chatUiState.value.copy(searchText = event.searchText)
            }
            is ChatUIEvent.ShowDialog -> {
                _chatUiState.value =
                    _chatUiState.value.copy(showCreateGroupNameDialog = event.showDialog)
            }
            is ChatUIEvent.OnCoachChange -> {
                _chatUiState.value =
                    _chatUiState.value.copy(selectedCoachesForNewGroup = event.selectedCoaches)

            }
            is ChatUIEvent.OnPlayerChange -> {
                _chatUiState.value =
                    _chatUiState.value.copy(selectedPlayersForNewGroup = event.selectedPlayers)

            }
            is ChatUIEvent.GetAllMembers -> {
                viewModelScope.launch {
//                    getTeamByTeamId(event.teamId)
                }
            }
            is ChatUIEvent.TeamSelectionChange -> {
                _chatUiState.value =
                    _chatUiState.value.copy(teamIndex = event.teamIndex)

                /* Saving team Id  */
                if (_chatUiState.value.teams.isNotEmpty() && event.teamIndex > -1) {
                    UIKitSettings.selectedTeamId = _chatUiState.value.teams[event.teamIndex]._id
                }
            }
            ChatUIEvent.GetChatListing -> {
                viewModelScope.launch {
                    getChatListings()
                }
            }
            ChatUIEvent.ClearData -> {
                _chatUiState.value =
                    _chatUiState.value.copy(selectedPlayersForNewGroup = mutableStateListOf())
                _chatUiState.value =
                    _chatUiState.value.copy(selectedCoachesForNewGroup = mutableStateListOf())
            }


        }
    }

    private suspend fun showToast(message: String) {
        _chatChannel.send(
            ChatChannel.ShowToast(
                UiText.DynamicString(
                    message
                )
            )
        )
    }




    private suspend fun getChatListings() {
        _chatUiState.value = _chatUiState.value.copy(isLoading = true)
        val response = chatRepo.getAllChats(userId = UserStorage.userId)
        _chatUiState.value = _chatUiState.value.copy(isLoading = false)
        when (response) {
            is ResultWrapper.GenericError -> {
                _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            "${response.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
               /* _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            response.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                response.value.let { resp ->
                    if (!resp.data.isNullOrEmpty()) {

                        val teams = resp.data

                        /*According to requirement setting default teams as first element of chat team list*/
                        val team = teams.find { team -> team._id == UserStorage.teamId }

                        team?.let {
                            teams.remove(team)
                            teams.add(0, team)
                        }
                        val mappedList = teams.map { orderedTeam ->
                            val mergedIds = mutableListOf<String>()
                            val playerIds = orderedTeam.players.map {
                                it._id
                            }
                            val coachIds = orderedTeam.coaches.map {
                                it._id
                            }
                            val supportingStaffIds = orderedTeam.supportingCastDetails.map {
                                it._Id
                            }
                            val groupId = orderedTeam.teamChatGroups.map {
                                it.groupId
                            }
                            mergedIds.addAll(playerIds)
                            mergedIds.addAll(coachIds)
                            mergedIds.addAll(supportingStaffIds)
                            mergedIds.addAll(groupId)

                            val commonCount =
                                getCommonElementsCount(mergedIds, listOf("4b3f73da-8e1f-4741-a9f6-9897d3afe25c","c38528f0-e585-4531-95df-8300e6743d74"))

                            Timber.i("findCommon--$commonCount ${orderedTeam.name}")

                            orderedTeam.copy(unreadMessageCount = commonCount)

                        }


                        _chatUiState.value =
                            _chatUiState.value.copy(teams = mappedList)
                        _chatChannel.send(ChatChannel.OnNewChatListingSuccess(teams))

                        /*Getting list of comet chat conversation*/
//                        getConversation()

                    } else {
                        _chatUiState.value =
                            _chatUiState.value.copy(isLoading = false)
                        _chatChannel.send(
                            ChatChannel.ShowToast(
                                UiText.DynamicString(
                                    resp.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }

    }

    private suspend fun saveChatGroup(groupId: String) {
        _chatUiState.value = _chatUiState.value.copy(isLoading = true)
        val response = chatRepo.saveChatGroup(
            teamId = chatUiState.value.teams[chatUiState.value.teamIndex]._id,
            groupId = groupId
        )
        _chatUiState.value = _chatUiState.value.copy(isLoading = false)

        when (response) {
            is ResultWrapper.GenericError -> {
                _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            "${response.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                /*_chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            response.message
                        )
                    )
                )*/
            }
            is ResultWrapper.Success -> {
                response.value.let { resp ->
                    if (!resp.status) {
                        _chatChannel.send(
                            ChatChannel.ShowToast(
                                UiText.DynamicString(
                                    resp.statusMessage
                                )
                            )
                        )
                    }
                }
            }
        }}

        private fun startNewConversation() {

            val randomUid = UUID.randomUUID().toString()
            val groupName = chatUiState.value.groupName

            val mergedMembers = mutableListOf<String>()
            mergedMembers.addAll(chatUiState.value.selectedPlayersForNewGroup.map { playerIdsList -> playerIdsList })
            mergedMembers.addAll(chatUiState.value.selectedCoachesForNewGroup.map { coachIdsList -> coachIdsList })

            // TODO: Need to confirm with IOS team for null password check
            val group = Group(randomUid, groupName, CometChatConstants.GROUP_TYPE_PRIVATE, null)
            val groupMembers: MutableList<GroupMember> = ArrayList()
            mergedMembers.map { memberId ->
                groupMembers.add(GroupMember(memberId, CometChatConstants.SCOPE_PARTICIPANT))
            }

            if (groupMembers.size > 1) {
                CometChat.createGroupWithMembers(
                    group,
                    groupMembers,
                    null,
                    object : CometChat.CreateGroupWithMembersListener() {
                        override fun onSuccess(group: Group, hashMap: HashMap<String?, String?>) {
                            Timber.i("CreateGroupWithMembersListener-- $group")
                            Timber.i("CreateGroupWithMembersListener-- $hashMap")
                            /* ChatChannel.OnNewConversationResponse(
                                 isSuccess = true,
                                 UiText.StringResource(R.string.new_group_created_successfully), groupId = group.guid
                             )*/
                            viewModelScope.launch {
                                saveChatGroup(randomUid)
                                _chatChannel.send(
                                    ChatChannel.OnNewConversationResponse(
                                        isSuccess = true,
                                        UiText.StringResource(R.string.new_group_created_successfully)
                                    )
                                )
                            }


                        }

                        override fun onError(e: CometChatException) {
                            Timber.e("CreateGroupWithMembersListener-- ${e.message}")
                            viewModelScope.launch {
                                _chatChannel.send(
                                    ChatChannel.OnNewConversationResponse(
                                        isSuccess = false,
                                        UiText.StringResource(R.string.something_went_wrong)
                                    )
                                )
                            }
                        }
                    })
            } else {

                val uId = mergedMembers.getOrNull(0) ?: ""
                CometChat.getUser(uId, object : CometChat.CallbackListener<User?>() {
                    override fun onSuccess(user: User?) {
                        Timber.d("User details fetched for user: " + user.toString())


                        user?.let {
                            viewModelScope.launch {
                                _chatChannel.send(
                                    ChatChannel.TriggerUserGetIntent(
                                        isSuccess = true,
                                        it
                                    )
                                )

                            }
                        }


                    }

                    override fun onError(e: CometChatException) {
                        Log.d("TAG", "User details fetching failed with exception: " + e.message)
                        viewModelScope.launch {
                            _chatChannel.send(
                                ChatChannel.TriggerUserGetIntent(
                                    isSuccess = false,
                                    null,
                                    UiText.StringResource(R.string.user_has_not_registered_with_chat)
                                )
                            )
                        }

                    }

                })

            }

        }
    private fun getConversation() {
        _chatUiState.value = _chatUiState.value.copy(isLoading = true)

        val conversationRequest = ConversationsRequest.ConversationsRequestBuilder()
            .setLimit(50).setConversationType(ConversationMode.ALL_CHATS.toString())
            .build()

        conversationRequest.fetchNext(object : CometChat.CallbackListener<List<Conversation?>?>() {

            override fun onSuccess(conversations: List<Conversation?>?) {
                _chatUiState.value = _chatUiState.value.copy(isLoading = false)

                Timber.i("getConversation-- ${conversations}")
                val json= Gson().toJson(conversations).replace("\\","")
                Timber.i("getConversation json-- $json")
                _chatUiState.value = _chatUiState.value.copy(conversation = conversations)


            }

            override fun onError(e: CometChatException) {
                _chatUiState.value = _chatUiState.value.copy(isLoading = false)

                Timber.i("getConversation-- ${e.message}")
                e.printStackTrace()
            }

        })
    }


}


sealed class ChatChannel {
    data class ShowToast(val message: UiText) : ChatChannel()
    data class OnNewConversationResponse(val isSuccess: Boolean, val message: UiText) :
        ChatChannel()

    data class TriggerUserGetIntent(
        val isSuccess: Boolean,
        val user: User?,
        val message: UiText? = null
    ) : ChatChannel()

    data class OnNewChatListingSuccess(val teams: ArrayList<com.allballapp.android.data.response.team.Team>) :
        ChatChannel()

}

enum class ConversationMode(private val label: String) {
    ALL_CHATS("all_chats"), GROUP("groups"), USER("users");
}

fun getConvoType(conversation: Conversation): Any {
    return if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP)
        conversation.conversationWith as Group else
        conversation.conversationWith as User
}