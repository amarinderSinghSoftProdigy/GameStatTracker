package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageLeftTextItemBindingImpl extends MessageLeftTextItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(13);
        sIncludes.setIncludes(1, 
            new String[] {"message_left_reply_item"},
            new int[] {2},
            new int[] {com.cometchat.pro.uikit.R.layout.message_left_reply_item});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rl_message, 3);
        sViewsWithIds.put(R.id.iv_user, 4);
        sViewsWithIds.put(R.id.tv_user, 5);
        sViewsWithIds.put(R.id.go_txt_message, 6);
        sViewsWithIds.put(R.id.img_pending, 7);
        sViewsWithIds.put(R.id.reaction_group, 8);
        sViewsWithIds.put(R.id.reactions_layout, 9);
        sViewsWithIds.put(R.id.reply_avatar_layout, 10);
        sViewsWithIds.put(R.id.thread_reply_count, 11);
        sViewsWithIds.put(R.id.txt_time, 12);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageLeftTextItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private MessageLeftTextItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ImageView) bindings[7]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar) bindings[4]
            , (android.widget.HorizontalScrollView) bindings[8]
            , (com.google.android.material.chip.ChipGroup) bindings[9]
            , (android.widget.LinearLayout) bindings[10]
            , (com.cometchat.pro.uikit.databinding.MessageLeftReplyItemBinding) bindings[2]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[12]
            );
        this.cvMessageContainer.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        setContainedBinding(this.replyItem);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        replyItem.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (replyItem.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.textMessage == variableId) {
            setTextMessage((com.cometchat.pro.models.TextMessage) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setTextMessage(@Nullable com.cometchat.pro.models.TextMessage TextMessage) {
        this.mTextMessage = TextMessage;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.textMessage);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        replyItem.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeReplyItem((com.cometchat.pro.uikit.databinding.MessageLeftReplyItemBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeReplyItem(com.cometchat.pro.uikit.databinding.MessageLeftReplyItemBinding ReplyItem, int fieldId) {
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
        com.cometchat.pro.models.TextMessage textMessage = mTextMessage;

        if ((dirtyFlags & 0x6L) != 0) {
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            this.replyItem.setReplyTextMessage(textMessage);
        }
        executeBindingsOn(replyItem);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): replyItem
        flag 1 (0x2L): textMessage
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}