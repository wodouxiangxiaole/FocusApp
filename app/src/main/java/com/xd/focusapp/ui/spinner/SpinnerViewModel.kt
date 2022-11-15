package com.xd.focusapp.ui.spinner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpinnerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Spinner Fragment"
    }
    val text: LiveData<String> = _text
}