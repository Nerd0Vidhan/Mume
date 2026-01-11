package com.lokal.mume.data.model

import com.google.gson.annotations.SerializedName

// In package com.lokal.mume.data.model
data class BaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T
)

// The Paginated data structure
data class PaginatedData<T>(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<T>
)