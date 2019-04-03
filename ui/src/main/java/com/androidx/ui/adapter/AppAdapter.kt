package com.androidx.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper

class AppAdapter<T>() : DelegateAdapter.Adapter<AppViewHolder<T>>() {
    private lateinit var items: MutableList<T>
    private lateinit var layoutResId: (position: Int) -> Int
    private lateinit var createHolder: (itemView: View) -> AppViewHolder<T>
    private lateinit var layoutHelper: LayoutHelper
    private var item: T? = null

    constructor(
        items: MutableList<T>,
        layoutResId: (position: Int) -> Int,
        createHolder: (itemView: View) -> AppViewHolder<T>,
        layoutHelper: LayoutHelper
    ) : this() {
        this.items = items
        this.layoutResId = layoutResId
        this.createHolder = createHolder
        this.layoutHelper = layoutHelper
    }

    constructor(
        item: T,
        layoutResId: (position: Int) -> Int,
        createHolder: (itemView: View) -> AppViewHolder<T>,
        layoutHelper: LayoutHelper) : this() {
        this.item = item
        this.layoutResId = layoutResId
        this.createHolder = createHolder
        this.layoutHelper = layoutHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val mItemView = layoutInflater.inflate(viewType, parent, false)
        return createHolder(mItemView)

    }

    override fun getItemViewType(position: Int): Int {
        return layoutResId(position)
    }

    override fun getItemCount(): Int = if (item == null) {
        items.size
    } else {
        1
    }


    override fun onCreateLayoutHelper(): LayoutHelper = layoutHelper

    override fun onBindViewHolder(holder: AppViewHolder<T>, position: Int) {
        if (item != null) {
            holder.bindData(item!!)
        } else {
            holder.bindData(items[position])

        }
    }


}