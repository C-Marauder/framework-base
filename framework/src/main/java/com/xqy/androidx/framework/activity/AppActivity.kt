package com.xqy.androidx.framework.activity

import androidx.appcompat.app.AppCompatActivity
import com.xqy.androidx.framework.fragment.FragmentCallback

abstract class AppActivity:AppCompatActivity(), FragmentCallback {
    override fun onResponse(vararg pair: Pair<String, Any>) {

    }
}