package com.xqy.androidx.framework.template.creator.model

import android.view.View
import com.xqy.androidx.framework.state.UIStateCallback

data class UIModel(
    val themeColor:Int,
    val isNeedAppbar:Boolean,
    val toolbarHeight:Int,
    val isNeedToolbar:Boolean,
    val toolbarTitle: String,
    val enableArrowIcon: Boolean,
    var contentView: View?=null,
    val centerTitle: String? = null,
    val uiStateCallback: UIStateCallback?=null,
    val navOnClickListener:()->Boolean)