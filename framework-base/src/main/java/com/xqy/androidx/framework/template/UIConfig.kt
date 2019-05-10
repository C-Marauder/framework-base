package com.xqy.androidx.framework.template

class UIConfig private constructor() {

    companion object {

        internal var titleSize: Float = 16f
        internal var titleColor: Int = android.R.color.background_light
        internal var navIcon: Int = 0
        internal var canScroll: Boolean = true
        internal var clearElevation: Boolean = false
        internal var titlePadding:Int = 0
        fun Builder(init: Builder.() -> Unit) = Builder().apply(init)
    }

    class Builder {
        fun titlePadding(init: () -> Int){
            titlePadding = init()
        }
        fun clearElevation(init: () -> Boolean) {
            clearElevation = init()
        }

        fun behavior(init: () -> Boolean) {
            UIConfig.canScroll = init()
        }

        fun titleSize(init: () -> Float) {
            UIConfig.titleSize = init()
        }

        fun titleColor(init: () -> Int) {
            UIConfig.titleColor = init()
        }

        fun navIcon(init: () -> Int) {
            navIcon = init()
        }

    }
}