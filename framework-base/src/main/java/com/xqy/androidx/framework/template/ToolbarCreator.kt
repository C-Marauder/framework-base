package com.xqy.androidx.framework.template
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

internal class ToolbarCreator(mContext: AppCompatActivity) : UICreator(mContext) {
    fun createToolbar(toolbarTitle: String,enableArrowIcon: Boolean,centerTitle: String? = null,navOnClickListener:()->Boolean): Toolbar = Toolbar(mContext).apply {
        val middleTitleView = createMiddleTitleView(centerTitle)
        addView(middleTitleView, -2, -1)
        val lp = middleTitleView.layoutParams as Toolbar.LayoutParams
        lp.gravity = Gravity.CENTER
        middleTitleView.layoutParams = lp
        this.title = toolbarTitle
        mContext.setSupportActionBar(this)
        if (UIConfig.navIcon == 0) {
            mContext.supportActionBar?.let {
                if (enableArrowIcon) {
                    it.setDisplayHomeAsUpEnabled(true)
                } else {
                    this.setPadding(UIConfig.titlePadding, 0, 0, 0)
                }

            }
        } else {
            if (!enableArrowIcon) {
                this.setPadding(UIConfig.titlePadding, 0, 0, 0)
            } else {
                this.setNavigationIcon(UIConfig.navIcon)

            }
        }
        setNavigationOnClickListener {
            val isHandle = navOnClickListener()
            if (!isHandle) {
                mContext.onBackPressed()
            }
        }
    }

    private fun createMiddleTitleView(centerTitle: String? = null): AppCompatTextView {
        return AppCompatTextView(mContext).apply {
            this.gravity = Gravity.CENTER
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, UIConfig.titleSize)
            this.setTextColor(ContextCompat.getColor(context, UIConfig.titleColor))
            this.text = centerTitle

        }
    }
}