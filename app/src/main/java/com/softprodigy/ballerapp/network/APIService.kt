package com.softprodigy.ballerapp.network

import com.softprodigy.ballerapp.data.response.ForgotPasswordResponse
import com.softprodigy.ballerapp.data.response.ResetPasswordResponse
import com.softprodigy.ballerapp.data.response.VerifyOtpResponse
import com.softprodigy.ballerapp.common.ApiConstants
import com.softprodigy.ballerapp.data.UserInfo
import com.softprodigy.ballerapp.data.request.*
import com.softprodigy.ballerapp.data.request.LoginRequest
import com.softprodigy.ballerapp.data.request.ResendOtpRequest
import com.softprodigy.ballerapp.data.request.SignUpRequest
import com.softprodigy.ballerapp.data.response.SignUpResponse
import com.softprodigy.ballerapp.domain.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @POST(ApiConstants.LOGIN)
    suspend fun userLogin(@Body loginRequest: LoginRequest): BaseResponse<UserInfo>

    @POST(ApiConstants.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body loginRequest: SocialLoginRequest):UserInfo

    @POST(ApiConstants.SIGNUP)
    suspend fun userSignUp(@Body signUpRequest: SignUpRequest):SignUpResponse

    @POST(ApiConstants.FORGOT_PASSWORD)
    suspend fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("${ApiConstants.VERIFY_OTP}{token}")
    suspend fun verifyOtp(
        @Path("token") token: String,
        @Body verifyOtpRequest: VerifyOtpRequest
    ): VerifyOtpResponse

    @POST("${ApiConstants.RESET_PASS}{token}")
    suspend fun resetPassword(
        @Path("token") token: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): ResetPasswordResponse

    @POST(ApiConstants.RESEND_OTP)
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): ForgotPasswordResponse
}