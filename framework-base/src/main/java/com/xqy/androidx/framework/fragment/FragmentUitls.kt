package com.xqy.androidx.framework.fragment

import androidx.fragment.app.Fragment

typealias OnFragmentBackPressed = ()->Unit

inline fun <reified T:Fragment> getInstance():T{
    return T::class.java.newInstance()
}