package com.mizzenmast.agroai.repository

import android.content.Context
import android.net.Uri
import com.mizzenmast.agroai.api.PlantAnalysisResponse
import com.mizzenmast.agroai.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class PlantHealthRepository {

    suspend fun analyzePlant(context: Context, imageUri: Uri): Result<PlantAnalysisResponse> {
        return try {
            // Convert URI to File
            val imageFile = uriToFile(context, imageUri)

            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val metadata = "{}".toRequestBody("text/plain".toMediaTypeOrNull())

            val response = ApiClient.plantHealthApiService.analyzePlant(imagePart, metadata)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Analysis failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        return file
    }
}