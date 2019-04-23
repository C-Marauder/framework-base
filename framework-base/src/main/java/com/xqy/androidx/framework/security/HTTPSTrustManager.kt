package com.xqy.androidx.framework.security

import android.annotation.SuppressLint
import android.util.Log
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class HTTPSTrustManager(private val ca:Certificate):X509TrustManager {
    companion object {
        private const val TAG:String = "HTTPSTrustManager"
    }
    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

        if (chain == null || chain.isEmpty()){
            Log.e(TAG,"Certificate chain is invalid")
            return
        }

        if (authType.isNullOrEmpty()){
            Log.e(TAG,"Authentication type is invalid")
            return

        }

        chain.forEach {
            it.checkValidity()
            try {
              it.verify(ca.publicKey)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }



    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}