package com.softprodigy.ballerapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
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
}