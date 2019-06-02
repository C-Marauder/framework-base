package com.xqy.androidx.framework.adapter.builder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.LayoutHelper
import com.xqy.androidx.framework.adapter.list.AndroidAdapter
import com.xqy.androidx.framework.adapter.list.MVVMAdapter
import com.xqy.androidx.framework.adapter.list.MVVMViewHolder

/**
 *@desc
 *@creator 小灰灰
 *@Time 2019-06-01 - 22:37
 **/
class AdapterHelperUtils<T,VDB:ViewDataBinding>{

    companion object{

        fun <T,VDB:ViewDataBinding>creator(init:AdapterHelperUtils<T,VDB>.()->Unit) = AdapterHelperUtils<T,VDB>().apply(init)
    }
    internal lateinit var layoutHelper: LayoutHelper
    internal lateinit var layoutResId:(position:Int)->Int
    internal lateinit var mItems:MutableList<T>
    internal lateinit var viewHolder:(dataBinding: VDB, itemView: android.view.View) -> MVVMViewHolder<T, VDB>

    fun items(init:()->MutableList<T>){
        mItems = init()
    }

    fun viewHolder(init: (dataBinding: VDB, itemView: View)-> MVVMViewHolder<T, VDB>){
        viewHolder = init
    }

    fun layoutResId(init: (position:Int) -> Int){
        this.layoutResId = init
    }

    fun layoutHelper(init: () -> LayoutHelper){
        this.layoutHelper = init()
    }

    fun createAdapter(isAndroid:Boolean):RecyclerView.Adapter<MVVMViewHolder<T,VDB>> {
           return if (isAndroid){
                AndroidAdapter<T,VDB>(this)
            }else{
                MVVMAdapter<T,VDB>(this)
            }

    }

}