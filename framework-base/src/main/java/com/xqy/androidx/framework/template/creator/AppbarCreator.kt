package com.xqy.androidx.framework.template.creator

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.xqy.androidx.framework.R
import com.xqy.androidx.framework.template.UITemplate
import com.xqy.androidx.framework.template.creator.model.ConstrainSetModel
import com.xqy.androidx.framework.template.creator.model.UIModel

internal class AppbarCreator: UICreator<AppBarLayout>() {


    companion object {
        @SuppressLint("ResourceType")
        @IdRes
        private const val appId = 0x1
    }
    override fun createWidget(parentView: ViewGroup, model: UIModel, constrainSetModel: ConstrainSetModel?) {
        AppBarLayout(parentView.context).apply {
            id = appId
            clearAppbarElevation(this)
            unEnableScroll(this)
            createToolbar(this,model)
            parentView.addView(this, -1, -2)
            constrainSetModel?.let {
                it.constrainSetId = appId
                it.constraintSet.apply {
                    constrainWidth(appId, 0)
                    connect(appId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    connect(appId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                    connect(appId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                }

            }
        }
    }


    private fun unEnableScroll(appBarLayout: AppBarLayout) {
        if (!UITemplate.canScroll) {
            val lp = appBarLayout.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags = 0
        }
    }

    private fun clearAppbarElevation(appBarLayout: AppBarLayout) {
        if (UITemplate.clearElevation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBarLayout.stateListAnimator = AnimatorInflater.loadStateListAnimator(
                    appBarLayout.context,
                    R.animator.appbar_elevation
                )
            }

        }
    }


    private fun createToolbar(parentView: ViewGroup,model: UIModel){
        val mContext = parentView.context as AppCompatActivity
        Toolbar(mContext).apply {
            val middleTitleView = createMiddleTitleView(this,model.centerTitle)
            addView(middleTitleView, -2, -1)
            val lp = middleTitleView.layoutParams as Toolbar.LayoutParams
            lp.gravity = Gravity.CENTER
            middleTitleView.layoutParams = lp
            this.title = model.toolbarTitle
            mContext.setSupportActionBar(this)
            if (UITemplate.navIcon == 0) {
                mContext.supportActionBar?.let {
                    if (model.enableArrowIcon) {
                        it.setDisplayHomeAsUpEnabled(true)
                    } else {
                        this.setPadding(UITemplate.titlePadding, 0, 0, 0)
                    }

                }
            } else {
                if (!model.enableArrowIcon) {
                    this.setPadding(UITemplate.titlePadding, 0, 0, 0)
                } else {
                    this.setNavigationIcon(UITemplate.navIcon)

                }
            }
            setNavigationOnClickListener {
                val isHandle = model.navOnClickListener.invoke()
                if (!isHandle) {
                    mContext.onBackPressed()
                }
            }
            parentView.addView(this, -1, -2)
        }
    }

    private fun createMiddleTitleView(parentView:ViewGroup,centerTitle: String? = null): AppCompatTextView {
        return AppCompatTextView(parentView.context).apply {
            this.gravity = Gravity.CENTER
            this.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                UITemplate.titleSize
            )
            this.setTextColor(
                ContextCompat.getColor(context,
                    UITemplate.titleColor
                ))
            this.text = centerTitle

        }
    }


}