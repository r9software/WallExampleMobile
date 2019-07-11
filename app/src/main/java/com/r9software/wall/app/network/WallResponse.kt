package com.r9software.wall.app.network

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("success") val paginatedResponse: WallPaginated
)

data class WallPaginated(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("data") val data: List<WallModel>
)

data class WallModel( @SerializedName("id") val id: Int,
                      @SerializedName("user_id") val userId: Int,
                      @SerializedName("wall_content") val wallContent: String)