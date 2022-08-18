package com.softprodigy.ballerapp.ui.features.welcome

import com.softprodigy.ballerapp.common.AppConstants
import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.GoogleUserModel
import com.softprodigy.ballerapp.data.UserStorage
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.request.SocialLoginRequest
import com.softprodigy.ballerapp.data.response.LoginResponse
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface SocialLoginRepo {
    suspend fun loginWithGoogle(googleUser: GoogleUserModel): ResultWrapper<LoginResponse>
}

@Singleton
class SocialLoginRepoImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
):SocialLoginRepo {
    override suspend fun loginWithGoogle(googleUser: GoogleUserModel): ResultWrapper<LoginResponse> {
        val requestBody =
            SocialLoginRequest(
                firstName = googleUser.name,
                lastName = googleUser.name,
                providerId = googleUser.id,
                providerToken = googleUser.token,
                provider_type = AppConstants.GOOGLE,
                email = googleUser.email,
                deviceType = AppConstants.ANDROID,
                deviceToken = UserStorage.deviceHesh,
            )
        return safeApiCall(dispatcher) { service.socialLogin(requestBody) }
    }

}