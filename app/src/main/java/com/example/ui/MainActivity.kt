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
//        //mUserId = 12
//        NetWorkManager.run(application){
//                state, mNetWorkInfo->
//            when(state){
//                NetWorkState.CONNECTED-> {
//                    mNetWorkInfo?.let {
//
//                        alertMsg("网络已连接！"){}
//                    }
//                    UIStateManager.changeUIState("MainActivity",
//                        UIState.DEFAULT)
//                }
//                NetWorkState.LOST->{
//                    alertMsg("网络开小差了！"){}
//                    UIStateManager.changeUIState("MainActivity",
//                        UIState.EMPTY)
//
//                }
//            }
//        }

        setContentView(createContentView())
        textView.setOnClickListener {
            PermissionHelper.from(this).requestPermission(Manifest.permission.CAMERA,
                hasPermission = { permission ->//该权限已获取

                    alertMsg("$permission 已经获取"){

                    }

                },
                observer = { permission, isGranted ->//请求权限的回调

                    val msg = if (isGranted)"成功" else "失败"
                    alertMsg("$permission 获取$msg"){

                    }

                    //toDoSomeThings
                })
        }
    }
}
