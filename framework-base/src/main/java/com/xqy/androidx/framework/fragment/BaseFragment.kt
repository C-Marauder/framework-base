package com.xqy.androidx.framework.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *@desc
 *@creator 小灰灰
 *@Time 2019-05-27 - 15:32
 **/
abstract class BaseFragment<T:ViewDataBinding>:Fragment() {
    abstract val mLayoutResId:Int
    open val onFragmentBackPressed: OnFragmentBackPressed ?=null
    lateinit var mViewDataBinding: T
    lateinit var mFragmentCallback: FragmentCallback
    private lateinit var mAppCompatActivity: AppCompatActivity
    open val mEnableBackPressed:Boolean = true
    override fun onAttach(context: Context) {
        super.onAttach(context)
        with(context){
            mAppCompatActivity = context as AppCompatActivity
            if (context is FragmentCallback) mFragmentCallback = context
        }


    }

    open fun onCreatingView(mViewDataBinding: T) {}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate<T>(inflater, mLayoutResId, container, false)
        onCreatingView(mViewDataBinding)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentBackPressed?.let {
            this.mAppCompatActivity.onBackPressedDispatcher.addCallback(this,object :
                OnBackPressedCallback(mEnableBackPressed){
                override fun handleOnBackPressed() {
                    it.invoke()
                }

            })

        }
    }
}