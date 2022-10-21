package com.cometchat.pro.uikit.databinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MessageLeftLinkItemBindingImpl extends MessageLeftLinkItemBinding  {

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
        sViewsWithIds.put(R.id.cv_link_message_container, 4);
        sViewsWithIds.put(R.id.image_view, 5);
        sViewsWithIds.put(R.id.link_img, 6);
        sViewsWithIds.put(R.id.videoLink, 7);
        sViewsWithIds.put(R.id.link_title, 8);
        sViewsWithIds.put(R.id.link_subtitle, 9);
        sViewsWithIds.put(R.id.message, 10);
        sViewsWithIds.put(R.id.linkSeperator, 11);
        sViewsWithIds.put(R.id.visitLink, 12);
        sViewsWithIds.put(R.id.reaction_group, 13);
        sViewsWithIds.put(R.id.reactions_layout, 14);
        sViewsWithIds.put(R.id.reply_avatar_layout, 15);
        sViewsWithIds.put(R.id.thread_reply_count, 16);
        sViewsWithIds.put(R.id.txt_time, 17);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public MessageLeftLinkItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 18, sIncludes, sViewsWithIds));
    }
    private MessageLeftLinkItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.cardview.widget.CardView) bindings[4]
            , (android.widget.RelativeLayout) bindings[5]
            , (com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar) bindings[2]
            , (android.widget.ImageView) bindings[6]
            , (android.view.View) bindings[11]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.HorizontalScrollView) bindings[13]
            , (com.google.android.material.chip.ChipGroup) bindings[14]
            , (android.widget.LinearLayout) bindings[15]
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[17]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.TextView) bindings[12]
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
        if (BR.linkTextMessage == variableId) {
            setLinkTextMessage((com.cometchat.pro.models.TextMessage) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLinkTextMessage(@Nullable com.cometchat.pro.models.TextMessage LinkTextMessage) {
        this.mLinkTextMessage = LinkTextMessage;
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
        flag 0 (0x1L): linkTextMessage
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}