package com.softprodigy.ballerapp.ui.features.home.teams.chat

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.*
import com.cometchat.pro.uikit.R
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList
import com.cometchat.pro.uikit.ui_components.shared.cometchatConversations.CometChatConversation
import com.cometchat.pro.uikit.ui_resources.utils.ErrorMessagesUtils
import com.cometchat.pro.uikit.ui_resources.utils.Utils
import com.cometchat.pro.uikit.ui_settings.UIKitSettings
import com.cometchat.pro.uikit.ui_settings.enum.ConversationMode
import com.softprodigy.ballerapp.databinding.FragmentConversationBinding

@Composable
fun TeamsChatScreen() {
    val context = LocalContext.current

        AndroidViewBinding(FragmentConversationBinding::inflate) {
            converstion.getFragment<CometChatConversationList>()
    }
}

    /*  var conversationsRequest //Uses to fetch Conversations.
            : ConversationsRequest? = null

    conversationsRequest = ConversationsRequest.ConversationsRequestBuilder()
        .setConversationType(UIKitSettings.conversationInMode.toString()).setLimit(50).build()

    conversationsRequest?.fetchNext(object : CometChat.CallbackListener<List<Conversation>>() {
        override fun onSuccess(conversations: List<Conversation>) {
            if (conversations.isNotEmpty()) {
                Log.i("CallbackListener", "onSuccess: $conversations{}")
            }
        }

        override fun onError(e: CometChatException) {
            Log.i("CallbackListener", "onError: ${e.message}")
        }
    })*/


//}



/* val context = LocalContext.current
 AndroidView(factory = {
     View.inflate(it, R.layout.fragment_conversation_screen, null)
 }, modifier = Modifier.fillMaxSize(),
     update = {



     var rvConversation //Uses to display list of conversations.
             : CometChatConversation? = null
     var conversationsRequest //Uses to fetch Conversations.
             : ConversationsRequest? = null
     var searchEdit //Uses to perform search operations.
             : EditText? = null
     var clearSearch //Use to clear the search operation performed on list.
             : ImageView? = null
     var tvTitle: TextView? = null
//         var conversationShimmer: ShimmerFrameLayout? = null
     var rlSearchBox: RelativeLayout? = null
     var noConversationView: LinearLayout? = null
     var vw: View? = null
     var conversation: Conversation? = null
     var conversationList: MutableList<Conversation> = ArrayList()
     var ivStartConversation: ImageView? = null


     tvTitle = it.findViewById(R.id.tv_title)
     ivStartConversation =
         it.findViewById(R.id.iv_start_conversation)
     rlSearchBox = it.findViewById(R.id.rl_search_box)
     searchEdit = it.findViewById(R.id.search_bar)
     clearSearch = it.findViewById(R.id.clear_search)
     noConversationView =
         it.findViewById(R.id.no_conversation_view)
//        conversationShimmer=it.findViewById<LinearLayout>(R.id.shimmer_layout)
     rvConversation =
         it.findViewById(R.id.rv_conversation_list)


     if (!FeatureRestriction.isChatSearchEnabled()) {
         searchEdit?.visibility = View.GONE
         clearSearch?.visibility = View.GONE
     }

     checkDarkMode(context = context, view = it, tvTitle)

     ivStartConversation?.setOnClickListener {
         val intent = Intent(context, CometChatStartConversation::class.java)
         context.startActivity(intent)
     }

     searchEdit?.setOnEditorActionListener(TextView.OnEditorActionListener { textView: TextView, i: Int, keyEvent: KeyEvent? ->
         if (i == EditorInfo.IME_ACTION_SEARCH) {
             rvConversation?.searchConversation(textView.text.toString())
             clearSearch?.visibility = View.VISIBLE
             return@OnEditorActionListener true
         }
         false
     })

     clearSearch?.setOnClickListener {
         searchEdit?.setText("")
         clearSearch.visibility = View.GONE
         rvConversation?.searchConversation(searchEdit?.text.toString())
         val inputMethodManager: InputMethodManager =
             context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
         // Hide the soft keyboard
         inputMethodManager.hideSoftInputFromWindow(searchEdit?.windowToken, 0)
     }

  *//*   makeConversationList(
            context,
            conversationsRequest,
            noConversationView,
            rvConversation,
            conversationList,
            onConversationListChange = {
                conversationList = it
            },
            onConversationChange = {
                conversationsRequest = it
            }
        )*//*

        // Uses to fetch next list of conversations if rvConversationList (RecyclerView) is scrolled in upward direction.
        rvConversation?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    makeConversationList(
                        context,
                        conversationsRequest,
                        noConversationView,
                        rvConversation,
                        conversationList,
                        onConversationListChange = {
                            conversationList = it
                        },
                        onConversationChange = {
                            conversationsRequest = it
                        }
                    )
                }
            }
        })

        addConversationListener(rvConversation,noConversationView)



        // Used to trigger event on click of conversation item in rvConversationList (RecyclerView)
        rvConversation?.setItemClickListener(object : OnItemClickListener<Conversation>() {

            override fun OnItemClick(t: Any, position: Int) {
                conversation = t as Conversation
                CometChatConversationList.events.OnItemClick(t, position)
            }
        })

        val swipeHelper: RecyclerViewSwipeListener = object : RecyclerViewSwipeListener(context) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                val deleteBitmap: Bitmap = Utils.drawableToBitmap(
                    it.resources.getDrawable(R.drawable.ic_delete_white)
                )
                FeatureRestriction.isDeleteConversationEnabled(object :
                    FeatureRestriction.OnSuccessListener {
                    override fun onSuccess(booleanVal: Boolean) {
                        if (booleanVal) {
                            underlayButtons?.add(UnderlayButton(
                                "",
                                deleteBitmap,
                                it.resources.getColor(R.color.red),
                                object : UnderlayButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        val conversation: Conversation? =
                                            rvConversation?.getConversation(pos)
                                        if (conversation != null) {
                                            deleteConversations(
                                                conversation,
                                                context,
                                                conversationList,
                                                rvConversation
                                            )
                                        }
                                    }
                                }
                            ))
                        }
                    }
                })
            }
        }
        swipeHelper.attachToRecyclerView(rvConversation)

    })*/



private fun deleteConversations(
    conversation: Conversation,
    context: Context,
    conversationList: MutableList<Conversation>,
    rvConversation: CometChatConversation
) {
    var entity = AppEntity()
    if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
        entity = conversation.conversationWith as User
        CometChat.deleteConversation(
            entity.uid,
            conversation.conversationType,
            object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    conversationList.remove(conversation)
                    rvConversation.remove(conversation)
                }

                override fun onError(p0: CometChatException?) {
                    ErrorMessagesUtils.cometChatErrorMessage(context, p0?.code)
                }

            })
    } else {
        entity = conversation.conversationWith as Group
        CometChat.deleteConversation(
            entity.guid,
            conversation.conversationType,
            object : CometChat.CallbackListener<String>() {
                override fun onSuccess(p0: String?) {
                    conversationList.remove(conversation)
                    rvConversation?.remove(conversation)
                }

                override fun onError(p0: CometChatException?) {
                    ErrorMessagesUtils.cometChatErrorMessage(context, p0?.code)
                }

            })
    }
}

private fun checkDarkMode(context: Context, view: View, tvTitle: TextView?) {
    if (Utils.isDarkMode(context)) {
        tvTitle?.setTextColor(view.resources.getColor(R.color.textColorWhite))
    } else {
        tvTitle?.setTextColor(view.resources.getColor(R.color.primaryTextColor))
    }
}

/**
 * This method is used to retrieve list of conversations you have done.
 * For more detail please visit our official documentation []//prodocs.cometchat.com/docs/android-messaging-retrieve-conversations" ">&quot;https://prodocs.cometchat.com/docs/android-messaging-retrieve-conversations&quot;
 *
 * @see ConversationsRequest
 */
private fun makeConversationList(
    context: Context,
    conversationsRequest: ConversationsRequest?,
    noConversationView: LinearLayout,
    rvConversation: CometChatConversation,
    conversationList: MutableList<Conversation>,
    onConversationChange: (ConversationsRequest) -> Unit,
    onConversationListChange: (MutableList<Conversation>) -> Unit
) {
    if (conversationsRequest == null) {
        val conversationsReq = ConversationsRequest.ConversationsRequestBuilder()
            .setConversationType(UIKitSettings.conversationInMode.toString()).setLimit(50).build()
        onConversationChange.invoke(conversationsReq)


    }
    conversationsRequest?.fetchNext(object : CometChat.CallbackListener<List<Conversation>>() {
        override fun onSuccess(conversations: List<Conversation>) {
            if (conversations.isNotEmpty()) {
//                stopHideShimmer()
                noConversationView.visibility = View.GONE
                rvConversation.setConversationList(conversations)
                val conversationLst = conversations as MutableList<Conversation>
                onConversationListChange.invoke(conversationLst)
            } else {
                checkNoConverstaion(noConversationView, rvConversation)
            }
        }

        override fun onError(e: CometChatException) {
//            stopHideShimmer()
//            if (activity != null)
            ErrorMessagesUtils.cometChatErrorMessage(context, e.code)
            Log.d("CometChatConversationList.TAG", "onError: " + e.message)
        }
    })
}

private fun checkNoConverstaion(
    noConversationView: LinearLayout,
    rvConversation: CometChatConversation
) {
    if (rvConversation.size() == 0) {
//        stopHideShimmer()
        noConversationView.visibility = View.VISIBLE
        rvConversation.visibility = View.GONE
    } else {
        noConversationView.visibility = View.GONE
        rvConversation.visibility = View.VISIBLE
    }
}
/**
 * This method has message listener which recieve real time message and based on these messages, conversations are updated.
 *
 * @see CometChat.addMessageListener
 */
private fun addConversationListener(rvConversation: CometChatConversation?,noConversationView: LinearLayout) {
    CometChat.addMessageListener("CometChatConversationList.TAG", object : CometChat.MessageListener() {
        override fun onTextMessageReceived(message: TextMessage) {
            if (rvConversation != null) {
                rvConversation?.refreshConversation(message)
                checkNoConverstaion(rvConversation=rvConversation, noConversationView = noConversationView)
            }
        }

        override fun onMediaMessageReceived(message: MediaMessage) {
            if (rvConversation != null) {
                rvConversation?.refreshConversation(message)
                checkNoConverstaion(rvConversation=rvConversation, noConversationView = noConversationView)
            }
        }

        override fun onCustomMessageReceived(message: CustomMessage) {
            if (rvConversation != null) {
                rvConversation?.refreshConversation(message)
                checkNoConverstaion(rvConversation=rvConversation, noConversationView = noConversationView)
            }
        }

        override fun onMessagesDelivered(messageReceipt: MessageReceipt) {
            if (rvConversation != null) rvConversation?.setReciept(messageReceipt)
        }

        override fun onMessagesRead(messageReceipt: MessageReceipt) {
            if (rvConversation != null) rvConversation?.setReciept(messageReceipt)
        }

        override fun onMessageEdited(message: BaseMessage) {
            if (rvConversation != null) rvConversation?.refreshConversation(message)
        }

        override fun onMessageDeleted(message: BaseMessage) {
            if (rvConversation != null) rvConversation?.refreshConversation(message)
        }
    })
    CometChat.addGroupListener("CometChatConversationList.TAG", object : CometChat.GroupListener() {
        override fun onGroupMemberKicked(action: Action, kickedUser: User, kickedBy: User, kickedFrom: Group) {
            Log.e("CometChatConversationList.TAG", "onGroupMemberKicked: $kickedUser")
            if (kickedUser.uid == CometChat.getLoggedInUser().uid) {
                if (rvConversation != null) updateConversation(rvConversation,noConversationView,action, true)
            } else {
                updateConversation(rvConversation,noConversationView,action, false)
            }
        }

        override fun onMemberAddedToGroup(action: Action, addedby: User, userAdded: User, addedTo: Group) {
            Log.e("CometChatConversationList.TAG", "onMemberAddedToGroup: ")
            updateConversation(rvConversation,noConversationView,action, false)
        }

        override fun onGroupMemberJoined(action: Action, joinedUser: User, joinedGroup: Group) {
            Log.e("CometChatConversationList.TAG", "onGroupMemberJoined: ")
            updateConversation(rvConversation,noConversationView,action, false)
        }

        override fun onGroupMemberLeft(action: Action, leftUser: User, leftGroup: Group) {
            Log.e("CometChatConversationList.TAG", "onGroupMemberLeft: ")
            if (leftUser.uid == CometChat.getLoggedInUser().uid) {
                updateConversation(rvConversation,noConversationView,action, true)
            } else {
                updateConversation(rvConversation,noConversationView,action, false)
            }
        }

        override fun onGroupMemberScopeChanged(action: Action, updatedBy: User, updatedUser: User, scopeChangedTo: String, scopeChangedFrom: String, group: Group) {
            updateConversation(rvConversation,noConversationView,action, false)
        }
    })
}

/**
 * This method is used to update conversation received in real-time.
 * @param baseMessage is object of BaseMessage.class used to get respective Conversation.
 * @param isRemove is boolean used to check whether conversation needs to be removed or not.
 *
 * @see CometChatHelper.getConversationFromMessage
 */
private fun updateConversation(rvConversation: CometChatConversation?,noConversationView:LinearLayout,baseMessage: BaseMessage, isRemove: Boolean) {
    if (rvConversation != null) {
        val conversation = CometChatHelper.getConversationFromMessage(baseMessage)
        if (isRemove) rvConversation?.remove(conversation)
        else if (UIKitSettings.conversationInMode == ConversationMode.ALL_CHATS) rvConversation?.update(conversation)
        else if (UIKitSettings.conversationInMode == ConversationMode.GROUP && conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP)
            rvConversation?.update(conversation)
        else if (UIKitSettings.conversationInMode == ConversationMode.USER && conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER)
            rvConversation?.update(conversation)
        checkNoConverstaion(rvConversation=rvConversation, noConversationView = noConversationView)
    }
}
