// Generated by data binding compiler. Do not edit!
package com.cometchat.pro.uikit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
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
import com.google.android.material.chip.ChipGroup;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MessageRightTextItemBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout cvMessageContainer;

  @NonNull
  public final TextView goTxtMessage;

  @NonNull
  public final HorizontalScrollView reactionGroup;

  @NonNull
  public final ChipGroup reactionsLayout;

  @NonNull
  public final LinearLayout replyAvatarLayout;

  @NonNull
  public final MessageRightReplyItemBinding replyItem;

  @NonNull
  public final RelativeLayout rlMessage;

  @NonNull
  public final TextView threadReplyCount;

  @NonNull
  public final TextView txtTime;

  @Bindable
  protected TextMessage mTextMessage;

  protected MessageRightTextItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout cvMessageContainer, TextView goTxtMessage, HorizontalScrollView reactionGroup,
      ChipGroup reactionsLayout, LinearLayout replyAvatarLayout,
      MessageRightReplyItemBinding replyItem, RelativeLayout rlMessage, TextView threadReplyCount,
      TextView txtTime) {
    super(_bindingComponent, _root, _localFieldCount);
    this.cvMessageContainer = cvMessageContainer;
    this.goTxtMessage = goTxtMessage;
    this.reactionGroup = reactionGroup;
    this.reactionsLayout = reactionsLayout;
    this.replyAvatarLayout = replyAvatarLayout;
    this.replyItem = replyItem;
    this.rlMessage = rlMessage;
    this.threadReplyCount = threadReplyCount;
    this.txtTime = txtTime;
  }

  public abstract void setTextMessage(@Nullable TextMessage textMessage);

  @Nullable
  public TextMessage getTextMessage() {
    return mTextMessage;
  }

  @NonNull
  public static MessageRightTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_right_text_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MessageRightTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MessageRightTextItemBinding>inflateInternal(inflater, R.layout.message_right_text_item, root, attachToRoot, component);
  }

  @NonNull
  public static MessageRightTextItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_right_text_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MessageRightTextItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MessageRightTextItemBinding>inflateInternal(inflater, R.layout.message_right_text_item, null, false, component);
  }

  public static MessageRightTextItemBinding bind(@NonNull View view) {
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
  public static MessageRightTextItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (MessageRightTextItemBinding)bind(component, view, R.layout.message_right_text_item);
  }
}