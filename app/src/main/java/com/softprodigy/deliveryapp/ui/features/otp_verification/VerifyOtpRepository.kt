package com.softprodigy.deliveryapp.ui.features.otp_verification

import com.softprodigy.deliveryapp.data.request.VerifyOtpRequest
import com.softprodigy.deliveryapp.common.ResultWrapper
import com.softprodigy.deliveryapp.common.safeApiCall
import com.softprodigy.deliveryapp.data.response.VerifyOtpResponse
import com.softprodigy.deliveryapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface VerifyOtpRepository {

    suspend fun verifyOtp(
        token: String,
        otpType: String,
        otp: String
    ): ResultWrapper<VerifyOtpResponse>

}

class VerifyOtpRepoImpl @Inject constructor(
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : VerifyOtpRepository {

    override suspend fun verifyOtp(
        token: String,
        otpType: String,
        otp: String
    ): ResultWrapper<VerifyOtpResponse> {
        val requestBody = VerifyOtpRequest(otp, otpType)
        return safeApiCall(dispatcher) { service.verifyOtp(token, requestBody) }
    }
}