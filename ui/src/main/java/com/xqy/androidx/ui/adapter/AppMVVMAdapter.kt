package com.xqy.androidx.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper

class AppMVVMAdapter<T,D:ViewDataBinding>(): DelegateAdapter.Adapter<AppMVVMViewHolder<T, D>>() {
    private lateinit var items:MutableList<T>
    private lateinit var layoutResId:(position:Int)->Int
    private lateinit var createHolder:(dataBinding:D, itemView:View)-> AppMVVMViewHolder<T, D>
    private lateinit var  layoutHelper: LayoutHelper
    private  var item:T?=null
    constructor(items:MutableList<T>,
                layoutResId:(position:Int)->Int,
                createHolder:(dataBinding:D, itemView:View)-> AppMVVMViewHolder<T, D>,
                layoutHelper: LayoutHelper):this(){
        this.items = items
        this.layoutResId = layoutResId
        this.createHolder = createHolder
        this.layoutHelper = layoutHelper
    }
    constructor(item:T,
                layoutResId:(position:Int)->Int,
                createHolder:(dataBinding:D, itemView:View)-> AppMVVMViewHolder<T, D>,
                layoutHelper: LayoutHelper):this(){
        this.item = item
        this.layoutResId = layoutResId
        this.createHolder = createHolder
        this.layoutHelper = layoutHelper
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppMVVMViewHolder<T, D> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val dataBinding =
            DataBindingUtil.inflate<D>(layoutInflater, viewType, parent, false)
        return createHolder(dataBinding,dataBinding.root)

    }

    override fun getItemViewType(position: Int): Int {
        return layoutResId(position)
    }
    override fun getItemCount(): Int= if (item==null){
        items.size
    }else{
        1
    }

    override fun onCreateLayoutHelper(): LayoutHelper = layoutHelper

    override fun onBindViewHolder(holder: AppMVVMViewHolder<T, D>, position: Int) {
        if (item!=null){
            holder.bindData(item!!)
        }else{
            holder.bindData(items[position])

        }
    }


}