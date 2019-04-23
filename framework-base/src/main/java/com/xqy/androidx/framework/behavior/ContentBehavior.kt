package com.xqy.androidx.framework.behavior

import android.animation.FloatEvaluator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.xqy.androidx.framework.behavior.BehaviorCallback.Companion.DRAG_RATE
import com.xqy.androidx.framework.widget.AppContentLayout

class ContentBehavior<V:ViewGroup>(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<V>(context, attrs), BehaviorCallback {

    override var mIsBeingDragged: Boolean = false

    override var mActivePointerId: Int = 0

    override var mOriginX: Float = 0f

    override var mOriginY: Float = 0f

    override val mContext: Context by lazy {
        context!!
    }

    private val mFloatEvaluator: FloatEvaluator by lazy {
        FloatEvaluator()
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: V, ev: MotionEvent): Boolean {

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {

                mActivePointerId = ev.getPointerId(0)

                mOriginX = getMotionEventX(ev, mActivePointerId)
                mOriginY = getMotionEventY(ev, mActivePointerId)
                if (mOriginY == -1f) {

                    mIsBeingDragged = false
                }


            }
            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == BehaviorCallback.INVALID_POINTER) {

                    mIsBeingDragged = false
                }
                val y = getMotionEventY(ev, mActivePointerId)
                val x = getMotionEventX(ev, mActivePointerId)
                if (y == -1f) {
                    mIsBeingDragged = false
                }
                val moveY = y - mOriginY
                val moveX = x - mOriginX

                if (mOriginY < mOriginX) {
                    mIsBeingDragged = false
                }
                if (Math.atan((moveY / moveX).toDouble()) <= 10 && moveY > mTouchSlop) {
                    mIsBeingDragged = true
                }

                mIsBeingDragged = Math.abs(moveY) <= totalDragHeightDistance
                if (mIsBeingDragged){
                    val scrollTop = moveY * DRAG_RATE
                    val mCurrentDragPercent = Math.abs(scrollTop)/totalDragHeightDistance
                    val boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent))
                    val extraOS = Math.abs(scrollTop) - totalDragHeightDistance
                    val tensionSlingshotPercent = Math.max(0f,
                        Math.min(extraOS, totalDragHeightDistance * 2f) / totalDragHeightDistance*1f)
                    val tensionPercent = (tensionSlingshotPercent / 4 - Math.pow(
                        (tensionSlingshotPercent / 4)*1.00, 2.0
                    )) * 2f

                    val extraMove = totalDragHeightDistance * tensionPercent / 2
                    val targetY = (totalDragHeightDistance * boundedDragPercent + extraMove)
                    child.translationY = targetY.toFloat()
//                    setTargetOffsetTop(targetY - mCurrentOffsetTop)
                }


            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex
                mActivePointerId = ev.getPointerId(index)
            }
            MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                mOriginX = 0f
                mOriginY = 0f
                mIsBeingDragged = false
                mActivePointerId = BehaviorCallback.INVALID_POINTER

            }
        }
        return true


    }

//
//    internal inner class ViewDragCallback : ViewDragHelper.Callback() {
//
//
//
//        private var moveY: Int = 0
//        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
//
//            return child == parent.getChildAt(1)
//        }
//
//        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//            if (top < -totalDragHeightDistance) {
//                moveY = -totalDragHeightDistance
//
//            }
//
//            if (top > totalDragHeightDistance) {
//                moveY = totalDragHeightDistance
//            }
//
//            if (top >= -totalDragHeightDistance && top < totalDragHeightDistance) {
//                moveY = top
//            }
//
//            return moveY
//        }
//
//        override fun getViewVerticalDragRange(child: View): Int {
//            return totalDragHeightDistance
//        }
//
//        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
//            super.onViewPositionChanged(changedView, left, top, dx, dy)
//            val mPercent = top * 1f / totalDragHeightDistance
//            if (mPercent == 1f) {
//                changedView.translationX = 0f
//            } else if (mPercent == 0f) {
//                changedView.translationX = 0f
//            }
//        }
//
//        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
//            if (releasedChild.top >= totalDragHeightDistance) {
//                mViewDragHelper.settleCapturedViewAt(totalDragHeightDistance, 0)
//
//            }
//            if (releasedChild.top <= -totalDragHeightDistance) {
//                mViewDragHelper.settleCapturedViewAt(totalDragHeightDistance, 0)
//
//            }
//            parent.invalidate()
//        }
//    }


}