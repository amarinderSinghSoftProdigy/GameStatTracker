package com.softprodigy.ballerapp.di.modules


import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.network.APIService
import com.softprodigy.ballerapp.ui.features.create_new_password.ResetPasswordRepoImpl
import com.softprodigy.ballerapp.ui.features.create_new_password.ResetPasswordRepository
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordRepoImpl
import com.softprodigy.ballerapp.ui.features.forgot_password.ForgotPasswordRepository
import com.softprodigy.ballerapp.data.repository.UserRepository
import com.softprodigy.ballerapp.domain.repository.IUserRepository
import com.softprodigy.ballerapp.ui.features.otp_verification.VerifyOtpRepoImpl
import com.softprodigy.ballerapp.ui.features.otp_verification.VerifyOtpRepository
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpRepoImpl
import com.softprodigy.ballerapp.ui.features.sign_up.SignUpRepository
import com.softprodigy.ballerapp.ui.features.welcome.SocialLoginRepo
import com.softprodigy.ballerapp.ui.features.welcome.SocialLoginRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): IUserRepository {
        return UserRepository(service = apiService, dataStoreManager = dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideSignUpRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager,
    ): SignUpRepository {
        return SignUpRepoImpl(service = apiService, dataStoreManager = dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideForgotPassRepo(apiService: APIService): ForgotPasswordRepository =
        ForgotPasswordRepoImpl(apiService)

    @Provides
    @Singleton
    fun provideVerifyOtp(apiService: APIService): VerifyOtpRepository =
        VerifyOtpRepoImpl(apiService)

    @Provides
    @Singleton
    fun provideResetPassword(apiService: APIService): ResetPasswordRepository =
        ResetPasswordRepoImpl(apiService)

    @Provides
    @Singleton
    fun provideSocialLoginRepo(
        apiService: APIService,
        dataStoreManager: DataStoreManager
    ): SocialLoginRepo =
        SocialLoginRepoImpl(service = apiService, dataStoreManager = dataStoreManager)

}
