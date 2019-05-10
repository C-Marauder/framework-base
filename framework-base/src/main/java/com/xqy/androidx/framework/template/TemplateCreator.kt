package com.xqy.androidx.framework.template

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout

internal class TemplateCreator(mContext:AppCompatActivity): UICreator(mContext) {

    fun createTemplate(template: Int):ViewGroup{
       return when(template){
            SCAFFLD -> CircularRevealCoordinatorLayout(mContext)
            CONSTRAINT-> ConstraintLayout(mContext)
           else -> throw IllegalArgumentException("unKnow template type ")
       }
    }

}