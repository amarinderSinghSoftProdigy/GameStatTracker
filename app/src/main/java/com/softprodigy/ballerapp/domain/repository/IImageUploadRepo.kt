package com.softprodigy.ballerapp.domain.repository

import com.softprodigy.ballerapp.common.ResultWrapper
import com.softprodigy.ballerapp.data.response.ImageUpload
import com.softprodigy.ballerapp.domain.BaseResponse
import java.io.File

interface IImageUploadRepo {
    suspend fun uploadSingleImage(type: String, file: File?):ResultWrapper<BaseResponse<ImageUpload>>
}