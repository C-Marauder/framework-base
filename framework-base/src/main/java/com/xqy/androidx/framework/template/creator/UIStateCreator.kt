package com.xqy.androidx.framework.template.creator

import android.annotation.SuppressLint
import android.view.View
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
            visibility = View.GONE

            constrainSetModel?.let {
                it.constraintSet.apply {
                    this.setVisibility(mStateViewId,View.GONE)
                    connect(mStateViewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(mStateViewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                    connect(mStateViewId, ConstraintSet.TOP, it.preView!!.id, ConstraintSet.TOP)
                    connect(mStateViewId, ConstraintSet.BOTTOM, it.preView!!.id, ConstraintSet.BOTTOM)
                }

            }
        }
    }

}