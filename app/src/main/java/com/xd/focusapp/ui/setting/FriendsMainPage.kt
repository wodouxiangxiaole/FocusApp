package com.xd.focusapp.ui.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import com.xd.focusapp.R

class FriendsMainPage : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var addFriendBtn:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_main_page)

        toolbar = findViewById(R.id.toolbar)
        addFriendBtn= findViewById(R.id.friend_search_btn)

        addFriendBtn.setOnClickListener{
            val intent = Intent(this, FriendSearchActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }




    }
}