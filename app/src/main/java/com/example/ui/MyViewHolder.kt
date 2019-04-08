package com.example.ui

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.xqy.androidx.framework.adapter.AppViewHolder

class MyViewHolder(private val go:()->Unit,itemView: View) : AppViewHolder<String>(itemView) {
    override fun bindData(item: String) {
        titleView.text = item
    }
    private var titleView:AppCompatTextView = itemView.findViewById(R.id.title)

    init {
        OnClickHelper.instance.onClick(titleView,1000){
            go()
        }

    }
}