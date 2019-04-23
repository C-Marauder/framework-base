package com.xqy.androidx.framework.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class MVVMViewHolder<T,VDB:ViewDataBinding>(val viewDataBinding: VDB, itemView: View) :
    RecyclerView.ViewHolder(itemView) {


    abstract fun bindData(item:T)

}