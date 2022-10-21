package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageRightWhiteboardItemBindingImpl extends MessageRightWhiteboardItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rl_message, 1);
        sViewsWithIds.put(R.id.cv_message_container, 2);
        sViewsWithIds.put(R.id.top_message_layout, 3);
        sViewsWithIds.put(R.id.icon, 4);
        sViewsWithIds.put(R.id.whiteboard_message, 5);
        sViewsWithIds.put(R.id.join_whiteboard, 6);
        sViewsWithIds.put(R.id.txt_time, 7);
        sViewsWithIds.put(R.id.reaction_group, 8);
        sViewsWithIds.put(R.id.reactions_layout, 9);
        sViewsWithIds.put(R.id.thread_reply_count, 10);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageRightWhiteboardItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private MessageRightWhiteboardItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[2]
            , (android.widget.ImageView) bindings[4]
            , (com.google.android.material.button.MaterialButton) bindings[6]
            , (android.widget.HorizontalScrollView) bindings[8]
            , (com.google.android.material.chip.ChipGroup) bindings[9]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[10]
            , (android.widget.RelativeLayout) bindings[3]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[5]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
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
        if (BR.whiteBoardMessage == variableId) {
            setWhiteBoardMessage((com.cometchat.pro.models.CustomMessage) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setWhiteBoardMessage(@Nullable com.cometchat.pro.models.CustomMessage WhiteBoardMessage) {
        this.mWhiteBoardMessage = WhiteBoardMessage;
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
        flag 0 (0x1L): whiteBoardMessage
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}