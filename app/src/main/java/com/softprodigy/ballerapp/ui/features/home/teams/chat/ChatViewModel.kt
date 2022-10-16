package com.softprodigy.ballerapp.ui.features.home.teams.chat

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    val teamChannel = _chatChannel.receiveAsFlow()

    private val _chatUiState = mutableStateOf(ChatUIState())
    val chatUiState: State<ChatUIState> = _chatUiState


    init {
        viewModelScope.launch {
            getChatListings()
        }
    }


    fun onEvent(event: ChatUIEvent) {
        when (event) {

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


}

sealed class ChatChannel {
    data class ShowToast(val message: UiText) : ChatChannel()
}