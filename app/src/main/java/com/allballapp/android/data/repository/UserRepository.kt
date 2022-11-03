package com.allballapp.android.data.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.common.safeApiCall
import com.allballapp.android.data.datastore.DataStoreManager
import com.allballapp.android.data.request.*
import com.allballapp.android.data.response.*
import com.allballapp.android.domain.BaseResponse
import com.allballapp.android.domain.repository.IUserRepository
import com.allballapp.android.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.FormBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IUserRepository {

    override suspend fun userLogin(
        loginRequest: LoginRequest
    ): ResultWrapper<BaseResponse<UserInfo>> {
        return safeApiCall(dispatcher) { service.userLogin(loginRequest) }
    }

    override suspend fun verifyPhone(phone: String): ResultWrapper<BaseResponse<Any>> {
        val requestBody = VerifyPhoneRequest(phone = phone)
        return safeApiCall(dispatcher = dispatcher) { service.verifyPhone(requestBody) }
    }

    override suspend fun confirmPhone(
        phone: String,
        otp: String
    ): ResultWrapper<BaseResponse<ProfileList>> {
        val requestBody = ConfirmPhoneRequest(phone = phone, otp = otp)
        return safeApiCall(dispatcher = dispatcher) { service.confirmPhone(requestBody) }
    }

    override suspend fun signUp(signUpData: SignUpData): ResultWrapper<BaseResponse<UserInfo>> =
        safeApiCall(dispatcher = dispatcher) { service.signUp(signUpData) }

    override suspend fun signUpPhone(signUpData: SignUpPhoneData): ResultWrapper<BaseResponse<UserPhoneInfo>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.registerMobile(signUpData)
        }
    }

    override suspend fun forgotPassword(email: String): ResultWrapper<BaseResponse<Any>> {
        val requestBody = ForgotPasswordRequest(email)
        return safeApiCall(dispatcher = dispatcher) { service.forgotPassword(requestBody) }
    }

    override suspend fun updateUserProfile(userProfile: SignUpPhoneData): ResultWrapper<BaseResponse<UserInfo>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.updateUserProfile(userProfile = userProfile)
        }
    }

    override suspend fun getUserProfile(): ResultWrapper<BaseResponse<User>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getUserDetails()
        }
    }

    override suspend fun getFullUserFullDetails(): ResultWrapper<BaseResponse<User>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getUserFullDetails()
        }
    }

    override suspend fun updateUserFullDetails(userDetailsReq: UpdateUserDetailsReq): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.updateUserFullDetails(userDetailsReq = userDetailsReq)
        }
    }

    override suspend fun leaveTeam(teamId: String): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher = dispatcher) {
            val request: RequestBody = FormBody.Builder()
                .add("teamId", teamId)
                .build()
            service.leaveTeam(request = request)
        }
    }

    override suspend fun getDocTypes(teamId: String): ResultWrapper<BaseResponse<List<UserDocType>>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getDocTypes()
        }
    }

    override suspend fun deleteUserDoc(key: String): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher = dispatcher) {
            val request: RequestBody = FormBody.Builder()
                .add("documentType", key)
                .build()
            service.deleteDoc(request = request)
        }
    }

    override suspend fun updateUserDoc(key: String, url: String): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher = dispatcher) {
            val request: RequestBody = FormBody.Builder()
                .add("documentType", key)
                .add("documentValue", url)
                .build()
            service.updateUserDoc(request = request)
        }
    }

    override suspend fun getSwapProfiles(): ResultWrapper<BaseResponse<List<SwapUser>>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getSwapProfiles()
        }
    }

    override suspend fun updateProfileToken(userId: String): ResultWrapper<BaseResponse<String>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.updateProfileToken(userId)
        }
    }

    override suspend fun updateInitialProfileToken(userId: String): ResultWrapper<BaseResponse<String>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.updateInitialProfileToken(userId)
        }
    }

    override suspend fun addProfile(request: AddProfileRequest): ResultWrapper<BaseResponse<UserInfo>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.addProfile(request)
        }
    }

    override suspend fun getSearchGameStaff(search: String): ResultWrapper<BaseResponse<List<GetSearchStaff>>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.getSearchGameStaff(search)
        }
    }

    override suspend fun authorizeGuardian(request: AuthorizeRequest): ResultWrapper<BaseResponse<Any>> {
        return safeApiCall(dispatcher = dispatcher) {
            service.authorizeGuardian(request)
        }
    }
}