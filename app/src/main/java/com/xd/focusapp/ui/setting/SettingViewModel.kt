package com.xd.focusapp.ui.setting

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

    val userImg = MutableLiveData<Bitmap>()
    val settingOptions = arrayOf(
        "Friends",
        "Account Security",
        "Message Notifications", "General", "Friends' Permissions",
        "About", "Help & Feedback",
        "Log out")

    private val _text = MutableLiveData<String>().apply {
        value = "This is Setting Fragment"
    }
    val text: LiveData<String> = _text
}