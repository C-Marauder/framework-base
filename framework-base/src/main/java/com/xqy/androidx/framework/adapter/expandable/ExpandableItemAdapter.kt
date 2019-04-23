package com.xqy.androidx.framework.adapter.expandable
import android.view.View

import androidx.databinding.ViewDataBinding

class ExpandableItemAdapter<T, VDB : ViewDataBinding, EVH : ExpandableViewHolder<T, VDB>>(
    items: MutableList<T>,
    contentLayoutResId: (position: Int) -> Int,
    viewHolders: (viewDataBinding: ViewDataBinding, itemView: View) -> EVH) : CoreAdapter<T, VDB, EVH>(items, contentLayoutResId, viewHolders) {


}