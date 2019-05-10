package com.xqy.androidx.framework.adapter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper

class MVVMAdapter<T, D : ViewDataBinding> private constructor(

    private val builder: Builder<T, D>
) :
    DelegateAdapter.Adapter<MVVMViewHolder<T, D>>() {

    companion object {
        fun <T,D:ViewDataBinding>Builder(init: Builder<T, D>.()->Unit)= Builder<T, D>().apply(init).build()
    }
    class Builder<T,D:ViewDataBinding> {
        internal lateinit var items: MutableList<T>
        internal  var item: T? = null
        internal lateinit var layoutHelper: LayoutHelper
        internal lateinit var  layoutResId: (position: Int) -> Int
        internal lateinit var createHolder: (dataBinding: D, itemView: View) -> MVVMViewHolder<T, D>

        fun item(init:()->T){
            this.item = init()
        }

        fun items(init:()->MutableList<T>){
            this.items = init()
        }

        fun viewHolder(init: (dataBinding: D, itemView: View)-> MVVMViewHolder<T, D>){
            this.createHolder = init
        }

        fun layoutResId(init: (position:Int) -> Int){
            this.layoutResId = init
        }

        fun layoutHelper(init: () -> LayoutHelper){
            this.layoutHelper = init()
        }
        fun build()= MVVMAdapter(this)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MVVMViewHolder<T, D> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val dataBinding =
            DataBindingUtil.inflate<D>(layoutInflater, viewType, parent, false)
        return builder.createHolder(dataBinding, dataBinding.root)

    }

    override fun getItemViewType(position: Int): Int {
        return builder.layoutResId(position)
    }

    override fun getItemCount(): Int = if (builder.item == null) {
        builder.items.size
    } else {
        1
    }

    override fun onCreateLayoutHelper(): LayoutHelper = builder.layoutHelper

    override fun onBindViewHolder(holder: MVVMViewHolder<T, D>, position: Int) {


        if (builder.item != null) {
            holder.bindData(builder.item!!)
        } else {
            holder.bindData(builder.items[position])

        }
    }


}