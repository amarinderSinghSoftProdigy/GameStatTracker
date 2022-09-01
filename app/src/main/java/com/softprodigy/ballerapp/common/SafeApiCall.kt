package com.softprodigy.ballerapp.common

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.softprodigy.ballerapp.domain.BaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
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
                    var gsonErrorBody: BaseResponse<*>? = null
                    try {
                        gsonErrorBody = Gson().fromJson(
                            errorBody,
                            BaseResponse::class.java
                        )
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                    }

                    val message = gsonErrorBody?.statusMessage ?: AppConstants.DEFAULT_ERROR_MESSAGE
                    ResultWrapper.GenericError(code, message)
                }
                else -> {
                    Timber.e("GenericError", throwable.message)
                    throwable.printStackTrace()
                    ResultWrapper.GenericError(null, throwable.message)
                }
            }

        }
    }
}