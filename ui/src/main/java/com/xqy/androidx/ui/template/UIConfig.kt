package com.xqy.androidx.ui.template

class UIConfig {

    companion object {

        internal var titleSize:Float = 16f
        internal var titleColor:Int = android.R.color.background_light
        internal var navIcon:Int = 0
        internal var canScroll:Boolean = true
        internal var clearElevation:Boolean =false
        fun newBuilder() = Builder.builder
    }

    class Builder{
        companion object {
            val builder: Builder by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
                Builder()
            }
        }
        fun clearElevation(isClear:Boolean)=apply {
            clearElevation = isClear
        }
        fun behavior(canScroll:Boolean) = apply {
            UIConfig.canScroll = canScroll
        }
        fun titleSize(titleSize:Float)=apply {
            UIConfig.titleSize = titleSize
        }
        fun titleColor(titleColor:Int) = apply {
            UIConfig.titleColor = titleColor
        }

        fun navIcon(icon:Int)=apply {
            navIcon = icon
        }

    }
}