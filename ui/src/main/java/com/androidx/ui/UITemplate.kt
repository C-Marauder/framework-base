package com.androidx.ui

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.circularreveal.coordinatorlayout.CircularRevealCoordinatorLayout

interface UITemplate {
    companion object {
        @SuppressLint("ResourceType")
        @IdRes
        private const val appId = 0x1

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
                this.stateListAnimator = AnimatorInflater.loadStateListAnimator(parent.context,R.animator.appbar_elevation)

            }
        }
    }

    fun createToolbar(parent: View): Toolbar {
        return Toolbar(parent.context).apply {
            val titleView = AppCompatTextView(this.context).apply {
                this.gravity = Gravity.CENTER
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, UIConfig.titleSize)
                this.setTextColor(ContextCompat.getColor(mActivity, UIConfig.titleColor))
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
        if (mScaffold) {
            if (!UIConfig.canScroll) {
                val lp = appBarLayout.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
                lp.scrollFlags = 0
            }
            val contentLp = content.layoutParams as CoordinatorLayout.LayoutParams
            contentLp.behavior = AppBarLayout.ScrollingViewBehavior()
            content.layoutParams = contentLp

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
            constraintSet.applyTo(rootView)
        }

        return rootView
    }


}