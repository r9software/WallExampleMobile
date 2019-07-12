package com.r9software.wall.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.r9software.wall.app.data.login.LoginRepository
import com.r9software.wall.app.data.Result

import com.r9software.wall.app.R
import android.content.Context.MODE_PRIVATE
import android.content.Context
import com.r9software.wall.app.data.login.LoginCallback
import com.r9software.wall.app.util.SharedPreferencesUtil


class LoginViewModel(private val loginRepository: LoginRepository, private val context: Context) : ViewModel(),
    LoginCallback {
    override fun loginSuccessfull(result: Result<String>) {
        if (result is Result.Success) {
            _loginResult.value = LoginResult(token = result.data)
            saveToken(result.data)
        }
    }

    override fun loginError(result: Result.Error) {
        _loginResult.value = LoginResult(error = R.string.login_failed)
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        loginRepository.login(username, password, this)
    }

    fun registerDataChanged(username: String, password: String,confirmation:String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else if (password!=confirmation) {
            _loginForm.value = LoginFormState(passwordError = R.string.password_should_match)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }


    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }

    fun saveToken(token: String) {
        SharedPreferencesUtil.getInstance().setToken(context,token)
    }

    fun register(email: String, password: String, confirmation: String, name: String) {
        loginRepository.register(email, password,confirmation,name, this)
    }
}
