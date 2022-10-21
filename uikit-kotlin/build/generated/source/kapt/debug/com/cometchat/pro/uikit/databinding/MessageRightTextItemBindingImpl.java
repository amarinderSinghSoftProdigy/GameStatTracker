package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageRightTextItemBindingImpl extends MessageRightTextItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(10);
        sIncludes.setIncludes(1, 
            new String[] {"message_right_reply_item"},
            new int[] {2},
            new int[] {com.cometchat.pro.uikit.R.layout.message_right_reply_item});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rl_message, 3);
        sViewsWithIds.put(R.id.go_txt_message, 4);
        sViewsWithIds.put(R.id.reaction_group, 5);
        sViewsWithIds.put(R.id.reactions_layout, 6);
        sViewsWithIds.put(R.id.reply_avatar_layout, 7);
        sViewsWithIds.put(R.id.thread_reply_count, 8);
        sViewsWithIds.put(R.id.txt_time, 9);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageRightTextItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private MessageRightTextItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[4]
            , (android.widget.HorizontalScrollView) bindings[5]
            , (com.google.android.material.chip.ChipGroup) bindings[6]
            , (android.widget.LinearLayout) bindings[7]
            , (com.cometchat.pro.uikit.databinding.MessageRightReplyItemBinding) bindings[2]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[9]
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
                return onChangeReplyItem((com.cometchat.pro.uikit.databinding.MessageRightReplyItemBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeReplyItem(com.cometchat.pro.uikit.databinding.MessageRightReplyItemBinding ReplyItem, int fieldId) {
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