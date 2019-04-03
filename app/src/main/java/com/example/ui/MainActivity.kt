package com.example.ui

import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidx.ui.alertMsg
import com.androidx.ui.network.NetWorkManager
import com.androidx.ui.network.NetWorkState
import com.androidx.ui.state.UIState
import com.androidx.ui.state.UIStateCallback
import com.androidx.ui.state.UIStateManager
import com.androidx.ui.template.UITemplate

class MainActivity : AppCompatActivity(),UITemplate, UIStateCallback {
    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
//    override val mScaffold: Boolean = false
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
                    UIStateManager.changeUIState("MainActivity",UIState.DEFAULT)
                }
                NetWorkState.LOST->alertMsg("网络开小差了！"){}
            }
        }
        setContentView(createContentView())
//        supportFragmentManager.beginTransaction()
//            .replace(rv.id,MyFragment())
//            .commit()
//        val loadingFragment = AppDialog.newBuilder()
//            .layoutResId(R.layout.loading)
//            .setAlpha(0.3f)
//            .build()
//        val delegateAdapter = rv.initRecyclerView()
//        val adapter = AppAdapter<String>("3",{
//            R.layout.item
//        },{
//            MyViewHolder({
//
//                loadingFragment.show(supportFragmentManager,"loading")
//                //startActivity(Intent(this@MainActivity,MyActivity::class.java))
//
//            },it)
//        },LinearLayoutHelper())
//        delegateAdapter.addAdapter(adapter)
    }
}
