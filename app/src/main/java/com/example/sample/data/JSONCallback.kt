package com.example.sample.data

import com.example.sample.util.RequestResult
import org.chromium.net.UrlRequest
import kotlin.coroutines.Continuation

interface JSONCallback {
    fun make(continuation: Continuation<RequestResult>): UrlRequest.Callback
}
