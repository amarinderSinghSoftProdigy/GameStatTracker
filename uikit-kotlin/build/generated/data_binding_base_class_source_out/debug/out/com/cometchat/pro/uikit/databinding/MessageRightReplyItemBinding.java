// Generated by data binding compiler. Do not edit!
package com.cometchat.pro.uikit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.uikit.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MessageRightReplyItemBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout replyLayout;

  @NonNull
  public final ImageView replyMedia;

  @NonNull
  public final CardView replyMediaThumbnail;

  @NonNull
  public final TextView replyMessage;

  @NonNull
  public final TextView replyUser;

  @NonNull
  public final LinearLayout replyUserInfo;

  @Bindable
  protected TextMessage mTextMessage;

  protected MessageRightReplyItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      RelativeLayout replyLayout, ImageView replyMedia, CardView replyMediaThumbnail,
      TextView replyMessage, TextView replyUser, LinearLayout replyUserInfo) {
    super(_bindingComponent, _root, _localFieldCount);
    this.replyLayout = replyLayout;
    this.replyMedia = replyMedia;
    this.replyMediaThumbnail = replyMediaThumbnail;
    this.replyMessage = replyMessage;
    this.replyUser = replyUser;
    this.replyUserInfo = replyUserInfo;
  }

  public abstract void setTextMessage(@Nullable TextMessage textMessage);

  @Nullable
  public TextMessage getTextMessage() {
    return mTextMessage;
  }

  @NonNull
  public static MessageRightReplyItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_right_reply_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MessageRightReplyItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MessageRightReplyItemBinding>inflateInternal(inflater, R.layout.message_right_reply_item, root, attachToRoot, component);
  }

  @NonNull
  public static MessageRightReplyItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.message_right_reply_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MessageRightReplyItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MessageRightReplyItemBinding>inflateInternal(inflater, R.layout.message_right_reply_item, null, false, component);
  }

  public static MessageRightReplyItemBinding bind(@NonNull View view) {
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
  public static MessageRightReplyItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (MessageRightReplyItemBinding)bind(component, view, R.layout.message_right_reply_item);
  }
}
