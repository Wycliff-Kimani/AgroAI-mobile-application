package com.mizzenmast.agroai.data

// Data class for plant health results
data class PlantHealthResult(
    val status: String,
    val disease: String,
    val confidence: Float,
    val missingNutrients: List<String>,
    val recommendations: List<String>
)
