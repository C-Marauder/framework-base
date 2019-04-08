package com.xqy.androidx.framework.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class AppViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(item:T)
}