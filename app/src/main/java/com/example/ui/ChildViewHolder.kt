package com.example.ui

import android.view.View
import com.example.ui.databinding.ItemChildBinding
import com.xqy.androidx.framework.adapter.expandable.ExpandableViewHolder

class ChildViewHolder(viewDataBinding: ItemChildBinding, itemView: View) :
    ExpandableViewHolder<String, ItemChildBinding>(viewDataBinding, itemView) {
    override fun bindData(item: String) {
        viewDataBinding.content = item

    }
}