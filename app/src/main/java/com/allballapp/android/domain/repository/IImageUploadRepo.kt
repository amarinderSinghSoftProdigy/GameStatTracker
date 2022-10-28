package com.allballapp.android.domain.repository

import com.allballapp.android.common.ResultWrapper
import com.allballapp.android.data.response.ImageUpload
import com.allballapp.android.domain.BaseResponse
import java.io.File

interface IImageUploadRepo {
    suspend fun uploadSingleImage(
        type: String,
        file: File?
    ): ResultWrapper<BaseResponse<ImageUpload>>

    suspend fun uploadSingleFile(
        mime: String,
        type: String,
        file: File?
    ): ResultWrapper<BaseResponse<ImageUpload>>
}