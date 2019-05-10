package com.xqy.androidx.framework.adapter.expandable

import android.view.View
import androidx.databinding.ViewDataBinding

internal class HeaderAndFooterViewHolder<T,VDB:ViewDataBinding>(viewDataBinding: VDB, itemView: View
                                                                 , private val bindDate:(item:T)->Unit) :
    ExpandableViewHolder<T, VDB>(viewDataBinding, itemView) {
    override fun bindData(item: T) {
        bindDate(item)

    }

}