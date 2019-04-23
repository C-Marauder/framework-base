package com.xqy.androidx.framework.adapter.expandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

 abstract class CoreAdapter<T, VDB : ViewDataBinding, EVH : ExpandableViewHolder<T, VDB>>(
     val items: MutableList<T>,
     val contentLayoutResId: (position: Int) -> Int,
     val viewHolders: (viewDataBinding: ViewDataBinding, itemView: View) -> EVH
) : RecyclerView.Adapter<EVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EVH {
        val layoutInflater = LayoutInflater.from(parent.context)

        val dataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

        return viewHolders(dataBinding, dataBinding.root)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {

        return contentLayoutResId(position)
    }

    override fun onBindViewHolder(holder: EVH, position: Int) {
        holder.bindItemData(items[position])
    }
}