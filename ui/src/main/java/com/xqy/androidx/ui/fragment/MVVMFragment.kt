package com.xqy.androidx.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.xqy.androidx.ui.template.UITemplate

abstract class MVVMFragment<T:ViewDataBinding>:Fragment(), UITemplate {
    open val onBackPressed: Function0<Unit> ?=null
    lateinit var mViewDataBinding: T
    lateinit var mFragmentCallback: FragmentCallback
    private lateinit var mAppCompatActivity: AppCompatActivity
    override fun inflateContentView(rootView: ViewGroup): View {
        mViewDataBinding = DataBindingUtil.inflate<T>(layoutInflater,layoutResId,rootView,false)
        return mViewDataBinding.root
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
        onBackPressed?.let {
            this.mAppCompatActivity.addOnBackPressedCallback {
                it()
                true
            }
        }

    }

}