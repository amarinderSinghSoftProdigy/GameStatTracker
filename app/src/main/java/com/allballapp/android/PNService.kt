package com.allballapp.android


import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.allballapp.android.common.AppConstants
import com.allballapp.android.common.IntentData
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.helpers.CometChatHelper
import com.cometchat.pro.models.BaseMessage
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.MediaMessage
//import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
//import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class PNService :
    FirebaseMessagingService() {
    /*private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val TAG = "PNService"
    private var json: JSONObject? = null
    private val intent: Intent? = null
    private var count = 0
    var token: String? = null
    private val REQUEST_CODE = 12

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if(AppConstants.ENABLE_CHAT){
        *//*Crete channel for notification*//*
        createNotificationChannel()

        *//*Sending app level broadcast on notification *//*
        sendBroadcastToUpdateUnreadChatCount()


        Timber.i("PNService onMessageReceived: ${message.data} ${message.notification?.title}")



        try {
            count++
            json = (message.data as Map<*, *>?)?.let { JSONObject(it) }
            Log.d(TAG, "JSONObject: " + json.toString().replace("\\",  ""))
//            val messageData = json?.getString("message")?.let { JSONObject(it) }
            val baseMessage: BaseMessage =
                CometChatHelper.processMessage(
                    message.data["message"]
                        ?.let { JSONObject(it) })

            showNotification(baseMessage)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }
    }

    private fun sendBroadcastToUpdateUnreadChatCount() {

        val intent = Intent(IntentData.COMET_CHAT_READ_COUNT)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Log.i("PNService", "onSendError: $msgId")

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("PNService", "onNewToken: $token")

    }

    fun getBitmapFromURL(strURL: String?): Bitmap? {
        return if (strURL != null) {
            try {
                val url = URL(strURL)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    private fun showNotification(baseMessage: BaseMessage) {
        try {
            val m = Date().time.toInt()
            val GROUP_ID = "group_id"
            val messageIntent = Intent(
                applicationContext,
                CometChatMessageListActivity::class.java
            )
            messageIntent.putExtra(UIKitConstants.IntentStrings.TYPE, baseMessage.receiverType)
            if (baseMessage.receiverType == CometChatConstants.RECEIVER_TYPE_USER) {
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.NAME,
                    baseMessage.sender.name
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.UID,
                    baseMessage.sender.uid
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.AVATAR,
                    baseMessage.sender.avatar
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.STATUS,
                    baseMessage.sender.status
                )
            } else if (baseMessage.receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.GUID,
                    (baseMessage.receiver as Group).guid
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.NAME,
                    (baseMessage.receiver as Group).name
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.GROUP_DESC,
                    (baseMessage.receiver as Group).description
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.GROUP_TYPE,
                    (baseMessage.receiver as Group).groupType
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.GROUP_OWNER,
                    (baseMessage.receiver as Group).owner
                )
                messageIntent.putExtra(
                    UIKitConstants.IntentStrings.MEMBER_COUNT,
                    (baseMessage.receiver as Group).membersCount
                )
            }
             val messagePendingIntent = PendingIntent.getActivity(
                 applicationContext,
                 83, messageIntent, PendingIntent.FLAG_UPDATE_CURRENT
             )
           *//* val messagePendingIntent = PendingIntent.getActivity(
                applicationContext,
                83, messageIntent, PendingIntent.FLAG_IMMUTABLE
            )*//*
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "2")
                .setSmallIcon(R.drawable.ic_all_ball_logo)
                .setContentTitle(json?.getString("title"))
                .setContentText(json?.getString("alert"))
                .setColor(resources.getColor(R.color.colorPrimary))
                .setLargeIcon(getBitmapFromURL(baseMessage.sender.avatar))
                .setGroup(GROUP_ID)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            //Show image incase of media message
            if (baseMessage.type == CometChatConstants.MESSAGE_TYPE_IMAGE) {
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(
                            getBitmapFromURL(
                                (baseMessage as MediaMessage).attachment
                                    .fileUrl
                            )
                        )
                )
            }
            val summaryBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "2")
                .setContentTitle(json?.getString("title"))
                .setContentText("$count messages")
                .setSmallIcon(R.drawable.ic_all_ball_logo)
                .setGroup(GROUP_ID)
                .setGroupSummary(true)
                .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(this)
            builder.priority = NotificationCompat.PRIORITY_HIGH
            builder.setAutoCancel(true)
            builder.setContentIntent(messagePendingIntent)
            builder.setCategory(NotificationCompat.CATEGORY_MESSAGE)
            builder.setDefaults(Notification.DEFAULT_VIBRATE)
            notificationManager.notify(baseMessage.id, builder.build())
            notificationManager.notify(0, summaryBuilder.build())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val description = "channel_description"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("2", name, importance)
            channel.description = description
            channel.enableVibration(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }*/
}