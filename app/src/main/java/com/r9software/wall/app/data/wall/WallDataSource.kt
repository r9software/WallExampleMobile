package com.r9software.wall.app.data.wall

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.r9software.wall.app.network.NetworkService
import com.r9software.wall.app.network.State
import com.r9software.wall.app.network.WallModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class WallDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, WallModel>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, WallModel>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.downloadWall(1)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.paginatedResponse.data,
                            null,
                            2
                        )
                    },
                    {
                        updateState(State.ERROR)
                        it.printStackTrace()
                        setRetry(Action { loadInitial(params,callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, WallModel>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.downloadWall(params.key)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.paginatedResponse.data,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, WallModel>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}
