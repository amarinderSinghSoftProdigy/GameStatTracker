package com.softprodigy.ballerapp.ui.features.home.teams.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding
import com.softprodigy.ballerapp.ui.theme.appColors

@Composable
fun TeamsChatScreen(onTeamItemClick: () -> Unit, onCreateNewConversationClick: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
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
                modifier = Modifier.width(
                    dimensionResource(id = R.dimen.size_16dp)
                ).rotate(45f)
            )
        }
    }
}