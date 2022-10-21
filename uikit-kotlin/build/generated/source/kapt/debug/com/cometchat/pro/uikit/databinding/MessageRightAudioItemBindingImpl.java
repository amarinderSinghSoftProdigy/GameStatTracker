package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageRightAudioItemBindingImpl extends MessageRightAudioItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.cv_message_container, 1);
        sViewsWithIds.put(R.id.title, 2);
        sViewsWithIds.put(R.id.playBtn, 3);
        sViewsWithIds.put(R.id.audiolength_tv, 4);
        sViewsWithIds.put(R.id.reaction_group, 5);
        sViewsWithIds.put(R.id.reactions_layout, 6);
        sViewsWithIds.put(R.id.reply_avatar_layout, 7);
        sViewsWithIds.put(R.id.thread_reply_count, 8);
        sViewsWithIds.put(R.id.txt_time, 9);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageRightAudioItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 10, sIncludes, sViewsWithIds));
    }
    private MessageRightAudioItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[4]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.HorizontalScrollView) bindings[5]
            , (com.google.android.material.chip.ChipGroup) bindings[6]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[9]
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
        if (BR.mediaMessage == variableId) {
            setMediaMessage((com.cometchat.pro.models.MediaMessage) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setMediaMessage(@Nullable com.cometchat.pro.models.MediaMessage MediaMessage) {
        this.mMediaMessage = MediaMessage;
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
        flag 0 (0x1L): mediaMessage
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}