package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class CometchatConversationListItemBindingImpl extends CometchatConversationListItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.conversationView, 1);
        sViewsWithIds.put(R.id.av_user, 2);
        sViewsWithIds.put(R.id.user_status, 3);
        sViewsWithIds.put(R.id.rl_name_and_status_layout, 4);
        sViewsWithIds.put(R.id.txt_user_name, 5);
        sViewsWithIds.put(R.id.txt_user_message, 6);
        sViewsWithIds.put(R.id.tvSeprator, 7);
        sViewsWithIds.put(R.id.messageTime, 8);
        sViewsWithIds.put(R.id.messageCount, 9);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public CometchatConversationListItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private CometchatConversationListItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar) bindings[2]
            , (android.widget.RelativeLayout) bindings[1]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatBadgeCount.CometChatBadgeCount) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.RelativeLayout) bindings[4]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[5]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatUserPresence.CometChatUserPresence) bindings[3]
            , (android.widget.RelativeLayout) bindings[0]
            );
        this.viewForeground.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.conversation == variableId) {
            setConversation((com.cometchat.pro.models.Conversation) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setConversation(@Nullable com.cometchat.pro.models.Conversation Conversation) {
        this.mConversation = Conversation;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): conversation
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}