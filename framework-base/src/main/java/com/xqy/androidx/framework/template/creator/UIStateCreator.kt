package com.xqy.androidx.framework.template.creator

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.xqy.androidx.framework.template.creator.model.ConstrainSetModel
import com.xqy.androidx.framework.template.creator.model.UIModel

internal class UIStateCreator : UICreator<AppCompatImageView>() {
    companion object{
        @SuppressLint("ResourceType")
        @IdRes
        private const val mStateViewId = 0x2

    }

    override fun createWidget(parentView: ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?) {
        AppCompatImageView(parentView.context).apply {
            id = mStateViewId
            setBackgroundColor(
                ContextCompat.getColor(
                    rootView.context,
                    android.R.color.background_light
                )
            )
            model.uiStateCallback?.observeState(this)
            parentView.addView(this)
            constrainSetModel?.let {
                it.constraintSet.apply {
                    connect(mStateViewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(mStateViewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                    connect(mStateViewId, ConstraintSet.TOP, it.constrainSetId, ConstraintSet.TOP)
                    connect(mStateViewId, ConstraintSet.BOTTOM, it.constrainSetId, ConstraintSet.BOTTOM)
                }

            }
        }
    }

}