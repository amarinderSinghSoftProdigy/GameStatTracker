package com.softprodigy.ballerapp.common

import android.os.Bundle
import com.facebook.*
import com.softprodigy.ballerapp.data.FacebookUserModel
import timber.log.Timber


interface FacebookUserProfile {
    fun onFacebookUserFetch(fbUser: FacebookUserModel)
}

object CustomFBManager {
    fun getFacebookUserProfile(token: AccessToken?, fbUserProfile: FacebookUserProfile) {
        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, last_name, email"
        )
        var id = ""
        var email = ""
        var fName = ""
        var lName = ""

        val request = GraphRequest.newMeRequest(accessToken = token) { jsonObject, response ->
            try {
                //here is the data that you want
                Timber.d("FBLOGIN_JSON_RES", jsonObject.toString())

                val jsonObject = response?.jsonObject

                if (jsonObject != null) {
                    // Facebook Access Token
                    // You can see Access Token only in Debug mode.
                    // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                    if (BuildConfig.DEBUG) {
                        FacebookSdk.setIsDebugEnabled(true)
                        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                    }

                    // Facebook Id
                    if (jsonObject.has("id")) {
                        val facebookId = jsonObject.getString("id")
                        id = facebookId
                        Timber.tag("Facebook Id: ").i(facebookId.toString())
                    } else {
                        Timber.tag("Facebook Id: ").i("Not exists")
                    }


                    // Facebook First Name
                    if (jsonObject.has("first_name")) {
                        val facebookFirstName = jsonObject.getString("first_name")
                        fName = facebookFirstName
                        Timber.tag("Facebook First Name: ").i(facebookFirstName)
                    } else {
                        Timber.tag("Facebook First Name: ").i("Not exists")
                    }
                    // Facebook Last Name
                    if (jsonObject.has("last_name")) {
                        val facebookLastName = jsonObject.getString("last_name")
                        lName = facebookLastName
                        Timber.tag("Facebook Last Name: ").i(facebookLastName)
                    } else {
                        Timber.tag("Facebook Last Name: ").i("Not exists")
                    }
                    // Facebook Email
                    if (jsonObject.has("email")) {
                        val facebookEmail = jsonObject.getString("email")
                        email = facebookEmail
                        Timber.tag("Facebook Email: ").i(facebookEmail)
                    } else {
                        Timber.tag("Facebook Email: ").i("Not exists")
                    }
                }
                fbUserProfile.onFacebookUserFetch(
                    FacebookUserModel(
                        name = "$fName $lName",
                        email = email,
                        id = id,
                        token = token?.token
                    )
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        request.parameters = parameters
        request.executeAsync()
    }
}
