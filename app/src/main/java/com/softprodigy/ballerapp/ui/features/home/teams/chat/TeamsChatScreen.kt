package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding
import com.softprodigy.ballerapp.ui.features.components.AppTab
import com.softprodigy.ballerapp.ui.features.components.CommonProgressBar
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun TeamsChatScreen(
    vm: ChatViewModel = hiltViewModel(),
    onTeamItemClick: () -> Unit,
    onCreateNewConversationClick: () -> Unit
) {
    val state = vm.chatUiState.value
    val selected = remember {
        mutableStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))
            LazyRow {
                itemsIndexed(state.teams) { index, item ->
                    Row {
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16dp)))
                        AppTab(
                            title = item.name,
                            selected = index == selected.value,
                            onClick = {
                                selected.value = index
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_16dp)))

            AndroidViewBinding(FragmentConversationBinding::inflate) {
                converstionContainer.getFragment<CometChatConversationList>()

            }

        }
        IconButton(
            modifier = Modifier
                .offset((-20).dp, (-20).dp)
                .size(dimensionResource(id = R.dimen.size_44dp))
                .background(
                    MaterialTheme.appColors.material.primaryVariant,
                    RoundedCornerShape(50)
                )
                .align(Alignment.BottomEnd),
            enabled = true,
            onClick = { onCreateNewConversationClick.invoke() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cross_1),
                "",
                tint = MaterialTheme.appColors.buttonColor.textEnabled,
                modifier = Modifier
                    .width(
                        dimensionResource(id = R.dimen.size_16dp)
                    )
                    .rotate(45f)
            )
        }
        if (state.isLoading) {
            CommonProgressBar()
        }

    }
}