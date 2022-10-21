package com.softprodigy.ballerapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.softprodigy.ballerapp.data.UserStorage
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BallerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        getPhoneInfo()
        initChat()
    }
    @SuppressLint("HardwareIds")
    private fun getPhoneInfo() {
        //HASH
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        UserStorage.deviceHesh = androidId

        //Phone info
        val deviceDescription = "Model: ${Build.MODEL} (${Build.PRODUCT}), OS API Level: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})"
        UserStorage.deviceDescription = deviceDescription
    }

    private fun initChat() {
        val appID = BuildConfig.COMET_CHAT_APP_KEY
        val region = BuildConfig.COMET_CHAT_REGION
        val appSettings =
            AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region)
                .build()

        CometChat.init(this, appID, appSettings, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(successMessage: String) {
                Timber.i("Initialization completed successfully")
            }

            override fun onError(e: CometChatException) {
                Timber.i("Initialization failed with exception: ${e.message}")
            }
        })


    }
}