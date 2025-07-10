package com.mizzenmast.agroai.api

// API Service for Plant Health Analysis
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PlantHealthApiService {
    @Multipart
    @POST("analyze-plant")
    suspend fun analyzePlant(
        @Part image: MultipartBody.Part,
        @Part("metadata") metadata: RequestBody
    ): Response<PlantAnalysisResponse>
}

data class PlantAnalysisResponse(
    val status: String,
    val disease: String? = null,
    val confidence: Float,
    val missingNutrients: List<String>? = null,
    val recommendations: List<String>
)
