package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageLeftPollsItemBindingImpl extends MessageLeftPollsItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rl_message, 1);
        sViewsWithIds.put(R.id.iv_user, 2);
        sViewsWithIds.put(R.id.tv_user, 3);
        sViewsWithIds.put(R.id.cv_message_container, 4);
        sViewsWithIds.put(R.id.tv_question, 5);
        sViewsWithIds.put(R.id.loading_progressBar, 6);
        sViewsWithIds.put(R.id.options_group, 7);
        sViewsWithIds.put(R.id.total_votes, 8);
        sViewsWithIds.put(R.id.reaction_group, 9);
        sViewsWithIds.put(R.id.reactions_layout, 10);
        sViewsWithIds.put(R.id.thread_reply_count, 11);
        sViewsWithIds.put(R.id.txt_time, 12);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageLeftPollsItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private MessageLeftPollsItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[4]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar) bindings[2]
            , (android.widget.ProgressBar) bindings[6]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.HorizontalScrollView) bindings[9]
            , (com.google.android.material.chip.ChipGroup) bindings[10]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[12]
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
        if (BR.pollMessage == variableId) {
            setPollMessage((com.cometchat.pro.models.CustomMessage) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setPollMessage(@Nullable com.cometchat.pro.models.CustomMessage PollMessage) {
        this.mPollMessage = PollMessage;
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
        flag 0 (0x1L): pollMessage
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}