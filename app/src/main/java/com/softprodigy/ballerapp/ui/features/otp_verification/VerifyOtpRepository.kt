package com.softprodigy.ballerapp.ui.features.otp_verification

import com.softprodigy.ballerapp.data.request.VerifyOtpRequest
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.request.ResendOtpRequest
import com.softprodigy.ballerapp.data.response.ForgotPasswordResponse
import com.softprodigy.ballerapp.data.response.VerifyOtpResponse
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface VerifyOtpRepository {

    suspend fun verifyOtp(
        token: String,
        otpType: String,
        otp: String
    ): ResultWrapper<VerifyOtpResponse>

    suspend fun resendOtp(
        email: String,
        mobile: String,
        otpType: String
    ): ResultWrapper<ForgotPasswordResponse>

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

    override suspend fun resendOtp(
        email: String,
        mobile: String,
        otpType: String
    ): ResultWrapper<ForgotPasswordResponse> {
        val requestBody = ResendOtpRequest(email, mobile, otpType)
        return safeApiCall(dispatcher) { service.resendOtp(requestBody) }
    }
}