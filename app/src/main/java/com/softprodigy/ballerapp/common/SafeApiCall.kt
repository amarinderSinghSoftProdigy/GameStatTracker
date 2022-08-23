package com.softprodigy.ballerapp.common

import com.google.gson.Gson
import com.softprodigy.ballerapp.domain.BaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException ->{
                    val message = throwable.message
                    ResultWrapper.NetworkError(message = message?:AppConstants.DEFAULT_ERROR_MESSAGE)
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorBody = throwable.response()?.errorBody()?.string()
                    val gsonErrorBody = Gson().fromJson(
                        errorBody,
                        BaseResponse::class.java
                    )
                    val message = gsonErrorBody.statusMessage
                    ResultWrapper.GenericError(code, message)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }

        }
    }
}