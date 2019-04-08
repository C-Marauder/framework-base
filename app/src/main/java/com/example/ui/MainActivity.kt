package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.ui.databinding.FMyBinding
import com.example.ui.databinding.ItemTestBinding
import com.xqy.androidx.framework.adapter.AppAdapter
import com.xqy.androidx.framework.adapter.MVVMAdapter
import com.xqy.androidx.framework.dialog.AppDialog
import com.xqy.androidx.framework.dialog.DialogConfig
import com.xqy.androidx.framework.network.NetWorkManager
import com.xqy.androidx.framework.network.NetWorkState
import com.xqy.androidx.framework.security.SecurityHelper
import com.xqy.androidx.framework.state.UIState
import com.xqy.androidx.framework.state.UIStateCallback
import com.xqy.androidx.framework.state.UIStateManager
import com.xqy.androidx.framework.template.UITemplate
import com.xqy.androidx.framework.utils.*
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
    override val mScaffold: Boolean = false
    override val centerTitle: String = "Main"
    override val layoutResId: Int = R.layout.activity_main
    //var mUserId:Int by AppPreference(application,123)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mUserId = 12
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
//        for (i in 0..20){
//            val adapter = AppAdapter<String>("3", {
//                R.layout.item
//            }, {
//                MyViewHolder({
//
//                   // loadingFragment.show(supportFragmentManager, "loading")
//                    //startActivity(Intent(this@MainActivity,MyActivity::class.java))
//
//                }, it)
//            }, LinearLayoutHelper())
//            delegateAdapter.addAdapter(adapter)
//
//        }
        application.saveToCache("App","123333")
        val cache = application.getFromCache("App")
        appLog("=================")
        if (cache == null){
            Log.e("===","===22==")
            //appLog("kong")
        }else{
            Log.e("===",cache)

            //appLog(cache)
        }
        val result = SecurityHelper.mInstance.encryptByAES("123",cache!!)
        Log.e("====",result)

        val deResult = SecurityHelper.mInstance.decryptByAES(result)
        appLog(deResult!!)
       rv.onClick {
           appLog("${System.currentTimeMillis()}")
       }
      val testAdapter =  MVVMAdapter.Builder<String,ItemTestBinding> {
           items {
               val items = arrayListOf<String>()
               for (i in 0..20) {
                   items.add("$i")
               }
               items
           }
            layoutHelper {
                LinearLayoutHelper()
            }
          layoutResId {
              R.layout.item_test
          }
            viewHolder { dataBinding, itemView ->
                TestViewHolder(dataBinding,itemView)
            }
        }

        delegateAdapter.addAdapter(testAdapter)
//        application.deleteCache("App")
//        val content = application.getFromCache("App")
//        if (content == null){
//            alertMsg("==="){
//
//            }
//        }else{
//            appLog(content)
//        }

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
