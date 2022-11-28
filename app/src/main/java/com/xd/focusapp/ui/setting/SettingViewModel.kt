package com.xd.focusapp.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

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