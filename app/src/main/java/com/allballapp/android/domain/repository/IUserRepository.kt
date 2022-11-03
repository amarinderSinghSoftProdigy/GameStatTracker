package com.allballapp.android.domain.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.request.*
import com.allballapp.android.data.response.*
import com.allballapp.android.domain.BaseResponse
import javax.inject.Singleton


@Singleton
interface IUserRepository {
    suspend fun userLogin(
        loginRequest: LoginRequest
    ): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun verifyPhone(phone: String): ResultWrapper<BaseResponse<Any>>

    suspend fun confirmPhone(phone: String, otp: String): ResultWrapper<BaseResponse<ProfileList>>

    suspend fun signUp(signUpData: SignUpData): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun signUpPhone(signUpData: SignUpPhoneData): ResultWrapper<BaseResponse<UserPhoneInfo>>

    suspend fun forgotPassword(email: String): ResultWrapper<BaseResponse<Any>>

    suspend fun updateUserProfile(userProfile: SignUpPhoneData): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun getUserProfile(): ResultWrapper<BaseResponse<User>>

    suspend fun getFullUserFullDetails(): ResultWrapper<BaseResponse<User>>

    suspend fun updateUserFullDetails(userDetailsReq: UpdateUserDetailsReq): ResultWrapper<BaseResponse<Any>>

    suspend fun leaveTeam(teamId: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getDocTypes(teamId: String): ResultWrapper<BaseResponse<List<UserDocType>>>

    suspend fun deleteUserDoc(key: String): ResultWrapper<BaseResponse<Any>>

    suspend fun updateUserDoc(key: String, url: String): ResultWrapper<BaseResponse<Any>>

    suspend fun getSwapProfiles(): ResultWrapper<BaseResponse<List<SwapUser>>>

    suspend fun updateProfileToken(userId: String): ResultWrapper<BaseResponse<String>>

    suspend fun addProfile(request: AddProfileRequest): ResultWrapper<BaseResponse<UserInfo>>

    suspend fun updateInitialProfileToken(userId: String): ResultWrapper<BaseResponse<String>>

    suspend fun getSearchGameStaff(search: String): ResultWrapper<BaseResponse<List<GetSearchStaff>>>

    suspend fun authorizeGuardian(request: AuthorizeRequest): ResultWrapper<BaseResponse<Any>>

}