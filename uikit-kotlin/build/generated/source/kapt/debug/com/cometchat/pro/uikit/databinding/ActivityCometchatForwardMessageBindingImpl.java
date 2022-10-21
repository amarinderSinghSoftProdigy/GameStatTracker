package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityCometchatForwardMessageBindingImpl extends ActivityCometchatForwardMessageBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.forward_toolbar, 1);
        sViewsWithIds.put(R.id.searchbar_view, 2);
        sViewsWithIds.put(R.id.search_bar, 3);
        sViewsWithIds.put(R.id.clear_search, 4);
        sViewsWithIds.put(R.id.selected_view, 5);
        sViewsWithIds.put(R.id.selected_user, 6);
        sViewsWithIds.put(R.id.rv_conversation_list, 7);
        sViewsWithIds.put(R.id.btn_forward, 8);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityCometchatForwardMessageBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private ActivityCometchatForwardMessageBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (com.google.android.material.button.MaterialButton) bindings[8]
            , (android.widget.ImageView) bindings[4]
            , (com.google.android.material.appbar.MaterialToolbar) bindings[1]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatConversations.CometChatConversation) bindings[7]
            , (android.widget.EditText) bindings[3]
            , (android.widget.RelativeLayout) bindings[2]
            , (com.google.android.material.chip.ChipGroup) bindings[6]
            , (android.widget.HorizontalScrollView) bindings[5]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
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
        if (BR.conversationList == variableId) {
            setConversationList((androidx.databinding.ObservableList<com.cometchat.pro.models.Conversation>) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setConversationList(@Nullable androidx.databinding.ObservableList<com.cometchat.pro.models.Conversation> ConversationList) {
        this.mConversationList = ConversationList;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeConversationList((androidx.databinding.ObservableList<com.cometchat.pro.models.Conversation>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeConversationList(androidx.databinding.ObservableList<com.cometchat.pro.models.Conversation> ConversationList, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
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
        flag 0 (0x1L): conversationList
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}