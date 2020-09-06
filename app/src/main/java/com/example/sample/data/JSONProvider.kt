package com.example.sample.data

import org.chromium.net.UploadDataProvider
import org.chromium.net.UploadDataSink
import org.json.JSONObject
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.math.min

class JSONProvider(json: JSONObject) : UploadDataProvider() {

    private val data: ByteArray = json.toString().toByteArray()
    private var position = 0

    override fun getLength(): Long {
        return data.size.toLong()
    }

    override fun read(uploadDataSink: UploadDataSink?, byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            throw IOException("Null buffer given")
        }

        val end = min(position + byteBuffer.limit(), data.size)

        byteBuffer.put(data.copyOfRange(position, end))
        uploadDataSink?.onReadSucceeded(false)
    }

    override fun rewind(uploadDataSink: UploadDataSink?) {
        position = 0

        uploadDataSink?.onRewindSucceeded()
    }
}
