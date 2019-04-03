package com.androidx.ui

import android.view.View

class OnClickHelper {
    companion object {
        val instance:OnClickHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            OnClickHelper()
        }

    }
    private var lastTime:Long=0

    fun onClick(view:View,interval:Long,listener:()->Unit){
        view.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >=interval){
                lastTime = currentTime
                listener()
            }
        }

    }

}