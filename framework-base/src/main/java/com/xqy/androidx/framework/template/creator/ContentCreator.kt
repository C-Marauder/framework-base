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
                constrainWidth(model.contentView.id, 0)
                constrainHeight(model.contentView.id, 0)
                connect(model.contentView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(model.contentView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                if (it.constrainSetId==-1){
                    connect(model.contentView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                }else{

                    connect(model.contentView.id, ConstraintSet.TOP, it.constrainSetId, ConstraintSet.BOTTOM)
                }
                connect(model.contentView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                if (parentView is ConstraintLayout){
                    applyTo(parentView)

                }
            }


        }
    }
}