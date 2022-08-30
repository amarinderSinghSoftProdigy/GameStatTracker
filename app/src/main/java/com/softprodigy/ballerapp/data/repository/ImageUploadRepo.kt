package com.softprodigy.ballerapp.data.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.common.safeApiCall
import com.softprodigy.ballerapp.data.datastore.DataStoreManager
import com.softprodigy.ballerapp.data.response.ImageUpload
import com.softprodigy.ballerapp.domain.BaseResponse
import com.softprodigy.ballerapp.domain.repository.IImageUploadRepo
import com.softprodigy.ballerapp.network.APIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImageUploadRepo @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val service: APIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IImageUploadRepo {
    override suspend fun uploadSingleImage(
        type: String,
        file: File?
    ): ResultWrapper<BaseResponse<ImageUpload>> {
        val typeReqBody = type.toRequestBody()
        val fileRequestBody = file?.asRequestBody("image/*".toMediaType())
        val body: MultipartBody.Part? = if (fileRequestBody != null) {
            MultipartBody.Part.createFormData("file", file.name, fileRequestBody)
        } else null

        return safeApiCall(dispatcher) { service.uploadSingleImage(typeReqBody, body) }
    }
}