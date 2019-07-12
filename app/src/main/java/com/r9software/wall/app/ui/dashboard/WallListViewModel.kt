package com.r9software.wall.app.ui.dashboard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.r9software.wall.app.data.Result
import com.r9software.wall.app.data.wall.WallCallback
import com.r9software.wall.app.network.WallModel
import com.r9software.wall.app.data.wall.WallDataSource
import com.r9software.wall.app.data.wall.WallDataSourceFactory
import com.r9software.wall.app.network.NetworkService
import com.r9software.wall.app.network.State
import com.r9software.wall.app.ui.login.LoginResult
import com.r9software.wall.app.util.SharedPreferencesUtil
import io.reactivex.disposables.CompositeDisposable

class WallListViewModel : ViewModel(), WallCallback {


    private val networkService = NetworkService.getService()
    var newsList: LiveData<PagedList<WallModel>>

    val postedResult = MutableLiveData<Boolean>()
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 25
    private val newsDataSourceFactory: WallDataSourceFactory

    init {
        newsDataSourceFactory = WallDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setEnablePlaceholders(false)
                .build()
        newsList = LivePagedListBuilder<Int, WallModel>(newsDataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap<WallDataSource,
            State>(newsDataSourceFactory.newsDataSourceLiveData, WallDataSource::state)

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun postToWall(text: String,context: Context) {
        newsDataSourceFactory.postToWall(text,SharedPreferencesUtil.getInstance().getToken(context),this)
    }
    override fun wallPosted(result: Result.Success<WallModel>) {
        newsDataSourceFactory.newsDataSourceLiveData.value?.invalidate()
        postedResult.postValue(true)
    }

    override fun error(result: Result.Error) {
        postedResult.postValue(false)
    }

    fun restartPosted() {
        postedResult.postValue(false)
    }
}