package com.r9software.wall.app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.r9software.wall.app.network.WallModel
import com.r9software.wall.app.data.wall.WallDataSource
import com.r9software.wall.app.data.wall.WallDataSourceFactory
import com.r9software.wall.app.network.NetworkService
import com.r9software.wall.app.network.State
import io.reactivex.disposables.CompositeDisposable

class WallListViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var newsList: LiveData<PagedList<WallModel>>
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

    fun postToWall(text: String) {

    }
}