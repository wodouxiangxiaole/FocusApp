package com.xd.focusapp.ui.focus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FocusViewModel : ViewModel() {
    val timer_set = MutableLiveData<Int>()
    val timer_countDown = MutableLiveData<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is Focus Fragment"
    }
    val text: LiveData<String> = _text
}

class FocusViewModelFactory()
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FocusViewModel::class.java))
            return FocusViewModel() as T
        throw java.lang.IllegalArgumentException("Error")
    }
}