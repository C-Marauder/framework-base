package com.xqy.androidx.framework.template

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.xqy.androidx.framework.R

internal class AppbarCreator(mContext: AppCompatActivity) : UICreator(mContext) {

    companion object {
        @SuppressLint("ResourceType")
        @IdRes
        private const val appId = 0x1
    }

    fun createAppbarLayout(): AppBarLayout {
        return AppBarLayout(mContext).apply {
            id = appId
            clearAppbarElevation(this)
            unEnableScroll(this)
        }
    }

    private fun unEnableScroll(appBarLayout: AppBarLayout) {
        if (!UIConfig.canScroll) {
            val lp = appBarLayout.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
            lp.scrollFlags = 0
        }
    }

    private fun clearAppbarElevation(appBarLayout: AppBarLayout) {
        if (UIConfig.clearElevation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBarLayout.stateListAnimator = AnimatorInflater.loadStateListAnimator(
                    mContext,
                    R.animator.appbar_elevation
                )
            }

        }
    }


}