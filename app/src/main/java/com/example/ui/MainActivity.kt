package com.example.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.example.ui.databinding.ItemBinding
import com.xqy.androidx.framework.adapter.list.MVVMViewHolder
import com.xqy.androidx.framework.notification.NotificationHelper
import com.xqy.androidx.framework.notification.model.NotificationModel
import com.xqy.androidx.framework.state.UIStateCallback
import com.xqy.androidx.framework.template.CONSTRAINT
import com.xqy.androidx.framework.template.SCAFFOLD
import com.xqy.androidx.framework.template.UITemplate
import com.xqy.androidx.framework.utils.appLog
import com.xqy.androidx.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ConcurrentHashMap

class MainActivity : AppCompatActivity(), UITemplate,
    UIStateCallback {
    override val mTemplate: Int = CONSTRAINT

    override val mUnConnectedResId: Int by lazy {
        R.drawable.ic_network_error
    }
    override val mEmptyResId: Int by lazy {
        R.drawable.ic_empty
    }
    override val isNeedAppbar: Boolean = false
    override val mToolbarTitle: String = "fff"
    override val mLayoutResId: Int = R.layout.activity_main
    private val adapterMap: ConcurrentHashMap<MVVMViewHolder<String, ItemBinding>, Int> by lazy {
        ConcurrentHashMap<MVVMViewHolder<String, ItemBinding>, Int>()
    }

    //var mUserId:Int by AppPreference(application,123)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(createContentView())
        PermissionHelper.from(this)
            .requestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, hasPermission = { permission ->
                Log.e("===","LLLL")

            }) { permission, isGranted ->
                appLog("=====$isGranted")
                if (isGranted) {

                }
            }

//        val intent =  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.data = Uri.parse("package:" + getPackageName());
//        startActivityForResult(intent, 100)
        mRecyclerView.setOnClickListener {
            NotificationHelper.mInstance.sendNotification(
                NotificationModel(
                    1,
                    "123",
                    "222",
                    R.drawable.ic_network_error,
                    this
                )
            )
        }



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){

        }
    }

}
