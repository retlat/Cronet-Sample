package com.example.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _url = MutableLiveData("https://your.endpoint.example.com/")
    val url: LiveData<String> get() = _url

    private val _response = MutableLiveData("")
    val response: LiveData<String> get() = _response

    fun onClickPostButton() {
        TODO()
    }
}
