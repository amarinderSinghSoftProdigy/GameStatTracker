// Generated by data binding compiler. Do not edit!
package com.cometchat.pro.uikit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.google.android.material.chip.ChipGroup;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MessageLeftFileItemBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout cvMessageContainer;

  @NonNull
  public final LinearLayout fileTypeLayout;

  @NonNull
  public final ImageView ivIcon;

  @NonNull
  public final CometChatAvatar ivUser;

  @NonNull
  public final HorizontalScrollView reactionGroup;

  @NonNull
  public final ChipGroup reactionsLayout;

  @NonNull
  public final LinearLayout replyAvatarLayout;

  @NonNull
  public final RelativeLayout rlMessage;

  @NonNull
  public final TextView threadReplyCount;

  @NonNull
  public final TextView tvFileExtension;

  @NonNull
  public final TextView tvFileName;

  @NonNull
  public final TextView tvFileSize;

  @NonNull
  public final TextView tvUser;

  @NonNull
  public final TextView txtTime;

  @Bindable
  protected MediaMessage mMediaMessage;

  protected MessageLeftFileItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout cvMessageContainer, LinearLayout fileTypeLayout, ImageView ivIcon,
      CometChatAvatar ivUser, HorizontalScrollView reactionGroup, ChipGroup reactionsLayout,
      LinearLayout replyAvatarLayout, RelativeLayout rlMessage, TextView threadReplyCount,
      TextView tvFileExtension, TextView tvFileName, TextView tvFileSize, TextView tvUser,
      TextView txtTime) {
    super(_bindingComponent, _root, _localFieldCount);
    this.cvMessageContainer = cvMessageContainer;
    this.fileTypeLayout = fileTypeLayout;
    this.ivIcon = ivIcon;
    this.ivUser = ivUser;
    this.reactionGroup = reactionGroup;
    this.reactionsLayout = reactionsLayout;
    this.replyAvatarLayout = replyAvatarLayout;
    this.rlMessage = rlMessage;
    this.threadReplyCount = threadReplyCount;
    this.tvFileExtension = tvFileExtension;
    this.tvFileName = tvFileName;
    this.tvFileSize = tvFileSize;
    this.tvUser = tvUser;
    this.txtTime = txtTime;
  }

  public abstract void setMediaMessage(@Nullable MediaMessage mediaMessage);

  @Nullable
  public MediaMessage getMediaMessage() {
    return mMediaMessage;
  }

  @NonNull
  public static MessageLeftFileItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_left_file_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MessageLeftFileItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MessageLeftFileItemBinding>inflateInternal(inflater, R.layout.message_left_file_item, root, attachToRoot, component);
  }

  @NonNull
  public static MessageLeftFileItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_left_file_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MessageLeftFileItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MessageLeftFileItemBinding>inflateInternal(inflater, R.layout.message_left_file_item, null, false, component);
  }

  public static MessageLeftFileItemBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static MessageLeftFileItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (MessageLeftFileItemBinding)bind(component, view, R.layout.message_left_file_item);
  }
}