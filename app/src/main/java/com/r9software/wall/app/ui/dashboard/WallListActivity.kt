package com.r9software.wall.app.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.r9software.wall.app.R
import com.r9software.wall.app.network.State
import kotlinx.android.synthetic.main.activity_dashboard_list.*
import android.view.MenuItem
import android.widget.Toast
import com.r9software.wall.app.ui.login.LoginActivity
import com.r9software.wall.app.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.item_wall.*


class WallListActivity : AppCompatActivity() {
    private var menu: Menu? = null
    private val LOGIN_REQUEST = 101
    private lateinit var viewModel: WallListViewModel
    private lateinit var newsListAdapter: WallListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_list)
        setSupportActionBar(main_toolbar)
        viewModel = ViewModelProviders.of(this)
            .get(WallListViewModel::class.java)
        initAdapter()
        initState()
        btn_post.setOnClickListener {
            if (!TextUtils.isEmpty(txt_post_wall.text))
                viewModel.postToWall(txt_post_wall.text.toString(), this)
        }
        viewModel.postedResult.observe(this@WallListActivity, Observer {
            val posted = it ?: return@Observer
            if (posted) {
                txt_post_wall.setText("")
                Toast.makeText(this, "Content posted to the wall", Toast.LENGTH_LONG).show()
                viewModel.restartPosted()
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_wall_dashboard, menu)
        this.menu = menu
        checkSignIn()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.action_login -> {
                startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_REQUEST)
                return true
            }
            R.id.action_logout -> {
                SharedPreferencesUtil.getInstance().deleteToken(this)
                checkSignIn()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                checkSignIn()
            }
        }
    }

    private fun checkSignIn() {
        if (SharedPreferencesUtil.getInstance().isLoggedIn(this)) {
            txt_post_wall.visibility = View.VISIBLE
            btn_post.visibility = View.VISIBLE
            menu?.findItem(R.id.action_login)?.isVisible = false
            menu?.findItem(R.id.action_logout)?.isVisible = true
        } else {
            txt_post_wall.visibility = View.GONE
            btn_post.visibility = View.GONE
            menu?.findItem(R.id.action_login)?.isVisible = true
            menu?.findItem(R.id.action_logout)?.isVisible = false
        }
    }
}