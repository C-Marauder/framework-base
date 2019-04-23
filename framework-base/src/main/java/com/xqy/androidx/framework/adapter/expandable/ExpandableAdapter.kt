package com.xqy.androidx.framework.adapter.expandable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ConcurrentHashMap

class ExpandableAdapter<T, GVDB : ViewDataBinding, E, CVDB : ViewDataBinding, CEVH : ExpandableViewHolder<E, CVDB>>(
    private val groupItems: MutableList<T>,
    private val contentLayoutResId: () -> Pair<Int, Int>,
    private val bindGroupData: (viewDataBinding: GVDB, item: T) -> Unit,
    private val mChildViewHolder: (viewDataBinding: CVDB, itemView: View) -> CEVH,
    private val getChildItems: (position: Int) -> MutableList<E>,
    private val headerLayoutResId: HeaderLayoutResId? = null,
    private val footerLayoutResId: FooterLayoutResId? = null
) : RecyclerView.Adapter<ExpandableViewHolder<Any, out ViewDataBinding>>() {

    private val realItems: MutableList<Any> by lazy {
        mutableListOf<Any>()
    }
    private val mItemMap: ConcurrentHashMap<Int, Any> by lazy {
        ConcurrentHashMap<Int, Any>()
    }
    private val mItemTypes: ArrayList<Int> by lazy {
        arrayListOf<Int>()
    }
    private var itemCount: Int = 0
    private var mGroupLayoutResId: Int = -1
    private var mChildLayoutResId: Int = -1

    init {
        if (headerLayoutResId == null && footerLayoutResId == null) {

            itemCount = groupItems.size

        }
        if (headerLayoutResId != null) {
            itemCount = groupItems.size + 1
        }

        if (footerLayoutResId != null) {
            itemCount = if (itemCount == 0) {
                groupItems.size + 1
            } else {
                groupItems.size + 2
            }
        }
        val pair = contentLayoutResId()
        mChildLayoutResId = pair.second
        mGroupLayoutResId = pair.first
        groupItems.forEach {
            mItemTypes.add(GROUP)
            realItems.add(it as Any)
        }

    }

    private fun expandGroupItems(position: Int, childItems: MutableList<E>) {
        itemCount += childItems.size
        childItems.forEachIndexed { index, i ->
            val realIndex = position + index + 1
            realItems.add(realIndex, i as Any)
            mItemTypes.add(realIndex, CHILD)
        }
        mItemMap.clear()
        realItems.forEachIndexed { index, any ->
            mItemMap[index] = any
        }
        notifyItemRangeInserted(position + 1, childItems.size)

    }

    private fun removeGroupItems(position: Int, childItems: MutableList<E>) {
        itemCount -= childItems.size
        childItems.forEachIndexed { index, item ->
            val realIndex = position + index + 1
            realItems.remove(item as Any)
            mItemTypes.removeAt(realIndex)

        }
        mItemTypes[position+1] = GROUP
        mItemMap.clear()
        realItems.forEachIndexed { index, any ->
            mItemMap[index] = any
        }
        notifyItemRangeRemoved(position + 1, childItems.size)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder<Any, out ViewDataBinding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutResId = when (viewType) {
            GROUP -> mGroupLayoutResId
            CHILD -> mChildLayoutResId
            else -> 0
        }


        return if (viewType == GROUP) {
            val dataBinding = DataBindingUtil.inflate<GVDB>(layoutInflater, layoutResId, parent, false)
            GroupViewHolder<T, GVDB>(dataBinding, dataBinding.root, {
                bindGroupData(dataBinding, it)
            }, { isExpanded, position ->
                val childItems = getChildItems(position)
                if (isExpanded) {
                    removeGroupItems(position, childItems)
                } else {
                    expandGroupItems(position, childItems)
                }
            }) as ExpandableViewHolder<Any, out ViewDataBinding>
        } else {
            val dataBinding = DataBindingUtil.inflate<CVDB>(layoutInflater, layoutResId, parent, false)

            mChildViewHolder(dataBinding, dataBinding.root) as ExpandableViewHolder<Any, out ViewDataBinding>
        }
    }


    override fun getItemCount(): Int = itemCount

    override fun getItemViewType(position: Int): Int {
        if (headerLayoutResId != null && position == 0) {
            return headerLayoutResId.invoke()
        }

        if (footerLayoutResId != null && position == itemCount - 1) {
            return footerLayoutResId.invoke()
        }
        return mItemTypes[position]
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder<Any, out ViewDataBinding>, position: Int) {
        if (headerLayoutResId == null && footerLayoutResId == null) {
            holder.bindItemData(realItems[position])

            return
        }


        if (headerLayoutResId != null && footerLayoutResId == null) {
            holder.bindItemData(realItems[position - 1])
            return
        }

        if (headerLayoutResId == null && footerLayoutResId != null) {
            if (position != itemCount - 1) {
                holder.bindItemData(realItems[position])

            }
        }
    }

}