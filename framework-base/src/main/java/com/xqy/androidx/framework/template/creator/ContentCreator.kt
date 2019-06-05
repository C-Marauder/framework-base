package com.xqy.androidx.framework.template.creator

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.xqy.androidx.framework.template.creator.model.ConstrainSetModel
import com.xqy.androidx.framework.template.creator.model.UIModel

internal class ContentCreator:UICreator<View>() {


    override fun createWidget(parentView: ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?) {
        parentView.addView(model.contentView)
        constrainSetModel?.let {

            it.constraintSet.apply {
                model.contentView?.let {view->
                    constrainWidth(view.id, 0)
                    constrainHeight(view.id, 0)
                    connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(view.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                    if (it.preView==null){
                        connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    }else{
                        connect(view.id, ConstraintSet.TOP, it.preView!!.id, ConstraintSet.BOTTOM)
                    }
                    connect(view.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                }

            }
            it.preView = model.contentView


        }
    }
}