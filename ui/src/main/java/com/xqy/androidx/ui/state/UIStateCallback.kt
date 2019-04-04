package com.xqy.androidx.ui.state

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


interface UIStateCallback {
    val mLoadingResId: Int get() = 0
    val mUnConnectedResId: Int
    val mEmptyResId: Int
    private fun setLoadingState(mStateImageView:AppCompatImageView) {
        if (mLoadingResId != 0) {
            mStateImageView.setBackgroundResource(mLoadingResId)

        }
    }

    fun observeState(mStateImageView: AppCompatImageView) {
        val mUIState= MutableLiveData<UIState>()
        val mUIStateKey = this::class.java.simpleName
        UIStateManager.saveUIState(mUIStateKey, mUIState)
        setLoadingState(mStateImageView)
        val activity = when {
            this is AppCompatActivity -> this
            this is Fragment -> this.activity
            else -> throw Exception("in class $mUIStateKey can't get application")
        }
        val mConnectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mActiveNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mActiveNetworkInfo == null || !mActiveNetworkInfo.isConnected) {
            mStateImageView.setBackgroundResource(mUnConnectedResId)
        }
        mUIState.observe(activity, Observer {

            when (it!!) {
                UIState.LOADING -> setLoadingState(mStateImageView)
                UIState.EMPTY -> {
                    if (mStateImageView.visibility == View.GONE){
                        mStateImageView.visibility = View.VISIBLE
                    }
                    mStateImageView.setBackgroundResource(mEmptyResId)
                }
                UIState.DEFAULT -> mStateImageView.visibility = View.GONE
            }
        })


    }
}