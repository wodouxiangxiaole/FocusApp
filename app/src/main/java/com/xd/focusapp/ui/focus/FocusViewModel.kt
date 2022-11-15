package com.xd.focusapp.ui.focus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FocusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Focus Fragment"
    }
    val text: LiveData<String> = _text
}