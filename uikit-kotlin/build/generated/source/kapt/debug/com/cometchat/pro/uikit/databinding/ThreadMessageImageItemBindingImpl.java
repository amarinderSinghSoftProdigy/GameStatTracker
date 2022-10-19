package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ThreadMessageImageItemBindingImpl extends ThreadMessageImageItemBinding  {

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
        sViewsWithIds.put(R.id.cv_image_message_container, 4);
        sViewsWithIds.put(R.id.go_img_message, 5);
        sViewsWithIds.put(R.id.sensitive_layout, 6);
        sViewsWithIds.put(R.id.sensitive_desc, 7);
        sViewsWithIds.put(R.id.img_progress_bar, 8);
        sViewsWithIds.put(R.id.reactions_layout, 9);
        sViewsWithIds.put(R.id.txt_time, 10);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ThreadMessageImageItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private ThreadMessageImageItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.cardview.widget.CardView) bindings[4]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ProgressBar) bindings[8]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar) bindings[2]
            , (com.google.android.material.chip.ChipGroup) bindings[9]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[7]
            , (android.widget.RelativeLayout) bindings[6]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[10]
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