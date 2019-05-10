package com.xqy.androidx.framework.template
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.xqy.androidx.framework.state.UIStateCallback
import com.google.android.material.appbar.AppBarLayout

interface UITemplate {

    val mLayoutResId: Int
    val mTemplate: Int get() = SCAFFLD
    val mCenterTitle: String? get() = null
    val mToolbarTitle: String get() = ""
    val mEnableArrowIcon: Boolean get() = true
    val mIsNeedToolbar: Boolean get() = true
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


    fun inflateContentView(rootView: ViewGroup): View {
        return LayoutInflater.from(mActivity).inflate(mLayoutResId, rootView, false)
    }

    fun createContentView(): View {
        val templateView = TemplateCreator(mActivity).createTemplate(mTemplate)
        var appBarLayout:AppBarLayout?=null
        if (mIsNeedToolbar) {
            appBarLayout = AppbarCreator(mActivity).createAppbarLayout().apply {
                val mToolbarCreator = ToolbarCreator(mActivity).createToolbar(mToolbarTitle,mEnableArrowIcon,mCenterTitle){
                    handleNavListener()
                }
                addView(mToolbarCreator, -1, -2)
                templateView.addView(this, -1, -2)
            }

        }

        val content = inflateContentView(templateView)
        templateView.addView(content)
        var mStateView:AppCompatImageView?=null
        if (this is UIStateCallback) {
            mStateView = UIStateCreator(mActivity).createStateView(this)
            templateView.addView(mStateView)
        }
        if (mTemplate == SCAFFLD) {
            mStateView?.let {
                val mStateImageViewLp = it.layoutParams as CoordinatorLayout.LayoutParams
                mStateImageViewLp.behavior = AppBarLayout.ScrollingViewBehavior()
                mStateImageViewLp.gravity = Gravity.CENTER
                it.layoutParams = mStateImageViewLp

            }

        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(templateView as ConstraintLayout)
            appBarLayout?.let {
                constraintSet.constrainWidth(it.id, 0)
                constraintSet.connect(it.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(it.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(it.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            }

            constraintSet.constrainWidth(content.id, 0)
            constraintSet.constrainHeight(content.id, 0)
            constraintSet.connect(content.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(content.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            if (appBarLayout == null){
                constraintSet.connect(content.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }else{
                constraintSet.connect(content.id, ConstraintSet.TOP, appBarLayout.id, ConstraintSet.BOTTOM)
            }
            constraintSet.connect(content.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            mStateView?.let {
                constraintSet.connect(it.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(it.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.connect(it.id, ConstraintSet.TOP, content.id, ConstraintSet.TOP)
                constraintSet.connect(it.id, ConstraintSet.BOTTOM, content.id, ConstraintSet.BOTTOM)

            }

            constraintSet.applyTo(templateView)
        }

        return templateView
    }


}