package com.cometchat.pro.uikit;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.cometchat.pro.uikit.databinding.ActivityCometchatForwardMessageBindingImpl;
import com.cometchat.pro.uikit.databinding.ActivityCometchatUnifiedBindingImpl;
import com.cometchat.pro.uikit.databinding.CometchatCallHistoryItemBindingImpl;
import com.cometchat.pro.uikit.databinding.CometchatCallListItemBindingImpl;
import com.cometchat.pro.uikit.databinding.CometchatConversationListItemBindingImpl;
import com.cometchat.pro.uikit.databinding.CometchatUserListItemBindingImpl;
import com.cometchat.pro.uikit.databinding.FragmentCometchatAddMemberBindingImpl;
import com.cometchat.pro.uikit.databinding.FragmentCometchatUserProfileBindingImpl;
import com.cometchat.pro.uikit.databinding.FragmentConversationScreenBindingImpl;
import com.cometchat.pro.uikit.databinding.GroupListItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageActionItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftAudioItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftCustomItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftDeleteItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftFileItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftGroupCallItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftLinkItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftListImageItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftListVideoItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftLocationItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftPollsItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftReplyItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftStickerItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftTextItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftWhiteboardItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageLeftWriteboardItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightAudioItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightCustomItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightDeleteItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightFileItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightGroupCallItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightLinkItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightListImageItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightListVideoItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightLocationItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightPollsItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightReplyItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightStickerItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightTextItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightWhiteboardItemBindingImpl;
import com.cometchat.pro.uikit.databinding.MessageRightWriteboardItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadLocationMessageItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageAudioItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageFileItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageImageItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageLinkItemBindingImpl;
import com.cometchat.pro.uikit.databinding.ThreadMessageVideoItemBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYCOMETCHATFORWARDMESSAGE = 1;

  private static final int LAYOUT_ACTIVITYCOMETCHATUNIFIED = 2;

  private static final int LAYOUT_COMETCHATCALLHISTORYITEM = 3;

  private static final int LAYOUT_COMETCHATCALLLISTITEM = 4;

  private static final int LAYOUT_COMETCHATCONVERSATIONLISTITEM = 5;

  private static final int LAYOUT_COMETCHATUSERLISTITEM = 6;

  private static final int LAYOUT_FRAGMENTCOMETCHATADDMEMBER = 7;

  private static final int LAYOUT_FRAGMENTCOMETCHATUSERPROFILE = 8;

  private static final int LAYOUT_FRAGMENTCONVERSATIONSCREEN = 9;

  private static final int LAYOUT_GROUPLISTITEM = 10;

  private static final int LAYOUT_MESSAGEACTIONITEM = 11;

  private static final int LAYOUT_MESSAGELEFTAUDIOITEM = 12;

  private static final int LAYOUT_MESSAGELEFTCUSTOMITEM = 13;

  private static final int LAYOUT_MESSAGELEFTDELETEITEM = 14;

  private static final int LAYOUT_MESSAGELEFTFILEITEM = 15;

  private static final int LAYOUT_MESSAGELEFTGROUPCALLITEM = 16;

  private static final int LAYOUT_MESSAGELEFTLINKITEM = 17;

  private static final int LAYOUT_MESSAGELEFTLISTIMAGEITEM = 18;

  private static final int LAYOUT_MESSAGELEFTLISTVIDEOITEM = 19;

  private static final int LAYOUT_MESSAGELEFTLOCATIONITEM = 20;

  private static final int LAYOUT_MESSAGELEFTPOLLSITEM = 21;

  private static final int LAYOUT_MESSAGELEFTREPLYITEM = 22;

  private static final int LAYOUT_MESSAGELEFTSTICKERITEM = 23;

  private static final int LAYOUT_MESSAGELEFTTEXTITEM = 24;

  private static final int LAYOUT_MESSAGELEFTWHITEBOARDITEM = 25;

  private static final int LAYOUT_MESSAGELEFTWRITEBOARDITEM = 26;

  private static final int LAYOUT_MESSAGERIGHTAUDIOITEM = 27;

  private static final int LAYOUT_MESSAGERIGHTCUSTOMITEM = 28;

  private static final int LAYOUT_MESSAGERIGHTDELETEITEM = 29;

  private static final int LAYOUT_MESSAGERIGHTFILEITEM = 30;

  private static final int LAYOUT_MESSAGERIGHTGROUPCALLITEM = 31;

  private static final int LAYOUT_MESSAGERIGHTLINKITEM = 32;

  private static final int LAYOUT_MESSAGERIGHTLISTIMAGEITEM = 33;

  private static final int LAYOUT_MESSAGERIGHTLISTVIDEOITEM = 34;

  private static final int LAYOUT_MESSAGERIGHTLOCATIONITEM = 35;

  private static final int LAYOUT_MESSAGERIGHTPOLLSITEM = 36;

  private static final int LAYOUT_MESSAGERIGHTREPLYITEM = 37;

  private static final int LAYOUT_MESSAGERIGHTSTICKERITEM = 38;

  private static final int LAYOUT_MESSAGERIGHTTEXTITEM = 39;

  private static final int LAYOUT_MESSAGERIGHTWHITEBOARDITEM = 40;

  private static final int LAYOUT_MESSAGERIGHTWRITEBOARDITEM = 41;

  private static final int LAYOUT_THREADLOCATIONMESSAGEITEM = 42;

  private static final int LAYOUT_THREADMESSAGEAUDIOITEM = 43;

  private static final int LAYOUT_THREADMESSAGEFILEITEM = 44;

  private static final int LAYOUT_THREADMESSAGEIMAGEITEM = 45;

  private static final int LAYOUT_THREADMESSAGEITEM = 46;

  private static final int LAYOUT_THREADMESSAGELINKITEM = 47;

  private static final int LAYOUT_THREADMESSAGEVIDEOITEM = 48;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(48);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.activity_cometchat_forward_message, LAYOUT_ACTIVITYCOMETCHATFORWARDMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.activity_cometchat_unified, LAYOUT_ACTIVITYCOMETCHATUNIFIED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.cometchat_call_history_item, LAYOUT_COMETCHATCALLHISTORYITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.cometchat_call_list_item, LAYOUT_COMETCHATCALLLISTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.cometchat_conversation_list_item, LAYOUT_COMETCHATCONVERSATIONLISTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.cometchat_user_list_item, LAYOUT_COMETCHATUSERLISTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.fragment_cometchat_add_member, LAYOUT_FRAGMENTCOMETCHATADDMEMBER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.fragment_cometchat_user_profile, LAYOUT_FRAGMENTCOMETCHATUSERPROFILE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.fragment_conversation_screen, LAYOUT_FRAGMENTCONVERSATIONSCREEN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.group_list_item, LAYOUT_GROUPLISTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_action_item, LAYOUT_MESSAGEACTIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_audio_item, LAYOUT_MESSAGELEFTAUDIOITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_custom_item, LAYOUT_MESSAGELEFTCUSTOMITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_delete_item, LAYOUT_MESSAGELEFTDELETEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_file_item, LAYOUT_MESSAGELEFTFILEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_group_call_item, LAYOUT_MESSAGELEFTGROUPCALLITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_link_item, LAYOUT_MESSAGELEFTLINKITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_list_image_item, LAYOUT_MESSAGELEFTLISTIMAGEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_list_video_item, LAYOUT_MESSAGELEFTLISTVIDEOITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_location_item, LAYOUT_MESSAGELEFTLOCATIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_polls_item, LAYOUT_MESSAGELEFTPOLLSITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_reply_item, LAYOUT_MESSAGELEFTREPLYITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_sticker_item, LAYOUT_MESSAGELEFTSTICKERITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_text_item, LAYOUT_MESSAGELEFTTEXTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_whiteboard_item, LAYOUT_MESSAGELEFTWHITEBOARDITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_left_writeboard_item, LAYOUT_MESSAGELEFTWRITEBOARDITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_audio_item, LAYOUT_MESSAGERIGHTAUDIOITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_custom_item, LAYOUT_MESSAGERIGHTCUSTOMITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_delete_item, LAYOUT_MESSAGERIGHTDELETEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_file_item, LAYOUT_MESSAGERIGHTFILEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_group_call_item, LAYOUT_MESSAGERIGHTGROUPCALLITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_link_item, LAYOUT_MESSAGERIGHTLINKITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_list_image_item, LAYOUT_MESSAGERIGHTLISTIMAGEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_list_video_item, LAYOUT_MESSAGERIGHTLISTVIDEOITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_location_item, LAYOUT_MESSAGERIGHTLOCATIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_polls_item, LAYOUT_MESSAGERIGHTPOLLSITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_reply_item, LAYOUT_MESSAGERIGHTREPLYITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_sticker_item, LAYOUT_MESSAGERIGHTSTICKERITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_text_item, LAYOUT_MESSAGERIGHTTEXTITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_whiteboard_item, LAYOUT_MESSAGERIGHTWHITEBOARDITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.message_right_writeboard_item, LAYOUT_MESSAGERIGHTWRITEBOARDITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_location_message_item, LAYOUT_THREADLOCATIONMESSAGEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_audio_item, LAYOUT_THREADMESSAGEAUDIOITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_file_item, LAYOUT_THREADMESSAGEFILEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_image_item, LAYOUT_THREADMESSAGEIMAGEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_item, LAYOUT_THREADMESSAGEITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_link_item, LAYOUT_THREADMESSAGELINKITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.cometchat.pro.uikit.R.layout.thread_message_video_item, LAYOUT_THREADMESSAGEVIDEOITEM);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYCOMETCHATFORWARDMESSAGE: {
          if ("layout/activity_cometchat_forward_message_0".equals(tag)) {
            return new ActivityCometchatForwardMessageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_cometchat_forward_message is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYCOMETCHATUNIFIED: {
          if ("layout/activity_cometchat_unified_0".equals(tag)) {
            return new ActivityCometchatUnifiedBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_cometchat_unified is invalid. Received: " + tag);
        }
        case  LAYOUT_COMETCHATCALLHISTORYITEM: {
          if ("layout/cometchat_call_history_item_0".equals(tag)) {
            return new CometchatCallHistoryItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for cometchat_call_history_item is invalid. Received: " + tag);
        }
        case  LAYOUT_COMETCHATCALLLISTITEM: {
          if ("layout/cometchat_call_list_item_0".equals(tag)) {
            return new CometchatCallListItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for cometchat_call_list_item is invalid. Received: " + tag);
        }
        case  LAYOUT_COMETCHATCONVERSATIONLISTITEM: {
          if ("layout/cometchat_conversation_list_item_0".equals(tag)) {
            return new CometchatConversationListItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for cometchat_conversation_list_item is invalid. Received: " + tag);
        }
        case  LAYOUT_COMETCHATUSERLISTITEM: {
          if ("layout/cometchat_user_list_item_0".equals(tag)) {
            return new CometchatUserListItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for cometchat_user_list_item is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCOMETCHATADDMEMBER: {
          if ("layout/fragment_cometchat_add_member_0".equals(tag)) {
            return new FragmentCometchatAddMemberBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_cometchat_add_member is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCOMETCHATUSERPROFILE: {
          if ("layout/fragment_cometchat_user_profile_0".equals(tag)) {
            return new FragmentCometchatUserProfileBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_cometchat_user_profile is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTCONVERSATIONSCREEN: {
          if ("layout/fragment_conversation_screen_0".equals(tag)) {
            return new FragmentConversationScreenBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_conversation_screen is invalid. Received: " + tag);
        }
        case  LAYOUT_GROUPLISTITEM: {
          if ("layout/group_list_item_0".equals(tag)) {
            return new GroupListItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for group_list_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGEACTIONITEM: {
          if ("layout/message_action_item_0".equals(tag)) {
            return new MessageActionItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_action_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTAUDIOITEM: {
          if ("layout/message_left_audio_item_0".equals(tag)) {
            return new MessageLeftAudioItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_audio_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTCUSTOMITEM: {
          if ("layout/message_left_custom_item_0".equals(tag)) {
            return new MessageLeftCustomItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_custom_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTDELETEITEM: {
          if ("layout/message_left_delete_item_0".equals(tag)) {
            return new MessageLeftDeleteItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_delete_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTFILEITEM: {
          if ("layout/message_left_file_item_0".equals(tag)) {
            return new MessageLeftFileItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_file_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTGROUPCALLITEM: {
          if ("layout/message_left_group_call_item_0".equals(tag)) {
            return new MessageLeftGroupCallItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_group_call_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTLINKITEM: {
          if ("layout/message_left_link_item_0".equals(tag)) {
            return new MessageLeftLinkItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_link_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTLISTIMAGEITEM: {
          if ("layout/message_left_list_image_item_0".equals(tag)) {
            return new MessageLeftListImageItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_list_image_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTLISTVIDEOITEM: {
          if ("layout/message_left_list_video_item_0".equals(tag)) {
            return new MessageLeftListVideoItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_list_video_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTLOCATIONITEM: {
          if ("layout/message_left_location_item_0".equals(tag)) {
            return new MessageLeftLocationItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_location_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTPOLLSITEM: {
          if ("layout/message_left_polls_item_0".equals(tag)) {
            return new MessageLeftPollsItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_polls_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTREPLYITEM: {
          if ("layout/message_left_reply_item_0".equals(tag)) {
            return new MessageLeftReplyItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_reply_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTSTICKERITEM: {
          if ("layout/message_left_sticker_item_0".equals(tag)) {
            return new MessageLeftStickerItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_sticker_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTTEXTITEM: {
          if ("layout/message_left_text_item_0".equals(tag)) {
            return new MessageLeftTextItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_text_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTWHITEBOARDITEM: {
          if ("layout/message_left_whiteboard_item_0".equals(tag)) {
            return new MessageLeftWhiteboardItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_whiteboard_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGELEFTWRITEBOARDITEM: {
          if ("layout/message_left_writeboard_item_0".equals(tag)) {
            return new MessageLeftWriteboardItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_left_writeboard_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTAUDIOITEM: {
          if ("layout/message_right_audio_item_0".equals(tag)) {
            return new MessageRightAudioItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_audio_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTCUSTOMITEM: {
          if ("layout/message_right_custom_item_0".equals(tag)) {
            return new MessageRightCustomItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_custom_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTDELETEITEM: {
          if ("layout/message_right_delete_item_0".equals(tag)) {
            return new MessageRightDeleteItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_delete_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTFILEITEM: {
          if ("layout/message_right_file_item_0".equals(tag)) {
            return new MessageRightFileItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_file_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTGROUPCALLITEM: {
          if ("layout/message_right_group_call_item_0".equals(tag)) {
            return new MessageRightGroupCallItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_group_call_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTLINKITEM: {
          if ("layout/message_right_link_item_0".equals(tag)) {
            return new MessageRightLinkItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_link_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTLISTIMAGEITEM: {
          if ("layout/message_right_list_image_item_0".equals(tag)) {
            return new MessageRightListImageItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_list_image_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTLISTVIDEOITEM: {
          if ("layout/message_right_list_video_item_0".equals(tag)) {
            return new MessageRightListVideoItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_list_video_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTLOCATIONITEM: {
          if ("layout/message_right_location_item_0".equals(tag)) {
            return new MessageRightLocationItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_location_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTPOLLSITEM: {
          if ("layout/message_right_polls_item_0".equals(tag)) {
            return new MessageRightPollsItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_polls_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTREPLYITEM: {
          if ("layout/message_right_reply_item_0".equals(tag)) {
            return new MessageRightReplyItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_reply_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTSTICKERITEM: {
          if ("layout/message_right_sticker_item_0".equals(tag)) {
            return new MessageRightStickerItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_sticker_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTTEXTITEM: {
          if ("layout/message_right_text_item_0".equals(tag)) {
            return new MessageRightTextItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_text_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTWHITEBOARDITEM: {
          if ("layout/message_right_whiteboard_item_0".equals(tag)) {
            return new MessageRightWhiteboardItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_whiteboard_item is invalid. Received: " + tag);
        }
        case  LAYOUT_MESSAGERIGHTWRITEBOARDITEM: {
          if ("layout/message_right_writeboard_item_0".equals(tag)) {
            return new MessageRightWriteboardItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for message_right_writeboard_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADLOCATIONMESSAGEITEM: {
          if ("layout/thread_location_message_item_0".equals(tag)) {
            return new ThreadLocationMessageItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_location_message_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGEAUDIOITEM: {
          if ("layout/thread_message_audio_item_0".equals(tag)) {
            return new ThreadMessageAudioItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_audio_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGEFILEITEM: {
          if ("layout/thread_message_file_item_0".equals(tag)) {
            return new ThreadMessageFileItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_file_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGEIMAGEITEM: {
          if ("layout/thread_message_image_item_0".equals(tag)) {
            return new ThreadMessageImageItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_image_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGEITEM: {
          if ("layout/thread_message_item_0".equals(tag)) {
            return new ThreadMessageItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGELINKITEM: {
          if ("layout/thread_message_link_item_0".equals(tag)) {
            return new ThreadMessageLinkItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_link_item is invalid. Received: " + tag);
        }
        case  LAYOUT_THREADMESSAGEVIDEOITEM: {
          if ("layout/thread_message_video_item_0".equals(tag)) {
            return new ThreadMessageVideoItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for thread_message_video_item is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(20);

    static {
      sKeys.put(1, "ItemClicklistener");
      sKeys.put(0, "_all");
      sKeys.put(2, "actionMessage");
      sKeys.put(3, "call");
      sKeys.put(4, "conversation");
      sKeys.put(5, "conversationList");
      sKeys.put(6, "customMessage");
      sKeys.put(7, "group");
      sKeys.put(8, "linkTextMessage");
      sKeys.put(9, "mediaMessage");
      sKeys.put(10, "pollMessage");
      sKeys.put(11, "pollsMessage");
      sKeys.put(12, "replyTextMessage");
      sKeys.put(13, "stickerMessage");
      sKeys.put(14, "textMessage");
      sKeys.put(15, "user");
      sKeys.put(16, "userList");
      sKeys.put(17, "whiteBoardMessage");
      sKeys.put(18, "whiteBoardMessages");
      sKeys.put(19, "writeBoardMessage");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(48);

    static {
      sKeys.put("layout/activity_cometchat_forward_message_0", com.cometchat.pro.uikit.R.layout.activity_cometchat_forward_message);
      sKeys.put("layout/activity_cometchat_unified_0", com.cometchat.pro.uikit.R.layout.activity_cometchat_unified);
      sKeys.put("layout/cometchat_call_history_item_0", com.cometchat.pro.uikit.R.layout.cometchat_call_history_item);
      sKeys.put("layout/cometchat_call_list_item_0", com.cometchat.pro.uikit.R.layout.cometchat_call_list_item);
      sKeys.put("layout/cometchat_conversation_list_item_0", com.cometchat.pro.uikit.R.layout.cometchat_conversation_list_item);
      sKeys.put("layout/cometchat_user_list_item_0", com.cometchat.pro.uikit.R.layout.cometchat_user_list_item);
      sKeys.put("layout/fragment_cometchat_add_member_0", com.cometchat.pro.uikit.R.layout.fragment_cometchat_add_member);
      sKeys.put("layout/fragment_cometchat_user_profile_0", com.cometchat.pro.uikit.R.layout.fragment_cometchat_user_profile);
      sKeys.put("layout/fragment_conversation_screen_0", com.cometchat.pro.uikit.R.layout.fragment_conversation_screen);
      sKeys.put("layout/group_list_item_0", com.cometchat.pro.uikit.R.layout.group_list_item);
      sKeys.put("layout/message_action_item_0", com.cometchat.pro.uikit.R.layout.message_action_item);
      sKeys.put("layout/message_left_audio_item_0", com.cometchat.pro.uikit.R.layout.message_left_audio_item);
      sKeys.put("layout/message_left_custom_item_0", com.cometchat.pro.uikit.R.layout.message_left_custom_item);
      sKeys.put("layout/message_left_delete_item_0", com.cometchat.pro.uikit.R.layout.message_left_delete_item);
      sKeys.put("layout/message_left_file_item_0", com.cometchat.pro.uikit.R.layout.message_left_file_item);
      sKeys.put("layout/message_left_group_call_item_0", com.cometchat.pro.uikit.R.layout.message_left_group_call_item);
      sKeys.put("layout/message_left_link_item_0", com.cometchat.pro.uikit.R.layout.message_left_link_item);
      sKeys.put("layout/message_left_list_image_item_0", com.cometchat.pro.uikit.R.layout.message_left_list_image_item);
      sKeys.put("layout/message_left_list_video_item_0", com.cometchat.pro.uikit.R.layout.message_left_list_video_item);
      sKeys.put("layout/message_left_location_item_0", com.cometchat.pro.uikit.R.layout.message_left_location_item);
      sKeys.put("layout/message_left_polls_item_0", com.cometchat.pro.uikit.R.layout.message_left_polls_item);
      sKeys.put("layout/message_left_reply_item_0", com.cometchat.pro.uikit.R.layout.message_left_reply_item);
      sKeys.put("layout/message_left_sticker_item_0", com.cometchat.pro.uikit.R.layout.message_left_sticker_item);
      sKeys.put("layout/message_left_text_item_0", com.cometchat.pro.uikit.R.layout.message_left_text_item);
      sKeys.put("layout/message_left_whiteboard_item_0", com.cometchat.pro.uikit.R.layout.message_left_whiteboard_item);
      sKeys.put("layout/message_left_writeboard_item_0", com.cometchat.pro.uikit.R.layout.message_left_writeboard_item);
      sKeys.put("layout/message_right_audio_item_0", com.cometchat.pro.uikit.R.layout.message_right_audio_item);
      sKeys.put("layout/message_right_custom_item_0", com.cometchat.pro.uikit.R.layout.message_right_custom_item);
      sKeys.put("layout/message_right_delete_item_0", com.cometchat.pro.uikit.R.layout.message_right_delete_item);
      sKeys.put("layout/message_right_file_item_0", com.cometchat.pro.uikit.R.layout.message_right_file_item);
      sKeys.put("layout/message_right_group_call_item_0", com.cometchat.pro.uikit.R.layout.message_right_group_call_item);
      sKeys.put("layout/message_right_link_item_0", com.cometchat.pro.uikit.R.layout.message_right_link_item);
      sKeys.put("layout/message_right_list_image_item_0", com.cometchat.pro.uikit.R.layout.message_right_list_image_item);
      sKeys.put("layout/message_right_list_video_item_0", com.cometchat.pro.uikit.R.layout.message_right_list_video_item);
      sKeys.put("layout/message_right_location_item_0", com.cometchat.pro.uikit.R.layout.message_right_location_item);
      sKeys.put("layout/message_right_polls_item_0", com.cometchat.pro.uikit.R.layout.message_right_polls_item);
      sKeys.put("layout/message_right_reply_item_0", com.cometchat.pro.uikit.R.layout.message_right_reply_item);
      sKeys.put("layout/message_right_sticker_item_0", com.cometchat.pro.uikit.R.layout.message_right_sticker_item);
      sKeys.put("layout/message_right_text_item_0", com.cometchat.pro.uikit.R.layout.message_right_text_item);
      sKeys.put("layout/message_right_whiteboard_item_0", com.cometchat.pro.uikit.R.layout.message_right_whiteboard_item);
      sKeys.put("layout/message_right_writeboard_item_0", com.cometchat.pro.uikit.R.layout.message_right_writeboard_item);
      sKeys.put("layout/thread_location_message_item_0", com.cometchat.pro.uikit.R.layout.thread_location_message_item);
      sKeys.put("layout/thread_message_audio_item_0", com.cometchat.pro.uikit.R.layout.thread_message_audio_item);
      sKeys.put("layout/thread_message_file_item_0", com.cometchat.pro.uikit.R.layout.thread_message_file_item);
      sKeys.put("layout/thread_message_image_item_0", com.cometchat.pro.uikit.R.layout.thread_message_image_item);
      sKeys.put("layout/thread_message_item_0", com.cometchat.pro.uikit.R.layout.thread_message_item);
      sKeys.put("layout/thread_message_link_item_0", com.cometchat.pro.uikit.R.layout.thread_message_link_item);
      sKeys.put("layout/thread_message_video_item_0", com.cometchat.pro.uikit.R.layout.thread_message_video_item);
    }
  }
}
