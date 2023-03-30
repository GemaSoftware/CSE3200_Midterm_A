package com.agrongemajli.cse3200_midterm_a.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "00:00:00"
    }

    private val _timetext = MutableLiveData<Long>().apply {

        value = System.currentTimeMillis()
    }

    val currentTimer: LiveData<Long> = _timetext
    val text: LiveData<String> = _text
    var timeSinceStart: Long = 0;
}