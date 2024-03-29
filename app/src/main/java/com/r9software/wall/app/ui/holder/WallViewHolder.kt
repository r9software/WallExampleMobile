package com.r9software.wall.app.ui.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.r9software.wall.app.R
import com.r9software.wall.app.network.WallModel
import kotlinx.android.synthetic.main.item_wall.view.*
import com.r9software.wall.app.util.*

class WallViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(wall: WallModel?) {
        if (wall != null) {
            itemView.txt_wall_name.text = wall.userName
            itemView.txt_wall_content.text = wall.wallContent
            itemView.txt_wall_time.text = wall.createdAt.formatDate()

        }
    }

    companion object {
        fun create(parent: ViewGroup): WallViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_wall, parent, false)
            return WallViewHolder(view)
        }
    }
}

