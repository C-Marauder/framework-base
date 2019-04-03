package com.example.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidx.ui.fragment.MVVMFragment
import com.androidx.ui.template.UIConfig
import com.androidx.ui.template.UITemplate
import com.example.ui.databinding.FMyBinding

class MyFragment:MVVMFragment<FMyBinding>() {
    override val layoutResId: Int by lazy {
        R.layout.f_my
    }
    override val centerTitle: String = "MyFragment"

    override val onBackPressed: (() -> Unit)?={
        Log.e("====","===")
    }
}
