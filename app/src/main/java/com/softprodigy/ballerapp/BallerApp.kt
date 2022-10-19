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
        val appID = "22157742ffa95f12"
        val region = "us"
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


        val UID = "SUPERHERO1" // Replace with the UID of the user to login
        val AUTH_KEY = "3aee9c1b55e111786020c00c72669e83bfd0486e" // Replace with your App Auth Key

        CometChat.login(UID, AUTH_KEY, object : CometChat.CallbackListener<User?>() {
            override fun onSuccess(user: User?) {
                Timber.i("Login Successful : " + user.toString())
            }

            override fun onError(e: CometChatException) {
                Timber.i("Login failed with exception: " + e.message);
            }
        })

    }
}