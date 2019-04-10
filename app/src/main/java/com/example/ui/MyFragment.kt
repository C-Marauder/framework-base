package com.example.ui

import android.util.Log
import com.xqy.androidx.framework.fragment.MVVMFragment
import com.example.ui.databinding.FMyBinding

class MyFragment: MVVMFragment<FMyBinding>() {
    override val layoutResId: Int by lazy {
        R.layout.f_my
    }
    override val centerTitle: String = "MyFragment"

    override val onBackPressed: (() -> Unit)?={
        Log.e("====","===")
    }


}
