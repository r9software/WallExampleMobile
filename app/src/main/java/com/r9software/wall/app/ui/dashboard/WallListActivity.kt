package com.r9software.wall.app.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.r9software.wall.app.R
import com.r9software.wall.app.network.State
import kotlinx.android.synthetic.main.activity_dashboard_list.*

class WallListActivity : AppCompatActivity() {

    private lateinit var viewModel: WallListViewModel
    private lateinit var newsListAdapter: WallListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_list)

        viewModel = ViewModelProviders.of(this)
                .get(WallListViewModel::class.java)
        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = WallListAdapter { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(this, Observer {
            newsListAdapter.submitList(it)
        })
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }

}