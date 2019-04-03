package com.androidx.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class AppViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(item:T)
}