package com.xqy.androidx.framework.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.xqy.androidx.framework.template.UITemplate

abstract class MVVMFragment<T:ViewDataBinding>:Fragment(), UITemplate {

    open val onFragmentCallback: OnFragmentBackPressed ?=null
    lateinit var mViewDataBinding: T
    lateinit var mFragmentCallback: FragmentCallback
    private lateinit var mAppCompatActivity: AppCompatActivity
    open val enableMenu:Boolean = false
    override fun inflateContentView(rootView: ViewGroup): View {
        mViewDataBinding = DataBindingUtil.inflate(layoutInflater,mLayoutResId,rootView,false)
        return mViewDataBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enableMenu){
            setHasOptionsMenu(enableMenu)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mAppCompatActivity = context as AppCompatActivity
        if (context is FragmentCallback){
            mFragmentCallback = context
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createContentView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentCallback?.let {
            this.mAppCompatActivity.onBackPressedDispatcher.addCallback {
                it.invoke()
                true
            }
        }

    }

}