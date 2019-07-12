package com.r9software.wall.app.data.login

import android.text.TextUtils
import com.r9software.wall.app.data.Result
import com.r9software.wall.app.network.NetworkService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) {

    private var retryCompletable: Completable? = null
    fun login(email: String, password: String,callback: LoginCallback){
        try {
            var token: String
            compositeDisposable.add(
                networkService.login(email, password).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { response ->
                        token = response.obj.token
                        callback.loginSuccessfull(Result.Success(token))
                    },
                    {
                        it.printStackTrace()
                        setRetry(Action { login(email, password,callback) })
                    }
                ))

        } catch (e: Throwable) {
            callback.loginError(Result.Error(IOException("Error logging in", e)))
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    fun register(email: String, password: String, confirmation: String, name: String, callback: LoginCallback) {
        try {
            var token: String
            compositeDisposable.add(
                networkService.register(email, password,confirmation,name).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { response ->
                        token = response.obj.token
                        callback.loginSuccessfull(Result.Success(token))
                    },
                    {
                        it.printStackTrace()
                        setRetry(Action { login(email, password,callback) })
                    }
                ))

        } catch (e: Throwable) {
            callback.loginError(Result.Error(IOException("Error logging in", e)))
        }
    }

}
interface LoginCallback{
    fun loginSuccessfull(result:Result<String>)
    fun loginError(result: Result.Error)
}