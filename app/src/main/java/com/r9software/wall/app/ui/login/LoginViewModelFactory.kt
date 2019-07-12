package com.r9software.wall.app.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.r9software.wall.app.data.login.LoginDataSource
import com.r9software.wall.app.data.login.LoginRepository
import com.r9software.wall.app.network.NetworkService
import io.reactivex.disposables.CompositeDisposable

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(
                        networkService, compositeDisposable
                    )
                ), context = context
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
