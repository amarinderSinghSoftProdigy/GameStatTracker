// Generated by data binding compiler. Do not edit!
package com.cometchat.pro.uikit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatUserPresence.CometChatUserPresence;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class CometchatUserListItemBinding extends ViewDataBinding {
  @NonNull
  public final CometChatAvatar avUser;

  @NonNull
  public final CometChatUserPresence statusIndicator;

  @NonNull
  public final TextView tvSeprator;

  @NonNull
  public final TextView txtUserName;

  @NonNull
  public final TextView txtUserScope;

  @NonNull
  public final ImageView unblockUser;

  protected CometchatUserListItemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      CometChatAvatar avUser, CometChatUserPresence statusIndicator, TextView tvSeprator,
      TextView txtUserName, TextView txtUserScope, ImageView unblockUser) {
    super(_bindingComponent, _root, _localFieldCount);
    this.avUser = avUser;
    this.statusIndicator = statusIndicator;
    this.tvSeprator = tvSeprator;
    this.txtUserName = txtUserName;
    this.txtUserScope = txtUserScope;
    this.unblockUser = unblockUser;
  }

  @NonNull
  public static CometchatUserListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.cometchat_user_list_item, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static CometchatUserListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<CometchatUserListItemBinding>inflateInternal(inflater, R.layout.cometchat_user_list_item, root, attachToRoot, component);
  }

  @NonNull
  public static CometchatUserListItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.cometchat_user_list_item, null, false, component)
   */
  @NonNull
  @Deprecated
  public static CometchatUserListItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<CometchatUserListItemBinding>inflateInternal(inflater, R.layout.cometchat_user_list_item, null, false, component);
  }

  public static CometchatUserListItemBinding bind(@NonNull View view) {
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
  public static CometchatUserListItemBinding bind(@NonNull View view, @Nullable Object component) {
    return (CometchatUserListItemBinding)bind(component, view, R.layout.cometchat_user_list_item);
  }
}