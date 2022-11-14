package com.allballapp.android.ui.features.home.teams.chat



//import com.facebook.shimmer.ShimmerFrameLayout
import android.view.*
import android.widget.*
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.CometChat.*
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.*
//import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener

/*

* Purpose - CometChatConversationList class is a fragment used to display list of conversations and perform certain action on click of item.
            It also provide search bar to perform search operation on the list of conversations.User can search by username, groupname, last message of conversation.

* Created on - 20th December 2019

* Modified on  - 23rd March 2020

*/
class ConversationFragment /*: Fragment(), TextWatcher*/ {
   /* private var rvConversation //Uses to display list of conversations.
            : CometChatConversation? = null*//*
    private var conversationsRequest //Uses to fetch Conversations.
            : ConversationsRequest? = null
    private var searchEdit //Uses to perform search operations.
            : EditText? = null
    private var clearSearch //Use to clear the search operation performed on list.
            : ImageView? = null
    private var tvTitle: TextView? = null
//    private var conversationShimmer: ShimmerFrameLayout? = null
    private var rlSearchBox: RelativeLayout? = null
    private var noConversationView: LinearLayout? = null
    private var vw: View? = null
    private var conversation : Conversation? = null
    private var conversationList: MutableList<Conversation> = ArrayList()
    private var ivStartConversation: ImageView? = null
   lateinit var binding:FragmentConversationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentConversationBinding.inflate(inflater,container,false);
//        val vm =binding.root
//        return binding.root;

        // Inflate the layout for this fragment
//        vw = inflater.inflate(com.softprodigy.ballerapp.R.layout.fragment_conversation, container, false)
//        rvConversation = binding.rvConversationList
        noConversationView = binding.noConversationView
        searchEdit = binding.searchBar
        tvTitle = binding.tvTitle
//        tvTitle?.typeface = FontUtils.getInstance(activity).getTypeFace(FontUtils.robotoMedium)
        rlSearchBox = binding.rlSearchBox
//        conversationShimmer = vw?.findViewById(R.id.shimmer_layout)
        clearSearch = binding.clearSearch
        ivStartConversation = binding.ivStartConversation
        if (!FeatureRestriction.isChatSearchEnabled()) {
            searchEdit?.visibility = View.GONE
            clearSearch?.visibility = View.GONE
        }
        checkDarkMode()
        ivStartConversation?.setOnClickListener {
            var intent = Intent(context, CometChatStartConversation::class.java)
            startActivity(intent)
        }
        searchEdit?.setOnEditorActionListener(OnEditorActionListener { textView: TextView, i: Int, keyEvent: KeyEvent? ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                binding.rvConversationList?.searchConversation(textView.text.toString())
                clearSearch?.visibility = View.VISIBLE
                return@OnEditorActionListener true
            }
            false
        })
        clearSearch?.setOnClickListener {
            searchEdit?.setText("")
            clearSearch?.visibility = View.GONE
            binding.rvConversationList?.searchConversation(searchEdit?.text.toString())
            val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // Hide the soft keyboard
            inputMethodManager.hideSoftInputFromWindow(searchEdit?.windowToken, 0)
        }

        // Uses to fetch next list of conversations if rvConversationList (RecyclerView) is scrolled in upward direction.
        binding.rvConversationList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    makeConversationList()
                }
            }
        })

        // Used to trigger event on click of conversation item in rvConversationList (RecyclerView)
        binding.rvConversationList?.setItemClickListener(object : OnItemClickListener<Conversation>() {

            override fun OnItemClick(t: Any, position: Int) {
                conversation = t as Conversation
                if (events != null)
                    events.OnItemClick(t as Conversation, position)
            }
        })

        val swipeHelper: RecyclerViewSwipeListener = object : RecyclerViewSwipeListener(context) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                val deleteBitmap: Bitmap = Utils.drawableToBitmap(
                    resources.getDrawable(R.drawable.ic_delete_white)
                )
                FeatureRestriction.isDeleteConversationEnabled(object :
                    FeatureRestriction.OnSuccessListener {
                    override fun onSuccess(booleanVal: Boolean) {
                        if (booleanVal) {
                            underlayButtons?.add(UnderlayButton(
                                "",
                                deleteBitmap,
                                resources.getColor(R.color.red),
                                object : UnderlayButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        val conversation: Conversation? = binding.rvConversationList?.getConversation(pos)
                                        if (conversation != null) {
                                            deleteConversations(conversation)
                                        }
                                    }
                                }
                            ))
                        }
                    }
                })
            }
        }
        swipeHelper.attachToRecyclerView(binding.rvConversationList)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        object : MySwipeHelper(requireContext(),rvConversation as RecyclerView,260){
//            override fun instantiateMyButton(
//                    viewHolder: RecyclerView.ViewHolder,
//                    buffer: MutableList<MyButton>
//            ) {
//                buffer.add(MyButton(object : MyButtonClickListener {
//                    override fun onClick(pos: Int) {
//                        var conversation : Conversation = conversationList[pos]
//                        deleteConversations(conversation)
//                    }
//                }))
//            }
//
//        }
//    }

    private fun deleteConversations(conversation: Conversation) {
        var entity = AppEntity()
        if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER) {
            entity = conversation.conversationWith as User
            deleteConversation(entity.uid, conversation.conversationType, object : CallbackListener<String>(){
                override fun onSuccess(p0: String?) {
                    conversationList.remove(conversation)
                    binding.rvConversationList?.remove(conversation)
                }

                override fun onError(p0: CometChatException?) {
                    ErrorMessagesUtils.cometChatErrorMessage(context, p0?.code)
                }

            })
        } else {
            entity = conversation.conversationWith as Group
            deleteConversation(entity.guid, conversation.conversationType, object : CallbackListener<String>(){
                override fun onSuccess(p0: String?) {
                    conversationList.remove(conversation)
                    binding.rvConversationList?.remove(conversation)
                }

                override fun onError(p0: CometChatException?) {
                    ErrorMessagesUtils.cometChatErrorMessage(context, p0?.code)
                }

            })
        }
    }

    private fun checkDarkMode() {
        if (Utils.isDarkMode(context!!)) {
            tvTitle?.setTextColor(resources.getColor(R.color.textColorWhite))
        } else {
            tvTitle?.setTextColor(resources.getColor(R.color.primaryTextColor))
        }
    }

    *//**
     * This method is used to retrieve list of conversations you have done.
     * For more detail please visit our official documentation []//prodocs.cometchat.com/docs/android-messaging-retrieve-conversations" ">&quot;https://prodocs.cometchat.com/docs/android-messaging-retrieve-conversations&quot;
     *
     * @see ConversationsRequest
     *//*
    private fun makeConversationList() {
        if (conversationsRequest == null) {
            conversationsRequest = ConversationsRequestBuilder().setConversationType(UIKitSettings.conversationInMode.toString()).setLimit(50).build()

        }
        conversationsRequest?.fetchNext(object : CallbackListener<List<Conversation>>() {
            override fun onSuccess(conversations: List<Conversation>) {
                if (conversations.isNotEmpty()) {
                    stopHideShimmer()
                    noConversationView?.visibility = View.GONE
                    binding.rvConversationList?.setConversationList(conversations)
                    conversationList = conversations as MutableList<Conversation>
                } else {
                    checkNoConverstaion()
                }
            }

            override fun onError(e: CometChatException) {
                stopHideShimmer()
                if (activity != null)
                    ErrorMessagesUtils.cometChatErrorMessage(context, e.code)
                Log.d(TAG, "onError: " + e.message)
            }
        })
    }

    private fun checkNoConverstaion() {
        if (binding.rvConversationList?.size() == 0) {
            stopHideShimmer()
            noConversationView?.visibility = View.VISIBLE
            binding.rvConversationList?.visibility = View.GONE
        } else {
            noConversationView?.visibility = View.GONE
            binding.rvConversationList?.visibility = View.VISIBLE
        }
    }

    *//**
     * This method is used to hide shimmer effect if the list is loaded.
     *//*
    private fun stopHideShimmer() {
//        conversationShimmer?.stopShimmer()
//        conversationShimmer?.visibility = View.GONE
        tvTitle?.visibility = View.VISIBLE
        rlSearchBox?.visibility = View.VISIBLE
    }

    *//**
     * This method has message listener which recieve real time message and based on these messages, conversations are updated.
     *
     * @see CometChat.addMessageListener
     *//*
    private fun addConversationListener() {
        CometChat.addMessageListener(TAG, object : CometChat.MessageListener() {
            override fun onTextMessageReceived(message: TextMessage) {
                if (binding.rvConversationList != null) {
                    binding.rvConversationList?.refreshConversation(message)
                    checkNoConverstaion()
                }
            }

            override fun onMediaMessageReceived(message: MediaMessage) {
                if (binding.rvConversationList != null) {
                    binding.rvConversationList?.refreshConversation(message)
                    checkNoConverstaion()
                }
            }

            override fun onCustomMessageReceived(message: CustomMessage) {
                if (binding.rvConversationList != null) {
                    binding.rvConversationList?.refreshConversation(message)
                    checkNoConverstaion()
                }
            }

            override fun onMessagesDelivered(messageReceipt: MessageReceipt) {
                if (binding.rvConversationList != null) binding.rvConversationList?.setReciept(messageReceipt)
            }

            override fun onMessagesRead(messageReceipt: MessageReceipt) {
                if (binding.rvConversationList != null) binding.rvConversationList?.setReciept(messageReceipt)
            }

            override fun onMessageEdited(message: BaseMessage) {
                if (binding.rvConversationList != null) binding.rvConversationList?.refreshConversation(message)
            }

            override fun onMessageDeleted(message: BaseMessage) {
                if (binding.rvConversationList != null) binding.rvConversationList?.refreshConversation(message)
            }
        })
        CometChat.addGroupListener(TAG, object : GroupListener() {
            override fun onGroupMemberKicked(action: Action, kickedUser: User, kickedBy: User, kickedFrom: Group) {
                Log.e(TAG, "onGroupMemberKicked: $kickedUser")
                if (kickedUser.uid == CometChat.getLoggedInUser().uid) {
                    if (binding.rvConversationList != null) updateConversation(action, true)
                } else {
                    updateConversation(action, false)
                }
            }

            override fun onMemberAddedToGroup(action: Action, addedby: User, userAdded: User, addedTo: Group) {
                Log.e(TAG, "onMemberAddedToGroup: ")
                updateConversation(action, false)
            }

            override fun onGroupMemberJoined(action: Action, joinedUser: User, joinedGroup: Group) {
                Log.e(TAG, "onGroupMemberJoined: ")
                updateConversation(action, false)
            }

            override fun onGroupMemberLeft(action: Action, leftUser: User, leftGroup: Group) {
                Log.e(TAG, "onGroupMemberLeft: ")
                if (leftUser.uid == CometChat.getLoggedInUser().uid) {
                    updateConversation(action, true)
                } else {
                    updateConversation(action, false)
                }
            }

            override fun onGroupMemberScopeChanged(action: Action, updatedBy: User, updatedUser: User, scopeChangedTo: String, scopeChangedFrom: String, group: Group) {
                updateConversation(action, false)
            }
        })
    }

    *//**
     * This method is used to update conversation received in real-time.
     * @param baseMessage is object of BaseMessage.class used to get respective Conversation.
     * @param isRemove is boolean used to check whether conversation needs to be removed or not.
     *
     * @see CometChatHelper.getConversationFromMessage
     *//*
    private fun updateConversation(baseMessage: BaseMessage, isRemove: Boolean) {
        if (binding.rvConversationList != null) {
            val conversation = CometChatHelper.getConversationFromMessage(baseMessage)
            if (isRemove) binding.rvConversationList?.remove(conversation)
            else if (UIKitSettings.conversationInMode == ConversationMode.ALL_CHATS) binding.rvConversationList?.update(conversation)
            else if (UIKitSettings.conversationInMode == ConversationMode.GROUP && conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP)
                binding.rvConversationList?.update(conversation)
            else if (UIKitSettings.conversationInMode == ConversationMode.USER && conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_USER)
                binding.rvConversationList?.update(conversation)
            checkNoConverstaion()
        }
    }

    *//**
     * This method is used to remove the conversationlistener.
     *//*
    private fun removeConversationListener() {
        CometChat.removeMessageListener(TAG)
        CometChat.removeGroupListener(TAG)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        conversationsRequest = null
        searchEdit?.addTextChangedListener(this)
        binding.rvConversationList?.clearList()
        makeConversationList()
        addConversationListener()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        searchEdit?.removeTextChangedListener(this)
        removeConversationListener()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        if (s.isEmpty()) {
//                    // if searchEdit is empty then fetch all conversations.
            conversationsRequest = null
            binding.rvConversationList?.clearList()
            makeConversationList()
        } else {
//                    // Search conversation based on text in searchEdit field.
            binding.rvConversationList?.searchConversation(s.toString())
        }
    }

    companion object {
        lateinit var events: OnItemClickListener<Any>
        private const val TAG = "ConversationList"

        *//**
         * @param onItemClickListener An object of `OnItemClickListener<T>` abstract class helps to initialize with events
         * to perform onItemClick & onItemLongClick.
         * @see OnItemClickListener
         *//*
        fun setItemClickListener(onItemClickListener: OnItemClickListener<Any>) {
            events = onItemClickListener
        }
    }*/
}