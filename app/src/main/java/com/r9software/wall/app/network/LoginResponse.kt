package com.r9software.wall.app.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val obj: TokenObject
)

data class TokenObject(
    @SerializedName("token") val token: String
)
