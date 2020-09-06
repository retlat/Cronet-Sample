package com.example.sample.ui

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sample.R
import com.example.sample.data.SampleDataSource
import com.example.sample.util.RequestResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val source: SampleDataSource,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _url = MutableLiveData("https://your.endpoint.example.com/")
    val url: LiveData<String> get() = _url

    private val _response = MutableLiveData("")
    val response: LiveData<String> get() = _response

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    fun onClickPostButton() {
        viewModelScope.launch {
            _loading.value = true
            _response.value = ""

            when (val result = source.post(url.value!!)) {
                is RequestResult.Success -> _response.value = result.data
                else -> {
                    val id = when (result) {
                        is RequestResult.NotFound -> R.string.home_result_404
                        is RequestResult.InternalServerError -> R.string.home_result_500
                        is RequestResult.ServiceUnavailable -> R.string.home_result_503
                        else -> R.string.home_result_else
                    }

                    _response.value = context.getString(id)
                }
            }

            _loading.value = false
        }
    }
}
