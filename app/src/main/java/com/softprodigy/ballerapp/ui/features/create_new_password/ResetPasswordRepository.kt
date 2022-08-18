package com.softprodigy.ballerapp.ui.features.create_new_password


import com.softprodigy.ballerapp.data.request.ResetPasswordRequest
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.response.ResetPasswordResponse
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface ResetPasswordRepository {
    suspend fun resetPassword(token: String, password: String): ResultWrapper<ResetPasswordResponse>
}

class ResetPasswordRepoImpl @Inject constructor(
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ResetPasswordRepository {
    override suspend fun resetPassword(
        token: String,
        password: String
    ): ResultWrapper<ResetPasswordResponse> {
        val requestBody = ResetPasswordRequest(password = password)
        return safeApiCall(dispatcher) { service.resetPassword(token, requestBody) }
    }

}