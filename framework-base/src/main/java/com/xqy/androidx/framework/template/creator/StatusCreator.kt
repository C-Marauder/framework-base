package com.xqy.androidx.framework.template.creator

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import com.xqy.androidx.framework.template.creator.model.ConstrainSetModel
import com.xqy.androidx.framework.template.creator.model.UIModel
import com.xqy.androidx.framework.utils.dp

/**
 *@desc 创建状态栏
 *@creator 小灰灰
 *@Time 2019-06-05 - 11:41
 **/
internal class StatusCreator: UICreator<View>() {
    companion object{
        @SuppressLint("ResourceType")
        @IdRes
        private const val statusId = 0x13
    }
    override fun createWidget(parentView: ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?) {
       val statusView = View(parentView.context).apply {
            id = statusId
            setBackgroundResource(model.themeColor)
        }
        parentView.addView(statusView,-1,-2)
        constrainSetModel?.let {
            it.preView = statusView
            with(it.constraintSet){
                constrainHeight(statusView.id,23.dp)
                constrainWidth(statusView.id,0)
                connect(statusView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                connect(statusView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(statusView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            }

        }
    }
}