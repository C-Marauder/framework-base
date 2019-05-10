package com.xqy.androidx.framework.adapter.expandable

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding

internal class GroupViewHolder< T,GVDB : ViewDataBinding>(
    viewDataBinding: GVDB, itemView: View,
    private val bindGroupData: (item: T) -> Unit,
    private val onGroupClick: (isExpanded: Boolean, position: Int) -> Unit
) :
    ExpandableViewHolder<T, GVDB>(viewDataBinding, itemView), View.OnClickListener {
    override fun bindData(item: T) {
    }

    private  var isExpanded: Boolean = false
    override fun onClick(v: View?) {
        onGroupClick(isExpanded, adapterPosition)
    }

    override fun bindItemData(item: Group<T>) {
        bindGroupData(item.item)
        isExpanded =item.isExpanded
    }

    init {
        viewDataBinding.root.setOnClickListener(this)
    }

}