package com.xqy.androidx.framework.template

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.xqy.androidx.framework.state.UIStateCallback

internal class UIStateCreator(mContext: AppCompatActivity) : UICreator(mContext) {
    companion object{
        @SuppressLint("ResourceType")
        @IdRes
        private const val mStateViewId = 0x2

    }
    fun createStateView(mUIStateCallback: UIStateCallback):AppCompatImageView{
        return AppCompatImageView(mContext).apply {
            id = mStateViewId
            setBackgroundColor(
                ContextCompat.getColor(
                    rootView.context,
                    android.R.color.background_light
                )
            )
            mUIStateCallback.observeState(this)

        }
    }
}