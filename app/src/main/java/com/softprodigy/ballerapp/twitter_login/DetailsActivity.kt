package com.softprodigy.ballerapp.twitter_login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.softprodigy.ballerapp.R

class DetailsActivity : AppCompatActivity() {
  lateinit var twitter_id_textview:TextView
  lateinit var twitter_handle_textview:TextView
  lateinit var twitter_name_textview:TextView
  lateinit var twitter_email_textview:TextView
  lateinit var twitter_profile_pic_url_textview:TextView
  lateinit var twitter_access_token_textview:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)


        twitter_id_textview.text = findViewById(R.id.twitter_id_textview)
        twitter_handle_textview.text = findViewById(R.id.twitter_handle_textview)
        twitter_name_textview.text = findViewById(R.id.twitter_name_textview)
        twitter_email_textview.text = findViewById(R.id.twitter_email_textview)
        twitter_profile_pic_url_textview.text = findViewById(R.id.twitter_profile_pic_url_textview)
        twitter_access_token_textview.text = findViewById(R.id.twitter_access_token_textview)



        val twitterId = intent.getStringExtra("twitter_id")
        val twitterHandle = intent.getStringExtra("twitter_handle")
        val twitterName = intent.getStringExtra("twitter_name")
        val twitterEmail = intent.getStringExtra("twitter_email")
        val twitterProfilePicURL = intent.getStringExtra("twitter_profile_pic_url")
        val twitterAccessToken = intent.getStringExtra("twitter_access_token")

        twitter_id_textview.text = twitterId
        twitter_handle_textview.text = twitterHandle
        twitter_name_textview.text = twitterName
        twitter_email_textview.text = twitterEmail
        twitter_profile_pic_url_textview.text = twitterProfilePicURL
        twitter_access_token_textview.text = twitterAccessToken
    }

}