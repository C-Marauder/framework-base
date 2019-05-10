package com.xqy.androidx.framework.adapter.expandable

import android.view.View
import androidx.databinding.ViewDataBinding

typealias ChildItemData<T> = (position:Int)->MutableList<T>
typealias BindItemData<T> = (item:T,dataBinding:ViewDataBinding)->Unit
typealias CreateChildViewHolder<T> = (dataBinding:ViewDataBinding,itemView:View)-> ExpandableViewHolder<T, out ViewDataBinding>
const val HEADER: Int = 0
const val GROUP: Int = 1
const val CHILD: Int = 2
const val FOOTER: Int = 3

 data class Group<T>(var isExpanded:Boolean,var item:T)
