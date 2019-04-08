package com.xqy.androidx.framework.behavior

import android.content.Context
import android.content.res.Resources
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout


internal interface BehaviorCallback {
    companion object {
         const val INVALID_POINTER = -1

    }
    var mOriginX: Float
    var mOriginY:Float
    var mActivePointerId:Int

    fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index = ev.findPointerIndex(activePointerId)
        return if (index < 0) {
            -1f
        } else ev.getY( index)
    }
    fun getMotionEventX(ev: MotionEvent, activePointerId: Int): Float {
        val index = ev.findPointerIndex(activePointerId)
        return if (index < 0) {
            -1f
        } else ev.getX( index)
    }

    val totalDragHeightDistance: Int
        get() {
            return Resources.getSystem().displayMetrics.heightPixels / 2
        }

    val totalDragWidthDistance: Int
        get() {
            return Resources.getSystem().displayMetrics.widthPixels / 3
        }

    val mContext: Context
    val mTouchSlop: Int
        get() {
            return ViewConfiguration.get(mContext).scaledTouchSlop
        }
}