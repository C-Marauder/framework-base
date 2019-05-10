package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui.databinding.ItemBinding
import com.xqy.androidx.framework.adapter.list.MVVMViewHolder
import com.xqy.androidx.framework.state.UIStateCallback
import com.xqy.androidx.framework.template.UITemplate
import java.util.concurrent.ConcurrentHashMap

class MainActivity : AppCompatActivity(), UITemplate,
    UIStateCallback {

    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
    override val mToolbarTitle: String = "hhhhh"
    override val mLayoutResId: Int = R.layout.activity_main
    private val adapterMap: ConcurrentHashMap<MVVMViewHolder<String, ItemBinding>, Int> by lazy {
        ConcurrentHashMap<MVVMViewHolder<String, ItemBinding>, Int>()
    }

    //var mUserId:Int by AppPreference(application,123)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(createContentView())
//       val keyPair = SecurityHelper.mInstance.generateRSAKeyPair()
//        val encodeResult = SecurityHelper.mInstance.encryptByRsa("1111",keyPair.public)
//
//        appLog(encodeResult)
//        val result= SecurityHelper.mInstance.decryptByRsa(encodeResult,keyPair.private)
//        encodeView.setOnClickListener {
//            //存储到本地
//            val encodeContent = SecurityHelper.mInstance.encryptToLocalByRsa("123456","test")
//            encodeView.text =encodeContent
//        }
//
//        decodeView.setOnClickListener {
//            //从本地读取
//            val decodeContent = SecurityHelper.mInstance.decryptFromLocalByRsa("test")
//            decodeView.text = decodeContent
//        }
//        val publicKey= Base64.encodeToString(SecurityHelper.mInstance.generateRSAKeyPair().public.encoded,Base64.DEFAULT)
//
//        SecurityHelper.mInstance.saveBase64PubKey(publicKey)
//
//        val encodeContent = SecurityHelper.mInstance.encryptByRsa("111111")
//
//        appLog(encodeContent!!)

    }


}
