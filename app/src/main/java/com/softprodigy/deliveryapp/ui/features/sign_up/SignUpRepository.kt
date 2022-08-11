package com.softprodigy.deliveryapp.ui.features.sign_up

import com.softprodigy.deliveryapp.common.AppConstants
import com.softprodigy.deliveryapp.common.ResultWrapper
import com.softprodigy.deliveryapp.common.safeApiCall
import com.softprodigy.deliveryapp.data.UserStorage
import com.softprodigy.deliveryapp.data.datastore.DataStoreManager
import com.softprodigy.deliveryapp.data.request.SignUpRequest
import com.softprodigy.deliveryapp.data.response.SignUpResponse
import com.softprodigy.deliveryapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface SignUpRepository {
    suspend fun signUpWithDetails(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultWrapper<SignUpResponse>

    suspend fun SignUpWithGoogle(googleUser: String): ResultWrapper<SignUpResponse>
}

@Singleton
class SignUpRepoImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SignUpRepository {
    override suspend fun signUpWithDetails(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): ResultWrapper<SignUpResponse> {
        val requestBody =
            SignUpRequest(
                firstName = firstName,
                lastName = lastName,
                mobile = "1111111111",
                email = email,
                password = password,
                deviceType = AppConstants.ANDROID,
                deviceToken = UserStorage.deviceHesh
            )
        return safeApiCall(dispatcher) {service.userSignUp(requestBody) }
    }

    override suspend fun SignUpWithGoogle(googleUser: String): ResultWrapper<SignUpResponse> {
        TODO("Not yet implemented")
    }
}