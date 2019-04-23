package com.xqy.androidx.framework.adapter.expandable



typealias HeaderLayoutResId=()->Int
typealias FooterLayoutResId=()->Int
typealias ChildViewHolder<T> =()->ExpandableViewHolder<T,*>

 const val GROUP:Int =0
 const val CHILD:Int =1
