package com.xqy.androidx.framework.adapter.expandable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ConcurrentHashMap

class ExpandableAdapter private constructor(private val mBuilder: Builder) :
    RecyclerView.Adapter<ExpandableViewHolder<Any, out ViewDataBinding>>() {
    companion object {
        private const val INVALID_LAYOUT = -1

        fun builder(init: Builder.() -> Unit) = Builder().apply(init).build()
    }

    private val mItemMap: ConcurrentHashMap<Int, Group<Any>> by lazy {
        ConcurrentHashMap<Int, Group<Any>>()
    }
    private val mItemTypes: ConcurrentHashMap<Int, Int> by lazy {
        ConcurrentHashMap<Int, Int>()
    }

    private var itemCount: Int = 0

    init {
        val mGroupCount = mBuilder.mGroupItems.size
        if (mBuilder.mHeaderLayout == INVALID_LAYOUT && mBuilder.mFooterLayout == INVALID_LAYOUT) {

            itemCount = mGroupCount

        }
        if (mBuilder.mHeaderLayout != INVALID_LAYOUT) {
            itemCount = mGroupCount + 1
            mItemTypes[0] = HEADER
            mBuilder.mHeaderData?.let {
                mItemMap[0] = Group(false,it)

            }

        }

        mBuilder.mGroupItems.forEachIndexed { index, item ->
            val realIndex = if (mBuilder.mHeaderLayout != INVALID_LAYOUT) {
                index + 1
            } else {
                index
            }
            mItemTypes[realIndex] = GROUP
            mItemMap[realIndex] = Group(false,item)
        }

        if (mBuilder.mFooterLayout != INVALID_LAYOUT) {
            itemCount = if (itemCount == 0) {
                mGroupCount + 1
            } else {
                mGroupCount + 2
            }
            mItemTypes[itemCount - 1] = FOOTER
            mBuilder.mFooterData?.let {
                mItemMap[itemCount - 1] = Group(false,it)
            }
        }

    }

    private fun <E> expandGroupItems(position: Int, childItems: MutableList<E>) {
        itemCount += childItems.size

        val tempItemMap = ConcurrentHashMap<Int,Group<Any>>()
        val tempItemTypeMap = ConcurrentHashMap<Int,Int>()
        mItemMap.filterKeys {
            it <= position
        }.keys.forEachIndexed { index, i ->
            tempItemMap[index] = mItemMap[i]!!
            tempItemTypeMap[index] = mItemTypes[i]!!

        }
        childItems.forEachIndexed { index, i ->
            val realIndex = position + index + 1
            tempItemMap[realIndex] = Group(false,i as Any)
            tempItemTypeMap[realIndex] = CHILD
        }
        val newItemCount = tempItemMap.size
        mItemMap.filterKeys {
            it > position
        }.keys.forEachIndexed { index, i ->
            tempItemMap[newItemCount + index] = mItemMap[i]!!
            tempItemTypeMap[newItemCount + index] = mItemTypes[i]!!

        }
        tempItemMap.keys.forEachIndexed { index, i ->
            mItemMap[index] = tempItemMap[i]!!
            mItemTypes[index] = tempItemTypeMap[i]!!
        }
        notifyItemRangeInserted(position + 1, childItems.size)

    }

    private fun <E> removeGroupItems(position: Int, childItems: MutableList<E>) {
        itemCount -= childItems.size
        mItemMap.filterKeys {
            it<=position || it>position+childItems.size
        }.keys.forEachIndexed { index, i ->
            Log.e("==$position=","$index---$i")
            mItemMap[index] = mItemMap[i]!!
            mItemTypes[index] = mItemTypes[i]!!

        }


        notifyItemRangeRemoved(position + 1, childItems.size)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder<Any, out ViewDataBinding> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutResId = when (viewType) {
            GROUP -> mBuilder.mGroupLayout
            CHILD -> mBuilder.mChildLayout
            HEADER -> mBuilder.mHeaderLayout
            FOOTER -> mBuilder.mFooterLayout
            else -> -1
        }
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutResId, parent, false)

        return when (viewType) {
            GROUP -> {
                GroupViewHolder(dataBinding, dataBinding.root, {
                    mBuilder.mBindGroupItemData.invoke(it, dataBinding)
                }, { isExpanded, position ->
                    val childItems = mBuilder.mChildItemData.invoke(position)
                    if (isExpanded) {
                        removeGroupItems(position, childItems)
                    } else {
                        expandGroupItems(position, childItems)
                    }
                    Log.e("===","$isExpanded----$position")
                    mItemMap[position]!!.isExpanded = !isExpanded
                    notifyItemChanged(position)
                })
            }
            CHILD -> {

                mBuilder.mCreateChildViewHolder.invoke(dataBinding, dataBinding.root)
            }

            HEADER -> {
                HeaderAndFooterViewHolder(dataBinding, dataBinding.root) {
                    mBuilder.mBindHeaderItemData.invoke(it, dataBinding)
                }
            }
            FOOTER -> {
                HeaderAndFooterViewHolder(dataBinding, dataBinding.root) {
                    mBuilder.mBindFooterItemData.invoke(it, dataBinding)
                }
            }

            else -> throw Exception("unKnow ViewType")
        }


    }


    override fun getItemCount(): Int = itemCount

    override fun getItemViewType(position: Int): Int {

        return mItemTypes[position]!!
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder<Any, out ViewDataBinding>, position: Int) {
        holder.bindItemData(mItemMap[position]!!)
    }

    class Builder {
        internal lateinit var mGroupItems: MutableList<Any>
        internal var mGroupLayout: Int = -1
        internal var mChildLayout: Int = -1
        internal var mHeaderLayout: Int = -1
        internal var mFooterLayout: Int = -1
        internal lateinit var mBindGroupItemData: BindItemData<Any>
        internal lateinit var mBindFooterItemData: BindItemData<Any>
        internal lateinit var mChildItemData: ChildItemData<*>
        internal var mHeaderData: Any? = null
        internal var mFooterData: Any? = null
        internal lateinit var mBindHeaderItemData: BindItemData<Any>
        internal lateinit var mCreateChildViewHolder: CreateChildViewHolder<Any>
        fun <T> groupItems(init: () -> MutableList<T>) {
            this.mGroupItems = init() as MutableList<Any>
        }

        fun headerLayout(init: () -> Int) {
            this.mHeaderLayout = init()
        }

        fun footerLayout(init: () -> Int) {
            this.mFooterLayout = init()
        }

        fun groupLayout(init: () -> Int) {
            this.mGroupLayout = init()
        }

        fun childLayout(init: () -> Int) {
            this.mChildLayout = init()
        }

        fun <T> getHeaderItemData(init: () -> T) {
            this.mHeaderData = init() as Any
        }

        fun <T> getFooterItemData(init: () -> T) {
            this.mFooterData = init()
        }

        fun <T> bindHeaderItemData(bindItemData: BindItemData<T>) {
            this.mBindHeaderItemData = bindItemData as BindItemData<Any>
        }

        fun <T> bindFooterItemData(bindItemData: BindItemData<T>) {
            this.mBindFooterItemData = bindItemData as BindItemData<Any>
        }

        fun <T> bindGroupItemData(bindItemData: BindItemData<T>) {
            this.mBindGroupItemData = bindItemData as BindItemData<Any>
        }


        fun <T> createChildViewHolder(childViewHolder: CreateChildViewHolder<T>) {
            this.mCreateChildViewHolder = childViewHolder as CreateChildViewHolder<Any>
        }

        fun <T> childItems(childItemData: ChildItemData<T>) {
            this.mChildItemData = childItemData
        }

        internal fun build() = ExpandableAdapter(this)
    }
}