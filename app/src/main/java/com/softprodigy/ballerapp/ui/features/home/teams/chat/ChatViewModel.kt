package com.softprodigy.ballerapp.ui.features.home.teams.chat

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.GroupMember
import com.cometchat.pro.models.User
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.getFileFromUri
import com.softprodigy.ballerapp.core.util.UiText
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.domain.repository.IChatRepository
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
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
    private val imageUploadRepo: IImageUploadRepo,
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {
    var searchJob: Job? = null
    private val _chatChannel = Channel<ChatChannel>()
    val chatChannel = _chatChannel.receiveAsFlow()

    private val _chatUiState = mutableStateOf(ChatUIState())
    val chatUiState: State<ChatUIState> = _chatUiState


    init {
        viewModelScope.launch {
            getChatListings()
        }
    }


    fun onEvent(event: ChatUIEvent) {
        when (event) {


            /*New conversation event*/
            ChatUIEvent.OnInitiateNewConversation -> {
                startNewConversation()
            }
            is ChatUIEvent.OnGroupNameChange -> {
                _chatUiState.value = _chatUiState.value.copy(groupName = event.groupName)
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


    private suspend fun uploadTeamLogo() {
        _chatUiState.value = _chatUiState.value.copy(isLoading = true)
        val isLocalImageTaken = _chatUiState.value.localLogo != null
        val uri = Uri.parse("")
        val file = getFileFromUri(getApplication<Application>().applicationContext, uri)

        if (file != null) {
            val size = Integer.parseInt((file.length() / 1024).toString())
            Timber.i("Filesize compressed --> $size")
        }
        val uploadLogoResponse = imageUploadRepo.uploadSingleImage(
            type = AppConstants.TEAM_LOGO,
            file
        )
        _chatUiState.value = _chatUiState.value.copy(isLoading = false)

        when (uploadLogoResponse) {
            is ResultWrapper.GenericError -> {
                _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            "${uploadLogoResponse.message}"
                        )
                    )
                )
            }
            is ResultWrapper.NetworkError -> {
                _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            uploadLogoResponse.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                uploadLogoResponse.value.let { response ->
                    if (response.status) {

                    } else {
                        _chatUiState.value =
                            _chatUiState.value.copy(isLoading = false)
                        _chatChannel.send(
                            ChatChannel.ShowToast(
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
                _chatChannel.send(
                    ChatChannel.ShowToast(
                        UiText.DynamicString(
                            response.message
                        )
                    )
                )
            }
            is ResultWrapper.Success -> {
                response.value.let { resp ->
                    if (!resp.data.isNullOrEmpty()) {
                        _chatUiState.value =
                            _chatUiState.value.copy(teams = resp.data)
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

    private fun startNewConversation() {

        val randomUid = UUID.randomUUID().toString()
        val groupName = chatUiState.value.groupName

        val mergedMembers = mutableListOf<String>()
        mergedMembers.addAll(chatUiState.value.selectedPlayersForNewGroup.map { playerIdsList -> playerIdsList })
        mergedMembers.addAll(chatUiState.value.selectedCoachesForNewGroup.map { coachIdsList -> coachIdsList })

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
                        ChatChannel.OnNewConversationResponse(
                            isSuccess = true,
                            UiText.StringResource(R.string.new_group_created_successfully)
                        )
                        viewModelScope.launch {
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
}