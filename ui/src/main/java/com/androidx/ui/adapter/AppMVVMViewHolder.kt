package com.androidx.ui.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class AppMVVMViewHolder<T,D:ViewDataBinding>(val dataBinding: D, itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(item:T)
}