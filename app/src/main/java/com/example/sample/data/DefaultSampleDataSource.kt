package com.example.sample.data

import com.example.sample.di.IODispatcher
import com.example.sample.util.RequestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.chromium.net.CronetEngine
import org.json.JSONObject
import java.util.concurrent.Executors
import javax.inject.Inject

class DefaultSampleDataSource @Inject constructor(
    private val engine: CronetEngine,
    private val callback: JSONCallback,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : SampleDataSource {

    override suspend fun post(url: String): RequestResult {
        val data = JSONObject()
        data.put("dummy_text", "Hello, World!")

        return withContext(dispatcher) {
            doPost(url, data)
        }
    }

    private suspend fun doPost(url: String, json: JSONObject): RequestResult {
        return suspendCancellableCoroutine {
            val executor = Executors.newSingleThreadExecutor()
            val request = engine
                .newUrlRequestBuilder(url, callback.make(it), executor)
                .setHttpMethod("POST")
                .addHeader("Content-Type", "application/json")
                .setUploadDataProvider(JSONProvider(json), executor)
                .build()

            request.start()

            it.invokeOnCancellation {
                if (!request.isDone) {
                    request.cancel()
                }
            }
        }
    }
}
