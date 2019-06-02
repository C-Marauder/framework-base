package com.xqy.androidx.framework.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.xqy.androidx.framework.fragment.FragmentCallback

abstract class AppActivity<T:ViewDataBinding>:AppCompatActivity(), FragmentCallback {
    lateinit var mDataBinding:T
    override fun onResponse(vararg pair: Pair<String, Any?>?) {

    }
}