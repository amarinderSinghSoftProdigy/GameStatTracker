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
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.google.android.material.chip.ChipGroup;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MessageLeftTextItemBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout cvMessageContainer;

  @NonNull
  public final TextView goTxtMessage;

  @NonNull
  public final ImageView imgPending;

  @NonNull
  public final CometChatAvatar ivUser;

  @NonNull
  public final HorizontalScrollView reactionGroup;

  @NonNull
  public final ChipGroup reactionsLayout;

  @NonNull
  public final LinearLayout replyAvatarLayout;

  @NonNull
  public final MessageLeftReplyItemBinding replyItem;

  @NonNull
  public final RelativeLayout rlMessage;

  @NonNull
  public final TextView threadReplyCount;

  @NonNull
  public final TextView tvUser;

  @NonNull
  public final TextView txtTime;

  @Bindable
  protected TextMessage mTextMessage;

  protected MessageLeftTextItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout cvMessageContainer, TextView goTxtMessage, ImageView imgPending,
      CometChatAvatar ivUser, HorizontalScrollView reactionGroup, ChipGroup reactionsLayout,
      LinearLayout replyAvatarLayout, MessageLeftReplyItemBinding replyItem,
      RelativeLayout rlMessage, TextView threadReplyCount, TextView tvUser, TextView txtTime) {
    super(_bindingComponent, _root, _localFieldCount);
    this.cvMessageContainer = cvMessageContainer;
    this.goTxtMessage = goTxtMessage;
    this.imgPending = imgPending;
    this.ivUser = ivUser;
    this.reactionGroup = reactionGroup;
    this.reactionsLayout = reactionsLayout;
    this.replyAvatarLayout = replyAvatarLayout;
    this.replyItem = replyItem;
    this.rlMessage = rlMessage;
    this.threadReplyCount = threadReplyCount;
    this.tvUser = tvUser;
    this.txtTime = txtTime;
  }

  public abstract void setTextMessage(@Nullable TextMessage textMessage);

  @Nullable
  public TextMessage getTextMessage() {
    return mTextMessage;
  }

  @NonNull
  public static MessageLeftTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_left_text_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MessageLeftTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MessageLeftTextItemBinding>inflateInternal(inflater, R.layout.message_left_text_item, root, attachToRoot, component);
  }

  @NonNull
  public static MessageLeftTextItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_left_text_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MessageLeftTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MessageLeftTextItemBinding>inflateInternal(inflater, R.layout.message_left_text_item, null, false, component);
  }

  public static MessageLeftTextItemBinding bind(@NonNull View view) {
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
  public static MessageLeftTextItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (MessageLeftTextItemBinding)bind(component, view, R.layout.message_left_text_item);
  }
}
