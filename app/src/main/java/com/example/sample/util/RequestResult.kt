package com.example.sample.util

sealed class RequestResult {
    data class Success(val data: String) : RequestResult()
    object NotFound : RequestResult()
    object InternalServerError : RequestResult()
    object ServiceUnavailable : RequestResult()
    object UnknownError : RequestResult()
}
