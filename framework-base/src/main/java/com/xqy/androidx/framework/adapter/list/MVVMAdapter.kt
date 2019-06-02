package com.xqy.androidx.framework.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.xqy.androidx.framework.adapter.builder.AdapterHelperUtils

internal class MVVMAdapter<T, D : ViewDataBinding>(

    private val creator: AdapterHelperUtils<T, D>
) :
    DelegateAdapter.Adapter<MVVMViewHolder<T, D>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVVMViewHolder<T, D> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding =
            DataBindingUtil.inflate<D>(layoutInflater, viewType, parent, false)
        return creator.viewHolder(dataBinding, dataBinding.root)

    }

    override fun getItemViewType(position: Int): Int {
        return creator.layoutResId(position)
    }

    override fun getItemCount(): Int = creator.mItems.size

    override fun onCreateLayoutHelper(): LayoutHelper = creator.layoutHelper

    override fun onBindViewHolder(holder: MVVMViewHolder<T, D>, position: Int) {
            holder.bindData(creator.mItems[position])


    }


}