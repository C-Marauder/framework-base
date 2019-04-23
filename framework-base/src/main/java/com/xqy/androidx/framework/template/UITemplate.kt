package com.xqy.androidx.framework.template

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.xqy.androidx.framework.state.UIStateCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout
import com.xqy.androidx.framework.R

interface UITemplate {
    companion object {
        @SuppressLint("ResourceType")
        @IdRes
        private const val appId = 0x1

        @SuppressLint("ResourceType")
        @IdRes
        private const val mStateViewId=0x2

    }
    val layoutResId: Int
    val mScaffold: Boolean get() = true
    val centerTitle: String
    fun handleNavListener(): Boolean {
        return false
    }

    private val mActivity: AppCompatActivity
        get() {
            return when (this) {
                is AppCompatActivity -> this
                is Fragment -> this.activity!! as AppCompatActivity
                else -> {
                    throw Exception("")
                }
            }
        }

    private fun createCoordinatorLayout(): CircularRevealCoordinatorLayout {
        return CircularRevealCoordinatorLayout(mActivity)

    }
    fun inflateContentView(rootView:ViewGroup):View{
       return LayoutInflater.from(mActivity).inflate(layoutResId, rootView, false)
    }
    private fun createConstraintLayout(): ConstraintLayout {
        return ConstraintLayout(mActivity)

    }

    private fun createAppbarLayout(parent: View): AppBarLayout {
        return AppBarLayout(parent.context).apply {
            id = appId
            if (UIConfig.clearElevation){
                this.stateListAnimator = AnimatorInflater.loadStateListAnimator(parent.context,
                    R.animator.appbar_elevation
                )

            }
        }
    }

    fun createToolbar(parent: View): Toolbar {
        return Toolbar(parent.context).apply {
            val titleView = AppCompatTextView(this.context).apply {
                this.gravity = Gravity.CENTER
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, UIConfig.titleSize)
                this.setTextColor(ContextCompat.getColor(mActivity,
                    UIConfig.titleColor
                ))
                this.text = centerTitle
            }
            addView(titleView, -2, -1)
            val lp = titleView.layoutParams as Toolbar.LayoutParams
            lp.gravity = Gravity.CENTER
            titleView.layoutParams = lp
            mActivity.setSupportActionBar(this)
            this.title = ""
            if (UIConfig.navIcon == 0) {
                mActivity.supportActionBar?.let {
                    it.setDisplayHomeAsUpEnabled(true)
                    it.setDisplayShowTitleEnabled(false)
                }
            } else {
                this.setNavigationIcon(UIConfig.navIcon)
            }
            this.setNavigationOnClickListener {
                val isHandle = handleNavListener()
                if (!isHandle) {
                    mActivity.onBackPressed()
                }
            }
        }
    }

    fun createContentView(): View {
        val rootView = if (mScaffold) {
            createCoordinatorLayout()

        } else {
            createConstraintLayout()
        }
        val appBarLayout = createAppbarLayout(rootView)
        val toolbar = createToolbar(appBarLayout)
        appBarLayout.addView(toolbar, -1, -2)
        rootView.addView(appBarLayout, -1, -2)
        val content = inflateContentView(rootView)

        rootView.addView(content)
        var mStateImageView:AppCompatImageView?=null
        if (this is UIStateCallback){
            //val mStateContainer = FrameLayout(rootView.context)
            mStateImageView = AppCompatImageView(rootView.context).apply {
                id = mStateViewId
            }
            mStateImageView.setBackgroundColor(ContextCompat.getColor(rootView.context,android.R.color.background_light))
            rootView.addView(mStateImageView)
            observeState(mStateImageView)
        }
        if (mScaffold) {
            if (!UIConfig.canScroll) {
                val lp = appBarLayout.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
                lp.scrollFlags = 0
            }
//            val contentLp = content.layoutParams as CoordinatorLayout.LayoutParams
//            contentLp.behavior = AppBarLayout.ScrollingViewBehavior()
//            content.layoutParams = contentLp
            mStateImageView?.let {
                val mStateImageViewLp = mStateImageView.layoutParams as CoordinatorLayout.LayoutParams
                mStateImageViewLp.behavior = AppBarLayout.ScrollingViewBehavior()
                mStateImageViewLp.gravity = Gravity.CENTER
                mStateImageView.layoutParams = mStateImageViewLp

            }


        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(rootView as ConstraintLayout)
            constraintSet.constrainWidth(appBarLayout.id,0)
            constraintSet.connect(appBarLayout.id,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP)
            constraintSet.connect(appBarLayout.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START)
            constraintSet.connect(appBarLayout.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END)
            constraintSet.constrainWidth(content.id,0)
            constraintSet.constrainHeight(content.id,0)
            constraintSet.connect(content.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START)
            constraintSet.connect(content.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END)
            constraintSet.connect(content.id,ConstraintSet.TOP,appBarLayout.id,ConstraintSet.BOTTOM)
            constraintSet.connect(content.id,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM)
            mStateImageView?.let {
                constraintSet.connect(it.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START)
                constraintSet.connect(it.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END)
                constraintSet.connect(it.id,ConstraintSet.TOP,content.id,ConstraintSet.TOP)
                constraintSet.connect(it.id,ConstraintSet.BOTTOM,content.id,ConstraintSet.BOTTOM)

            }

            constraintSet.applyTo(rootView)
        }

        return rootView
    }


}