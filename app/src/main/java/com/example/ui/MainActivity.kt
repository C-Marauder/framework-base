package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.androidx.ui.UITemplate
import com.androidx.ui.adapter.AppAdapter
import com.androidx.ui.dialog.AppDialog
import com.androidx.ui.initRecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),UITemplate {
    override val centerTitle: String = "Main"
    override val layoutResId: Int = R.layout.activity_main
    override val mScaffold: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(rv.id,MyFragment())
            .commit()
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
