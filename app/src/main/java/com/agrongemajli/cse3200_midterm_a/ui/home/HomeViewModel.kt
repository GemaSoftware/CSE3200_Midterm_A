package com.agrongemajli.cse3200_midterm_a.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "00:00:00"
    }

    private val _starttime = MutableLiveData<Long>().apply {
        value = 0
    }

    val startTime: LiveData<Long> = _starttime
    val text: LiveData<String> = _text

    var timeSinceStart: Long = 0;
}