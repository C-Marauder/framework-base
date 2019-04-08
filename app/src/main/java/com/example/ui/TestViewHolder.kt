package com.example.ui

import android.view.View
import com.example.ui.databinding.ItemTestBinding
import com.xqy.androidx.framework.adapter.MVVMViewHolder

class TestViewHolder(dataBinding: ItemTestBinding, itemView: View) :
    MVVMViewHolder<String, ItemTestBinding>(dataBinding, itemView) {
    override fun bindData(item: String) {
        dataBinding.num = item
    }
}