package com.example.ui

import android.util.Log
import com.xqy.androidx.framework.fragment.MVVMFragment
import com.example.ui.databinding.FMyBinding
import com.xqy.androidx.framework.template.SCAFFOLD

class MyFragment: MVVMFragment<FMyBinding>() {
    override val mTemplate: Int = SCAFFOLD
    override val mLayoutResId: Int by lazy {
        R.layout.f_my
    }
    override val mCenterTitle: String = "MyFragment"




}
