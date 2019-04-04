package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.xqy.androidx.ui.adapter.AppAdapter
import com.xqy.androidx.ui.alertMsg
import com.xqy.androidx.ui.dialog.AppDialog
import com.xqy.androidx.ui.dialog.DialogConfig
import com.xqy.androidx.ui.initRecyclerView
import com.xqy.androidx.ui.network.NetWorkManager
import com.xqy.androidx.ui.network.NetWorkState
import com.xqy.androidx.ui.state.UIState
import com.xqy.androidx.ui.state.UIStateCallback
import com.xqy.androidx.ui.state.UIStateManager
import com.xqy.androidx.ui.template.UITemplate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UITemplate,
    UIStateCallback {
    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
    override val mScaffold: Boolean = false
    override val centerTitle: String = "Main"
    override val layoutResId: Int = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetWorkManager.run(application){
                state, mNetWorkInfo->
            when(state){
                NetWorkState.CONNECTED-> {
                    mNetWorkInfo?.let {

                        alertMsg("网络已连接！"){}
                    }
                    UIStateManager.changeUIState("MainActivity",
                        UIState.DEFAULT)
                }
                NetWorkState.LOST->{
                    alertMsg("网络开小差了！"){}
                    UIStateManager.changeUIState("MainActivity",
                        UIState.EMPTY)

                }
            }
        }

        setContentView(createContentView())
//        supportFragmentManager.beginTransaction()
//            .replace(rv.id,MyFragment())
//            .commit()
        val loadingFragment = AppDialog.newBuilder()
            .isDefaultConfirmType(true, DialogConfig(16f,
                16f, R.color.colorAccent,
                R.color.colorPrimaryDark,
                android.R.color.black,
                {

                }, {
                    true
                })
            )
            .isCancel(false)
            .setAlpha(0.3f)
//            .dialogHeight()//设置高度
//            .dialogWidth()//设置宽度
//            .dialogRadius()//设置圆角大小,直接给数值,单位默认为dp
            .build()
        //loadingFragment.show(supportFragmentManager,"")
        val delegateAdapter = mRecyclerView.initRecyclerView()
        for (i in 0..20){
            val adapter = AppAdapter<String>("3", {
                R.layout.item
            }, {
                MyViewHolder({

                    loadingFragment.show(supportFragmentManager, "loading")
                    //startActivity(Intent(this@MainActivity,MyActivity::class.java))

                }, it)
            }, LinearLayoutHelper())
            delegateAdapter.addAdapter(adapter)

        }
//        val items = arrayListOf<String>()
//        for (i in 0 ..20){
//            items.add("$i")
//        }
//        val adapters = AppAdapter<String>(items,{
//            R.layout.item
//        },{
//            MyViewHolder({
//
//                loadingFragment.show(supportFragmentManager,"loading")
//                //startActivity(Intent(this@MainActivity,MyActivity::class.java))
//
//            },it)
//        }, LinearLayoutHelper())
//        delegateAdapter.addAdapter(adapters)


    }
}
