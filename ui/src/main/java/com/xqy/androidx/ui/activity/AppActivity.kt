package com.xqy.androidx.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.xqy.androidx.ui.fragment.FragmentCallback

abstract class AppActivity:AppCompatActivity(), FragmentCallback {
    override fun onResponse(vararg pair: Pair<String, Any>) {

    }
}