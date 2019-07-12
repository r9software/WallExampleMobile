package com.r9software.wall.app.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val token: String = "",
    val error: Int? = null
)
