package com.baller_app.core.util
import com.baller_app.core.BuildConfig
import com.baller_app.core.model.NetworkError
import com.baller_app.core.model.RepositoryResult
import com.slack.eithernet.ApiResult
import timber.log.Timber

fun <T : Any> getAnswerSuccess(response: ApiResult<T, NetworkError>): RepositoryResult<T> {
    return when (response) {
        is ApiResult.Success -> {
            if (BuildConfig.DEBUG) {
                Timber.wtf("RepositoryResult  Success = ${response.value}")
            }
            RepositoryResult.Success(response.value)
        }
        is ApiResult.Failure.NetworkFailure -> {
            if (BuildConfig.DEBUG) {
                Timber.wtf("RepositoryResult NetworkFailure = ${response.error}")
            }
            RepositoryResult.Error(response.error.toString())
        }
        is ApiResult.Failure.UnknownFailure -> {
            if (BuildConfig.DEBUG) {
                Timber.wtf("RepositoryResult UnknownFailure = ${response.error}")
            }
            RepositoryResult.Error(response.error.toString())
        }
        is ApiResult.Failure.HttpFailure -> {
            if (BuildConfig.DEBUG) {
                Timber.wtf("RepositoryResult HttpFailure= ${response.error?.code} : ${response.error?.errorMessage}")
            }
            RepositoryResult.Error(
                response.error?.errorMessage ?: ApiProvider().errorDefault,
                response.error?.code
            )
        }
        is ApiResult.Failure.ApiFailure -> {
            if (BuildConfig.DEBUG) {
                Timber.wtf("RepositoryResult ApiFailure = ${response.error}")
            }
            RepositoryResult.Error(
                response.error?.errorMessage ?: ApiProvider().errorDefault,
                response.error?.code
            )
        }
        else -> {
            RepositoryResult.Error("ApiFailure ELSE = Unexpected ERROR")
        }
    }
}