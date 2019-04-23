package com.xqy.androidx.framework.adapter.expandable

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class ExpandableViewHolder<T,VDB:ViewDataBinding>(val viewDataBinding: VDB, itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindItemData(item:T)
}