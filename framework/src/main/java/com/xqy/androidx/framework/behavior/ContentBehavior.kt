package com.xqy.androidx.framework.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class ContentBehavior<V:View>(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<V>(context, attrs),BehaviorCallback {
    override var mActivePointerId: Int =0

    override var mOriginX: Float = 0f

    override var mOriginY: Float = 0f

    override val mContext: Context by lazy {
        context!!
    }


    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {
        when(ev.actionMasked){
            MotionEvent.ACTION_DOWN-> {
                mActivePointerId = ev.getPointerId(0)

                mOriginX = getMotionEventX(ev,mActivePointerId)
                mOriginY = getMotionEventY(ev,mActivePointerId)
                if (mOriginY== -1f){
                    return false
                }




            }
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }


}