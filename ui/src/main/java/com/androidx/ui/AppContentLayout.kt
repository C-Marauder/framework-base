package com.androidx.ui

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.Transformation
import android.widget.ListView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.ListViewCompat
import androidx.recyclerview.widget.RecyclerView


class AppContentLayout : CoordinatorLayout {

    private lateinit var mTarget: View
    private val mDecelerateInterpolator: Interpolator
    private val mTouchSlop: Int
    var totalDragDistance: Int = 0
    private var mCurrentDragPercent: Float = 0.toFloat()
    private var mCurrentOffsetTop: Int = 0
    private var mActivePointerId: Int = 0
    private var mIsBeingDragged: Boolean = false
    private var mInitialMotionY: Float = 0.toFloat()
    private var mFrom: Int = 0
    private var mFromDragPercent: Float = 0.toFloat()
    private val mAnimateToStartPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            moveToStart(interpolatedTime)
        }
    }

    private val mAnimateToCorrectPosition = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val targetTop: Int
            val endTarget = totalDragDistance
            targetTop = mFrom + ((endTarget - mFrom) * interpolatedTime).toInt()
            val offset = targetTop - mTarget.top

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime

            setTargetOffsetTop(offset /* requires update */)
        }
    }

    private val mToStartListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            mCurrentOffsetTop = mTarget.top
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mDecelerateInterpolator = DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        totalDragDistance = DRAG_MAX_DISTANCE
        setWillNotDraw(false)
        this.isChildrenDrawingOrderEnabled = true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
       ensureTarget()
    }
    private lateinit var mAppBarLayout: View
    private fun ensureTarget() {
        if (childCount !=2)
            return
        mAppBarLayout = getChildAt(0)
        mTarget = getChildAt(1)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isEnabled || canChildScrollUp()) {
            return false
        }

        val action = ev.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                setTargetOffsetTop(0)
                mActivePointerId = ev.getPointerId( 0)
                mIsBeingDragged = false
                val initialMotionY = getMotionEventY(ev, mActivePointerId)
                if (initialMotionY<mAppBarLayout.bottom){
                    return false
                }
                if (initialMotionY == -1f) {
                    return false
                }
                if (mTarget !is ListView && mTarget !is RecyclerView){
                    mIsBeingDragged = true

                }
                mInitialMotionY = initialMotionY
            }
            MotionEvent.ACTION_MOVE -> {

                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                val y = getMotionEventY(ev, mActivePointerId)
                if (y == -1f) {
                    return false
                }
                val yDiff = y - mInitialMotionY
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
            }
            MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
        }

        return mIsBeingDragged
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev)
        }
        when (ev.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = ev.findPointerIndex( mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }
                val y = ev.getY( pointerIndex)
                val yDiff = y - mInitialMotionY
                if (yDiff<0) return false
                val scrollTop = yDiff * DRAG_RATE
                mCurrentDragPercent = scrollTop / totalDragDistance
                if (mCurrentDragPercent < 0) {
                    return false
                }
                val boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent))
                val extraOS = Math.abs(scrollTop) - totalDragDistance
                val slingshotDist = totalDragDistance.toFloat()
                val tensionSlingshotPercent = Math.max(
                    0f,
                    Math.min(extraOS, slingshotDist * 2) / slingshotDist
                )
                val tensionPercent = (tensionSlingshotPercent / 4 - Math.pow(
                    (tensionSlingshotPercent / 4).toDouble(), 2.0
                )).toFloat() * 2f
                val extraMove = slingshotDist * tensionPercent / 2
                val targetY = (slingshotDist * boundedDragPercent + extraMove).toInt()
                setTargetOffsetTop(targetY - mCurrentOffsetTop)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = ev.actionIndex
                mActivePointerId = ev.getPointerId( index)
            }
            MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                mIsBeingDragged = false
                animateOffsetToStartPosition()
                mActivePointerId = INVALID_POINTER
                return false
            }
        }

        return true
    }

    private fun animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop
        mFromDragPercent = mCurrentDragPercent
        animationDuration =  if (mFromDragPercent <= 1/2){
             700
        }else{
            800
        }
        val animationDuration = Math.abs((animationDuration * mFromDragPercent).toLong())
        mAnimateToStartPosition.reset()
        mAnimateToStartPosition.duration = animationDuration
        mAnimateToStartPosition.interpolator = mDecelerateInterpolator
        mAnimateToStartPosition.setAnimationListener(mToStartListener)
        mTarget.clearAnimation()
        mTarget.startAnimation(mAnimateToStartPosition)
    }

    private fun animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop
        mFromDragPercent = mCurrentDragPercent
        mAnimateToCorrectPosition.reset()
        mAnimateToCorrectPosition.duration = animationDuration.toLong()
        mAnimateToCorrectPosition.interpolator = mDecelerateInterpolator
        mTarget.clearAnimation()
        mTarget.startAnimation(mAnimateToCorrectPosition)
        animateOffsetToStartPosition()
        mCurrentOffsetTop = mTarget.top
    }

    private fun moveToStart(interpolatedTime: Float) {
        val targetTop = mFrom - (mFrom * interpolatedTime).toInt()
        val targetPercent = mFromDragPercent * (1.0f - interpolatedTime)
        val offset = targetTop - mTarget.top
        mCurrentDragPercent = targetPercent
        setTargetOffsetTop(offset)
    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = ev.actionIndex
        val pointerId = ev.findPointerIndex( pointerIndex)
        if (pointerId == mActivePointerId) {
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mActivePointerId = ev.getPointerId( newPointerIndex)
        }
    }

    private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index = ev.findPointerIndex(activePointerId)
        return if (index < 0) {
            -1f
        } else ev.getY( index)
    }
    private fun setTargetOffsetTop(offset: Int) {
        if (offset == 0) return
        mTarget.offsetTopAndBottom(offset)
        mCurrentOffsetTop = mTarget.top

    }

    private fun canChildScrollUp(): Boolean {
        return if (mTarget is ListView) {
            ListViewCompat.canScrollList(mTarget as ListView, -1)
        } else mTarget.canScrollVertically(-1)
    }
    private  var animationDuration = 100

    companion object {
        private  var DRAG_MAX_DISTANCE = Resources.getSystem().displayMetrics.heightPixels*2/3
        private const val DRAG_RATE = .5f
        private const val DECELERATE_INTERPOLATION_FACTOR = 2f

        private const val INVALID_POINTER = -1
    }

    private fun convertDpToPixel(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

}

