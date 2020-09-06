package com.example.sample.data

import com.example.sample.util.RequestResult

interface SampleDataSource {
    suspend fun post(url: String): RequestResult
}
