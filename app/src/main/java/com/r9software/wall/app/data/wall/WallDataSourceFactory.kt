package com.r9software.wall.app.data.wall

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.r9software.wall.app.network.NetworkService
import com.r9software.wall.app.data.model.WallModel
import io.reactivex.disposables.CompositeDisposable

class WallDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NetworkService
)
    : DataSource.Factory<Int, WallModel>() {

    val newsDataSourceLiveData = MutableLiveData<WallDataSource>()

    override fun create(): DataSource<Int, WallModel> {
        val newsDataSource = WallDataSource(networkService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}