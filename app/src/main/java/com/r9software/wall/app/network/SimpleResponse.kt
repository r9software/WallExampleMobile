package com.r9software.wall.app.network

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("success") val wall: WallObject
)

data class WallObject(
    @SerializedName("wall_element") val element: WallModel
)
