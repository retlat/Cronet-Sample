package com.example.sample.data

import android.util.Log
import com.example.sample.util.RequestResult
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class DefaultJSONCallback : JSONCallback {

    override fun make(continuation: Continuation<RequestResult>): UrlRequest.Callback {
        return object : UrlRequest.Callback() {

            private val redirectLimit = 10
            private val readBufferSize = 102400
            private val received = ByteArrayOutputStream(0)

            override fun onRedirectReceived(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                newLocationUrl: String?
            ) {
                val chain = info?.urlChain
                if (chain == null) {
                    request?.cancel()
                    continuation.resume(RequestResult.UnknownError)
                    return
                }

                // We have to stop following redirect loop manually
                if (chain.size < redirectLimit) {
                    request?.followRedirect()
                } else {
                    request?.cancel()
                    continuation.resume(RequestResult.UnknownError)
                }
            }

            override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
                request?.read(ByteBuffer.allocateDirect(readBufferSize))
            }

            override fun onReadCompleted(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                byteBuffer: ByteBuffer?
            ) {
                if (byteBuffer != null) {
                    val offset = when {
                        byteBuffer.hasArray() -> byteBuffer.arrayOffset()
                        else -> 0
                    }
                    received.write(byteBuffer.array(), offset, byteBuffer.position())
                }

                byteBuffer?.clear()
                request?.read(byteBuffer)
            }

            override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
                val contentType = info?.allHeaders?.get("Content-Type")?.last() ?: ""

                when (info?.httpStatusCode ?: 0) {
                    in 200..299 -> {
                        if (contentType == "application/json") {
                            val jsonString = String(received.toByteArray())
                            continuation.resume(RequestResult.Success(jsonString))
                        } else {
                            continuation.resume(RequestResult.UnknownError)
                        }
                    }
                    404 -> continuation.resume(RequestResult.NotFound)
                    500 -> continuation.resume(RequestResult.InternalServerError)
                    503 -> continuation.resume(RequestResult.ServiceUnavailable)
                    else -> continuation.resume(RequestResult.UnknownError)
                }
            }

            override fun onFailed(
                request: UrlRequest?,
                info: UrlResponseInfo?,
                error: CronetException?
            ) {
                if (error != null) {
                    Log.d("DefaultJSONCallback", "JSONCallback onFailed: $error")
                }

                continuation.resume(RequestResult.UnknownError)
            }
        }
    }
}
