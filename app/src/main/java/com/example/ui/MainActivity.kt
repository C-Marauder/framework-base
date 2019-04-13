package com.example.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.ui.databinding.FMyBinding
import com.example.ui.databinding.ItemTestBinding
import com.xqy.androidx.framework.adapter.AppAdapter
import com.xqy.androidx.framework.adapter.MVVMAdapter
import com.xqy.androidx.framework.behavior.ContentBehavior
import com.xqy.androidx.framework.dialog.AppDialog
import com.xqy.androidx.framework.dialog.DialogConfig
import com.xqy.androidx.framework.network.NetWorkManager
import com.xqy.androidx.framework.network.NetWorkState
import com.xqy.androidx.framework.prefers.AppPreference
import com.xqy.androidx.framework.security.SecurityHelper
import com.xqy.androidx.framework.state.UIState
import com.xqy.androidx.framework.state.UIStateCallback
import com.xqy.androidx.framework.state.UIStateManager
import com.xqy.androidx.framework.template.UITemplate
import com.xqy.androidx.framework.utils.*
import com.xqy.androidx.permission.PermissionHelper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.internal.operators.flowable.FlowablePublish
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), UITemplate,
    UIStateCallback {

    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
    override val centerTitle: String = "Main"
    override val layoutResId: Int = R.layout.activity_main
    //var mUserId:Int by AppPreference(application,123)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(createContentView())
       val keyPair = SecurityHelper.mInstance.generateRSAKeyPair()
        val encodeResult = SecurityHelper.mInstance.encryptByRsa("1111",keyPair.public)

        appLog(encodeResult)
        val result= SecurityHelper.mInstance.decryptByRsa(encodeResult,keyPair.private)
        appLog(result)

        val deResult = SecurityHelper.mInstance.encryptByRsa("123456")

        appLog(deResult)
        val content = SecurityHelper.mInstance.decryptByRsa(deResult)
        appLog(content!!)

    }
}
