package com.xqy.androidx.framework.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.xqy.androidx.framework.adapter.builder.AdapterHelperUtils

/**
 *@desc
 *@creator 小灰灰
 *@Time 2019-06-01 - 22:43
 **/
internal class AndroidAdapter<T,VDB:ViewDataBinding>(private val mAdapterHelperCreator: AdapterHelperUtils<T,VDB>): RecyclerView.Adapter<MVVMViewHolder<T,VDB>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVVMViewHolder<T, VDB> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val dataBinding =
            DataBindingUtil.inflate<VDB>(layoutInflater, viewType, parent, false)
        return mAdapterHelperCreator.viewHolder(dataBinding,dataBinding.root)
    }
    override fun getItemViewType(position: Int): Int {
        return mAdapterHelperCreator.layoutResId(position)
    }
    override fun getItemCount(): Int = mAdapterHelperCreator.mItems.size

    override fun onBindViewHolder(holder: MVVMViewHolder<T, VDB>, position: Int) {
        holder.bindData(mAdapterHelperCreator.mItems[position])
    }
}