package com.xqy.androidx.framework.adapter.expandable

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.xqy.androidx.framework.utils.onClick

internal class GroupViewHolder<T,GVDB:ViewDataBinding>(viewDataBinding: GVDB, itemView: View,
                                                       private val bindData:(item:T)->Unit,
                                                       onGroupClick:(isExpanded:Boolean,position:Int)->Unit) :
    ExpandableViewHolder<T, GVDB>(viewDataBinding, itemView) {
    private var isExpanded:Boolean = false
    override fun bindItemData(item: T) {
        bindData(item)
    }
    init {
        itemView.onClick {
            onGroupClick(isExpanded,adapterPosition)
            isExpanded=!isExpanded
        }

    }

}